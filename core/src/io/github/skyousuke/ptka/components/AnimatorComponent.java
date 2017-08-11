package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by Bill on 8/8/2560.
 */
public class AnimatorComponent implements Component {

    public ObjectMap<Enum, Animation<TextureRegion>> animations = new ObjectMap<Enum, Animation<TextureRegion>>();
    public Array<TextureAtlas.AtlasRegion> regions = new Array<TextureAtlas.AtlasRegion>();

    public Enum currentAnimation;

    public float animationTime;
    public boolean freeze;
}
