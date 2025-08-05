package com.mygdx.game.plataforma.tipos_plataformas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.objects.Spring;
import com.mygdx.game.plataforma.Platform;

public class MovingPlatformX extends Platform {

    private int sideToMove;
    private Spring s;

    public MovingPlatformX(World world, float screenX, float screenY, int sideToMove) {

        super(world, screenX, screenY, "movingX");
        this.sideToMove = sideToMove;
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        TextureRegion textura = AssetLoader.movingTileX;
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

    /**
     * Move a plataforma para a esquerda ou direita de acordo com o sideToMove
     */
    public void move() {

        // faz o player mover de acordo coma direção random sideToMove
        this.getPlatformBody().setLinearVelocity(new Vector2(sideToMove, 0));

        // se ultrapassar o tamanho do ecra á direita
        if (this.getPlatformBody().getPosition().x > 28) {
            // coloca o player á esquerda
            this.getPlatformBody().setTransform(new Vector2(0 - this.getWidth(), this.getPlatformBody().getPosition().y), 0);
        }
        // se ultrapassar o tamanho do ecra á esquerda
        else if (this.getPlatformBody().getPosition().x + this.getWidth() < 0) {
            // coloca o player á direita
            this.getPlatformBody().setTransform(new Vector2(28, this.getPlatformBody().getPosition().y), 0);
        }

        // se a mola existir movo-a de acordo com a posiçao em x da plataforma (e de forma a ficar centrada)
        if(s != null) s.move(this.getPlatformBody().getPosition().x  + (this.getWidth()/2) - 0.75f, s.getPosition().y);

    }
}
