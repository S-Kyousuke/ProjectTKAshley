package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bill on 8/8/2560.
 */
public class PhysicsComponent implements Component {

    public Vector2 velocity = new Vector2();
    public Vector2 friction = new Vector2();
    public Vector2 acceleration = new Vector2();
}
