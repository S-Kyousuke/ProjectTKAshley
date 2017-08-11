package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.skyousuke.ptka.components.ColliderComponent;
import io.github.skyousuke.ptka.utils.Mappers;

/**
 * Created by Bill on 8/8/2560.
 */

public class DebugRenderingSystem extends IteratingSystem {

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;

    public DebugRenderingSystem(ShapeRenderer shapeRenderer, OrthographicCamera camera) {
        super(Family.one(ColliderComponent.class).get(), 4);
        this.shapeRenderer = shapeRenderer;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ColliderComponent collider = Mappers.collider.get(entity);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(collider.bounds.x, collider.bounds.y,
                collider.bounds.width, collider.bounds.height);

        shapeRenderer.end();
    }
}
