package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.DoodleJump;

public class MainMenuScreen implements Screen, InputProcessor {

    private DoodleJump game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private static final float BACK_HEIGHT = Gdx.graphics.getHeight();
    private static final float BACK_WIDTH = Gdx.graphics.getWidth();
    private static final int BUTTONS_WIDTH = 111;

    private Rectangle playBtn;
    private boolean playNow;
    private Rectangle optionsBtn;
    private boolean optionsNow;

    // fonts
    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private static BitmapFont font;

    // para saber o tamanho em X da string autoria
    private String autoria = "©2019 Luís Loureiro";
    private GlyphLayout layout;
    private float textWidth, textHeight;


    public MainMenuScreen(DoodleJump game){

        this.game = game;
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

        playBtn = new Rectangle(camera.viewportWidth / 4f
                - AssetLoader.playButtonInativo.getWidth()/2, camera.viewportHeight / 2-
                AssetLoader.playButtonInativo.getHeight()/2, AssetLoader.playButtonInativo.getWidth(),
                AssetLoader.playButtonInativo.getHeight());
        playNow = false;

        optionsBtn = new Rectangle(camera.viewportWidth / 1.35f
                - AssetLoader.optionsButtonInativo.getWidth()/2, camera.viewportHeight / 2-
                AssetLoader.optionsButtonInativo.getHeight()/2, AssetLoader.optionsButtonInativo.getWidth(),
                AssetLoader.optionsButtonInativo.getHeight());
        optionsNow = false;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/al-seana.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        font = generator.generateFont(parameter);
        font.setColor(255,255,255,255);

        // tamanho da autoria
        layout = new GlyphLayout(font, autoria);
        textWidth = layout.width;
        textHeight = layout.height;

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
        batch.draw(AssetLoader.mainMenuTile, camera.viewportWidth/2 - AssetLoader.mainMenuTile.getWidth()/2,
                camera.viewportHeight - AssetLoader.mainMenuTile.getHeight()*2);
        batch.draw(AssetLoader.mainMenuBottomPaper, -2, 30, BACK_WIDTH+1, AssetLoader.mainMenuBottomPaper.getRegionHeight());
        font.draw(batch, autoria, camera.viewportWidth/2 - textWidth/2, 15 + font.getLineHeight());

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


        if(playNow) batch.draw(AssetLoader.playButtonAtivo, playBtn.x, playBtn.y);
        else batch.draw(AssetLoader.playButtonInativo, playBtn.x, playBtn.y);
        if(optionsNow) batch.draw(AssetLoader.optionsButtonAtivo, optionsBtn.x, optionsBtn.y);
        else batch.draw(AssetLoader.optionsButtonInativo, optionsBtn.x, optionsBtn.y);
        batch.end();

        // shapes render -> debug
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.setColor(new Color(1, 0, 0, 1));
        //shapeRenderer.rect(playBtn.x,playBtn.y,playBtn.width,playBtn.height);
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
        generator.dispose();
        font.dispose();
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
        if(playBtn.contains(viewportX, viewportY)){
            playNow = true; // desenha a imagem mais avermelhada
        }
        if(optionsBtn.contains(viewportX, viewportY)){
            optionsNow = true; // desenha a imagem mais avermelhada
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        Gdx.app.log("touchUp", "x:" + screenX + " y:" + screenY);
        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        Gdx.app.log("View", "x:" + viewportX + " y:" + viewportY);

        if(playBtn.contains(viewportX, viewportY)){
            Gdx.app.log("RectContains", "play button");
            game.setScreen(new GameScreen(this.game));
            playNow = false;
        }
        if(optionsBtn.contains(viewportX, viewportY)){
            Gdx.app.log("RectContains", "options button");
            game.setScreen(new OptionsMenuScreen(this.game));
            optionsNow = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        // para nao ficar vermelho qd é arrastado o dedo
        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        if(playBtn.contains(viewportX, viewportY)){
            playNow = false;
        }
        if(optionsBtn.contains(viewportX, viewportY)){
            optionsNow = false;
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
