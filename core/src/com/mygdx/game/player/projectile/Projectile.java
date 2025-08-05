package com.mygdx.game.player.projectile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;

import java.util.ArrayList;

public class Projectile{

    private Body projectileBody;
    private float radius;
    private float width, height;
    private World world;

    public Projectile(World world, float screenX, float screenY, float radius){

        this.radius = radius;
        this.width = 1.5f;
        this.height = 0.5f;
        this.world = world;
        projectileBody = createProjectileBody(this.world, screenX, screenY);
    }

    /**
     * Cria o Body da bala
     * @param world mundo de física
     * @param screenX posição y da bala
     * @param screenY posição x da bala
     */
    private Body createProjectileBody(World world, float screenX, float screenY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(screenX, screenY);
        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(this.radius);
        circle.setPosition(new Vector2(circle.getRadius() , circle.getRadius()));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.0f;
        body.createFixture(fixtureDef);
        body.setUserData("bala");
        return body;
    }

    public void draw(SpriteBatch batch) {

        // o angulo nao esta certo pq as dimensoes do mundo nao sao as do telemovel
        batch.draw(AssetLoader.projectileSpace, projectileBody.getPosition().x + (this.radius * 2), projectileBody.getPosition().y,
                0, 0, this.width, this.height, 1.0f, 1.0f, 1.55f* MathUtils.radiansToDegrees);
    }

    public Body getProjectileBody() {
        return projectileBody;
    }


    /**
     * Remove e destroi a bala
     * @param balas ArrayList de Projectiles com as balas
     * @param bala Projectile que quero remover da lista
     * @param acertouTIro Refere se o player acertou ou nao o tiro
     */
    public void destroyProjectile(ArrayList<Projectile> balas, Projectile bala, Body teto, boolean acertouTIro){

        // se acertou o tiro destroi logo a bala
        if(acertouTIro){

            balas.remove(bala);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    world.destroyBody(projectileBody);
                }
            });
        }
        // por outro lado se a posiçao da bala for maior que a do teto entao posso a remover
        else if(projectileBody.getPosition().y > teto.getPosition().y) {

            balas.remove(bala);
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    world.destroyBody(projectileBody);
                }
            });
        }
    }
}
