package com.bat.projectgdx.Items;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 14.03.2016.
 */

//Definieren des Items Coin - Festlegen der Position im Spiel und Animation der Texturen
public class Coin extends Item {
    private final String ITEM_NAME = "coin";
    
    private float stateTime;
    private Animation coinAnimation;
    private Array<TextureRegion> frames;

    public Coin(GameScreen gameScreen, float x, float y) {
        super(gameScreen, x, y);
        setRegion(gameScreen.getAtlas().findRegion(ITEM_NAME), 0, 0, 16, 16);
        
        //Coin Animation
        frames = new Array<TextureRegion>();
        for(int i = 0; i < 4; i++){
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("coin"), i*16, 0, 16, 16));
        }
        coinAnimation = new Animation(0.2f, frames);
        stateTime = 0;
    }
    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / ProjectGdx.PPM);

        fdef.filter.categoryBits = ProjectGdx.ITEM_BIT;
        fdef.filter.maskBits = ProjectGdx.PLAYER_BIT |
                ProjectGdx.OBJECT_BIT |
                ProjectGdx.GROUND_BIT |
                ProjectGdx.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    //Wenn eingesammelt erhöhe Spielerpunktestand um Betrag 100
    @Override
    public void use() {
        destroy();
        gameScreen.getHud().addScore(100);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //Coin Animation
        stateTime += delta;
        setRegion(coinAnimation.getKeyFrame(stateTime, true));
    }
}

