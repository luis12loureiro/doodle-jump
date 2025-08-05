package com.mygdx.game.plataforma.tipos_plataformas;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.objects.Spring;
import com.mygdx.game.plataforma.Platform;

public class MovingPlatformY extends Platform {

    private int directionToMove;
    private float timeOfMovement;
    private float timeSeconds;
    private boolean mudouDirecao;
    private Spring s;


    public MovingPlatformY(World world, float screenX, float screenY, int directionToMove) {

        super(world, screenX, screenY, "movingY");
        this.directionToMove = directionToMove;
        this.timeOfMovement = 3f;
        this.timeSeconds = 0f;
        this.mudouDirecao = false;
    }

    @Override
    public void draw(SpriteBatch batch, World world) {

        TextureRegion textura = AssetLoader.movingTileY;
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
     * Move a plataforma para cima ou baixo de acordo com o directionToMove
     */
    public void move() {

        // se o tempo for maior ou igual que 0
        if(timeSeconds >= 0){
            // aumento o tempo
            timeSeconds += Gdx.graphics.getRawDeltaTime();
            // se mudou a direcao ent ando ao contrario
            if(mudouDirecao)this.getPlatformBody().setLinearVelocity(new Vector2(0, directionToMove));
            else this.getPlatformBody().setLinearVelocity(new Vector2(0, directionToMove * -1));

            // se o tempo passado for maior que o tempo de movimento (3)
            if(timeSeconds > timeOfMovement){
                timeSeconds = 0; // atualizo o tempo para zero
                // atualizo o mudouDirecao para o contrario do que está agora
                if(mudouDirecao) mudouDirecao = false;
                else mudouDirecao = true;
            }

        }

        // se a mola existir movo-a de acordo com a posiçao em y da plataforma
        if(s != null) s.move(s.getPosition().x, this.getPlatformBody().getPosition().y + this.getHeight());

    }

}
