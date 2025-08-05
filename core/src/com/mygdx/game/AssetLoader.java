package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class AssetLoader {

    // in Game
    public static Texture playerTexture1, playerTexture0, playerTexture2, playerTexture3, backTexture;
    public static Texture tiles, projectiles, topScore, rocket, propeller;
    public static TextureRegion normalTile, movingTileX, movingTileY, disappearingTile, walkingEnemyLeft, walkingEnemyRight;
    public static TextureRegion projectileSpace;
    public static TextureRegion barra, pauseBtn;
    public static Texture resumeGame, backPause;
    public static TextureRegion[] Flyingenemy = new TextureRegion[5];
    public static Animation<TextureRegion> FlyingenemyAnimation;
    public static TextureRegion[] breakingTile = new TextureRegion[4];
    public static Animation<TextureRegion> breakingTileAnimation;
    public static TextureRegion[] rocketTravel = new TextureRegion[9];
    public static Animation<TextureRegion> rocketAnimation;
    public static TextureRegion[] propellerTravel = new TextureRegion[4];
    public static Animation<TextureRegion> propellerAnimation;
    public static TextureRegion spring, spring2, emptyRocket, emptyPropeller;
    public static TextureRegion lua1, planeta;

    // Menu
    public static Texture backgroundMenu, optionsMenu, mainMenuTile, mainMenuBottom, hole;
    public static Texture playButtonAtivo, playButtonInativo;
    public static Texture optionsButtonAtivo, optionsButtonInativo;
    public static Texture menuButtonAtivo, menuButtonInativo;
    public static Texture soundOnButtonAtivo, soundOnButtonInativo, soundOffButtonAtivo, soundOffButtonInativo;

    public static TextureRegion mainMenuBottomPaper, bug1, bug2, bug3, bug4, bug5;
    public static TextureRegion optionsMenuTitle, soundTitle;

    public static Texture gameOver;
    public static TextureRegion n1, n2, n3, n4, n5, n6, n7, n8, n9, n0;


    // sounds
    public static Sound shoot, jump, jumpOnEnemy, mola, breakPlatform, disappearPlatform, rocketSound, propellerSound;

    // fonts
    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public static BitmapFont font;


    public static void load() {

        // game
        backTexture = new Texture(Gdx.files.internal("playImgs/bck.png"));
        playerTexture1 = new Texture(Gdx.files.internal("playImgs/right.png"));
        playerTexture0 = new Texture(Gdx.files.internal("playImgs/left.png"));
        playerTexture2 = new Texture(Gdx.files.internal("playImgs/top.png"));
        playerTexture3 = new Texture(Gdx.files.internal("playImgs/shooter.png"));

        tiles = new Texture(Gdx.files.internal("playImgs/tiles.png"));
        rocket = new Texture(Gdx.files.internal("playImgs/rocket.png"));
        propeller = new Texture(Gdx.files.internal("playImgs/propeller-space.png"));
        normalTile = new TextureRegion(tiles, 0, 0, 130,33);
        movingTileX = new TextureRegion(tiles, 0, 365, 130,33);
        movingTileY = new TextureRegion(tiles, 0, 509, 130,33);
        disappearingTile = new TextureRegion(tiles, 0, 110, 130,33);
        walkingEnemyRight = new TextureRegion(tiles, 120, 370, 80,115);
        walkingEnemyLeft = new TextureRegion(tiles, 205, 370, 80,115);
        spring = new TextureRegion(tiles, 808, 195, 32, 35);
        spring2 = new TextureRegion(tiles, 808, 220, 32, 70);
        emptyRocket = new TextureRegion(tiles, 920, 800, 150, 220);
        emptyPropeller = new TextureRegion(propeller, 0, 0, 64, 64);
        lua1 = new TextureRegion(tiles, 1020, 230, 90, 330);
        planeta = new TextureRegion(tiles, 700, 700, 180, 200);
        planeta.flip(true, false);

        projectiles = new Texture(Gdx.files.internal("playImgs/projectiles.png"));
        projectileSpace = new TextureRegion(projectiles, 0, 50, 60,15);

        topScore = new Texture(Gdx.files.internal("playImgs/space-top-score.png"));
        barra = new TextureRegion(topScore, 0, 0, 630, topScore.getHeight());

        resumeGame = new Texture(Gdx.files.internal("playImgs/resume.png"));
        backPause = new Texture(Gdx.files.internal("playImgs/pause-cover.png"));

        pauseBtn = new TextureRegion(backPause, 580, 0, 40, 45);

        // obter a animacao do inimigo
        int stepX = 155; int stepY = 90; int x = 300; int y = 0;
        for(int i = 0; i < Flyingenemy.length; ++i){

            // (textura, posx, posy, width, height)
            TextureRegion txtreg = new TextureRegion(tiles, x, y, stepX, stepY);
            Flyingenemy[i] = txtreg;

            if(x >= 2* stepX){
                stepX+=4;
            }
            if(x >= 4 * stepX){
                x = 290;
                y += stepY;
            }else{
                x += stepX;
            }
        }
        // Animating the Flyingenemy
        FlyingenemyAnimation = new Animation(0.05f, Flyingenemy);
        FlyingenemyAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // obter a animacao da breaking tile
        int stepY1 = 33; int y1 = 144;
        for(int i = 0; i < breakingTile.length; ++i){

            // (textura, posx, posy, width, height)
            TextureRegion txtreg = new TextureRegion(tiles, 0, y1, 130, stepY1);
            breakingTile[i] = txtreg;

            stepY1 += 8 + (i*1);
            y1 += stepY1 + (i*1);

        }
        // Animating the breaking platform
        breakingTileAnimation = new Animation(0.1f, breakingTile);
        breakingTileAnimation.setPlayMode(Animation.PlayMode.NORMAL);


        // obter a animacao do rocket
        int stepX2 = 144; int stepY2 = 336; int x2 = 0; int y2 = 0;
        for(int i = 0; i < rocketTravel.length; ++i){

            // (textura, posx, posy, width, height)
            TextureRegion txtreg = new TextureRegion(rocket, x2, y2, stepX2, stepY2);
            rocketTravel[i] = txtreg;

            if(x2 >= 2*stepX2){
                x2 = 0;
                y2 += stepY2;
            }
            else{
                x2 += stepX2;
            }
        }
        // Animating the rocket
        rocketAnimation = new Animation(0.05f, rocketTravel);
        rocketAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // obter a animacao do propeller
        int stepX3 = 64; int stepY3 = 64; int x3 = 0; int y3 = 0;
        for(int i = 0; i < propellerTravel.length; ++i){

            // (textura, posx, posy, width, height)
            TextureRegion txtreg = new TextureRegion(propeller, x3, y3, stepX3, stepY3);
            propellerTravel[i] = txtreg;

            if(x3 >= 1*stepX3){
                x3 = 0;
                y3 += stepY3;
            }
            else{
                x3 += stepX3;
            }
        }
        // Animating the propeller
        propellerAnimation = new Animation(0.05f, propellerTravel);
        propellerAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        // menu
        mainMenuTile = new Texture(Gdx.files.internal("menuImgs/doodle-jump-title.png"));
        mainMenuBottom = new Texture(Gdx.files.internal("menuImgs/bottom-bug-tiles.png"));
        hole = new Texture(Gdx.files.internal("menuImgs/hole.png"));
        backgroundMenu = new Texture(Gdx.files.internal("menuImgs/backgroundMenu.png"));
        playButtonAtivo = new Texture(Gdx.files.internal("menuImgs/play-on.png"));
        playButtonInativo = new Texture(Gdx.files.internal("menuImgs/play.png"));
        optionsButtonAtivo = new Texture(Gdx.files.internal("menuImgs/options-on.png"));
        optionsButtonInativo = new Texture(Gdx.files.internal("menuImgs/options.png"));
        menuButtonAtivo = new Texture(Gdx.files.internal("menuImgs/menu-on.png"));
        menuButtonInativo = new Texture(Gdx.files.internal("menuImgs/menu.png"));
        soundOnButtonAtivo = new Texture(Gdx.files.internal("menuImgs/on-button-on.png"));
        soundOnButtonInativo = new Texture(Gdx.files.internal("menuImgs/on-button.png"));
        soundOffButtonAtivo = new Texture(Gdx.files.internal("menuImgs/off-button-on.png"));
        soundOffButtonInativo = new Texture(Gdx.files.internal("menuImgs/off-button.png"));

        mainMenuBottomPaper = new TextureRegion(mainMenuBottom, 0, 0, 640, mainMenuBottom.getHeight());
        bug1 = new TextureRegion(mainMenuBottom, 645, 0, 48, 40);
        bug2 = new TextureRegion(mainMenuBottom, 690, 0, 80, 40);
        bug3 = new TextureRegion(mainMenuBottom, 645, 43, 48, 40);
        bug4 = new TextureRegion(mainMenuBottom, 690, 43, 60, 50);
        bug5 = new TextureRegion(mainMenuBottom, 645, 90, 60, 40);
        optionsMenu = new Texture(Gdx.files.internal("menuImgs/options-bck-title.png"));
        optionsMenuTitle = new TextureRegion(optionsMenu, 0, 0, optionsMenu.getWidth(), 190);
        soundTitle = new TextureRegion(optionsMenu, 0, 730, optionsMenu.getWidth(), 80);
        gameOver = new Texture(Gdx.files.internal("playImgs/game_over.png"));
        n1 = new TextureRegion(topScore, 640, 0, 15, topScore.getHeight());
        n2 = new TextureRegion(topScore, 655, 0, 25, topScore.getHeight());
        n3 = new TextureRegion(topScore, 685, 0, 30, topScore.getHeight());
        n4 = new TextureRegion(topScore, 715, 0, 22, topScore.getHeight());
        n5 = new TextureRegion(topScore, 735, 0, 30, topScore.getHeight());
        n6 = new TextureRegion(topScore, 768, 0, 30, topScore.getHeight());
        n7 = new TextureRegion(topScore, 798, 0, 25, topScore.getHeight());
        n8 = new TextureRegion(topScore, 825, 0, 28, topScore.getHeight());
        n9 = new TextureRegion(topScore, 852, 0, 22, topScore.getHeight());
        n0 = new TextureRegion(topScore, 875, 0, 30, topScore.getHeight());

        // sounds
        // sounds, LibGDX supports 3 audio formats: ogg, mp3 and wav
        shoot = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot_sound.mp3"));
        jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.mp3"));
        jumpOnEnemy = Gdx.audio.newSound(Gdx.files.internal("sounds/jump_on_monster.mp3"));
        mola = Gdx.audio.newSound(Gdx.files.internal("sounds/mola.mp3"));
        breakPlatform = Gdx.audio.newSound(Gdx.files.internal("sounds/break_platform.mp3"));
        disappearPlatform = Gdx.audio.newSound(Gdx.files.internal("sounds/disappear_platform.mp3"));
        rocketSound = Gdx.audio.newSound(Gdx.files.internal("sounds/rocket.mp3"));
        propellerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/propeller.mp3"));

        // fonts
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/al-seana.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 80;
        font = generator.generateFont(parameter);
    }

    // dispose das texturas
    public static void dispose() {

        // game
        backTexture.dispose();
        playerTexture1.dispose();
        playerTexture0.dispose();
        playerTexture2.dispose();
        playerTexture3.dispose();

        tiles.dispose();
        rocket.dispose();
        propeller.dispose();
        projectiles.dispose();
        topScore.dispose();
        resumeGame.dispose();
        backPause.dispose();
        gameOver.dispose();

        // menu
        mainMenuTile.dispose();
        mainMenuBottom.dispose();
        backgroundMenu.dispose();
        playButtonAtivo.dispose();
        playButtonInativo.dispose();
        optionsButtonAtivo.dispose();
        optionsButtonInativo.dispose();
        menuButtonAtivo.dispose();
        menuButtonInativo.dispose();
        soundOnButtonAtivo.dispose();
        soundOnButtonInativo.dispose();
        soundOffButtonAtivo.dispose();
        soundOffButtonInativo.dispose();
        optionsMenu.dispose();
        hole.dispose();

        // sounds
        shoot.dispose();
        jump.dispose();
        jumpOnEnemy.dispose();
        mola.dispose();
        breakPlatform.dispose();
        rocketSound.dispose();
        propellerSound.dispose();

        // fonts
        generator.dispose();
        font.dispose();
    }
}
