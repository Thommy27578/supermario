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

//Bildschirm der angezeigt wird wenn das Spiel beendet ist - Bietet Möglichkeit neu zu starten
public class GameOverScreen implements Screen {
	private Viewport viewport;
	private Stage stage;
	private final String winMessage = "GEWONNEN!";
	private final String winMessageEnd = "Gewonnen, das Spiel ist vorbei!";
	private final String playAgainMessage = "Klicke um erneut zu spielen";
	private final String playNextMessage = "Klicke um weiter zu spielen";
	private final String loseMessage = "DU BIST GESTORBEN!";
	private final String pointsMessage = "Deine Punktzahl: ";
	private String player;
	private int nextLevel;
	private boolean end = false;
	private int oldPoints;

	private Game game;

	public GameOverScreen(Game game, boolean gameWon) {
		this.game = game;
		player = ((GameScreen) this.game.getScreen()).getPlayername();

		if (((GameScreen) this.game.getScreen()).getLevel() < 2) {
			nextLevel = ((GameScreen) this.game.getScreen()).getLevel() + 1;
		} else
			end = true;
		oldPoints = ((GameScreen) this.game.getScreen()).getHud().getScore();
		viewport = new FitViewport(ProjectGdx.V_WIDTH, ProjectGdx.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, ((ProjectGdx) game).batch);

		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

		Table table = new Table();
		table.center();
		table.setFillParent(true);
		Label gameOverLabel;
		Label playAgainLabel;
		if (end) {
			gameOverLabel = new Label(winMessageEnd, font);
			playAgainLabel = new Label(playAgainMessage, font);
		} else {
			gameOverLabel = new Label(winMessage, font);
			playAgainLabel = new Label(playNextMessage, font);
		}

		if (!gameWon) {
			gameOverLabel.setText(loseMessage);
			end = true;
		}

		Label pointsLabel = new Label(pointsMessage + oldPoints, font);

		table.add(gameOverLabel).expandX();

		if (end) {
			table.row();
			table.add(pointsLabel).expandX().padTop(10f);
		}

		table.row();
		table.add(playAgainLabel).expandX().padTop(10f);

		stage.addActor(table);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		// Wenn Mauseingabe (Linksklick), dann wird das Spiel neugestartet
		if (Gdx.input.justTouched()) {

			if (!end) {
				game.setScreen(new GameScreen((ProjectGdx) game, nextLevel));
				((GameScreen) this.game.getScreen()).setPlayername(player);
				((GameScreen) this.game.getScreen()).setPlayerHudName();
				((GameScreen) this.game.getScreen()).setOldPoints(oldPoints);
				;
			} else
				game.setScreen(new TitleScreen((ProjectGdx) game));
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