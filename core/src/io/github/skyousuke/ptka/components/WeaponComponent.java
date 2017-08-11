package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import io.github.skyousuke.ptka.utils.Direction;

public class WeaponComponent implements Component {

    public float damage;
    public float knockbackSpeed;
    public Direction travelDirection;
}
