package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.EntitySystem;
import io.github.skyousuke.ptka.components.WeaponComponent;

public class WeaponSystem extends EntitySystem {

    public float getKnockbackAngle(WeaponComponent weaponComponent) {
        switch (weaponComponent.travelDirection) {
            case DOWN:
                return 270;
            case LEFT:
                return 180;
            case UP:
                return 90;
            case RIGHT:
                return 0;
            default:
                throw new IllegalArgumentException();
        }
    }
}
