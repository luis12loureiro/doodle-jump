package com.mygdx.game.plataforma;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.Spring;

public abstract class Platform {

    private Body platformBody;
    private String type;
    private float width, height;
    private int nSprings; // numero de molas -> so pode ser 1 no maximo
    private double springProbability; // probabilidade da plataforma ter uma mola
    private Spring spring; // objeto que representa a mola


    public Platform(World world, float screenX, float screenY, String type){

        this.type = type;
        this.width = 6f;
        this.height = 1.1f;
        this.nSprings = 0;
        this.springProbability = Math.random();
        this.platformBody = createPlatformBody(world, screenX, screenY);
    }

    /**
     * Cria o corpo da plataforma
     * @param world Mundo
     * @param screenX Posição X no mundo
     * @param screenY Posição Y no mundo
     * @return Body
     */
    private Body createPlatformBody(World world, float screenX, float screenY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(screenX, screenY);
        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        //hx the half-width
        //hy the half-height
        box.setAsBox(this.width/2 - 0.4f, this.height/2, new Vector2(this.width/2-0.3f,this.height/2),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
        body.setUserData(type);
        return body;
    }

    /**
     * Desenha a plataforma
     * É implmentado de forma diferente para cada plataforma
     * @param batch
     * @param world
     */
    public abstract void draw(SpriteBatch batch, World world);

    public String getType() { return type; }

    public Body getPlatformBody() {
        return platformBody;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    //public Rectangle getRectangle() { return rectangle; }

    // springs
    public int getnSprings() { return nSprings; }

    public void setnSprings(int nSprings) { this.nSprings = nSprings; }

    public double getSpringProbability() { return springProbability; }

    public void setSpringProbability(double springProbability) { this.springProbability = springProbability; }

    public Spring getSpring() { return spring; }

    public void setSpring(Spring spring) { this.spring = spring; }
}
