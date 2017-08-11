package io.github.skyousuke.ptka;

import com.badlogic.gdx.Game;
import io.github.skyousuke.ptka.screens.GameScreen;
import io.github.skyousuke.ptka.utils.Assets;

public class ProjectTKAshley extends Game {

    @Override
    public void create() {
        Assets.instance.init();
        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        Assets.instance.dispose();
    }
}
