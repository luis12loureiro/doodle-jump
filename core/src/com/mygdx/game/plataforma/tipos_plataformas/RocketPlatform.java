package com.mygdx.game.plataforma.tipos_plataformas;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.plataforma.Platform;

public class RocketPlatform extends Platform {


    public RocketPlatform(World world, float screenX, float screenY) {

        super(world, screenX, screenY, "rocket");
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        TextureRegion textura = AssetLoader.normalTile;
        TextureRegion textura2 = AssetLoader.emptyRocket;
        batch.draw(textura, this.getPlatformBody().getPosition().x, this.getPlatformBody().getPosition().y, this.getWidth(), this.getHeight());
        batch.draw(textura2, this.getPlatformBody().getPosition().x , this.getPlatformBody().getPosition().y + this.getHeight(), 6f, 8f);
    }

}

