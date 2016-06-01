package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Scenes.Hud;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 14.03.2016.
 */

//Klasse für Brick Objekt 
public class Brick extends InteractiveTileObject {
    private Hud hud;
    public Brick(MapObject object, GameScreen gameScreen){
        super(object, gameScreen);
        fixture.setUserData(this);
        setCategoryFilter(ProjectGdx.BRICK_BIT);
        hud = gameScreen.getHud();
    }

    @Override
    public void onHeadHit(Player player) {
        setCategoryFilter(ProjectGdx.DESTROYED_BIT);
        getCell().setTile(null);
        hud.addScore(10);
    }
}
