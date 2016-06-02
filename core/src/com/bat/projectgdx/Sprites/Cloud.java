package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;



//Klasse für Cloud Objekt
public class Cloud extends InteractiveTileObject{
    public Cloud(MapObject object, GameScreen gameScreen) {
        super(object, gameScreen);
        fixture.setUserData(this);
        setCategoryFilter(ProjectGdx.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Player player) {
    }
}
