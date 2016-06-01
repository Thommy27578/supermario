package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Scenes.Hud;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 14.03.2016.
 */

//Klasse für Brick Objekt 
public class Door extends InteractiveTileObject {
	
	private GameScreen screen;
	
    public Door(MapObject object, GameScreen gameScreen){
        super(object, gameScreen);
        fixture.setUserData(this);
        setCategoryFilter(ProjectGdx.DOOR_BIT);
        screen = gameScreen;
    }

    @Override
    public void onHeadHit(Player player) {
    	screen.gameOver(true);
    }
}
