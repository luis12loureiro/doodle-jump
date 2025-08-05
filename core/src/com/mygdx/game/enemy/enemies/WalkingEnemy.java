package com.mygdx.game.enemy.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.enemy.Enemy;

public class WalkingEnemy extends Enemy {

    private float velX;
    private boolean direita; // direçao de movimento para a direita

    public WalkingEnemy(World world, float screenX, float screenY) {

        super(world, screenX, screenY, 1.7f);
        this.velX = 5;
        this.direita = true;
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {

        TextureRegion texture;
        // se tiver a andar para a direita a textura é a direita, para a esquerda ao contrario
        if(velX > 0) texture = AssetLoader.walkingEnemyRight;
        else texture = AssetLoader.walkingEnemyLeft;

        // as posiçaoes e tamanhos estam feitos para o inimigo fique no centro do body
        batch.draw(texture, this.getPosition().x - this.getRadius()/2,
                this.getPosition().y - this.getRadius()/2,
                this.getRadius()*2, this.getRadius()*2);
    }

    public void move(){

        this.getEnemyBody().setLinearVelocity(new Vector2(velX, 0));

        // se ultrapassar o tamanho do ecra á direita e se tiver a andar para a direita
        if (this.getPosition().x + this.getRadius()*2 > 28 && direita) {
            velX *= -1; // altera a direçao do movimento
            direita = false;
        }
        // se ultrapassar o tamanho do ecra á esquerda e se tiver a andar para a esquerda
        else if (this.getPosition().x - this.getRadius() < 0 && !direita) {
            velX *= -1; // altera a direçao do movimento
            direita = true;
        }
    }
}

