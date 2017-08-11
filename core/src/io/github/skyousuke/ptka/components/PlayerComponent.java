package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.skyousuke.ptka.utils.PlayerState;

/**
 * Created by Bill on 8/8/2560.
 */
public class PlayerComponent implements Component {

//    public int trapCount;
//    public int bulletCount;
    public int beamCount;
    public PlayerState state;
    public Entity item;
}
