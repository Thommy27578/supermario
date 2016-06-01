package com.bat.projectgdx.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bat.projectgdx.ProjectGdx;

/**
 * Created by msc on 05/28/16.
 */

//Bildschirm der angezeigt wird wenn das Spiel beendet ist - Bietet Möglichkeit neu zu starten
public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private final String winMessage = "GEWONNEN!";
    private final String loseMessage = "DU BIST GESTORBEN!";

    private Game game;

    public GameOverScreen(Game game, boolean gameWon){
        this.game = game;
        viewport = new FitViewport(ProjectGdx.V_WIDTH, ProjectGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((ProjectGdx) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label(winMessage, font);
        Label playAgainLabel = new Label("Klicke um erneut zu Spielen", font);
        
        if(!gameWon){
        	gameOverLabel.setText(loseMessage);
        }
        
        
        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    	//Wenn Mauseingabe (Linksklick), dann wird das Spiel neugestartet
        if(Gdx.input.justTouched()) {
            game.setScreen(new GameScreen((ProjectGdx) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}