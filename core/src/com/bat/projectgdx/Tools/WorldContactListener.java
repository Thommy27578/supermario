package com.bat.projectgdx.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bat.projectgdx.Items.Item;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Sprites.Enemy;
import com.bat.projectgdx.Sprites.InteractiveTileObject;
import com.bat.projectgdx.Sprites.Player;

/**
 * Created by MSC on 14.03.2016.
 */

//Kollisionslistener - fixtureA Kollisionsteilhaber 1 fixtureB entsprechend anderer Teilhaber
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Bitweise Addition der Teilhaber zwecks Identifizierung des Kollisionsfalls
        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        //Identifizierung der Kollision und entsprechende Reaktion auf Ereignis
        switch (cDef){

            case ProjectGdx.PLAYER_HEAD_BIT | ProjectGdx.BRICK_BIT:
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.PLAYER_HEAD_BIT)
                    ((InteractiveTileObject) fixtureB.getUserData()).onHeadHit((Player) fixtureA.getUserData());
                else
                    ((InteractiveTileObject) fixtureB.getUserData()).onHeadHit((Player) fixtureB.getUserData());
                break;

            case ProjectGdx.PLAYER_BIT | ProjectGdx.ENEMY_BIT:
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.ENEMY_BIT && fixtureB.getUserData() != null){
                    ((Player)fixtureB.getUserData()).die();
                    ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                }
                else if (fixtureA.getUserData() != null){
                    ((Player)fixtureA.getUserData()).die();
                    ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case ProjectGdx.PLAYER_BIT | ProjectGdx.ENEMY_HEAD_BIT:
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.ENEMY_HEAD_BIT){
                    ((Enemy)fixtureA.getUserData()).hitOnHead();
                }
                else{
                    ((Enemy)fixtureB.getUserData()).hitOnHead();
                }
                break;
            case ProjectGdx.ENEMY_BIT | ProjectGdx.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.ENEMY_BIT){
                    ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                }
                else{
                    ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case ProjectGdx.ENEMY_BIT | ProjectGdx.ENEMY_BIT:
                ((Enemy)fixtureA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixtureB.getUserData()).reverseVelocity(true, false);
                break;
            case ProjectGdx.ITEM_BIT | ProjectGdx.OBJECT_BIT:
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.ITEM_BIT){
                    ((Item)fixtureA.getUserData()).reverseVelocity(true, false);
                }
                else{
                    ((Item)fixtureB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case ProjectGdx.PLAYER_BIT | ProjectGdx.ITEM_BIT:
            	contact.setEnabled(false);
                if(fixtureA.getFilterData().categoryBits == ProjectGdx.ITEM_BIT){
                    ((Item)fixtureA.getUserData()).use();
                }
                else{
                    ((Item)fixtureB.getUserData()).use();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
