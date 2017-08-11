package io.github.skyousuke.ptka.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import io.github.skyousuke.ptka.components.CameraHelperComponent;
import io.github.skyousuke.ptka.components.TransformComponent;
import io.github.skyousuke.ptka.utils.Mappers;

/**
 * Created by Bill on 8/8/2560.
 */
public class CameraHelperSystem extends IteratingSystem {

    public CameraHelperSystem() {
        super(Family.all(CameraHelperComponent.class).get(), 1);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CameraHelperComponent cameraHelper = Mappers.cameraHelper.get(entity);
        TransformComponent transform = Mappers.transform.get(entity);

        cameraHelper.speed = MathUtils.clamp(cameraHelper.speed, 0.01f, 1);

        if (cameraHelper.target != null) {
            followTarget(cameraHelper, transform);
        }

        final float halfViewportWidth = cameraHelper.camera.viewportWidth * 0.5f;
        final float halfViewportHeight = cameraHelper.camera.viewportHeight * 0.5f;

        limitZoom(halfViewportWidth, halfViewportHeight, cameraHelper);
        keepCameraInBound(halfViewportWidth * cameraHelper.zoom, halfViewportHeight * cameraHelper.zoom, cameraHelper, transform);
        applyToCamera(cameraHelper, transform);
    }

    private void followTarget(CameraHelperComponent cameraHelper, TransformComponent transform) {
        TransformComponent targetTransform = Mappers.transform.get(cameraHelper.target);

        transform.position.x += (targetTransform.position.x + targetTransform.origin.x - transform.position.x) * cameraHelper.speed;
        transform.position.y += (targetTransform.position.y + targetTransform.origin.y - transform.position.y) * cameraHelper.speed;
    }

    private void limitZoom(float halfViewportWidth, float halfViewportHeight,
                           CameraHelperComponent cameraHelper) {
        final float maxHorizontalZoom = (cameraHelper.rightMost - cameraHelper.leftMost) / (halfViewportWidth * 2);
        final float maxVerticalZoom = (cameraHelper.topMost - cameraHelper.bottomMost) / (halfViewportHeight * 2);
        final float maxZoom = Math.min(maxHorizontalZoom, maxVerticalZoom);
        final float minZoom = 0.01f;

        cameraHelper.zoom = MathUtils.clamp(cameraHelper.zoom, minZoom, maxZoom);
    }

    private void keepCameraInBound(float halfCameraWidth, float halfCameraHeight,
                                   CameraHelperComponent cameraHelper, TransformComponent transform) {

        if (transform.position.x > cameraHelper.rightMost - halfCameraWidth) {
            transform.position.x = cameraHelper.rightMost - halfCameraWidth;
        } else if (transform.position.x < cameraHelper.leftMost + halfCameraWidth)
            transform.position.x = cameraHelper.leftMost + halfCameraWidth;

        if (transform.position.y > cameraHelper.topMost - halfCameraHeight)
            transform.position.y = cameraHelper.topMost - halfCameraHeight;
        else if (transform.position.y < cameraHelper.bottomMost + halfCameraHeight)
            transform.position.y = cameraHelper.bottomMost + halfCameraHeight;
    }

    private void applyToCamera(CameraHelperComponent cameraHelper, TransformComponent transform) {
        cameraHelper.camera.position.x = transform.position.x;
        cameraHelper.camera.position.y = transform.position.y;
        cameraHelper.camera.zoom = cameraHelper.zoom;
        cameraHelper.camera.update();
    }
}
