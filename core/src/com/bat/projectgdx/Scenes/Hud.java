package com.bat.projectgdx.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Screens.GameScreen;



//Overlay zwecks Timer, Levelname etc anzeige
public class Hud implements Disposable{
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    public String name = "";
    
    private GameScreen gameScreen;

    //Label die Text enthalten der im Overlay angezeigt wird
    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label levelLabel;
    Label worldLabel;
    Label playerLabel;

    //Initialiseren der Variablen und festlegen der Positionen der Texte in Tabellenform
    public Hud(SpriteBatch spriteBatch, GameScreen gameScreen){
        worldTimer = 100;
        timeCount = 0;
        score = 0;
        
        this.gameScreen = gameScreen;

        viewport = new FitViewport(ProjectGdx.V_WIDTH, ProjectGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        this.name=gameScreen.getPlayername();
        
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel = new Label("1-"+ gameScreen.getLevel(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("WORLD", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel = new Label(name, new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(playerLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
        

    }

    //Updaten des Timers - Zeit wird heruntergez�hlt
    public void update(float delta){
       timeCount += delta;
        if(timeCount >= 1 && worldTimer >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
        else if(worldTimer < 1){
        	gameScreen.getPlayer().die(false);
        }
        playerLabel.setText(name);
    }

    //Erh�hen des vom Spieler erzielten Punktestands
    public void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

	public Integer getScore() {
		return score;
	}

}
