package com.bat.projectgdx.Sprites;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bat.projectgdx.Scenes.Hud;
import com.bat.projectgdx.Screens.GameScreen;

/**
 * Created by MSC on 18.03.2016.
 */
public class Water extends InteractiveTileObject {


        private Player player;

        public Water(MapObject object, GameScreen gameScreen){
            super(object, gameScreen);
            fixture.setUserData(this);
            player = gameScreen.getPlayer();

        }

        public boolean collides(){
            return Intersector.overlaps(this.bounds, player.getBoundingRectangle());
        }

        @Override
        public void onHeadHit(Player player) {
        }
    }


