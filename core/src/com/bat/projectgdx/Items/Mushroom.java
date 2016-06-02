package com.bat.projectgdx.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;



//Definieren des Items Mushroom - Festlegen der Position im Spiel und Animation der Texturen
public class Mushroom extends Item {
    private final String ITEM_NAME = "mushroom";

    public Mushroom(GameScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion(ITEM_NAME), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
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

    @Override
    public void use() {
    	gameScreen.getPlayer().setInvincible();
    	gameScreen.getPlayer().setAlpha(100);
        destroy();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
