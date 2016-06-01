package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 18.03.2016.
 */

//Gegner Turtle - Verwalten der Position, des Körpers und Animation der Texturen 
public class Turtle extends Enemy {
	
	private int live = 2;
	private TextureRegion shellTexture;
	private boolean faceToRight = false;
	private TextureRegion texture;

    public Turtle(GameScreen gameScreen, float x, float y) {
		super(gameScreen, x, y);
		 frames = new Array<TextureRegion>();
	        for(int i = 0; i < 2; i++){
	            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("turtle"), i*16, 0, 16, 24));
	        }
	        walkAnimation = new Animation(0.4f, frames);
	        stateTime = 0;
	        
	        shellTexture = new TextureRegion(gameScreen.getAtlas().findRegion("turtle"), 4*16, 0, 16, 24);

	        setBounds(getX(), getY(), 16 / ProjectGdx.PPM, 16 / ProjectGdx.PPM);
	        setToDestroy = false;
	        destroyed = false;
	        
	        velocity = new Vector2(-0.4f, 0f);
	    }

	    public void update(float delta){
	        stateTime += delta;
	        if(destroyed){
	            return;
	        }

	        //Entfernen des Gegners wenn zerstört
	        else if(setToDestroy){
	    		b2dbody.applyLinearImpulse(new Vector2(b2dbody.getLinearVelocity().x * -1, 4f), b2dbody.getWorldCenter(), true);
	            world.destroyBody(b2dbody);
	            destroyed = true;
	            stateTime = 0;
	            gameScreen.getHud().addScore(250);
	            return;
	        }
	        setPosition(b2dbody.getPosition().x - getWidth() / 2, b2dbody.getPosition().y - getHeight() / 2);
	        
	        texture = walkAnimation.getKeyFrame(stateTime, true);
	        
	        if((b2dbody.getLinearVelocity().x > 0 || faceToRight) && !texture.isFlipX()){
	        	texture.flip(true, false);
	            faceToRight = true; 
	        }
	        else if((b2dbody.getLinearVelocity().x < 0 || !faceToRight) && texture.isFlipX()){
	        	texture.flip(true, false);
	            faceToRight = false; 
	        }
	        
	        if(live == 2){
		        setRegion(texture);
	        }
	        
	     
	        
	        b2dbody.setLinearVelocity(velocity);
	    }
	    
	    //Definieren des Körpers für Kollisionen

	    @Override
	    protected void defineEnemy() {
	        BodyDef bdef = new BodyDef();
	        bdef.position.set(getX(), getY());
	        bdef.type = BodyDef.BodyType.DynamicBody;
	        b2dbody = world.createBody(bdef);

	        FixtureDef fdef = new FixtureDef();
	        CircleShape shape = new CircleShape();
	        shape.setRadius(6 / ProjectGdx.PPM);
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

	private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDestroy;
    private boolean destroyed;

    public void draw(SpriteBatch spriteBatch){
        if(stateTime < 1 || !destroyed){
            super.draw(spriteBatch);
        }
    }

    //Wenn Gegner an Kopf getroffen, dann zerstöre ihn
    public void hitOnHead(){
    	live -= 1;
    	if(live == 0){
            setToDestroy = true;
    	}
    	else{
    		setRegion(shellTexture);
    	}

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
