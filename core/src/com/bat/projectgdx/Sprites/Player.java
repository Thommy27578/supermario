package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.Gdx;
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
public class Player extends Sprite {
    public enum State {JUMPING, FALLING, RUNNING, IDLE, DEAD};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion idle;
    private Animation jumpAnimation;
    private Animation runAnimation;
    private float stateTimer;
    private boolean faceToRight;
    private boolean isDead = false;

    public Player(World world, GameScreen screen){
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 1; i < 4; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        }

        runAnimation = new Animation(0.1f, frames);
        frames.clear();

        for(int i = 4; i < 6; i++){
            frames.add(new TextureRegion(getTexture(), i * 16, 10, 16, 16));
        }

        jumpAnimation = new Animation(0.1f, frames);
        frames.clear();

        idle = new TextureRegion(getTexture(), 0, 10, 16, 16);

        definePlayer();

        setBounds(0, 0, 16 / ProjectGdx.PPM, 16 / ProjectGdx.PPM);
        setRegion(idle);
    }

    public void update(float delta){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta){
        currentState = getState();
        TextureRegion region = null;

        switch(currentState){
            case JUMPING:
                region = jumpAnimation.getKeyFrame(stateTimer);
                break;
            case FALLING:
            case RUNNING:
                region = runAnimation.getKeyFrame(stateTimer, true);
                break;
            default:
                region = idle;
                break;
        }

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

    public State getState(){
        if(b2body.getLinearVelocity().y > 0){
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

    public void die() {

        if (!isDead()) {
            isDead = true;
            Filter filter = new Filter();
            filter.maskBits = ProjectGdx.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            Gdx.app.log("Player", "dieded");
        }
        Gdx.app.log("Die", "--");
    }

    private void definePlayer(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / ProjectGdx.PPM,32 / ProjectGdx.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ProjectGdx.PPM);
        fdef.filter.categoryBits = ProjectGdx.PLAYER_BIT;
        fdef.filter.maskBits = ProjectGdx.GROUND_BIT
                | ProjectGdx.BRICK_BIT
                | ProjectGdx.OBJECT_BIT
                | ProjectGdx.ENEMY_BIT
                | ProjectGdx.ENEMY_HEAD_BIT
                | ProjectGdx.ITEM_BIT ;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / ProjectGdx.PPM, 6 / ProjectGdx.PPM), new Vector2(2 / ProjectGdx.PPM, 6 / ProjectGdx.PPM));
        fdef.filter.categoryBits = ProjectGdx.PLAYER_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);//head
    }
}
