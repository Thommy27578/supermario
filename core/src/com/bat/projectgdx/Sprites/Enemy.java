package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 18.03.2016.
 */

//Abstrakte Klasse für Gegner
public abstract class Enemy extends Sprite {

    protected World world;
    protected GameScreen gameScreen;
    public Body b2dbody;

    public Vector2 velocity;

    public Enemy(GameScreen gameScreen, float x, float y){
        this.world = gameScreen.getWorld();
        this.gameScreen = gameScreen;
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(1f, 0f);
        b2dbody.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void reverseVelocity(boolean x, boolean y);

    public abstract void update(float delta);

}
