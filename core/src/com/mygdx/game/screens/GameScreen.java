package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
// import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.DoodleJump;
import com.mygdx.game.objects.Spring;
import com.mygdx.game.plataforma.Platform;
import com.mygdx.game.plataforma.tipos_plataformas.BreakingPlatform;
import com.mygdx.game.plataforma.tipos_plataformas.DisappearingPlatform;
import com.mygdx.game.utils.Ground;
import com.mygdx.game.utils.LevelGenerator;
import com.mygdx.game.player.Player;
import com.mygdx.game.player.projectile.Projectile;

import java.util.ArrayList;

//  This class makes the rendering on the screen and therefore typically
//  contains the "world" and "camera" objects
public class GameScreen implements Screen, InputProcessor, ContactListener {

    private DoodleJump game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private World world;
    private Player player;
    private LevelGenerator levelGenerator;
    private Ground ground, teto;
    private ArrayList<Projectile> balas;
    private Rectangle pauseButton;

    // private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private boolean canUseAcel = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

    private static  float BOX_STEP = 1 / 45f;
    private static final int BOX_VELOCITY_ITERATIONS = 6;
    private static final int BOX_POSITION_ITERATIONS = 2;
    private static final float GRAVITY = -25f;

    private boolean isPaused = false;
    private boolean acertouTiro = false;
    private float elapsedTime = 0;
    private int score = 0;
    private long tempoShoot;
    private final static int salto = 25;

    public GameScreen(DoodleJump game){

        this.game = game;

        batch = new SpriteBatch();

        camera = new OrthographicCamera(28, 48);

        //Setting the camera's initial position to the bottom left of the
        //map. The camera's position is in the center of the camera
        camera.position.set(camera.viewportWidth * .5f, camera.viewportHeight * .5f, 0f);
        //Update our camera
        camera.update();
        //Update the batch with our Camera's view and projection matrices
        batch.setProjectionMatrix(camera.combined);

        // First argument sets the horizontal and vertical gravity forces
        // Second argument is a boolean value which tells the world if we
        // want objects to sleep or not (to conserve CPU usage)
        world = new World(new Vector2(0, GRAVITY), true);
        player = new Player(world, camera.viewportWidth/2, camera.viewportHeight/4);
        ground = new Ground(world, new Vector2(-camera.viewportWidth/2,0), camera.viewportWidth * 2, 0f); // aumentar a altura para ve-lo
        teto = new Ground(world, new Vector2(0,camera.viewportHeight), camera.viewportWidth, 0f);
        //levelGenerator = new LevelGenerator(world, camera.position.y * -0.1f, camera.position.y * 0.40f); // tava 0.40
        // quanto maior o valor maxY mais afastadas estam as plataformas
        levelGenerator = new LevelGenerator(world, camera.position.y * -0.1f, camera.position.y * 0.51f);
        balas = new ArrayList<Projectile>();

        debugRenderer = new Box2DDebugRenderer();
        Gdx.input.setInputProcessor(this);
        world.setContactListener(this);

        pauseButton = new Rectangle(camera.viewportWidth-1, camera.position.y + (camera.viewportHeight / 2) - 2.6f, 2, 2);

        // To render shapes like a SpriteBatch -> para ver o rectangulo desenhado
        // shapeRenderer = new ShapeRenderer();
        // shapeRenderer.setProjectionMatrix(camera.combined);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        runGame(delta);
    }

