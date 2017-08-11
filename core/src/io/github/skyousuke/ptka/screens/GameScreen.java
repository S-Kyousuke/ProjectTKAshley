package io.github.skyousuke.ptka.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.skyousuke.ptka.components.CameraHelperComponent;
import io.github.skyousuke.ptka.components.CharacterComponent;
import io.github.skyousuke.ptka.components.TransformComponent;
import io.github.skyousuke.ptka.systems.*;
import io.github.skyousuke.ptka.utils.*;

/**
 * Created by Bill on 8/8/2560.
 */
public class GameScreen extends AbstractGameScreen implements EntityListener {

    private static final int SCENE_WIDTH = 1024;
    private static final int SCENE_HEIGHT = 576;

    private OrthographicCamera camera = new OrthographicCamera();
    private FitViewport viewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, camera);

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthogonalTiledMapRenderer tiledRenderer;

    private OrthographicCamera uiCamera = new OrthographicCamera();
    private FitViewport uiViewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, uiCamera);
    private Stage uiStage;

    private OrthographicCamera hudCamera = new OrthographicCamera();
    private FitViewport hudViewport = new FitViewport(SCENE_WIDTH, SCENE_HEIGHT, hudCamera);
    private Stage hudStage;

    private Label fpsLabel;
    private Label beamCountLabel;
    private ObjectMap<Entity, Label> healthLabels = new ObjectMap<Entity, Label>();

    private Engine engine;
    private Entity cameraHelper;
    private Entity player;

    private TiledMapTileLayer mapLayer;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        tiledRenderer = new OrthogonalTiledMapRenderer(null, batch);

        setupEngine();
        setupHud();
        setupUi();

        mapLayer = (TiledMapTileLayer) Assets.instance.testMap.getLayers().get(0);

        player = EntityBuilder.buildPlayer(100, 100, mapLayer);
        cameraHelper = EntityBuilder.buildCameraHelper(camera, mapLayer, player);

        engine.addEntity(cameraHelper);
        engine.addEntity(EntityBuilder.buildMap(Assets.instance.testMap));
        engine.addEntity(player);

        engine.addEntity(EntityBuilder.buildPepo(400, 440, mapLayer, player));
        engine.addEntity(EntityBuilder.buildPepoKnight(640, 200, mapLayer, player));
        engine.addEntity(EntityBuilder.buildPepoDevil(840, 420, mapLayer, player));

        engine.addEntity(EntityBuilder.buildBattery(400, 420));
        engine.addEntity(EntityBuilder.buildInverter(300, 300));
        engine.addEntity(EntityBuilder.buildSolarCell(300, 150));
        engine.addEntity(EntityBuilder.buildChargeController(100, 40));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        uiViewport.update(width, height);
        hudViewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(delta);

        engine.update(delta);

        hudCamera.zoom = camera.zoom;
        hudCamera.update();

        updateHud();
        hudStage.act(delta);
        hudStage.draw();

        updateUi();
        uiStage.act(delta);
        uiStage.draw();

    }

    private void handleInput(float delta) {
        CameraHelperComponent cameraHelperComponent = cameraHelper.getComponent(CameraHelperComponent.class);
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            cameraHelperComponent.zoom -= 1 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            cameraHelperComponent.zoom += 1 * delta;
        }

        if (!Mappers.knockback.has(player) && !Mappers.stun.has(player)) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                engine.getSystem(CharacterSystem.class).move(player, Direction.RIGHT);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                engine.getSystem(CharacterSystem.class).move(player, Direction.LEFT);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                engine.getSystem(CharacterSystem.class).move(player, Direction.UP);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                engine.getSystem(CharacterSystem.class).move(player, Direction.DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                engine.getSystem(CharacterSystem.class).move(player, Direction.DOWN);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.C)) {
                engine.getSystem(PlayerSystem.class).beamAttack(player, mapLayer);
            }
        }
    }

    private void setupEngine() {
        engine = new Engine();
        engine.addSystem(new PhysicsSystem());
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new AnimatorSystem());
        engine.addSystem(new CharacterSystem());
        engine.addSystem(new PlayerSystem());
        engine.addSystem(new EnemySystem());
        engine.addSystem(new ItemSystem());
        engine.addSystem(new WeaponSystem());
        engine.addSystem(new CameraHelperSystem());
        engine.addSystem(new TiledMapRenderingSystem(tiledRenderer, camera));
        engine.addSystem(new RenderingSystem(batch, camera));
        engine.addSystem(new DebugRenderingSystem(shapeRenderer, camera));

        engine.addEntityListener(this);
    }

    private void setupUi() {
        fpsLabel = new Label("FPS: 60", Assets.instance.skin);
        fpsLabel.setPosition(SCENE_WIDTH / 2f - fpsLabel.getWidth() / 2, 0);

        uiStage = new Stage(uiViewport);
        uiStage.addActor(fpsLabel);
    }

    private void setupHud() {
        beamCountLabel = new Label("", Assets.instance.skin);

        hudStage = new Stage(hudViewport);
        hudStage.addActor(beamCountLabel);
    }


    private void updateUi() {
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    private void updateHud() {
        beamCountLabel.setText("Beam: " + Mappers.player.get(player).beamCount);
        beamCountLabel.pack();

        TransformComponent transform = Mappers.transform.get(player);
        float labelPositionX = transform.position.x + transform.origin.x - beamCountLabel.getWidth() / 2;
        float labelPositionY = transform.position.y + transform.dimension.y;
        Vector2 labelPosition = CoordsUtils.worldToStageCoords(labelPositionX, labelPositionY, camera, hudStage);

        beamCountLabel.setPosition(labelPosition.x, labelPosition.y);

        for (ObjectMap.Entry<Entity, Label> entry : healthLabels.entries()) {
            Entity entity = entry.key;
            Label healthLabel = entry.value;

            healthLabel.setText("HP: " + MathUtils.ceil(Mappers.character.get(entity).health));
            healthLabel.pack();

            transform = Mappers.transform.get(entity);
            labelPositionX = transform.position.x + transform.origin.x - healthLabel.getWidth() / 2;
            labelPositionY = transform.position.y - healthLabel.getHeight();
            labelPosition = CoordsUtils.worldToStageCoords(labelPositionX, labelPositionY, camera, hudStage);

            healthLabel.setPosition(labelPosition.x, labelPosition.y);
        }
    }

    @Override
    public void hide() {
        batch.dispose();
        tiledRenderer.dispose();
        hudStage.dispose();
        uiStage.dispose();
        engine.removeEntityListener(this);
    }

    @Override
    public void entityAdded(Entity entity) {
        CharacterComponent character = Mappers.character.get(entity);
        if (character != null) {
            Label healthLabel = new Label("", Assets.instance.skin);
            healthLabels.put(entity, healthLabel);
            hudStage.addActor(healthLabel);
        }
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (healthLabels.containsKey(entity)) {
            healthLabels.remove(entity).remove();
        }
        if (entity == player) {
            beamCountLabel.remove();
        }
    }
}
