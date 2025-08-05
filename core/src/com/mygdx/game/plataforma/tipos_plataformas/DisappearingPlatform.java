package com.mygdx.game.plataforma.tipos_plataformas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.plataforma.Platform;


public class DisappearingPlatform extends Platform {

    private boolean saltou;
    private int velocidadeQueda;

    public DisappearingPlatform(World world, float screenX, float screenY) {

        super(world, screenX, screenY, "disappearing");
        this.saltou = false;
        this.velocidadeQueda = -50;
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        TextureRegion textura = AssetLoader.disappearingTile;
       if(!saltou) batch.draw(textura, this.getPlatformBody().getPosition().x, this.getPlatformBody().getPosition().y, this.getWidth(), this.getHeight());
    }

    /**
     * Move a plataforma para baixo quando esta é saltada
     */
    public void move() {

        if(saltou) this.getPlatformBody().setLinearVelocity(new Vector2(0, velocidadeQueda));
    }

    /**
     * Altera o valor da variavel booleana quando player salta na plataforma
     * @param saltou Diz se o player saltou na plataforma
     */
    public void setSaltou(boolean saltou) {
        this.saltou = saltou;
    }

}

