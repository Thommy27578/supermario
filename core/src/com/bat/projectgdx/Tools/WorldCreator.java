package com.bat.projectgdx.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bat.projectgdx.Items.ItemDef;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;
import com.bat.projectgdx.Sprites.Brick;
import com.bat.projectgdx.Items.Coin;
import com.bat.projectgdx.Sprites.Cloud;
import com.bat.projectgdx.Sprites.Goomba;
import com.bat.projectgdx.Sprites.SpecialBrick;

/**
 * Created by MSC on 14.03.2016.
 */
public class WorldCreator {

    private Array<Goomba> goomba;
    private Coin tempCoin;

    public WorldCreator(GameScreen gameScreen){
        World world = gameScreen.getWorld();
        TiledMap map = gameScreen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject object : map.getLayers().get("Ground").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2) / ProjectGdx.PPM, (rect.getY() + rect.getHeight()/2) / ProjectGdx.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2) / ProjectGdx.PPM, (rect.getHeight()/2) / ProjectGdx.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //pipe
        for(MapObject object : map.getLayers().get("Pipe").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2) / ProjectGdx.PPM, (rect.getY() + rect.getHeight()/2) / ProjectGdx.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2) / ProjectGdx.PPM, (rect.getHeight()/2) / ProjectGdx.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = ProjectGdx.OBJECT_BIT;
            body.createFixture(fdef);
        }
        //Bricks
        for(MapObject object : map.getLayers().get("Brick").getObjects().getByType(RectangleMapObject.class)){
           new Brick(object, gameScreen);
        }
        //Coins
        for(MapObject object : map.getLayers().get("Coin").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            gameScreen.spawnItem(new ItemDef(new Vector2((rect.getX()+ rect.getWidth()/2) / ProjectGdx.PPM, (rect.getY()+ rect.getWidth()/2) / ProjectGdx.PPM), Coin.class));
        }

        //Special-Brick
        for(MapObject object : map.getLayers().get("SpecialBrick").getObjects().getByType(RectangleMapObject.class)){
            new SpecialBrick(object, gameScreen);
        }

        //Cloud
        for(MapObject object : map.getLayers().get("Cloud").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.KinematicBody;
            bdef.position.set((rect.getX()+ rect.getWidth()/2) / ProjectGdx.PPM, (rect.getY() + rect.getHeight()/2) / ProjectGdx.PPM);

            body = world.createBody(bdef);
            shape.setAsBox((rect.getWidth()/2) / ProjectGdx.PPM, (rect.getHeight()/2) / ProjectGdx.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = ProjectGdx.BRICK_BIT;
            body.createFixture(fdef);

            new Cloud(object, gameScreen);
        }

        //Goomba
        goomba = new Array<Goomba>();
        for(MapObject object : map.getLayers().get("Goomba").getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goomba.add(new Goomba(gameScreen, rect.getX() / ProjectGdx.PPM, rect.getY() / ProjectGdx.PPM));
        }

    }

    public Array<Goomba> getGoomba() {
        return goomba;
    }
}
