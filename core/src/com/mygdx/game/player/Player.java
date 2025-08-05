package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.AssetLoader;
import com.mygdx.game.player.projectile.Projectile;

public class Player{

    private Body playerBody;
    private float width, height, radius;
    private float prevAccelX;
    private boolean isShooting;
    private boolean isInRocket;
    private float timeToTravelInRocket, timeInRocket, velInRocket;
    private boolean isInPropeller;
    private float timeToTravelInPropeller, timeInPropeller, velInPropeller;
    private Texture lastTexture; // ultima textura -> para o jogador nao ficar sempre a rodar

    public Player(World world, float screenX, float screenY){

        this.width = 5f;
        this.height = 4.5f;
        this.radius = 1f;
        this.isShooting = false;
        this.isInRocket = false;
        this.timeToTravelInRocket = 5;
        this.timeInRocket = 0;
        this.velInRocket = 35;

        this.isInPropeller = false;
        this.timeToTravelInPropeller = 3;
        this.timeInPropeller = 0;
        this.velInPropeller = 25;
        lastTexture = AssetLoader.playerTexture1;

        this.playerBody = createPlayerBody(world, screenX, screenY);
    }

    /**
     * Cria o Body do jogador
     * @param world mundo de física
     * @param screenX posição y do player
     * @param screenY posição x do player
     */
    private Body createPlayerBody(World world, float screenX, float screenY) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(screenX, screenY);
        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(this.radius);
        circle.setPosition(new Vector2(circle.getRadius() * 2.5f, circle.getRadius()));
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = 1.5f;
        body.createFixture(fixtureDef);
        body.setUserData("Player");
        return body;
    }

    /**
     * Obtem a posição do player
     * @return posição do body do player
     */
    public Vector2 getPosition(){

        return playerBody.getPosition();
    }

    /**
     * Obtem o Body do player
     * @return Body
     */
    public Body getPlayerBody() {
        return playerBody;
    }

    /**
     * Desenha o player de acordo com a textura
     * @param batch permite desnehar o player
     */
    public void draw(SpriteBatch batch, float elapsedTime) {

        // desenha a textura virada para a direita
        // Texture textura = AssetLoader.playerTexture1;
        Texture textura = null;
        // se tiver a disparar (pressionar ecra) e nao tiver no rocket e no propeller
        if(isShooting && !isInRocket && !isInPropeller){
            textura = AssetLoader.playerTexture2;
            // desenha o shooter na posiçao correta
            batch.draw(AssetLoader.playerTexture3, playerBody.getPosition().x + this.width/2 - 1.2f/2,
                    playerBody.getPosition().y + 3.8f/2 - 1.2f, 1.2f, 3.8f);
        }
        else if(isInRocket){

            batch.draw(AssetLoader.rocketAnimation.getKeyFrame(elapsedTime, true), this.getPosition().x - this.radius/2,
                    this.getPosition().y - this.radius/2,
                    this.radius*6, this.radius*12);
        }
        // no caso de o acelerometro for menor que -2 desenha a textura virada para a esquerda e atualiza a ultima textura
        else if(prevAccelX < -2){
            textura = AssetLoader.playerTexture0;
            lastTexture = textura;
        }
        // no caso de o acelerometro for maior que 2 desenha a textura virada para a direita e atualiza a ultima textura
        else if(prevAccelX > 2){
            textura = AssetLoader.playerTexture1;
            lastTexture = textura;
        }else textura = lastTexture; // no caso de estar entre [-2,-1,0,1,2] dizer que a textura é igual á ultima textura

        // raio * width e height para ficar responsivo
        if(!isInRocket){
            batch.draw(textura, playerBody.getPosition().x, playerBody.getPosition().y,
                    this.radius * this.width, this.radius * this.height);
        }
        // se tiver no propeller desenha-lo (desenha tb o player ao contrario do rocket)
        if(isInPropeller){

            batch.draw(AssetLoader.propellerAnimation.getKeyFrame(elapsedTime, true), this.getPosition().x + this.radius,
                    this.getPosition().y + this.height/2,
                    this.radius*3f, this.radius*3f);
        }

    }

    /**
     * Controla a posição do player quando sai fora do ecra
     * @param screenWidth largura do ecra
     */
    public void handleWallPosition(int screenWidth){

        // se for á direita
        if(playerBody.getPosition().x > screenWidth)
            // a sua posiçao é mudada para a posiçao 0
            // -width para nao aparecer o corpo inteiro -> smooth
            playerBody.setTransform(new Vector2(0 - this.width, playerBody.getPosition().y), 0);

        // se for á esquerda -> +5 para nao desaparecer logo
        // tem que o corpo inteiro desaparecer
        else if(playerBody.getPosition().x + this.width < 0)
            // a sua posiçao é mudada para a posiçao da largura do ecra
            playerBody.setTransform(new Vector2(screenWidth, playerBody.getPosition().y), 0);
    }

    /**
     * Permite disparar a bala
     * @param bala bala que é disparada
     */
    public void shoot(Projectile bala){

        bala.getProjectileBody().setLinearVelocity(new Vector2(0, 50));
    }

    /**
     * Permite fazer o jogador saltar
     * @param velocity Velocidade em Y do salto
     */
    public void jump(float velocity){

        this.playerBody.setLinearVelocity(new Vector2(0, velocity));
    }

    /**
     * Remove o rocket se o player ja estiver no rocket e o timeInRocket ja tiver atingido o máximo ou
     * Remove o propeller se o player ja estiver no propeller e o timeInPropeller ja tiver atingido o máximo
     */
    public void removeRocketOrPropeller(){

        if(isInRocket && timeInRocket > timeToTravelInRocket){
            isInRocket = false;
            timeInRocket = 0;
        }
        else if(isInPropeller && timeInPropeller > timeToTravelInPropeller){
            isInPropeller = false;
            timeInPropeller = 0;
        }
    }

    /**
     * Faz o player andar no rocket durante timeToTravelInRocket
     */
    public void travelInRocketOrPropeller(){

        if(isInRocket) {
            timeInRocket += Gdx.graphics.getRawDeltaTime();
            if (timeInRocket <= timeToTravelInRocket)
                this.getPlayerBody().setLinearVelocity(new Vector2(prevAccelX, velInRocket));
        }
        else if(isInPropeller) {
            timeInPropeller += Gdx.graphics.getRawDeltaTime();
            if (timeInPropeller <= timeToTravelInPropeller)
                this.getPlayerBody().setLinearVelocity(new Vector2(prevAccelX, velInPropeller));
        }
    }

    /**
     * Processa o acelerometro
     * @param world mundo em que o player está inserido
     */
    public void processAccelerometer(World world) {

        // valor do acelerometro na posição x
        float x = Gdx.input.getAccelerometerX() * -10f;
        if ((prevAccelX != x)) {
            prevAccelX = x;
            // adiciona gravidade no mundo no eixo x
            // para o player mover para a esquerda e direita
            world.setGravity(new Vector2(x, -25f));
        }
    }

    /**
     * Indica se um corpo intersetou o player
     * @param platX posição x da plataforma
     * @param platY posição y da plataforma
     * @return Se o corpo intersetou o player ou nao
     */
    public boolean contains(float platX, float platY){

        // radius * 3.5 porque o valor da distancia da entre 0. ate cerca de 3.xx e o radius é so 1.
        return Vector2.dst(platX, platY, playerBody.getPosition().x, playerBody.getPosition().y) < this.radius * 3.5;
    }

    public void setShooting(boolean shooting) {
        this.isShooting = shooting;
    }

    public float getWidth() {
        return width;
    }

    public void setInRocket(boolean inRocket) {
        isInRocket = inRocket;
    }

    public boolean isInRocket() {
        return isInRocket;
    }

    public boolean isInPropeller() { return isInPropeller; }

    public void setInPropeller(boolean inPropeller) { isInPropeller = inPropeller; }
}
