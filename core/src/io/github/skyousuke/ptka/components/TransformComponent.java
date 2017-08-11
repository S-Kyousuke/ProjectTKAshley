package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bill on 7/8/2560.
 */
public class TransformComponent implements Component {

    public Vector2 oldPosition = new Vector2();

    public Vector2 position = new Vector2();
    public Vector2 dimension = new Vector2();
    public Vector2 origin = new Vector2();
    public Vector2 scale = new Vector2(1f, 1f);
    public float rotation = 0;
}
