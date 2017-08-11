package io.github.skyousuke.ptka.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class CoordsUtils {

    private CoordsUtils() {
    }

    private static Vector3 tempCoords = new Vector3();
    private static Vector2 stageCoords = new Vector2();

    public static Vector2 worldToStageCoords(float worldX, float worldY, Camera worldCamera, Stage stage) {
        tempCoords = worldCamera.project(tempCoords.set(worldX, worldY, 0));
        tempCoords = stage.getCamera().unproject(tempCoords.set(tempCoords.x, tempCoords.y, 0));
        tempCoords.y = stage.getCamera().viewportHeight - tempCoords.y;
        return stageCoords.set(tempCoords.x, tempCoords.y) ;
    }
}
