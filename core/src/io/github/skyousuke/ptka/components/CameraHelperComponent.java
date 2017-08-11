package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by Bill on 8/8/2560.
 */
public class CameraHelperComponent implements Component {

    public OrthographicCamera camera;
    public float zoom = 1f;
    public float speed = 0.1f;
    public Entity target;

    public float leftMost = -Float.MAX_VALUE;
    public float rightMost = Float.MAX_VALUE;
    public float topMost = Float.MAX_VALUE;
    public float bottomMost = -Float.MAX_VALUE;
}
