package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.skyousuke.ptka.components.*;
import io.github.skyousuke.ptka.utils.Direction;
import io.github.skyousuke.ptka.utils.Mappers;

/**
 * Created by Bill on 8/8/2560.
 */
public class CharacterSystem extends IteratingSystem {

    public CharacterSystem() {
        super(Family.all(CharacterComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PhysicsComponent physics = Mappers.physics.get(entity);
        KnockbackComponent knockback = Mappers.knockback.get(entity);
        StunComponent stun = Mappers.stun.get(entity);
        InvulnerableComponent invulnerable = Mappers.invulnerable.get(entity);

        long currentTime = TimeUtils.nanoTime();
        if (knockback != null && physics.velocity.isZero()) {
            entity.remove(KnockbackComponent.class);
        }
        if (stun != null && stun.stopTime < currentTime) {
            entity.remove(StunComponent.class);
        }
        if (invulnerable != null && invulnerable.stopTime <= currentTime) {
            entity.remove(InvulnerableComponent.class);
        }
    }

    public void move(Entity entity, Direction direction) {
        PhysicsComponent physics = Mappers.physics.get(entity);
        CharacterComponent character = Mappers.character.get(entity);

        switch (direction) {
            case LEFT:
                physics.velocity.x = -character.movingSpeed;
                break;
            case RIGHT:
                physics.velocity.x = character.movingSpeed;
                break;
            case DOWN:
                physics.velocity.y = -character.movingSpeed;
                break;
            case UP:
                physics.velocity.y = character.movingSpeed;
                break;
            default:
                break;
        }
        character.viewDirection = direction;
        physics.velocity.setLength(character.movingSpeed);
    }

    public void damageTo(Entity entity, float damage) {
        CharacterComponent character = Mappers.character.get(entity);
        character.health -= damage;
        if (character.health <= 0) {
            getEngine().removeEntity(entity);
        }
    }

    public void knockbackTo(Entity entity, float knockbackSpeed, float knockbackAngle) {
        PhysicsComponent physics = Mappers.physics.get(entity);
        physics.velocity.set(
                knockbackSpeed * MathUtils.cosDeg(knockbackAngle),
                knockbackSpeed * MathUtils.sinDeg(knockbackAngle));

        entity.add(new KnockbackComponent());
    }

    public void stunTo(Entity entity, float duration) {
        StunComponent stun = new StunComponent();

        final long nanosPerSecond = 1000000000;
        stun.stopTime = TimeUtils.nanoTime() + (long) (duration * nanosPerSecond);
        entity.add(stun);
    }

    public void makeInvulnerableTo(Entity entity, float duration) {
        InvulnerableComponent invulnerable = new InvulnerableComponent();

        final long nanosPerSecond = 1000000000;
        invulnerable.stopTime = TimeUtils.nanoTime() + (long) (duration * nanosPerSecond);
        entity.add(invulnerable);
    }
}
