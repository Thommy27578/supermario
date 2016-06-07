package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;



//Klasse für Brick Objekt 
public class Door extends InteractiveTileObject {
	
	
    public Door(MapObject object, GameScreen gameScreen){
        super(object, gameScreen);
        fixture.setUserData(this);
        setCategoryFilter(ProjectGdx.DOOR_BIT);
        screen = gameScreen;
    }

    @Override
    public void onHeadHit(Player player) {
    	screen.getHud().addScore(1000);
    	player.die(true);
    }
}
