package com.bat.projectgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Scenes.Hud;

/**
 * Created by msc on 10.05.2016
 */
public class CreditScreen implements Screen {

    //Main variables
    private ProjectGdx game;
    private TextureAtlas atlas;
    private Viewport viewport;
    private OrthographicCamera gameCam;
    private Hud hud;

   

    public CreditScreen(ProjectGdx game){

        this.game = game;
        gameCam = new OrthographicCamera();
        viewport = new FitViewport(ProjectGdx.V_WIDTH / ProjectGdx.PPM, ProjectGdx.V_HEIGHT / ProjectGdx.PPM, gameCam);
    }

    @Override
    public void show() {

    }

    public void handleInput(float delta){

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) ){
            // TODO: Implement switch to GameScreen
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void update(float delta){
        handleInput(delta);     
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //renderer.render();

        //render box2d
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

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
        hud.dispose();
    }
}
