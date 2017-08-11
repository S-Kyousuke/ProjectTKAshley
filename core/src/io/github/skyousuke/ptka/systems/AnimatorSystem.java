package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import io.github.skyousuke.ptka.components.AnimatorComponent;
import io.github.skyousuke.ptka.components.SpriteComponent;
import io.github.skyousuke.ptka.components.TransformComponent;
import io.github.skyousuke.ptka.utils.Mappers;

import java.util.Comparator;

/**
 * Created by Bill on 8/8/2560.
 */
public class AnimatorSystem extends IteratingSystem {

    public static void setAtlasTo(AnimatorComponent animatorComponent, TextureAtlas atlas) {
        animatorComponent.regions = new Array<TextureAtlas.AtlasRegion>(atlas.getRegions());
        animatorComponent.regions.sort(new RegionNameComparator());
    }

    public static void addLoopTo(AnimatorComponent animator, Enum animationName, float frameTime, int regionStart, int size) {
        addTo(animator, animationName, frameTime, regionStart, size, Animation.PlayMode.LOOP);
    }

    public static void addNormalTo(AnimatorComponent animator, Enum animationName, float frameTime, int regionStart, int size) {
        addTo(animator, animationName, frameTime, regionStart, size, Animation.PlayMode.NORMAL);
    }

    public static void addTo(AnimatorComponent animator, Enum animationName, float frameTime, int regionStart, int size, Animation.PlayMode mode) {
        Array<TextureAtlas.AtlasRegion> animationRegions = new Array<TextureAtlas.AtlasRegion>();
        animationRegions.addAll(animator.regions, regionStart, size);
        animator.animations.put(animationName, new Animation<TextureRegion>(frameTime, animationRegions, mode));
    }

    public static boolean isFinished(AnimatorComponent animator, Enum animationName) {
        return animator.animations.get(animationName).isAnimationFinished(animator.animationTime);
    }

    public AnimatorSystem() {
        super(Family.all(AnimatorComponent.class, TransformComponent.class, SpriteComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transform = Mappers.transform.get(entity);
        SpriteComponent sprite = Mappers.sprite.get(entity);
        AnimatorComponent animator = Mappers.animator.get(entity);

        if (!animator.freeze) animator.animationTime += deltaTime;

        sprite.region = animator.animations.get(animator.currentAnimation).getKeyFrame(animator.animationTime);

        transform.dimension.x = sprite.region.getRegionWidth() * transform.scale.x;
        transform.dimension.y = sprite.region.getRegionHeight() * transform.scale.y;

        transform.origin.set(transform.dimension.x / 2, transform.dimension.y / 2);
    }

    private static class RegionNameComparator implements Comparator<TextureAtlas.AtlasRegion> {

        @Override
        public int compare(TextureAtlas.AtlasRegion region1, TextureAtlas.AtlasRegion region2) {
            return region1.name.compareTo(region2.name);
        }
    }
}
