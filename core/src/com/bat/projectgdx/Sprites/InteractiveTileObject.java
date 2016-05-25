package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 14.03.2016.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected GameScreen screen;

    protected MapObject object;
    protected Fixture fixture;

    public InteractiveTileObject(MapObject object, GameScreen gameScreen){
        this.object = object;
        this.screen = gameScreen;
        this.world = gameScreen.getWorld();
        this.map = gameScreen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();


        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX()+ bounds.getWidth()/2) / ProjectGdx.PPM, (bounds.getY() + bounds.getHeight()/2) / ProjectGdx.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2) / ProjectGdx.PPM, (bounds.getHeight()/2) / ProjectGdx.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public abstract void onHeadHit(Player player);

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * ProjectGdx.PPM /16), (int)(body.getPosition().y * ProjectGdx.PPM / 16));
    }
}
