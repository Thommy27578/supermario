package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.bat.projectgdx.Items.ItemDef;
import com.bat.projectgdx.Items.Mushroom;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Scenes.Hud;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 18.03.2016.
 */

//Klasse für SpecialBrick Objekt - Beinhaltet Item nach Zerstörung
public class SpecialBrick extends InteractiveTileObject{
    private Hud hud;
    private TiledMapTileSet tileSet;
    private final int BLANK_BOX = 61;
    private final int SCORE_POINTS = 200;
    private final String TILESET_NAME = "object_gutter";

    public SpecialBrick(MapObject object, GameScreen gameScreen){
        super(object, gameScreen);
        tileSet = map.getTileSets().getTileSet(TILESET_NAME);
        fixture.setUserData(this);
        setCategoryFilter(ProjectGdx.BRICK_BIT);
        hud = gameScreen.getHud();
    }

    //Ändern der Textur nach Zerstören des Objekts
    @Override
    public void onHeadHit(Player player) {
        Gdx.app.log("SpecialBrick", "Collision");
        if(getCell().getTile().getId() == BLANK_BOX){
            Gdx.app.log("No gutter found", "");
            return;
        }

        screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / ProjectGdx.PPM), Mushroom.class));
        hud.addScore(SCORE_POINTS);
        getCell().setTile(tileSet.getTile(BLANK_BOX));
    }
}
