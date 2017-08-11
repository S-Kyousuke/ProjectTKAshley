package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.github.skyousuke.ptka.components.AnimatorComponent;
import io.github.skyousuke.ptka.components.ItemComponent;
import io.github.skyousuke.ptka.utils.ItemAnimation;
import io.github.skyousuke.ptka.utils.ItemState;
import io.github.skyousuke.ptka.utils.Mappers;

public class ItemSystem extends IteratingSystem {

    public ItemSystem() {
        super(Family.all(ItemComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ItemComponent item = Mappers.item.get(entity);
        AnimatorComponent animator = Mappers.animator.get(entity);

        if (item.state == ItemState.ON) {
            animator.currentAnimation = ItemAnimation.ON;

        } else if (item.state == ItemState.OFF) {
            animator.currentAnimation = ItemAnimation.OFF;
        }

    }
}
