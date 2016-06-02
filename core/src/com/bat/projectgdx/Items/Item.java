package com.bat.projectgdx.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;




/*Abstrakte Klasse für alle Items - Vorgabe für Position, Definieren des Körpers und 
 behandeln der Nutzung des Objekts (Spieler kollidiert mit Item)*/

public abstract class Item extends Sprite {
    protected GameScreen gameScreen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(GameScreen gameScreen, float x, float y){
        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16 / ProjectGdx.PPM, 16 / ProjectGdx.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use();

    public void update(float delta){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void reverseVelocity(boolean x, boolean y){
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }

    @Override
    public void draw(Batch batch){
        if(!destroyed){
            super.draw(batch);
        }
    }

    public void destroy(){
        toDestroy = true;
    }
}
