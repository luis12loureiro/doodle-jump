package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;

public class Spring{

    private Body springBody;
    private float width, height;
    private boolean jumpOnSpring;

    public Spring(World world, float screenX, float screenY){

        this.width = 1.5f;
        this.height = 1f;
        jumpOnSpring = false;
        this.springBody = createSpringBody(world, screenX, screenY);
    }

    /**
     * Cria o Body da mola
     * @param world mundo de física
     * @param screenX posição y da mola
     * @param screenY posição x da mola
     */
    private Body createSpringBody(World world, float screenX, float screenY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(screenX, screenY);
        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(this.width/2, this.height/2, new Vector2(this.width/2,this.height/2),0);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
        body.setUserData("spring");
        return body;
    }

    /**
     * Desenha a mola
     * @param batch permite desenhar a mola
     */
    public void draw(SpriteBatch batch) {

        // atualiza o valor do atributo para o paametro para nao
        // fazer todas as molas salterem, mas apenas a que foi tocada

        TextureRegion texture = AssetLoader.spring;
        float altura = this.height;
        if(jumpOnSpring){
            texture = AssetLoader.spring2;
            altura = altura * 2;
        }
        batch.draw(texture, this.getPosition().x, this.getPosition().y-0.4f, this.width, altura);
    }

    /**
     * Move a mola de acordo com a posiçao em x da plataforma em que está inserida
     * @param platformPosX posição x da plataforma da mola
     * @param platformPosY posição y da plataforma da mola
     * Dependendo do tipo de plataforma a posiçao em x e y é diferente
     * Se for movingX so interessa o x logo o y é a posição da mola
     * Se for movingY so interessa o y logo o x é a posição da mola
     */
    public void move(float platformPosX, float platformPosY) {

        this.getSpringBody().setTransform(platformPosX, platformPosY, 0);
    }

    /**
     * Obtem a posição da mola
     * @return posição do body da mola
     */
    public Vector2 getPosition(){

        return springBody.getPosition();
    }

    /**
     * Obtem o Body da mola
     * @return Body
     */
    public Body getSpringBody() {
        return springBody;
    }


    public void setJumpOnSpring(boolean jumpOnSpring) {
        this.jumpOnSpring = jumpOnSpring;
    }

    /**
     * Obtem a largura da mola
     * @return float largura
     */
    public float getWidth() {
        return width;
    }

}

