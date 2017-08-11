package io.github.skyousuke.ptka.utils;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.skyousuke.ptka.components.*;
import io.github.skyousuke.ptka.systems.AnimatorSystem;

/**
 * Created by Bill on 8/8/2560.
 */
public class EntityBuilder {

    private EntityBuilder() {
    }

    public static Entity buildPlayer(float x, float y, TiledMapTileLayer mapLayer) {
        final float friction = 500f;
        final float movingSpeed = 120f;
        final float frameDuration = 1.0f / 16.0f;
        final float scale = 0.7f;
        final float health = 10f;
        final int beamCount = 100;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        CharacterComponent character = new CharacterComponent();
        PlayerComponent player = new PlayerComponent();
        TiledMapLayerComponent tiledMapLayer = new TiledMapLayerComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        physics.friction.set(friction, friction);

        TextureRegion firstRegion = Assets.instance.playerAltas.getRegions().get(0);
        collider.bounds.x = x;
        collider.bounds.y = y;
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 3;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.playerAltas);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.STAND_UP, frameDuration, 120, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.STAND_DOWN, frameDuration, 112, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.STAND_LEFT, frameDuration, 128, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.STAND_RIGHT, frameDuration, 136, 8);
        AnimatorSystem.addNormalTo(animator, PlayerAnimation.ATK_LEFT, frameDuration, 32, 8);
        AnimatorSystem.addNormalTo(animator, PlayerAnimation.ATK_RIGHT, frameDuration, 40, 8);
        AnimatorSystem.addNormalTo(animator, PlayerAnimation.ATK_UP, frameDuration, 24, 8);
        AnimatorSystem.addNormalTo(animator, PlayerAnimation.ATK_DOWN, frameDuration, 16, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.WALK_UP, frameDuration, 8, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.WALK_DOWN, frameDuration, 0, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.WALK_LEFT, frameDuration, 144, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.WALK_RIGHT, frameDuration, 152, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_UP, frameDuration, 56, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_DOWN, frameDuration, 48, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_LEFT, frameDuration, 64, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_RIGHT, frameDuration, 72, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_STAND_UP, frameDuration, 88, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_STAND_DOWN, frameDuration, 80, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_STAND_LEFT, frameDuration, 96, 8);
        AnimatorSystem.addLoopTo(animator, PlayerAnimation.ITEM_STAND_RIGHT, frameDuration, 104, 8);

        animator.currentAnimation = PlayerAnimation.STAND_UP;

        character.health = health;
        character.movingSpeed = movingSpeed;
        character.viewDirection = Direction.UP;

        player.state = PlayerState.STANDING;
        player.beamCount = beamCount;

        tiledMapLayer.mapLayer = mapLayer;

        return buildEntityFrom(transform, physics, collider, sprite, animator, character, player, tiledMapLayer);
    }

    public static Entity buildPepo(float x, float y, TiledMapTileLayer mapLayer, Entity player) {
        final float attackDamage = 1f;
        final float attackKnockbackSpeed = 190f;
        final float friction = 600f;
        final float movingSpeed = 50f;
        final float frameDuration = 1.0f / 8.0f;
        final float findingRange = 400f;
        final float scale = 1f;
        final float health = 5f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        CharacterComponent character = new CharacterComponent();
        EnemyComponent enemy = new EnemyComponent();
        TiledMapLayerComponent tiledMapLayer = new TiledMapLayerComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        physics.friction.set(friction, friction);

        TextureRegion firstRegion = Assets.instance.pepoAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 2;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.pepoAltas);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_UP, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_DOWN, frameDuration, 3, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_LEFT, frameDuration, 6, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_RIGHT, frameDuration, 9, 3);

        animator.currentAnimation = EnemyAnimation.WALK_DOWN;

        character.health = health;
        character.movingSpeed = movingSpeed;
        character.viewDirection = Direction.DOWN;

        enemy.attackDamage = attackDamage;
        enemy.attackKnockbackSpeed = attackKnockbackSpeed;
        enemy.player = player;
        enemy.pathFinding = new Pathfinding(mapLayer);
        enemy.findingRange = findingRange;

        tiledMapLayer.mapLayer = mapLayer;

        return buildEntityFrom(transform, physics, collider, sprite, animator, character, enemy, tiledMapLayer);
    }

    public static Entity buildPepoKnight(float x, float y, TiledMapTileLayer mapLayer, Entity player) {
        final float attackDamage = 1f;
        final float attackKnockbackSpeed = 205f;
        final float friction = 600f;
        final float movingSpeed = 60f;
        final float frameDuration = 1.0f / 8.0f;
        final float findingRange = 400f;
        final float scale = 1f;
        final float health = 8f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        CharacterComponent character = new CharacterComponent();
        EnemyComponent enemy = new EnemyComponent();
        TiledMapLayerComponent tiledMapLayer = new TiledMapLayerComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        physics.friction.set(friction, friction);

        TextureRegion firstRegion = Assets.instance.pepoKnightAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 2;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.pepoKnightAltas);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_UP, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_DOWN, frameDuration, 3, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_LEFT, frameDuration, 6, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_RIGHT, frameDuration, 9, 3);

        animator.currentAnimation = EnemyAnimation.WALK_DOWN;

        character.health = health;
        character.movingSpeed = movingSpeed;
        character.viewDirection = Direction.DOWN;

        enemy.attackDamage = attackDamage;
        enemy.attackKnockbackSpeed = attackKnockbackSpeed;
        enemy.player = player;
        enemy.pathFinding = new Pathfinding(mapLayer);
        enemy.findingRange = findingRange;

        tiledMapLayer.mapLayer = mapLayer;

        return buildEntityFrom(transform, physics, collider, sprite, animator, character, enemy, tiledMapLayer);
    }

    public static Entity buildPepoDevil(float x, float y, TiledMapTileLayer mapLayer, Entity player) {
        final float attackDamage = 1f;
        final float attackKnockbackSpeed = 230f;
        final float friction = 600f;
        final float movingSpeed = 80f;
        final float frameDuration = 1.0f / 8.0f;
        final float findingRange = 400f;
        final float scale = 0.5f;
        final float health = 15f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        CharacterComponent character = new CharacterComponent();
        EnemyComponent enemy = new EnemyComponent();
        TiledMapLayerComponent tiledMapLayer = new TiledMapLayerComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        physics.friction.set(friction, friction);

        TextureRegion firstRegion = Assets.instance.pepoDevilAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 2;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.pepoDevilAltas);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_UP, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_DOWN, frameDuration, 3, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_LEFT, frameDuration, 6, 3);
        AnimatorSystem.addLoopTo(animator, EnemyAnimation.WALK_RIGHT, frameDuration, 9, 3);

        animator.currentAnimation = EnemyAnimation.WALK_DOWN;

        character.health = health;
        character.movingSpeed = movingSpeed;
        character.viewDirection = Direction.DOWN;

        enemy.attackDamage = attackDamage;
        enemy.attackKnockbackSpeed = attackKnockbackSpeed;
        enemy.player = player;
        enemy.pathFinding = new Pathfinding(mapLayer);
        enemy.findingRange = findingRange;

        tiledMapLayer.mapLayer = mapLayer;

        return buildEntityFrom(transform, physics, collider, sprite, animator, character, enemy, tiledMapLayer);
    }

    public static Entity buildBattery(float x, float y) {
        final float frameDuration = 1.0f / 8.0f;
        final float scale = 1f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        ItemComponent item = new ItemComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        TextureRegion firstRegion = Assets.instance.batteryAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 1;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.batteryAltas);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.OFF, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.ON, frameDuration, 3, 3);

        animator.currentAnimation = ItemAnimation.OFF;

        item.state = ItemState.OFF;

        return buildEntityFrom(transform, physics, collider, sprite, animator, item);
    }

    public static Entity buildSolarCell(float x, float y) {
        final float frameDuration = 1.0f / 8.0f;
        final float scale = 1f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        ItemComponent item = new ItemComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        TextureRegion firstRegion = Assets.instance.solarCellAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 1;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.solarCellAltas);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.OFF, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.ON, frameDuration, 3, 3);

        animator.currentAnimation = ItemAnimation.OFF;

        item.state = ItemState.OFF;

        return buildEntityFrom(transform, physics, collider, sprite, animator, item);
    }

    public static Entity buildChargeController(float x, float y) {
        final float frameDuration = 1.0f / 8.0f;
        final float scale = 1f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        ItemComponent item = new ItemComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        TextureRegion firstRegion = Assets.instance.chargeControllerAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 1;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.chargeControllerAltas);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.OFF, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.ON, frameDuration, 3, 3);

        animator.currentAnimation = ItemAnimation.OFF;

        item.state = ItemState.OFF;

        return buildEntityFrom(transform, physics, collider, sprite, animator, item);
    }

    public static Entity buildInverter(float x, float y) {
        final float frameDuration = 1.0f / 8.0f;
        final float scale = 1f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        AnimatorComponent animator = new AnimatorComponent();
        ItemComponent item = new ItemComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);

        TextureRegion firstRegion = Assets.instance.inverterAltas.getRegions().get(0);
        collider.bounds.setPosition(x, y);
        collider.bounds.width = firstRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = firstRegion.getRegionHeight() * transform.scale.y;

        sprite.layer = 1;

        AnimatorSystem.setAtlasTo(animator, Assets.instance.inverterAltas);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.OFF, frameDuration, 0, 3);
        AnimatorSystem.addLoopTo(animator, ItemAnimation.ON, frameDuration, 3, 3);

        animator.currentAnimation = ItemAnimation.OFF;

        item.state = ItemState.OFF;

        return buildEntityFrom(transform, physics, collider, sprite, animator, item);
    }

    public static Entity buildBeam(float x, float y, TiledMapTileLayer mapLayer, Direction travelDirection) {
        final float friction = 50f;
        final float scale = 1f;
        final float damage = 1f;
        final float speed = 400f;
        final float knockbackSpeed = 100f;

        TransformComponent transform = new TransformComponent();
        PhysicsComponent physics = new PhysicsComponent();
        ColliderComponent collider = new ColliderComponent();
        SpriteComponent sprite = new SpriteComponent();
        WeaponComponent weapon = new WeaponComponent();
        TiledMapLayerComponent tiledMapLayer = new TiledMapLayerComponent();

        transform.position.set(x, y);
        transform.scale.set(scale, scale);
        transform.dimension.x = Assets.instance.beamRegion.getRegionWidth() * transform.scale.x;
        transform.dimension.y = Assets.instance.beamRegion.getRegionHeight() * transform.scale.y;
        transform.origin.set(transform.dimension.x / 2, transform.dimension.y / 2);

        physics.friction.set(friction, friction);

        collider.bounds.setPosition(x, y);
        collider.bounds.width = Assets.instance.beamRegion.getRegionWidth() * transform.scale.x;
        collider.bounds.height = Assets.instance.beamRegion.getRegionHeight() * transform.scale.y;

        sprite.region = Assets.instance.beamRegion;
        sprite.layer = 4;

        weapon.damage = damage;
        weapon.knockbackSpeed = knockbackSpeed;
        weapon.travelDirection = travelDirection;

        tiledMapLayer.mapLayer = mapLayer;

        switch (travelDirection) {
            case DOWN:
                transform.rotation = 90;
                physics.velocity.set(0, -speed);
                break;
            case LEFT:
                physics.velocity.set(-speed, 0);
                break;
            case RIGHT:
                physics.velocity.set(speed, 0);
                break;
            case UP:
                transform.rotation = 90;
                physics.velocity.set(0, speed);
                break;
        }

        return buildEntityFrom(transform, physics, collider, sprite, weapon, tiledMapLayer);
    }

    public static Entity buildCameraHelper(OrthographicCamera camera, TiledMapTileLayer mapLayer, Entity target) {
        TransformComponent transformComponent = new TransformComponent();
        CameraHelperComponent cameraHelper = new CameraHelperComponent();

        cameraHelper.speed = 0.05f;
        cameraHelper.target = target;
        cameraHelper.camera = camera;
        cameraHelper.leftMost = mapLayer.getTileWidth() * 1;
        cameraHelper.rightMost = (mapLayer.getWidth() - 1) * mapLayer.getTileWidth();
        cameraHelper.bottomMost = mapLayer.getTileHeight() * 1;
        cameraHelper.topMost = (mapLayer.getHeight() - 1) * mapLayer.getTileHeight();

        return buildEntityFrom(transformComponent, cameraHelper);
    }

    public static Entity buildMap(TiledMap tiledMap) {
        TiledMapComponent tiledMapComponent = new TiledMapComponent();
        tiledMapComponent.map = tiledMap;

        return buildEntityFrom(tiledMapComponent);
    }

    private static Entity buildEntityFrom(Component... components) {
        Entity entity = new Entity();
        for (Component component : components) {
            entity.add(component);
        }
        return entity;
    }
}
