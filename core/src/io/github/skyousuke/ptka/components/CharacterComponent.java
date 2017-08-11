package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import io.github.skyousuke.ptka.utils.Direction;

/**
 * Created by Bill on 8/8/2560.
 */
public class CharacterComponent implements Component {

    public float health = 1f;
    public float movingSpeed;
    public Direction viewDirection;
}
