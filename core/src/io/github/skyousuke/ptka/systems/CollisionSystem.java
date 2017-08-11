package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import io.github.skyousuke.ptka.components.*;
import io.github.skyousuke.ptka.utils.Mappers;


/**
 * Created by Bill on 9/8/2560.
 */
public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> weapons;
    private ImmutableArray<Entity> enemyWeapons;

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class, ColliderComponent.class).get());
        enemies = engine.getEntitiesFor(Family.all(EnemyComponent.class, ColliderComponent.class).get());
        weapons = engine.getEntitiesFor(Family.all(WeaponComponent.class, ColliderComponent.class).get());
        enemyWeapons = engine.getEntitiesFor(Family.all(WeaponComponent.class, EnemyWeaponComponent.class, ColliderComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        updateEnemyAttackCollision();
        updateEnemyWeaponCollision();
        updatePlayerWeaponCollision();
    }

    private void updateEnemyAttackCollision() {
        for (int i = 0; i < players.size(); ++i) {
            Entity playerEntity = players.get(i);

            for (int j = 0; j < enemies.size(); ++j) {
                Entity enemyEntity = enemies.get(j);

                final Rectangle playerBounds = Mappers.collider.get(playerEntity).bounds;
                final Rectangle enemyBounds = Mappers.collider.get(enemyEntity).bounds;

                if (playerBounds.overlaps(enemyBounds) && !Mappers.invulnerable.has(playerEntity)) {

                    EnemySystem enemySystem = getEngine().getSystem(EnemySystem.class);
                    CharacterSystem characterSystem = getEngine().getSystem(CharacterSystem.class);

                    EnemyComponent enemy = Mappers.enemy.get(enemyEntity);
                    final float enemyAttackAngle = enemySystem.getAttackAngle(enemyBounds, playerBounds);

                    characterSystem.knockbackTo(playerEntity, enemy.attackKnockbackSpeed, enemyAttackAngle);
                    characterSystem.damageTo(playerEntity, enemy.attackDamage);
                    characterSystem.makeInvulnerableTo(playerEntity, 1f);

                    characterSystem.knockbackTo(enemyEntity, enemy.attackKnockbackSpeed, enemyAttackAngle + 180f);
                }
            }
        }
    }

    private void updateEnemyWeaponCollision() {
        for (int i = 0; i < players.size(); ++i) {
            Entity playerEntity = players.get(i);

            for (int j = 0; j < enemyWeapons.size(); ++j) {
                Entity weaponEntity = enemyWeapons.get(j);

                final Rectangle playerBounds = Mappers.collider.get(playerEntity).bounds;
                final Rectangle weaponBounds = Mappers.collider.get(weaponEntity).bounds;

                if (playerBounds.overlaps(weaponBounds)) {
                    CharacterSystem characterSystem = getEngine().getSystem(CharacterSystem.class);
                    WeaponSystem weaponSystem = getEngine().getSystem(WeaponSystem.class);

                    WeaponComponent weapon = Mappers.weapon.get(weaponEntity);

                    characterSystem.damageTo(playerEntity, weapon.damage);
                    characterSystem.knockbackTo(playerEntity, weapon.knockbackSpeed, weaponSystem.getKnockbackAngle(weapon));

                    getEngine().removeEntity(weaponEntity);
                }
            }
        }
    }

    private void updatePlayerWeaponCollision() {
        for (int i = 0; i < enemies.size(); ++i) {
            Entity enemyEntity = enemies.get(i);

            for (int j = 0; j < weapons.size(); ++j) {
                Entity weaponEntity = weapons.get(j);

                final Rectangle enemyBounds = Mappers.collider.get(enemyEntity).bounds;
                final Rectangle weaponBounds = Mappers.collider.get(weaponEntity).bounds;

                if (enemyBounds.overlaps(weaponBounds)) {
                    CharacterSystem characterSystem = getEngine().getSystem(CharacterSystem.class);
                    WeaponSystem weaponSystem = getEngine().getSystem(WeaponSystem.class);

                    WeaponComponent weapon = Mappers.weapon.get(weaponEntity);

                    characterSystem.damageTo(enemyEntity, weapon.damage);
                    characterSystem.knockbackTo(enemyEntity, weapon.knockbackSpeed, weaponSystem.getKnockbackAngle(weapon));

                    getEngine().removeEntity(weaponEntity);
                }
            }
        }
    }

    public void onTileCollision(Entity entity) {
        if (Mappers.weapon.has(entity)) {
            getEngine().removeEntity(entity);
        }
    }

    public boolean isCollidesRight(Rectangle bounds, TiledMapTileLayer mapLayer) {
        for (float step = 0; step < bounds.height; step += mapLayer.getTileHeight())
            if (isBlockedCell(bounds.x + bounds.width, bounds.y + step, mapLayer))
                return true;
        return isBlockedCell(bounds.x + bounds.width, bounds.y + bounds.height, mapLayer);
    }

    public boolean isCollidesLeft(Rectangle bounds, TiledMapTileLayer mapLayer) {
        for (float step = 0; step < bounds.height; step += mapLayer.getTileHeight())
            if (isBlockedCell(bounds.x, bounds.y + step, mapLayer))
                return true;
        return isBlockedCell(bounds.x, bounds.y + bounds.height, mapLayer);
    }

    public boolean isCollidesTop(Rectangle bounds, TiledMapTileLayer mapLayer) {
        for (float step = 0; step < bounds.width; step += mapLayer.getTileWidth())
            if (isBlockedCell(bounds.x + step, bounds.y + bounds.height, mapLayer))
                return true;
        return isBlockedCell(bounds.x + bounds.width, bounds.y + bounds.height, mapLayer);
    }

    public boolean isCollidesBottom(Rectangle bounds, TiledMapTileLayer mapLayer) {
        for (float step = 0; step < bounds.width; step += mapLayer.getTileWidth())
            if (isBlockedCell(bounds.x + step, bounds.y, mapLayer))
                return true;
        return isBlockedCell(bounds.x + bounds.width, bounds.y, mapLayer);
    }

    private boolean isBlockedCell(float x, float y, TiledMapTileLayer mapLayer) {
        int cellX = (int) (x / mapLayer.getTileWidth());
        int cellY = (int) (y / mapLayer.getTileHeight());
        final boolean hasCell = cellX < mapLayer.getWidth() && cellX >= 0 && cellY < mapLayer.getHeight() && cellY >= 0;
        return hasCell && mapLayer.getCell(cellX, cellY).getTile().getProperties().containsKey("blocked");
    }
}
