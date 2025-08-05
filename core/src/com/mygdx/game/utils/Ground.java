package com.mygdx.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Ground {

    private Vector2 pos;
    private Body groundBody;

    public Ground(World world, Vector2 pos, float width, float height){

        this.pos = pos;
        groundBody = createGroundBody(world, width, height);
    }

    public Body createGroundBody(World world, float width, float height){

        // bottom
        // First we create a body definition
        BodyDef groundBodyDef = new BodyDef();
        // Set our body's starting position in the world
        groundBodyDef.position.set(new Vector2(pos.x, pos.y));
        // Create our body in the world using our body definition
        Body groundBody = world.createBody(groundBodyDef);
        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 1 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(width, height);
        // Create a fixture from our polygon shape and add it to our ground body
        groundBody.createFixture(groundBox, 0.0f);
        groundBody.setUserData("ground");
        return groundBody;
    }

    /**
     * Obtem o body do ground
     * @return groundBody
     */
    public Body getGroundBody() {
        return groundBody;
    }


}
