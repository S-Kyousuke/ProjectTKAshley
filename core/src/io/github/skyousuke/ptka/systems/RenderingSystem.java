package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import io.github.skyousuke.ptka.components.SpriteComponent;
import io.github.skyousuke.ptka.components.TransformComponent;
import io.github.skyousuke.ptka.utils.Mappers;

import java.util.Comparator;

/**
 * Created by Bill on 8/8/2560.
 */

public class RenderingSystem extends SortedIteratingSystem {

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Array<Entity> renderQueue = new Array<Entity>();

    public RenderingSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(SpriteComponent.class, TransformComponent.class).get(), new LayerComparator(), 3);

        this.batch = batch;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity entity : renderQueue) {
            TransformComponent transform = Mappers.transform.get(entity);
            SpriteComponent sprite = Mappers.sprite.get(entity);

            batch.draw(sprite.region,
                    transform.position.x, transform.position.y,
                    transform.origin.x, transform.origin.y,
                    transform.dimension.x, transform.dimension.y,
                    1.0f, 1.0f, transform.rotation);
        }
        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    private static class LayerComparator implements Comparator<Entity> {
        @Override
        public int compare(Entity e1, Entity e2) {
            return Mappers.sprite.get(e1).layer - Mappers.sprite.get(e2).layer;
        }
    }
}
