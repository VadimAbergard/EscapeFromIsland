package com.survivalonisland.game.scene;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Title implements Screen  {

    private Texture title;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private final float zoomCamera = 0.2f;
    private float timer;

    private final Game game;
    public Title(Game game) {
        this.game = game;
    }


    @Override
    public void show() {
        batch = new SpriteBatch();
        title = new Texture("sprites\\elements\\title.png");
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0));
        camera.update();
        camera.zoom = zoomCamera;
        batch.setColor(1, 1, 1, 0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(timer > 2.6f) {
            this.dispose();
            game.setScreen(new MainMenu(game));
        }
        else if(timer > 1.3f) modifyAlpha(-delta);
        else if(timer > 0.3f) modifyAlpha(delta);

        batch.begin();

        batch.draw(title, Gdx.graphics.getWidth() - title.getWidth() / 2f, Gdx.graphics.getHeight()- title.getHeight() / 2f);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        timer += delta;
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.zoom = zoomCamera;
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

    }

    private void modifyAlpha(float alpha) {
        batch.setColor(1, 1, 1, batch.getColor().a + alpha);
    }
}
