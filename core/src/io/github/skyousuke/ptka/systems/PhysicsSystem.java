package io.github.skyousuke.ptka.systems;


import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.skyousuke.ptka.components.ColliderComponent;
import io.github.skyousuke.ptka.components.PhysicsComponent;
import io.github.skyousuke.ptka.components.TiledMapLayerComponent;
import io.github.skyousuke.ptka.components.TransformComponent;
import io.github.skyousuke.ptka.utils.Mappers;

/**
 * Created by Bill on 7/8/2560.
 */
public class PhysicsSystem extends IteratingSystem {

    public PhysicsSystem() {
        super(Family.all(TransformComponent.class, PhysicsComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);
        ColliderComponent collider = Mappers.collider.get(entity);

        updateVelocity(deltaTime, physics);

        transform.oldPosition.set(transform.position);

        if (collider == null) {
            transform.position.x += physics.velocity.x * deltaTime;
            transform.position.y += physics.velocity.y * deltaTime;

        } else {
            TiledMapLayerComponent mapLayerComp = Mappers.tiledMapLayer.get(entity);

            if (mapLayerComp == null) {
                transform.position.x += physics.velocity.x * deltaTime;
                collider.bounds.x = transform.position.x;

                transform.position.y += physics.velocity.y * deltaTime;
                collider.bounds.y = transform.position.y;

            } else {
                resolveTileCollision(entity, transform, physics, collider, mapLayerComp, deltaTime);
            }
        }
    }

    private void updateVelocity(float deltaTime, PhysicsComponent physics) {
        if (physics.velocity.x > 0) {
            physics.velocity.x = Math.max(physics.velocity.x - physics.friction.x * deltaTime, 0);
        } else if (physics.velocity.x < 0) {
            physics.velocity.x = Math.min(physics.velocity.x + physics.friction.x * deltaTime, 0);
        }
        if (physics.velocity.y > 0) {
            physics.velocity.y = Math.max(physics.velocity.y - physics.friction.y * deltaTime, 0);
        } else if (physics.velocity.y < 0) {
            physics.velocity.y = Math.min(physics.velocity.y + physics.friction.y * deltaTime, 0);
        }

        physics.velocity.x += physics.acceleration.x * deltaTime;
        physics.velocity.y += physics.acceleration.y * deltaTime;
    }

    private void resolveTileCollision(Entity entity, TransformComponent transform, PhysicsComponent physics,
                                      ColliderComponent collider, TiledMapLayerComponent mapLayerComp, float deltaTime) {
        CollisionSystem collisionSystem = getEngine().getSystem(CollisionSystem.class);
        boolean tileCollision = false;

        transform.position.x += physics.velocity.x * deltaTime;
        collider.bounds.x = transform.position.x;
        if (collisionSystem.isCollidesLeft(collider.bounds, mapLayerComp.mapLayer)
                || collisionSystem.isCollidesRight(collider.bounds, mapLayerComp.mapLayer)) {
            transform.position.x = transform.oldPosition.x;
            collider.bounds.x = transform.position.x;
            tileCollision = true;
        }

        transform.position.y += physics.velocity.y * deltaTime;
        collider.bounds.y = transform.position.y;
        if (collisionSystem.isCollidesTop(collider.bounds, mapLayerComp.mapLayer)
                || collisionSystem.isCollidesBottom(collider.bounds, mapLayerComp.mapLayer)) {
            transform.position.y = transform.oldPosition.y;
            collider.bounds.y = transform.position.y;
            tileCollision = true;
        }

        if (tileCollision)
            collisionSystem.onTileCollision(entity);
    }

}