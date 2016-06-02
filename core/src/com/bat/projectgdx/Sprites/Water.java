package com.bat.projectgdx.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.bat.projectgdx.Screens.GameScreen;



//Klasse für Wasser Objekt
public class Water extends InteractiveTileObject {

        public Water(MapObject object, GameScreen gameScreen){
            super(object, gameScreen);
            fixture.setUserData(this);

        }
        
        @Override
        public void onHeadHit(Player player) {
        }
    }


