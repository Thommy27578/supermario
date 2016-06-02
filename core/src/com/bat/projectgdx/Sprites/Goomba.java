package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;



//Gegner Goomba - Verwalten der Position, des Körpers und Animation der Texturen 
public class Goomba extends Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;


    public Goomba(GameScreen gameScreen, float x, float y) {
        super(gameScreen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 2; i++){
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("goomba"), i*16, 0, 16, 16));
        }
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;

        setBounds(getX(), getY(), 16 / ProjectGdx.PPM, 16 / ProjectGdx.PPM);
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float delta){
        stateTime += delta;
        if(destroyed){
            return;
        }

        //Entfernen des Gegners wenn zerstört
        else if(setToDestroy){
            world.destroyBody(b2dbody);
            destroyed = true;
            setRegion(new TextureRegion(gameScreen.getAtlas().findRegion("goomba"), 32 , 0, 16, 16));
            stateTime = 0;
            gameScreen.getHud().addScore(250);
            return;
        }
        setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
        setRegion(walkAnimation.getKeyFrame(stateTime, true));
        b2dbody.setLinearVelocity(velocity);
    }
    
    //Definieren des Körpers für Kollisionen

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2dbody = world.createBody(bdef);
        b2dbody.setGravityScale(15f);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] shapevec = new Vector2[4];
        shapevec[0] = new Vector2(-6, -3).scl(1 / ProjectGdx.PPM);
        shapevec[1] = new Vector2(6, -3).scl(1 / ProjectGdx.PPM);
        shapevec[2] = new Vector2(-6, 4).scl(1 / ProjectGdx.PPM);
        shapevec[3] = new Vector2(6, 4).scl(1 / ProjectGdx.PPM);
        shape.set(shapevec);
        fdef.filter.categoryBits = ProjectGdx.ENEMY_BIT;
        fdef.filter.maskBits =
                ProjectGdx.OBJECT_BIT
                | ProjectGdx.GROUND_BIT
                | ProjectGdx.BRICK_BIT
                | ProjectGdx.PLAYER_BIT
                | ProjectGdx.ENEMY_BIT;

        fdef.shape = shape;
        b2dbody.createFixture(fdef).setUserData(this);

        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / ProjectGdx.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / ProjectGdx.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / ProjectGdx.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / ProjectGdx.PPM);

        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = ProjectGdx.ENEMY_HEAD_BIT;
        b2dbody.createFixture(fdef).setUserData(this);

    }

    public void draw(SpriteBatch spriteBatch){
        if(stateTime < 1 || !destroyed){
            super.draw(spriteBatch);
        }
    }

    //Wenn Gegner an Kopf getroffen, dann zerstöre ihn
    public void hitOnHead(){
        setToDestroy = true;
    }

    //Umkehren der Bewegungsrichtung nach Kollision mit statischem Objekt (Aufruf durch Kollisionslistener)
    @Override
    public void reverseVelocity(boolean x, boolean y) {
        if(x){
            velocity.x *= -1;
        }
        if(y){
            velocity.y *= -1;
        }
    }
}
