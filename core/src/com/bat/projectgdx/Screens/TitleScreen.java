package com.bat.projectgdx.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bat.projectgdx.ProjectGdx;

/**
 * Created by msc on 05/28/16.
 */

//Startbildschirm nach Anwendungsstart - Führt zu Spielbilschirm
public class TitleScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private final String TITLE = "The Journey";
    private final String BUTTONTEXT = "Drücke Enter";
    private final String TEXTFIELD_PLACEHOLDER = "Dein Name";
    
    private TextField nameTextField;
    
    private String name;
    
    private Game game;

    public TitleScreen(Game game){
        this.game = game;
        viewport = new FitViewport(ProjectGdx.V_WIDTH, ProjectGdx.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((ProjectGdx) game).batch);
        
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label titleLabel = new Label(TITLE, font);
        Label buttonLabel = new Label(BUTTONTEXT, font);
        Label textFieldLabel = new Label(TEXTFIELD_PLACEHOLDER, font);
        TextFieldStyle textFieldStyle = new TextFieldStyle();
        textFieldStyle.font = new BitmapFont();
        textFieldStyle.fontColor = Color.BLACK;
        nameTextField = new TextField("", textFieldStyle);
        nameTextField.setMaxLength(10);
        stage.setKeyboardFocus(nameTextField);
        
        
        table.add(titleLabel).expandX();
        table.row();
        table.add(textFieldLabel);
        table.add(nameTextField);
        table.row();
        table.add(buttonLabel).expandX().padTop(20f);
        

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyJustPressed(Keys.ENTER)) {
        	name = nameTextField.getText();
        	GameScreen screen = new GameScreen((ProjectGdx) game);
        	screen.setPlayerName(name);
            game.setScreen(screen);
            dispose();
        }
        Gdx.gl.glClearColor(1, 1, 1, 1);
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