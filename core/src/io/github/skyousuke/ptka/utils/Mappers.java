package io.github.skyousuke.ptka.utils;

import com.badlogic.ashley.core.ComponentMapper;
import io.github.skyousuke.ptka.components.*;

/**
 * Created by Bill on 8/8/2560.
 */
public class Mappers {

    public static final ComponentMapper<TransformComponent> transform = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<PhysicsComponent> physics = ComponentMapper.getFor(PhysicsComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<AnimatorComponent> animator = ComponentMapper.getFor(AnimatorComponent.class);
    public static final ComponentMapper<CameraHelperComponent> cameraHelper = ComponentMapper.getFor(CameraHelperComponent.class);
    public static final ComponentMapper<TiledMapComponent> tiledMap = ComponentMapper.getFor(TiledMapComponent.class);
    public static final ComponentMapper<TiledMapLayerComponent> tiledMapLayer = ComponentMapper.getFor(TiledMapLayerComponent.class);
    public static final ComponentMapper<CharacterComponent> character = ComponentMapper.getFor(CharacterComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<EnemyComponent> enemy = ComponentMapper.getFor(EnemyComponent.class);
    public static final ComponentMapper<ColliderComponent> collider = ComponentMapper.getFor(ColliderComponent.class);
    public static final ComponentMapper<ItemComponent> item = ComponentMapper.getFor(ItemComponent.class);
    public static final ComponentMapper<WeaponComponent> weapon = ComponentMapper.getFor(WeaponComponent.class);
    public static final ComponentMapper<KnockbackComponent> knockback = ComponentMapper.getFor(KnockbackComponent.class);
    public static final ComponentMapper<StunComponent> stun = ComponentMapper.getFor(StunComponent.class);
    public static final ComponentMapper<InvulnerableComponent> invulnerable = ComponentMapper.getFor(InvulnerableComponent.class);

    private Mappers() {
    }
}
