package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.DoodleJump;

public class OptionsMenuScreen implements Screen, InputProcessor {

    private DoodleJump game;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private static final float BACK_HEIGHT = Gdx.graphics.getHeight();
    private static final float BACK_WIDTH = Gdx.graphics.getWidth();

    private Rectangle soundOnBtn, soundOffBtn, menuBtn;
    private static boolean soundOnNow = true, soundOffNow = false, menuNow;
    public static boolean allowSound = true;

    // fonts
    private static FreeTypeFontGenerator generator;
    private static FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private static BitmapFont font;


    public OptionsMenuScreen(DoodleJump game){

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

        soundOnBtn = new Rectangle(camera.viewportWidth / 2 - AssetLoader.soundOnButtonInativo.getWidth()/2,
                camera.viewportHeight / 1.3f - AssetLoader.soundOnButtonInativo.getHeight()/2,
                AssetLoader.soundOnButtonInativo.getWidth()*1.5f,
                AssetLoader.soundOnButtonInativo.getHeight() *1.5f);
        //soundOnNow = true;

        soundOffBtn = new Rectangle(camera.viewportWidth / 4 - AssetLoader.soundOffButtonInativo.getWidth()/2,
                camera.viewportHeight / 1.3f - AssetLoader.soundOffButtonInativo.getHeight()/2,
                AssetLoader.soundOffButtonInativo.getWidth()*1.5f,
                AssetLoader.soundOffButtonInativo.getHeight()*1.5f);
        //soundOffNow = false;

        menuBtn = new Rectangle(camera.viewportWidth / 2 - AssetLoader.menuButtonInativo.getWidth()/2,
                camera.viewportHeight / 6 - AssetLoader.menuButtonInativo.getHeight()/2,
                AssetLoader.menuButtonInativo.getWidth(),
                AssetLoader.menuButtonInativo.getHeight());
        menuNow = false;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/al-seana.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 60;
        font = generator.generateFont(parameter);
        font.setColor(0,0,0,255);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();


        batch.draw(AssetLoader.backgroundMenu, 0, 0, BACK_WIDTH, BACK_HEIGHT);
        batch.draw(AssetLoader.optionsMenuTitle, 0, camera.viewportHeight - AssetLoader.optionsMenuTitle.getRegionHeight());
        batch.draw(AssetLoader.soundTitle, 0, camera.viewportHeight/1.3f + AssetLoader.soundTitle.getRegionHeight());
        batch.draw(AssetLoader.soundOffButtonInativo, soundOffBtn.x, soundOffBtn.y, soundOffBtn.width, soundOffBtn.height);
        batch.draw(AssetLoader.soundOnButtonInativo, soundOnBtn.x, soundOnBtn.y, soundOnBtn.width, soundOnBtn.height);
        if(soundOnNow) batch.draw(AssetLoader.soundOnButtonAtivo, soundOnBtn.x, soundOnBtn.y, soundOnBtn.width, soundOnBtn.height);
        if(soundOffNow) batch.draw(AssetLoader.soundOffButtonAtivo, soundOffBtn.x, soundOffBtn.y, soundOffBtn.width, soundOffBtn.height);
        if(menuNow) batch.draw(AssetLoader.menuButtonAtivo, menuBtn.x, menuBtn.y);
        else batch.draw(AssetLoader.menuButtonInativo, menuBtn.x, menuBtn.y);

        font.draw(batch, "Tilt do move left or right, \n Touch the screen to shoot!" +
                "\n \n Avoid the monsters or \n shoot and jump on them..." +
                "\n \n Jump on the platforms, \n but be careful...", 100, camera.viewportHeight/1.6f + font.getLineHeight());

        batch.end();

        // shapes render -> debug
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(new Color(1, 0, 0, 1));
//        shapeRenderer.rect(soundOnBtn.x,soundOnBtn.y,soundOnBtn.width,soundOnBtn.height);
//        shapeRenderer.end();
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
        // se tocar e o som nao estiver ativo -> ativar
        if(soundOnBtn.contains(viewportX, viewportY) && !soundOnNow){
            soundOnNow = true;
            soundOffNow = false;
            // ativar som
            allowSound = true;
        }
        // se tocar e o som nao estiver ativo -> ativar
        if(soundOffBtn.contains(viewportX, viewportY) && !soundOffNow){
            soundOnNow = false;
            soundOffNow = true;
            // desativar som
            allowSound = false;
        }

        // se tocar ativar o On do botao
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

        // se tocar ir para o main menu
        if(menuBtn.contains(viewportX, viewportY)){
            game.setScreen(new MainMenuScreen(game));
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        // para nao ficar vermelho qd é arrastado o dedo
        int viewportX = screenX;
        int viewportY = Gdx.graphics.getHeight() - screenY;
        // se tocar ativar o On do botao
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

