package io.github.skyousuke.ptka.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import io.github.skyousuke.ptka.utils.Pathfinding;

/**
 * Created by Bill on 8/8/2560.
 */
public class EnemyComponent implements Component {

    public float attackDamage;
    public float attackKnockbackSpeed;

    public float findingRange;
    public Pathfinding pathFinding;
    public Entity player;
}
