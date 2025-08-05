package com.mygdx.game.enemy.enemies;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.enemy.Enemy;

public class FlyingEnemy extends Enemy {

    public FlyingEnemy(World world, float screenX, float screenY) {

        super(world, screenX, screenY, 2.5f);
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {

        // as posiçaoes e tamanhos estam feitos para o inimigo fique no centro do body
        batch.draw(AssetLoader.FlyingenemyAnimation.getKeyFrame(elapsedTime, true), this.getPosition().x - this.getRadius()/2,
                this.getPosition().y - this.getRadius()/2,
                this.getRadius()*2, this.getRadius()*2);
    }
}