    private void runGame(float delta){

        elapsedTime+=delta;

        // so usa o acelerometro se nao tiver em pausa e ainda nao perdeu
        if(canUseAcel && !isPaused){
            player.processAccelerometer(world);
        }

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        debugRenderer.render(world, camera.combined); // faz o rebordo a volta dos sprites (debug)

        // se a posição y do player for maior que a da camera
        if(player.getPosition().y > camera.position.y){

            // alterar a posição y da camera para a posiçao y do player
            //camera.position.set(camera.position.x, player.getPosition().y, camera.position.z);
            camera.position.y += (player.getPosition().y - camera.position.y) * 0.1f;
            score = (int)camera.position.y;
        }

        // atualiza a posiçao do ground para a posiçao y da camera mas de forma a nao se ver (sempre no chao)
        ground.getGroundBody().setTransform(new Vector2(ground.getGroundBody().getPosition().x,
                camera.position.y - (camera.viewportHeight / 2)), 0);

        teto.getGroundBody().setTransform(new Vector2(teto.getGroundBody().getPosition().x,
                camera.position.y + (camera.viewportHeight / 2)), 0);

        // faz update á matrix de projeçao do batch
        // se nao fizermos update o batch não sabe o que desenhar no ecra
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // desenha a imagem de background
        // + 3 na posy e na altura pq quando atualiza a posiçao da camera fica azul em baixo, assim aumentamos a altura da imagem, o que resolve
        batch.draw(AssetLoader.backTexture, camera.position.x - (camera.viewportWidth / 2), camera.position.y - (camera.viewportHeight / 2) - 3f,
                camera.viewportWidth, camera.viewportHeight + 3f);

        // desenha lua e planeta
        batch.draw(AssetLoader.lua1, camera.position.x + (camera.viewportWidth / 2) - 5,
                camera.position.y , 5, 20);
        batch.draw(AssetLoader.planeta, camera.position.x - (camera.viewportWidth / 2) - 0.5f,
                camera.position.y - camera.viewportHeight / 2 + 10, 10, 10);

        // desenha as plataformas
        levelGenerator.drawPlatformsAndEnemies(batch, elapsedTime);

        // gere a posição x do player quando este ultrapassa o maximo
        player.handleWallPosition((int)camera.viewportWidth);
        // desenha o player
        player.draw(batch, elapsedTime);
        // remove o rocket quando voltar a descer a vel em y (<=0)
        player.removeRocketOrPropeller();
        // faz o player andar no rocket ou no propeller dependedo do isInRocket e do isInPropeller
        if(!isPaused) player.travelInRocketOrPropeller();


        // dispara a bala e destroi-a se esta ultrapassar o teto ou acertar no inimigo
        for(int i = balas.size() - 1; i >= 0; i--){

            Projectile bala = balas.get(i);
            bala.draw(batch);
            player.shoot(bala);
            bala.destroyProjectile(balas, bala, teto.getGroundBody(), acertouTiro);
        }

        // desenha a imagem de pontuacao com o botao para fazer pause
        batch.draw(AssetLoader.barra, 0, camera.position.y + (camera.viewportHeight / 2) - 5, camera.viewportWidth, 5);
        pauseButton.setPosition(camera.viewportWidth - 2.65f, camera.position.y + (camera.viewportHeight / 2) - 2.3f);

        // para ver o rectangulo do botao de pausa
        // shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        // shapeRenderer.setColor(new Color(1, 0, 0, 1));
        // shapeRenderer.rect(pauseButton.x,pauseButton.y,pauseButton.width,pauseButton.height);
        // shapeRenderer.end();

        // desenha botao de pausa
        batch.draw(AssetLoader.pauseBtn, pauseButton.x, pauseButton.y, pauseButton.width, pauseButton.height);

        // se tiver em pausa desenho a imagem de pausa
        if(isPaused){
            batch.draw(AssetLoader.backPause, camera.position.x - (camera.viewportWidth / 2), camera.position.y - (camera.viewportHeight / 2) - 3f,
                camera.viewportWidth, camera.viewportHeight + 3f);
        }

        batch.end();

        // System.out.println("Número de bodies no mundo = " + world.getBodyCount());
        // System.out.println("Posiçao da camera = " + camera.position.y);

        // destroi as plataformas e inimigos que estam abaixo do ground e
        // gera o numero de plataformas e inimigos que foram destruidos
        int nPlataformasCriar = levelGenerator.destroyPlatforms(ground.getGroundBody());
        // destroi o inimigo se o matou (acertou tiro) ou ent se ele passou o ground
        int nInimigosCriar = levelGenerator.destroyEnemies(ground.getGroundBody(), acertouTiro);
        acertouTiro = false; // atualiza a variavel para o proximo inimigo
        levelGenerator.generateNewPlatformsAndEnemies(nPlataformasCriar, nInimigosCriar);

        // faz update á posição da camera
        camera.update();

        // The best place to call our step function is at the end of our render() loop
        world.step(BOX_STEP, BOX_VELOCITY_ITERATIONS, BOX_POSITION_ITERATIONS);
    }


