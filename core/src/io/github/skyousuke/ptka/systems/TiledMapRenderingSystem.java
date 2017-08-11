package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.skyousuke.ptka.components.TiledMapComponent;
import io.github.skyousuke.ptka.utils.Mappers;

/**
 * Created by Bill on 8/8/2560.
 */
public class TiledMapRenderingSystem extends IteratingSystem {

    private OrthogonalTiledMapRenderer tiledRenderer;
    private OrthographicCamera camera;

    public TiledMapRenderingSystem(OrthogonalTiledMapRenderer tiledRenderer, OrthographicCamera camera) {
        super(Family.all(TiledMapComponent.class).get(), 2);
        this.tiledRenderer = tiledRenderer;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TiledMapComponent tiledMap = Mappers.tiledMap.get(entity);

        tiledRenderer.setView(camera);
        tiledRenderer.setMap(tiledMap.map);
        tiledRenderer.render();
    }
}
