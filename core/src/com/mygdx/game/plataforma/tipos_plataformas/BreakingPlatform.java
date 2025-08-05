package com.mygdx.game.plataforma.tipos_plataformas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.plataforma.Platform;

public class BreakingPlatform extends Platform{

    private float elapsedTime;
    private boolean startAnimation;
    private static int velocidadeQueda;

    public BreakingPlatform(World world, float screenX, float screenY) {

        super(world, screenX, screenY, "breaking");
        this.elapsedTime = 0;
        this.startAnimation = false;
        velocidadeQueda = -20;
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        batch.draw(AssetLoader.breakingTileAnimation.getKeyFrame(elapsedTime, false), this.getPlatformBody().getPosition().x ,
                this.getPlatformBody().getPosition().y ,
                this.getWidth(), this.getHeight());
        if(startAnimation) this.elapsedTime += Gdx.graphics.getRawDeltaTime();
    }

    /**
     * Move a plataforma para baixo quando esta é saltada
     */
    public void move() {

        if(startAnimation) this.getPlatformBody().setLinearVelocity(new Vector2(0, velocidadeQueda));
    }

    /**
     * Altera o valor da variavel booleana quando player salta na plataforma
     * @param startAnimation Diz se o player saltou na plataforma
     */
    public void setStartAnimation(boolean startAnimation) {
        this.startAnimation = startAnimation;
    }

}
