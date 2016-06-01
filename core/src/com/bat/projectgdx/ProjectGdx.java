package com.bat.projectgdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bat.projectgdx.Screens.TitleScreen;

//Einstiegsklasse die nach Start den Bildschirm festlegt und die Werte der Kollisionsbits beinhaltet
public class ProjectGdx extends Game {

	public SpriteBatch batch;
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;

	public static final short GROUND_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short OBJECT_BIT = 16;
	public static final short ENEMY_BIT = 32;
	public static final short ENEMY_HEAD_BIT = 64;
	public static final short PLAYER_HEAD_BIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short NOTHING_BIT = 512;
	//public static GameScreen screen;
	public static TitleScreen titleScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		titleScreen = new TitleScreen(this);
		setScreen(titleScreen);
	}

	@Override
	public void render () {
		super.render();
	}
}