    @Override
    public void resize(int width, int height) {
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
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // obtem as coordenadas certas do touch
        // é feito isto pq a posicao do button aumenta de acordo com a da camera
        Vector3 novasCoord = new Vector3();
        camera.unproject(novasCoord.set(screenX, screenY, 0));

        // pausa o jogo
        if(pauseButton.contains(novasCoord.x, novasCoord.y) && !isPaused){
            Gdx.app.log("RectContains", "pause button");
            isPaused = true;
            BOX_STEP = 0; // faz freeze no jogo
            AssetLoader.FlyingenemyAnimation.setFrameDuration(0); // para a animaçao
        }
        // faz resume ao jogo
        else if(pauseButton.contains(novasCoord.x, novasCoord.y) && isPaused){
            Gdx.app.log("RectContains", "pause button");
            BOX_STEP = 1 / 45f;
            isPaused = false;
            AssetLoader.FlyingenemyAnimation.setFrameDuration(0.05f); // volta a por a animaçao
        }
        // se o jogo nao tiver pausado e o player nao tiver no rocket ou no propeller ent posso ativar o shooter
        else if (!isPaused && !player.isInRocket() && !player.isInPropeller()){
            // diz que player esta a disparar para fazer a animaçao de disparar
            player.setShooting(true);
            tempoShoot = TimeUtils.millis();
            // cria uma bala nova e adiciona-a á lista de balas
            Projectile bala = new Projectile(world, player.getPosition().x + player.getWidth()/2 - 1.2f/2 + 0.2f,
                    player.getPosition().y + 4, 0.3f);
            balas.add(bala);
            // faz o som de disparo
            if(OptionsMenuScreen.allowSound) AssetLoader.shoot.stop();
            if(OptionsMenuScreen.allowSound) AssetLoader.shoot.play();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        player.setShooting(false);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        long diffInMillis = TimeUtils.timeSinceMillis(tempoShoot);
        //System.out.println("TEMPO" + diffInMillis);
        // se passaram 0.3 segundos entao voltar a por o player com a textura inicial (tirar o shooter)
        if(diffInMillis > 300) player.setShooting(false);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    @Override
    public void beginContact(Contact contact) {

        // fixtureA = plataformas e grounds
        Body fixtureA = contact.getFixtureA().getBody();
        // fixtureB = player e bala
        Body fixtureB = contact.getFixtureB().getBody();

        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA = " + contact.getFixtureA().getBody().getUserData());
        //System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBB = " + contact.getFixtureB().getBody().getUserData());

        // se o player tiver a cair e nao tocar no chao
        if(((fixtureB.getUserData() == "Player") && (fixtureB.getLinearVelocity().y <= 0) && (fixtureA.getUserData() != "ground")) ||
           ((fixtureA.getUserData() == "Player") && (fixtureA.getLinearVelocity().y <= 0) && (fixtureB.getUserData() != "ground"))){

            Spring mola;
            // se saltar numa mola
            if((fixtureA.getUserData() == "spring")){

                mola = levelGenerator.getSpringWithCoordinates(fixtureA.getPosition());
                if(mola != null) mola.setJumpOnSpring(true);
                player.jump(salto * 2); // faz um jump com o dobro do tamanho
                if(OptionsMenuScreen.allowSound) AssetLoader.mola.stop();
                if(OptionsMenuScreen.allowSound) AssetLoader.mola.play();
            } else if (fixtureB.getUserData() == "spring"){

                mola = levelGenerator.getSpringWithCoordinates(fixtureB.getPosition());
                if(mola != null) mola.setJumpOnSpring(true);
                player.jump(salto * 2); // faz um jump com o dobro do tamanho
                if(OptionsMenuScreen.allowSound) AssetLoader.mola.stop();
                if(OptionsMenuScreen.allowSound) AssetLoader.mola.play();
            }
            // se saltar numa plataforma
            else {
                Platform platform; // plataforma que o player salta
                // se for plataforma rocket
                if(fixtureA.getUserData() == "propeller"){

                    player.setInPropeller(true);
                    if(OptionsMenuScreen.allowSound) AssetLoader.propellerSound.stop();
                    if(OptionsMenuScreen.allowSound) AssetLoader.propellerSound.play();
                }
                else if(fixtureB.getUserData() == "propeller"){

                    player.setInPropeller(true);
                    if(OptionsMenuScreen.allowSound) AssetLoader.propellerSound.stop();
                    if(OptionsMenuScreen.allowSound) AssetLoader.propellerSound.play();
                }
                // se for plataforma rocket
                else if(fixtureA.getUserData() == "rocket"){

                    player.setInRocket(true);
                    if(OptionsMenuScreen.allowSound) AssetLoader.rocketSound.stop();
                    if(OptionsMenuScreen.allowSound) AssetLoader.rocketSound.play();
                }
                else if(fixtureB.getUserData() == "rocket"){

                    player.setInRocket(true);
                    if(OptionsMenuScreen.allowSound) AssetLoader.rocketSound.stop();
                    if(OptionsMenuScreen.allowSound) AssetLoader.rocketSound.play();
                }
                // se for plataforma breaking
                else if(fixtureA.getUserData() == "breaking"){

                    platform = levelGenerator.getPlatformWithCoordinates(fixtureA.getPosition());
                    if(platform != null){
                        ((BreakingPlatform) platform).setStartAnimation(true);
                        if(OptionsMenuScreen.allowSound) AssetLoader.breakPlatform.stop();
                        if(OptionsMenuScreen.allowSound) AssetLoader.breakPlatform.play();

                    }
                }
                else if(fixtureB.getUserData() == "breaking"){

                    platform = levelGenerator.getPlatformWithCoordinates(fixtureB.getPosition());
                    if(platform != null){
                        ((BreakingPlatform) platform).setStartAnimation(true);
                        if(OptionsMenuScreen.allowSound) AssetLoader.breakPlatform.stop();
                        if(OptionsMenuScreen.allowSound) AssetLoader.breakPlatform.play();
                    }
                }
                // se for plataforma disappearing
                else if(fixtureA.getUserData() == "disappearing"){

                    platform = levelGenerator.getPlatformWithCoordinates(fixtureA.getPosition());
                    if(platform != null){
                        player.jump(salto);
                        ((DisappearingPlatform) platform).setSaltou(true);
                        if(OptionsMenuScreen.allowSound) AssetLoader.disappearPlatform.stop();
                        if(OptionsMenuScreen.allowSound) AssetLoader.disappearPlatform.play();

                    }
                }
                else if(fixtureB.getUserData() == "disappearing"){

                    platform = levelGenerator.getPlatformWithCoordinates(fixtureB.getPosition());
                    if(platform != null){
                        player.jump(salto);
                        ((DisappearingPlatform) platform).setSaltou(true);
                        if(OptionsMenuScreen.allowSound) AssetLoader.disappearPlatform.stop();
                        if(OptionsMenuScreen.allowSound) AssetLoader.disappearPlatform.play();
                    }
                }else{
                    // faz um jump da o som do jump se for plataforma sem ser a breaking
                    player.jump(salto);
                    if(OptionsMenuScreen.allowSound) AssetLoader.jump.stop();
                    if(OptionsMenuScreen.allowSound) AssetLoader.jump.play();
                }
            }

            // se saltar num inimigo entao mata-o
            if((fixtureA.getUserData() == "Enemy" || fixtureB.getUserData() == "Enemy")){
                acertouTiro = true; // faz com que mate o inimigo mesmo que nao seja um tiro
                if(OptionsMenuScreen.allowSound) AssetLoader.jumpOnEnemy.stop();
                if(OptionsMenuScreen.allowSound) AssetLoader.jumpOnEnemy.play();
            }
        }
        // game over
        else if((fixtureA.getUserData() == "Enemy" && fixtureB.getUserData() == "Player" && fixtureB.getLinearVelocity().y >= 0 && !player.isInRocket() && !player.isInPropeller()) ||
                (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "Enemy" && fixtureA.getLinearVelocity().y >= 0 && !player.isInRocket() && !player.isInPropeller()) ||
                (fixtureA.getUserData() == "ground" && fixtureB.getUserData() == "Player") ||
                (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "ground")){
            this.game.setScreen(new GameOverScreen(game, score));
        }

        // destroi o inimigo e gera um novo (acertou bala)
        if((fixtureA.getUserData() == "bala" && fixtureB.getUserData() == "Enemy") ||
           (fixtureA.getUserData() == "Enemy" && fixtureB.getUserData() == "bala")){
            acertouTiro = true;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        // fixtureA = plataformas e grounds
        Body fixtureA = contact.getFixtureA().getBody();
        // fixtureB = player e bala
        Body fixtureB = contact.getFixtureB().getBody();

        float xA = fixtureA.getPosition().x;
        float yA = fixtureA.getPosition().y;
        float xB = fixtureB.getPosition().x;
        float yB = fixtureB.getPosition().y;

        //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA = " + contact.getFixtureA().getBody().getUserData());
        //System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBB = " + contact.getFixtureB().getBody().getUserData());

        if(player.isInRocket()) contact.setEnabled(false); // removo o contato se o player estiver no rocket
        if(player.isInPropeller()) contact.setEnabled(false); // removo o contato se o player estiver no propeller

        // desativa o contact listenner se (a velocidade y do player for = 0 (esta no maximo do salto) e está a tocar numa plataforma)
        // ou esta a saltar -> velocidade em y = 0
        // ou esta a cair e está a tocar numa plataforma
        if((fixtureB.getUserData() == "Player") &&
          ((fixtureB.getLinearVelocity().y == 0 && player.contains(xA, yA)) ||
          (fixtureB.getLinearVelocity().y > 0) ||
          (fixtureB.getLinearVelocity().y < 0 && player.contains(xA, yA)))

                ||

          ((fixtureA.getUserData() == "Player") &&
          ((fixtureA.getLinearVelocity().y == 0 && player.contains(xB, yB)) ||
          (fixtureA.getLinearVelocity().y > 0) ||
          (fixtureA.getLinearVelocity().y < 0 && player.contains(xB, yB))))){

            contact.setEnabled(false);
        }

        // se a desappearing ja foi saltada entao tem a velocidade em y negativa
        // por isso retiro o contato
        if((fixtureA.getUserData() == "disappearing" &&  fixtureA.getUserData() == "Player" && fixtureA.getLinearVelocity().y < 0) ||
           (fixtureB.getUserData() == "disappearing" && fixtureB.getUserData() == "Player" && fixtureB.getLinearVelocity().y < 0)){
            contact.setEnabled(false);
        }
        // se a breaking ja foi saltada entao tem a velocidade em y negativa
        // por isso retiro o contato
        if((fixtureA.getUserData() == "breaking" && fixtureA.getUserData() == "Player" && fixtureA.getLinearVelocity().y < 0) ||
                (fixtureB.getUserData() == "breaking" && fixtureB.getUserData() == "Player" && fixtureB.getLinearVelocity().y < 0)){
            contact.setEnabled(false);
        }

        // desativa o contato se a bala embater em tudo menos no inimigo
        if((fixtureB.getUserData() == "bala" && fixtureA.getUserData() != "Enemy") ||
           (fixtureA.getUserData() == "bala" && fixtureB.getUserData() != "Enemy")){
            contact.setEnabled(false);
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
