package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.DoodleJump;
import com.mygdx.game.utils.ManageScore;

public class GameOverScreen implements Screen, InputProcessor {

    private DoodleJump game;
    private int score;
    private ManageScore manageScore;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private static final float BACK_HEIGHT = Gdx.graphics.getHeight();
    private static final float BACK_WIDTH = Gdx.graphics.getWidth();
    private static final int BUTTONS_WIDTH = 111;

    private Rectangle playAgainBtn;
    private boolean playNow;
    private Rectangle menuBtn;
    private boolean menuNow;


    public GameOverScreen(DoodleJump game, int score){

        this.game = game;
        this.score = score;
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.viewportWidth = Gdx.graphics.getWidth();

        //Setting the camera's initial position to the bottom left of the
        //map. The camera's position is in the center of the camera
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);

        //Update our camera
        camera.update();

        //Update the batch with our Camera's view and projection matrices
        batch.setProjectionMatrix(camera.combined);

        // To render shapes like a SpriteBatch
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        // To enable the touch user input (do not forget to implement InputProcessor
        Gdx.input.setInputProcessor(this);

        playAgainBtn = new Rectangle(camera.viewportWidth / 4f
                - AssetLoader.playButtonInativo.getWidth()/2, camera.viewportHeight / 2-
                AssetLoader.playButtonInativo.getHeight()*2, AssetLoader.playButtonInativo.getWidth(),
                AssetLoader.playButtonInativo.getHeight());
        playNow = false;

        menuBtn = new Rectangle(camera.viewportWidth / 1.35f
                - AssetLoader.menuButtonInativo.getWidth()/2, camera.viewportHeight / 2-
                AssetLoader.menuButtonInativo.getHeight()*2, AssetLoader.menuButtonInativo.getWidth(),
                AssetLoader.menuButtonInativo.getHeight());
        menuNow = false;

        manageScore = new ManageScore(this.score, camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(AssetLoader.backgroundMenu, 0, 155, BACK_WIDTH, BACK_HEIGHT);
        // titulo doodle jump
        batch.draw(AssetLoader.mainMenuTile, camera.viewportWidth/2 - AssetLoader.mainMenuTile.getWidth()/2,
                camera.viewportHeight - AssetLoader.mainMenuTile.getHeight()*2);
        // game over titulo
        batch.draw(AssetLoader.gameOver, camera.viewportWidth/2 - AssetLoader.gameOver.getWidth()/2,
                camera.viewportHeight / 2 + AssetLoader.gameOver.getHeight()*1.5f);
        // papel rasgado no fim
        batch.draw(AssetLoader.mainMenuBottomPaper, -2, 30, BACK_WIDTH+1, AssetLoader.mainMenuBottomPaper.getRegionHeight());

        AssetLoader.parameter.size = 80;
        AssetLoader.font.setColor(0,0,0,255);
        AssetLoader.font.draw(batch, "Score: ", camera.viewportWidth/5, camera.viewportHeight/2 + 110);
        manageScore.updateScore(batch);

        // bugs
        batch.draw(AssetLoader.bug1, camera.viewportWidth/2 - AssetLoader.bug1.getRegionWidth()/2,
                camera.viewportHeight - AssetLoader.mainMenuTile.getHeight()*2 - AssetLoader.bug1.getRegionHeight(),
                AssetLoader.bug1.getRegionWidth(), AssetLoader.bug1.getRegionHeight());
        batch.draw(AssetLoader.bug2, camera.viewportWidth/2 - AssetLoader.bug2.getRegionWidth()/2*4,
                camera.viewportHeight - AssetLoader.bug2.getRegionHeight()*3,
                AssetLoader.bug2.getRegionWidth(), AssetLoader.bug2.getRegionHeight());
        batch.draw(AssetLoader.bug3, camera.viewportWidth/1.5f - AssetLoader.bug3.getRegionWidth()/2,
                camera.viewportHeight - AssetLoader.bug3.getRegionHeight()*3,
                AssetLoader.bug3.getRegionWidth(), AssetLoader.bug3.getRegionHeight());
        batch.draw(AssetLoader.bug4, camera.viewportWidth/4 - AssetLoader.bug4.getRegionWidth()/2,
                camera.viewportHeight/1.5f - AssetLoader.bug4.getRegionHeight()/2,
                AssetLoader.bug4.getRegionWidth(), AssetLoader.bug4.getRegionHeight());
        batch.draw(AssetLoader.bug5, camera.viewportWidth/1.5f - AssetLoader.bug5.getRegionWidth()/2,
                camera.viewportHeight/4 - AssetLoader.bug5.getRegionHeight()/2,
                AssetLoader.bug5.getRegionWidth(), AssetLoader.bug5.getRegionHeight());

        // black hole
        batch.draw(AssetLoader.hole, camera.viewportWidth/1.5f - AssetLoader.hole.getWidth()/2,
                camera.viewportHeight/1.5f - AssetLoader.hole.getHeight()/2);

        // player
        batch.draw(AssetLoader.playerTexture1, camera.viewportWidth/4 - AssetLoader.playerTexture1.getWidth()/2,
                camera.viewportHeight/4 - AssetLoader.playerTexture1.getHeight()/2);


        if(playNow) batch.draw(AssetLoader.playButtonAtivo, playAgainBtn.x, playAgainBtn.y);
        else batch.draw(AssetLoader.playButtonInativo, playAgainBtn.x, playAgainBtn.y);
        if(menuNow) batch.draw(AssetLoader.menuButtonAtivo, menuBtn.x, menuBtn.y);
        else batch.draw(AssetLoader.menuButtonInativo, menuBtn.x, menuBtn.y);
        batch.end();

        // shapes render -> debug
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.setColor(new Color(1, 0, 0, 1));
        //shapeRenderer.rect(playAgainBtn.x,playAgainBtn.y,playAgainBtn.width,playAgainBtn.height);
        //shapeRenderer.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        batch.dispose();
        AssetLoader.dispose();
    }

    // input processor
    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        if(playAgainBtn.contains(viewportX, viewportY)){
            playNow = true; // desenha a imagem mais avermelhada
        }
        if(menuBtn.contains(viewportX, viewportY)){
            menuNow = true; // desenha a imagem mais avermelhada
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Gdx.app.log("touchUp", "x:" + screenX + " y:" + screenY);
        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        Gdx.app.log("View", "x:" + viewportX + " y:" + viewportY);

        if(playAgainBtn.contains(viewportX, viewportY)){
            Gdx.app.log("RectContains", "play button");
            game.setScreen(new GameScreen(this.game));
            playNow = false;
        }
        if(menuBtn.contains(viewportX, viewportY)){
            Gdx.app.log("RectContains", "options button");
            game.setScreen(new MainMenuScreen(game));
            menuNow = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        // para nao ficar vermelho qd é arrastado o dedo
        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        if(playAgainBtn.contains(viewportX, viewportY)){
            playNow = false;
        }
        if(menuBtn.contains(viewportX, viewportY)){
            menuNow = false;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}

