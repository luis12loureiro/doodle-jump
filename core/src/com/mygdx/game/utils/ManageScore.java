package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.AssetLoader;

public class ManageScore {

    private Camera camera;
    private int score; // valor do score recebido
    private float posX; // posicao inicial no eixo x
    private int step; // espaço para separar os números

    public ManageScore(int score, Camera camera){

        this.score = score;
        this.camera = camera;
        this.step = 30;
        this.posX = camera.viewportWidth/2;
    }

    public void updateScore(SpriteBatch batch){

        String value = Integer.toString(score);
        //System.out.println(score);
        for(int i = 0; i < value.length(); i++){

            this.posX += this.step;

            if(value.charAt(i) == '0') batch.draw(AssetLoader.n0, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '1') batch.draw(AssetLoader.n1, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '2') batch.draw(AssetLoader.n2, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '3') batch.draw(AssetLoader.n3, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '4') batch.draw(AssetLoader.n4, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '5') batch.draw(AssetLoader.n5, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '6') batch.draw(AssetLoader.n6, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '7') batch.draw(AssetLoader.n7, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '8') batch.draw(AssetLoader.n8, this.posX, camera.viewportHeight/2);
            if(value.charAt(i) == '9') batch.draw(AssetLoader.n9, this.posX, camera.viewportHeight/2);
        }
        this.posX = camera.viewportWidth/2;
    }
}
