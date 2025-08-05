package com.mygdx.game.plataforma.tipos_plataformas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.objects.Spring;
import com.mygdx.game.plataforma.Platform;

public class NormalPlatform extends Platform {

    // Spring para desenhar na plataforma
    private Spring s;

    public NormalPlatform(World world, float screenX, float screenY) {

        super(world, screenX, screenY, "normal");
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        TextureRegion textura = AssetLoader.normalTile;
        batch.draw(textura, this.getPlatformBody().getPosition().x, this.getPlatformBody().getPosition().y, this.getWidth(), this.getHeight());

        // se tiver uma probabilidade < 0.1 e nao tiver springs(mola)
        if(this.getSpringProbability() < 0.1 && this.getnSprings() == 0){

            // crio uma mola
            s = new Spring(world, (this.getPlatformBody().getPosition().x + (this.getWidth()/2) - 0.75f),
                    this.getPlatformBody().getPosition().y + this.getHeight());
            this.setSpring(s); // crio a spring do platform
            this.setnSprings(1); // digo que ja criou uma mola (para nao criar mais molas)
        }
        // desenho a mola se existir o objeto
        if(s != null) s.draw(batch);
    }
}
