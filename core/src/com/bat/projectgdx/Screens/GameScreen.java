package com.bat.projectgdx.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bat.projectgdx.Items.Coin;
import com.bat.projectgdx.Items.Item;
import com.bat.projectgdx.Items.ItemDef;
import com.bat.projectgdx.Items.Mushroom;
import com.bat.projectgdx.ProjectGdx;
import com.bat.projectgdx.Scenes.Hud;
import com.bat.projectgdx.Sprites.Enemy;
import com.bat.projectgdx.Sprites.Goomba;
import com.bat.projectgdx.Sprites.Player;
import com.bat.projectgdx.Tools.WorldContactListener;
import com.bat.projectgdx.Tools.WorldCreator;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by msc on 11.03.2016.
 */
public class GameScreen implements Screen {

    //Main variables
    private ProjectGdx game;
    private TextureAtlas atlas;
    private Viewport viewport;
    private OrthographicCamera gameCam;
    private Hud hud;
    private Player player;
    private final String TEXTUREPACK_NAME = "Sprites\\entities.pack";
    private final String LEVEL_NAME = "Maps\\world2.tmx";

    //Tiled Map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private WorldCreator worldCreator;

    //Items
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    public Hud getHud(){
        return hud;
    }

    public GameScreen(ProjectGdx game){
        atlas = new TextureAtlas(TEXTUREPACK_NAME);

        this.game = game;
        gameCam = new OrthographicCamera();
        viewport = new FitViewport(ProjectGdx.V_WIDTH / ProjectGdx.PPM, ProjectGdx.V_HEIGHT / ProjectGdx.PPM, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load(LEVEL_NAME);
        renderer = new OrthogonalTiledMapRenderer(map, 1 / ProjectGdx.PPM);

        //Item initialize
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        gameCam.position.set((viewport.getWorldWidth() / 2), viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        worldCreator = new WorldCreator(this);

        player = new Player(world, this);

        world.setContactListener(new WorldContactListener());

    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
            else if(idef.type == Coin.class){
                items.add(new Coin(this, idef.position.x, idef.position.y));
            }
        }
    }

    @Override
    public void show() {

    }

    public Player getPlayer(){
        return player;
    }

    public void handleInput(float delta){

        if(player.currentState == Player.State.DEAD) {
            return;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2f){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2f){
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !player.getState().equals(Player.State.JUMPING) && !player.getState().equals((Player.State.FALLING))){
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            player.b2body.setTransform(new Vector2(0.1f,0.2f), 0);
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    public void update(float delta){
        handleInput(delta);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);

        player.update(delta);


        for(Enemy enemy : worldCreator.getGoomba()){
            enemy.update(delta);
            if(enemy.getX() < player.getX() + 224 / ProjectGdx.PPM){
                enemy.b2dbody.setActive(true);
            }
        }

        for(Item item : items){
            item.update(delta);
        }
        hud.update(delta);

        if(player.currentState != Player.State.DEAD) {
            gameCam.position.x = player.b2body.getPosition().x;
        }

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //render box2d
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Goomba enemy : worldCreator.getGoomba()){
            enemy.draw(game.batch);
        }
        for(Item item : items){
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);

    }

    public World getWorld(){
        return world;
    }

    public TiledMap getMap(){
        return map;
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
        map.dispose();
        world.dispose();
        renderer.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
