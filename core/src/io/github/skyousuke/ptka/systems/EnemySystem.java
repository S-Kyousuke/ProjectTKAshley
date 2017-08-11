package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import io.github.skyousuke.ptka.components.*;
import io.github.skyousuke.ptka.utils.Direction;
import io.github.skyousuke.ptka.utils.EnemyAnimation;
import io.github.skyousuke.ptka.utils.Mappers;
import io.github.skyousuke.ptka.utils.Pathfinding;

/**
 * Created by Bill on 8/8/2560.
 */
public class EnemySystem extends IteratingSystem {

    public EnemySystem() {
        super(Family.all(EnemyComponent.class,
                TransformComponent.class,
                PhysicsComponent.class,
                AnimatorComponent.class,
                CharacterComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        PhysicsComponent physics = Mappers.physics.get(entity);
        AnimatorComponent animator = Mappers.animator.get(entity);
        CharacterComponent character = Mappers.character.get(entity);
        EnemyComponent enemy = Mappers.enemy.get(entity);
        KnockbackComponent knockback = Mappers.knockback.get(entity);
        StunComponent stun = Mappers.stun.get(entity);

        setAnimation(physics, character, animator);

        TransformComponent playerTransform = Mappers.transform.get(enemy.player);

        final float enemyX = transform.position.x + transform.origin.x;
        final float enemyY = transform.position.y + transform.origin.y;
        final float playerX = playerTransform.position.x + playerTransform.origin.x;
        final float playerY = playerTransform.position.y + playerTransform.origin.y;

        if (knockback == null && stun == null && isCanSeePlayer(enemyX, enemyY, playerX, playerY, enemy)) {
            runToPlayer(entity, enemyX, enemyY, playerX, playerY, enemy, character);
        }
    }

    private void setAnimation(PhysicsComponent physics, CharacterComponent character, AnimatorComponent animator) {
        animator.freeze = false;
        switch (character.viewDirection) {
            case DOWN:
                animator.currentAnimation = EnemyAnimation.WALK_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = EnemyAnimation.WALK_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = EnemyAnimation.WALK_RIGHT;
                break;
            case UP:
                animator.currentAnimation = EnemyAnimation.WALK_UP;
                break;
        }
        if (physics.velocity.isZero()) {
            animator.freeze = true;
            animator.animationTime = 0;
        }
    }

    private boolean isCanSeePlayer(float enemyX, float enemyY, float playerX, float playerY, EnemyComponent enemy) {
        final float xDiff = enemyX - playerX;
        final float yDiff = enemyY - playerY;
        final double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
        return distance <= enemy.findingRange;
    }

    private void runToPlayer(Entity enemyEntity, float enemyX, float enemyY, float playerX, float playerY,
                             EnemyComponent enemy, CharacterComponent character) {
        Array<Pathfinding.Node> list = enemy.pathFinding.findPath(enemyX, enemyY, playerX, playerY);
        if (list.size > 0) {
            Pathfinding.Node nextNode = list.get(0);

            float xDistanceToNextNode = nextNode.getPositionX() - enemyX;
            float yDistanceToNextNode = nextNode.getPositionY() - enemyY;

            final float minMovingDistance = character.movingSpeed / 8;
            final CharacterSystem characterSystem = getEngine().getSystem(CharacterSystem.class);

            if (yDistanceToNextNode > minMovingDistance) {
                characterSystem.move(enemyEntity, Direction.UP);
            } else if (yDistanceToNextNode < -minMovingDistance) {
                characterSystem.move(enemyEntity, Direction.DOWN);
            }

            if (xDistanceToNextNode > minMovingDistance) {
                characterSystem.move(enemyEntity, Direction.RIGHT);
            } else if (xDistanceToNextNode < -minMovingDistance) {
                characterSystem.move(enemyEntity, Direction.LEFT);
            }
        }
    }

    public float getAttackAngle(Rectangle enemyBounds, Rectangle targetBounds) {
        float yDistance = targetBounds.y + targetBounds.height / 2 - enemyBounds.y - enemyBounds.height / 2;
        float xDistance = targetBounds.x + targetBounds.width / 2 - enemyBounds.x - enemyBounds.width / 2;
        return MathUtils.atan2(yDistance, xDistance) * MathUtils.radiansToDegrees;
    }
}
