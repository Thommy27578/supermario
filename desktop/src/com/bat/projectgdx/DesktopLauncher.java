package com.bat.projectgdx;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bat.projectgdx.ProjectGdx;


//Hauptklasse - Startet �ber Klasse ProjectGdx das Spiel
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 624;
		new LwjglApplication(new ProjectGdx(), config);
	}
}

