package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.skyousuke.ptka.components.*;
import io.github.skyousuke.ptka.utils.*;

/**
 * Created by Bill on 8/8/2560.
 */
public class PlayerSystem extends IteratingSystem {

    public PlayerSystem() {
        super(Family.all(PlayerComponent.class,
                PhysicsComponent.class,
                AnimatorComponent.class,
                CharacterComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physics = Mappers.physics.get(entity);
        AnimatorComponent animator = Mappers.animator.get(entity);
        CharacterComponent character = Mappers.character.get(entity);
        PlayerComponent player = Mappers.player.get(entity);

        if (player.state == PlayerState.STANDING && physics.velocity.isZero()) {
            if (player.item == null) {
                setStandAnimation(character, animator);
            } else {
                setCarryItemStandAnimation(character, animator);
            }
        } else if (player.state == PlayerState.ATTACKING && player.item == null) {
            setAttackAnimation(player, character, animator);
        } else if (player.item != null) {
            setCarryItemAnimation(character, animator, physics);
        } else {
            setWalkAnimation(player, character, animator, physics);
        }
    }

    private void setWalkAnimation(PlayerComponent playerComponent, CharacterComponent characterComponent,
                                  AnimatorComponent animator, PhysicsComponent physics) {
        switch (characterComponent.viewDirection) {
            case DOWN:
                animator.currentAnimation = PlayerAnimation.WALK_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = PlayerAnimation.WALK_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = PlayerAnimation.WALK_RIGHT;
                break;
            case UP:
                animator.currentAnimation = PlayerAnimation.WALK_UP;
                break;
        }
        if (physics.velocity.isZero()) {
            playerComponent.state = PlayerState.STANDING;
            animator.animationTime = 0;
        }
    }

    private void setCarryItemAnimation(CharacterComponent characterComponent,
                                       AnimatorComponent animator, PhysicsComponent physics) {
        switch (characterComponent.viewDirection) {
            case DOWN:
                animator.currentAnimation = PlayerAnimation.ITEM_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = PlayerAnimation.ITEM_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = PlayerAnimation.ITEM_RIGHT;
                break;
            case UP:
                animator.currentAnimation = PlayerAnimation.ITEM_UP;
                break;
        }
        if (physics.velocity.isZero()) {
            animator.freeze = true;
            animator.animationTime = 0;
        }
    }

    private void setAttackAnimation(PlayerComponent playerComponent, CharacterComponent characterComponent, AnimatorComponent animator) {
        switch (characterComponent.viewDirection) {
            case DOWN:
                animator.currentAnimation = PlayerAnimation.ATK_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = PlayerAnimation.ATK_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = PlayerAnimation.ATK_RIGHT;
                break;
            case UP:
                animator.currentAnimation = PlayerAnimation.ATK_UP;
                break;
        }
        if (AnimatorSystem.isFinished(animator, PlayerAnimation.ATK_LEFT)
                || AnimatorSystem.isFinished(animator, PlayerAnimation.ATK_RIGHT)
                || AnimatorSystem.isFinished(animator, PlayerAnimation.ATK_UP)
                || AnimatorSystem.isFinished(animator, PlayerAnimation.ATK_DOWN)) {
            playerComponent.state = PlayerState.STANDING;
            animator.animationTime = 0;
        }
    }

    private void setCarryItemStandAnimation(CharacterComponent characterComponent, AnimatorComponent animator) {
        switch (characterComponent.viewDirection) {
            case DOWN:
                animator.currentAnimation = PlayerAnimation.ITEM_STAND_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = PlayerAnimation.ITEM_STAND_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = PlayerAnimation.ITEM_STAND_RIGHT;
                break;
            case UP:
                animator.currentAnimation = PlayerAnimation.ITEM_STAND_UP;
                break;
        }
    }

    private void setStandAnimation(CharacterComponent characterComponent, AnimatorComponent animator) {
        switch (characterComponent.viewDirection) {
            case DOWN:
                animator.currentAnimation = PlayerAnimation.STAND_DOWN;
                break;
            case LEFT:
                animator.currentAnimation = PlayerAnimation.STAND_LEFT;
                break;
            case RIGHT:
                animator.currentAnimation = PlayerAnimation.STAND_RIGHT;
                break;
            case UP:
                animator.currentAnimation = PlayerAnimation.STAND_UP;
                break;
        }
    }

    public void beamAttack(Entity playerEnitiy, TiledMapTileLayer mapLayer) {
        PlayerComponent player = Mappers.player.get(playerEnitiy);
        AnimatorComponent animator = Mappers.animator.get(playerEnitiy);

        if (player.state != PlayerState.ATTACKING && player.item == null) {
            player.state = PlayerState.ATTACKING;
            animator.animationTime = 0;
            if (player.beamCount != 0) {
                final TransformComponent playerTransform = Mappers.transform.get(playerEnitiy);
                final CharacterComponent character = Mappers.character.get(playerEnitiy);

                final float beamPositionX = playerTransform.position.x + playerTransform.origin.x;
                final float beamPositionY = playerTransform.position.y + playerTransform.origin.y;

                getEngine().addEntity(EntityBuilder.buildBeam(beamPositionX, beamPositionY, mapLayer, character.viewDirection));
                player.beamCount--;
            }
            Assets.instance.beamSound.play();
        }
    }
}
