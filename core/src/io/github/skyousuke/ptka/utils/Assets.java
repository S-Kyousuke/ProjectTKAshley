package io.github.skyousuke.ptka.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;

public class Assets implements Disposable, AssetErrorListener {

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public AssetManager manager;

    public TextureRegion bulletRegion;
    public TextureRegion beamRegion;
    public TextureRegion trapRegion;
    public TextureRegion enemyBallRegion;

    public TextureAtlas playerAltas;
    public TextureAtlas solarCellAltas;
    public TextureAtlas batteryAltas;
    public TextureAtlas inverterAltas;
    public TextureAtlas chargeControllerAltas;
    public TextureAtlas wireAltas;

    public Skin skin;

    public Texture rule1;
    public TextureRegion background;

    public TextureAtlas pepoAltas;
    public TextureAtlas pepoKnightAltas;
    public TextureAtlas pepoDevilAltas;

    public TiledMap map1;
    public TiledMap map2;
    public TiledMap map3;
    public TiledMap map4;
    public TiledMap testMap;

    public Music music;
    public Music introGameMusic;

    public Sound bulletSound;
    public Sound beamSound;
    public Sound trapSound;
    public Sound enemyBallSound;

    private Assets() {
    }

    public void init() {
        manager = new AssetManager();
        manager.setErrorListener(this);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("map1.tmx", TiledMap.class);
        manager.load("map2.tmx", TiledMap.class);
        manager.load("map3.tmx", TiledMap.class);
        manager.load("map4.tmx", TiledMap.class);
        manager.load("test-map.tmx", TiledMap.class);

        manager.load("uiskin.json", Skin.class, new SkinLoader.SkinParameter("uiskin.atlas"));

        manager.load("rule1.png", Texture.class);

        manager.load("player_pack.atlas", TextureAtlas.class);
        manager.load("bg.png", Texture.class);
        manager.load("bullet.png", Texture.class);
        manager.load("enemy_ball.png", Texture.class);
        manager.load("solarcell_pack.atlas", TextureAtlas.class);
        manager.load("cc_pack.atlas", TextureAtlas.class);
        manager.load("inverter_pack.atlas", TextureAtlas.class);
        manager.load("battery_pack.atlas", TextureAtlas.class);
        manager.load("link_pack.atlas", TextureAtlas.class);
        manager.load("tv_pack.atlas", TextureAtlas.class);
        manager.load("fan_pack.atlas", TextureAtlas.class);
        manager.load("trap.png", Texture.class);
        manager.load("beam.png", Texture.class);
        manager.load("enemy1_pack.atlas", TextureAtlas.class);
        manager.load("enemy2_pack.atlas", TextureAtlas.class);
        manager.load("enemy3_pack.atlas", TextureAtlas.class);
        manager.load("music.mp3", Music.class);
        manager.load("Dangerous.mp3", Music.class);
        manager.load("bullet.wav", Sound.class);
        manager.load("beam.wav", Sound.class);
        manager.load("trap.wav", Sound.class);
        manager.load("enemy_ball.wav", Sound.class);

        manager.finishLoading();

        map1 = manager.get("map1.tmx");
        map2 = manager.get("map2.tmx");
        map3 = manager.get("map3.tmx");
        map4 = manager.get("map4.tmx");
        testMap = manager.get("test-map.tmx");

        rule1 = manager.get("rule1.png");

        background = new TextureRegion((Texture) manager.get("bg.png"));
        bulletRegion = new TextureRegion((Texture) manager.get("bullet.png"));
        enemyBallRegion = new TextureRegion((Texture) manager.get("enemy_ball.png"));
        trapRegion = new TextureRegion((Texture) manager.get("trap.png"));
        beamRegion = new TextureRegion((Texture) manager.get("beam.png"));

        playerAltas = manager.get("player_pack.atlas");
        solarCellAltas = manager.get("solarcell_pack.atlas");
        chargeControllerAltas = manager.get("cc_pack.atlas");
        batteryAltas = manager.get("battery_pack.atlas");
        inverterAltas = manager.get("inverter_pack.atlas");
        wireAltas = manager.get("link_pack.atlas");
        pepoAltas = manager.get("enemy1_pack.atlas");
        pepoKnightAltas = manager.get("enemy2_pack.atlas");
        pepoDevilAltas = manager.get("enemy3_pack.atlas");

        music = manager.get("music.mp3");
        introGameMusic = manager.get("Dangerous.mp3");

        bulletSound = manager.get("bullet.wav");
        enemyBallSound = manager.get("enemy_ball.wav");
        beamSound = manager.get("beam.wav");
        trapSound = manager.get("trap.wav");

        skin = manager.get("uiskin.json");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        manager.dispose();
    }

}
