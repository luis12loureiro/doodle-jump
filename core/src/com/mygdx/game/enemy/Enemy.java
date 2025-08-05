package com.mygdx.game.enemy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy{

    private Body enemyBody;
    private float radius;

    public Enemy(World world, float screenX, float screenY, float radius){

        this.radius = radius;
        this.enemyBody = createEnemyBody(world, screenX, screenY);
    }

    /**
     * Cria o Body do Flyingenemy
     * @param world mundo de física
     * @param screenX posição y do Flyingenemy
     * @param screenY posição x do Flyingenemy
     */
    private Body createEnemyBody(World world, float screenX, float screenY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(screenX, screenY);
        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(this.radius);
        circle.setPosition(new Vector2(circle.getRadius()/2, circle.getRadius()/2));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
        body.setUserData("Enemy");
        return body;
    }

    /**
     * Desenha o Flyingenemy de acordo com a textura
     * @param batch permite desnehar o Flyingenemy
     */
    public abstract void draw(SpriteBatch batch, float elapsedTime);

    /**
     * Obtem a posição do Flyingenemy
     * @return posição do body do Flyingenemy
     */
    public Vector2 getPosition(){

        return enemyBody.getPosition();
    }

    /**
     * Obtem o Body do Flyingenemy
     * @return Body
     */
    public Body getEnemyBody() {
        return enemyBody;
    }

    /**
     * Obtem o raio do Flyingenemy
     * @return Raio
     */
    public float getRadius() { return radius; }
}

