package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.enemy.Enemy;
import com.mygdx.game.enemy.enemies.FlyingEnemy;
import com.mygdx.game.enemy.enemies.WalkingEnemy;
import com.mygdx.game.objects.Spring;
import com.mygdx.game.plataforma.Platform;
import com.mygdx.game.plataforma.tipos_plataformas.BreakingPlatform;
import com.mygdx.game.plataforma.tipos_plataformas.DisappearingPlatform;
import com.mygdx.game.plataforma.tipos_plataformas.MovingPlatformX;
import com.mygdx.game.plataforma.tipos_plataformas.MovingPlatformY;
import com.mygdx.game.plataforma.tipos_plataformas.NormalPlatform;
import com.mygdx.game.plataforma.tipos_plataformas.PropellerPlatform;
import com.mygdx.game.plataforma.tipos_plataformas.RocketPlatform;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class LevelGenerator {

    private World world;
    private int numberOfPlatforms;
    private float minY, maxY, minX, maxX;
    private ArrayList<Platform> platforms;
    private Vector2 spawnPosition;
    private ArrayList<Enemy> enemies;

    public LevelGenerator(World world, float minY, float maxY){

        numberOfPlatforms = 45;
        this.minY = minY;
        this.maxY = maxY;
        this.minX = 4.2f;
        this.maxX = 28-4.2f;
        spawnPosition = new Vector2();
        this.world = world;
        platforms = new ArrayList<Platform>();
        enemies = new ArrayList<Enemy>();
        generatePlatformsAndEnemies();
    }

    /**
     * Gera as plataformas e um inimigo numa posição random
     * Com o y sempre a aumentar
     */
    private void generatePlatformsAndEnemies(){

        // mete sempre duas plataformas no inicio no centro para o player nao cair inicialmente
        NormalPlatform platformaIniciail1 = new NormalPlatform(world, 8, (int)spawnPosition.y);
        NormalPlatform platformaIniciail2 = new NormalPlatform(world, 14, (int)spawnPosition.y);
        platforms.add(platformaIniciail1); platforms.add(platformaIniciail2);

        for(int i = 0; i < numberOfPlatforms; i++){

            spawnPosition.y += random(minY, maxY);
            spawnPosition.x = random(minX, maxX);
            Platform platform = chooseRandomPlatform();
            // se for uma plataforma breaking para nao haver possibilidade de nao conseguir saltar
            // gero em baixo da breaking uma plataforma normal com uma mola
            if(platform.getType().equals("breaking")){
                spawnPosition.y -= random(minY, maxY);
                spawnPosition.x = random(minX, maxX);
                Platform comMola = new NormalPlatform(world, spawnPosition.x, spawnPosition.y);
                comMola.setSpringProbability(0.05);
                platforms.add(comMola);
            }
            platforms.add(platform);
        }

        // adiciono um inimigo numa posiçao random
        Enemy enemy = chooseRandomEnemy();
        enemies.add(enemy);
    }

    /**
     * Desenha as plataformas e inimigos e move-os
     * @param batch batch que permite desenhar
     * @param elapsedTime tempo para a animaçao
     */
    public void drawPlatformsAndEnemies(SpriteBatch batch, float elapsedTime){

        for(Platform platform: platforms){

            platform.draw(batch, world);
            if (platform.getType().equals("movingX")) ((MovingPlatformX) platform).move();
            if (platform.getType().equals("movingY")) ((MovingPlatformY) platform).move();
            if (platform.getType().equals("breaking")) ((BreakingPlatform) platform).move();
            if (platform.getType().equals("disappearing")) ((DisappearingPlatform) platform).move();
        }
        for(Enemy enemy: enemies){
            enemy.draw(batch, elapsedTime);
            if(enemy instanceof WalkingEnemy) ((WalkingEnemy) enemy).move();
        }
    }

    /**
     * Destroi os bodies das plataformas que estam em baixo do ground
     * @param ground Body do Objeto Ground
     * @return Número de plataformas destruidas
     */
    public int destroyPlatforms(Body ground){

        System.out.println("Numero de plataformas = " + platforms.size());
        int nPlatDest = 0;
        for(int i = platforms.size() - 1; i >= 0; i--){

            final Platform platform = platforms.get(i);

                if(!platform.getType().equals("rocket")) {
                    // se a posiçao y da plataforma for menor que a posiçao y do ground
                    if (platform.getPlatformBody().getPosition().y + platforms.get(i).getHeight() < ground.getPosition().y) {

                        // remove da lista a plataforma
                        platforms.remove(platforms.get(i));
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                // destroi a plataforma
                                world.destroyBody(platform.getPlatformBody());
                                // destroi a mola se a plataforma tiver uma mola
                                if (platform.getnSprings() == 1)
                                    world.destroyBody(platform.getSpring().getSpringBody());
                            }
                        });
                        nPlatDest += 1;
                    }
                }else{
                    // se a posiçao y da plataforma + a altura do rocket for menor que a posiçao y do ground
                    if (platform.getPlatformBody().getPosition().y + platforms.get(i).getHeight() + 8f < ground.getPosition().y) {

                        // remove da lista a plataforma
                        platforms.remove(platforms.get(i));
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() { world.destroyBody(platform.getPlatformBody()); }
                        });
                        nPlatDest += 1;
                    }
                }
        }
        return nPlatDest;
    }

    /**
     * Destroi os bodies dos inimigos que estam em baixo do ground ou que o Player acerta
     * @param ground Body do Objeto Ground
     * @param isShooting boolean que dis se o player disparou e acertou ou nao
     * @return Número de inimigos destruidos
     */
    public int destroyEnemies(Body ground, boolean isShooting){

        //System.out.println("Numero de inimigos = " + enemies.size());
        int nInimigosDest = 0;
        for(int i = enemies.size() - 1; i >= 0; i--){

            final Body inimigo = enemies.get(i).getEnemyBody();
            // se o player acertar no inimigo
            if(isShooting){

                enemies.remove(enemies.get(i));
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run () {
                        world.destroyBody(inimigo);
                    }
                });
                nInimigosDest += 1;
            }else { // se nao acertar
                // se a posiçao y do inimigo for menor que a posiçao y do ground
                if (inimigo.getPosition().y + enemies.get(i).getRadius() < ground.getPosition().y) {

                    enemies.remove(enemies.get(i));
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() { world.destroyBody(inimigo);
                        }
                    });
                    nInimigosDest += 1;
                }
            }
        }
        return nInimigosDest;
    }

    /**
     * Gera as novas plataformas de acoroo com o número de plataformas destruidas
     * Assim mantém o número de plataformas sempre o mesmo
     * Gera tambem apenas um inimigo mantendo o mesmo numero de inimigos
     * @param nPlatforms Número de plataformas destuídas
     */
    public void generateNewPlatformsAndEnemies(int nPlatforms, int nEnemies){

        for(int i = 0; i < nPlatforms; i++){

            spawnPosition.y += random(minY, maxY);
            spawnPosition.x =  random(minX, maxX);
            // NormalPlatform platform = new NormalPlatform(world, (int)spawnPosition.x, (int)spawnPosition.y, "normal");
            Platform platform = chooseRandomPlatform();
            // se for uma plataforma breaking para nao haver possibilidade de nao conseguir saltar
            // gero em baixo da breaking uma plataforma normal com uma mola
            if(platform.getType().equals("breaking")){
                spawnPosition.y -= random(minY, maxY);
                spawnPosition.x =  random(minX, maxX);
                Platform comMola = new NormalPlatform(world, spawnPosition.x, spawnPosition.y);
                comMola.setSpringProbability(0.05);
                platforms.add(comMola);
            }
            platforms.add(platform);
        }

        for(int i = 0; i < nEnemies; i++){
            Enemy enemy = chooseRandomEnemy();
            enemies.add(enemy);
        }
    }

    /**
     * Escolhe o tipo de plataforma para desenhar de acordo com probabilidades
     * @return Plataforma a desenhar
     */
    private Platform chooseRandomPlatform(){

        double platType = Math.random();
        Platform plataforma;

        if (platType >= 0.5) {
            plataforma = new NormalPlatform(world, spawnPosition.x, spawnPosition.y);
        }
        else if (platType < 0.5 && platType >= 0.2) {
            int n = random(-4, 4);
            plataforma = new MovingPlatformX(world, spawnPosition.x, spawnPosition.y, n);
        }
        else if (platType < 0.2 && platType >= 0.1) {
            int n = random(-4, 4);
            plataforma = new MovingPlatformY(world, spawnPosition.x, spawnPosition.y, n);
        }
        else if (platType < 0.1 && platType >= 0.03) {
            plataforma = new DisappearingPlatform(world, spawnPosition.x, spawnPosition.y);
        }
        else if(platType < 0.03 && platType >= 0.01){
            plataforma = new BreakingPlatform(world, spawnPosition.x, spawnPosition.y);
        }
        else{
            double rocketOrPropeller = Math.random();
            if(rocketOrPropeller > 0.3) plataforma = new PropellerPlatform(world, spawnPosition.x, spawnPosition.y);
            else plataforma = new RocketPlatform(world, spawnPosition.x, spawnPosition.y);
        }
        return plataforma;
    }

    /**
     * Escolhe o tipo de inimigo para desenhar de acordo com probabilidades
     * @return Enemy a desenhar
     */
    private Enemy chooseRandomEnemy(){

        double enemyType = Math.random();
        Enemy enemy;

        if (enemyType >= 0.5) {
            enemy = new FlyingEnemy(world, spawnPosition.x, spawnPosition.y);
        }else enemy = new WalkingEnemy(world, spawnPosition.x, spawnPosition.y);
        return enemy;
    }

    /**
     * Obtem a plataforma que tem as mesmas coordenadas que a plataforma de contato com o player
     * @param fixTureBodyPosition coordenadas da plataforma de contato
     * @return Platform
     */
    public Platform getPlatformWithCoordinates(Vector2 fixTureBodyPosition){

        Platform platforma = null;

        for(Platform platform : platforms){

            if(platform.getPlatformBody().getPosition() == fixTureBodyPosition){

                platforma =  platform;
            }
        }
        return platforma;
    }

    /**
     * Obtem a Mola que tem as mesmas coordenadas que a Mola de contato com o player
     * @param fixTureBodyPosition coordenadas da mola de contato
     * @return Spring
     */
    public Spring getSpringWithCoordinates(Vector2 fixTureBodyPosition){

        Spring mola = null;

        for(Platform platform : platforms){

            Spring molaDaPlataforma = platform.getSpring();
            if(molaDaPlataforma != null) {
                if (molaDaPlataforma.getPosition() == fixTureBodyPosition) {

                    mola = platform.getSpring();
                }
            }
        }
        return mola;
    }
}
