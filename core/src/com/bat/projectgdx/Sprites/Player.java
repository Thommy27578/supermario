package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 14.03.2016.
 */

//Spieler Klasse - Legt den Körper zwecks Kollisionen fest und die jeweils passenden Texturen
public class Player extends Sprite {
	
	//Verwalten der Stati des Spielers um die richtigen Texturen zu laden
    public enum State {JUMPING, FALLING, RUNNING, IDLE, DEAD};
    public State currentState;
    public State previousState;
    
    public World world;
    public Body b2body;
    
    //Bestimmte Texturen für bestimmte Aktionen des Spielers
    private TextureRegion idle;
    private TextureRegion jump;
    private TextureRegion fall;
    
    //Mehrere Texturen für Laufanimation des Spielers
    private Animation runAnimation;
    
    private float stateTimer;
    private boolean faceToRight;
    private boolean isDead = false;
    GameScreen gameScreen;


    //Initialisieren der Variablen
    public Player(World world, GameScreen gameScreen){
        super(gameScreen.getAtlas().findRegion("little_mario"));
    	this.gameScreen = gameScreen;
        this.world = world;
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        //Ausschneiden bestimmter Texturen und hinzufügen zu Animation für das Laufen
        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        }

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        jump = new TextureRegion(getTexture(), 80, 10, 16, 16);

        idle = new TextureRegion(getTexture(), 0, 10, 16, 16);
        
        fall = new TextureRegion(getTexture(), 94, 10, 16, 16);

        definePlayer();

        setBounds(0, 0, 16 / ProjectGdx.PPM, 16 / ProjectGdx.PPM);
        setRegion(idle);
    }

    /*Updaten der Position des Spielers und der Textur in abhängigkeit der aktuellen Textur und
    der vergangenen Zeit zwischen diesem und dem letzten Aufruf der Methode (delta)*/
    public void update(float delta){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
        
        //Prüfen ob Spieler gefallen ist (Unter Boden Höhe)
        if(b2body.getPosition().y < 0){
        	die();
        }
    }

    
    /*Festlegen der Region von welcher Texturen ausgeschnitten werden in Abhängigkeit des Zustandes
    in welcher der Spieler sich momentan befindet und der vergangenen Zeit*/
    public TextureRegion getFrame(float delta){
        currentState = getState();
        TextureRegion region = null;

        switch(currentState){
            case JUMPING:
                region = jump;
                break;
            case RUNNING:
                region = runAnimation.getKeyFrame(stateTimer, true);
                break;
        	case DEAD:
        		region = fall;
            default:
                region = idle;
                break;
        }

        //Spiegeln der Textur in Blickrichtung des Spielers
        if((b2body.getLinearVelocity().x < 0 || !faceToRight) && !region.isFlipX()){
            region.flip(true, false);
            faceToRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || faceToRight) && region.isFlipX()){
            region.flip(true, false);
            faceToRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;

        return region;
    }

    //Bestimmen der Stati des Spielers in Abhängigkeit der Bewegungsvektoren in x, y Richtung
    public State getState(){
    	
    	if(isDead){
    		return State.DEAD;
    	}
    	else if(b2body.getLinearVelocity().y > 0){
            return State.JUMPING;
        }
        else if(b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if(b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        return State.IDLE;
    }

    public boolean isDead(){
        return this.isDead;
    }

    
    //Behandeln des Ereignisses, wenn Spieler stirbt
    public void die() {

        if (!isDead()) {
            isDead = true;
            
            //Gibt Spielfigur Bewegungsimpuls nach oben und hebt Bewegung in x Richtung auf
            b2body.applyLinearImpulse(new Vector2(b2body.getLinearVelocity().x * -1, 6f + b2body.getLinearVelocity().y * -1), b2body.getWorldCenter(), true);
            
            //Setzt das Filterbit auf Nothing_Bit um Spieler mit nichts mehr kollidieren zu lassen
            Filter filter = new Filter();
            filter.maskBits = ProjectGdx.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
        }
    }

    //Erezugt Körper für Spieler der für alle Kollisionen verantwortlich ist
    private void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / ProjectGdx.PPM,32 / ProjectGdx.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ProjectGdx.PPM);
        fdef.filter.categoryBits = ProjectGdx.PLAYER_BIT;
        
        //Liste der Obekte mit der Spieler kollidieren kann
        fdef.filter.maskBits = ProjectGdx.GROUND_BIT
                | ProjectGdx.BRICK_BIT
                | ProjectGdx.OBJECT_BIT
                | ProjectGdx.ENEMY_BIT
                | ProjectGdx.ENEMY_HEAD_BIT
                | ProjectGdx.ITEM_BIT
                | ProjectGdx.DOOR_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Festlegen des Kopfes, da bei Kollisionen des Spielers unterschieden wird zwischen Kopf und Körper
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ProjectGdx.PPM, 6 / ProjectGdx.PPM), new Vector2(2 / ProjectGdx.PPM, 6 / ProjectGdx.PPM));
        fdef.filter.categoryBits = ProjectGdx.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }
    
    public float getStateTimer(){
    	return stateTimer;
    }
}
