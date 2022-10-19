package com.survivalonisland.game.scene;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.survivalonisland.game.addition.AnimationFrames;

public class MainMenu implements Screen, InputProcessor {
    private final SpriteBatch batch = new SpriteBatch();
    private final Texture backround = new Texture("sprites\\elements\\backroundMenu.png");
    private OrthographicCamera camera;
    private Animation<TextureRegion> animPlayerIdle;
    private float stateTime;
    private TextureRegion frame;
    private final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\menu\\menuMusic.ogg"));
    private final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\PixelFont.ttf"));
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private BitmapFont text;
    private Texture blackSquare;
    private int blackSquarePositionX;

    private final Game game;
    private Stage stage;

    private int selectButton = 0;
    public MainMenu(Game game) {
        this.game = game;
    }


    @Override
    public void show() {
        Gdx.input.setCursorCatched(true);

        new GameScene(game).setReturnInMenu(false);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(new Vector3(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0));
        camera.update();

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        blackSquare = new Texture(pixmap);

        menuMusic.setVolume(0.3f);
        menuMusic.setLooping(true);
        menuMusic.play();

        parameter.size = (int)((Gdx.graphics.getWidth() / 100f) * 2 + (Gdx.graphics.getHeight() / 100f) * 2) /*30*/;
        text = generator.generateFont(parameter);

        Texture animTexture = new Texture("sprites\\entity\\player.png");
        AnimationFrames animationFrames = new AnimationFrames(animTexture, 6, 5);

        animPlayerIdle = new Animation<>(1f, animationFrames.getFrames(6, 1));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,  0.631f , 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // animation
        int SPEED_ANIMATION = 7;
        stateTime += delta * SPEED_ANIMATION;

        frame = animPlayerIdle.getKeyFrame(stateTime, true);


        batch.begin();

        batch.draw(backround, camera.position.x - Gdx.graphics.getWidth() / 2f, camera.position.y - Gdx.graphics.getHeight() / 2f,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(frame, camera.position.x - Gdx.graphics.getHeight() / 2f, camera.position.y - Gdx.graphics.getHeight() / 2.7f,
                frame.getRegionWidth() * 3 + Gdx.graphics.getWidth() / 6f + backround.getWidth() / 3f, frame.getRegionHeight() * 3 + Gdx.graphics.getHeight() / 5f + backround.getHeight() / 2f);

        text.setColor(Color.WHITE);
        if(selectButton == 0) text.setColor(Color.YELLOW);
        text.draw(batch, "play", camera.position.x + Gdx.graphics.getWidth() / 2f - 7 * parameter.size,
                camera.position.y + 100 + parameter.size);

        text.setColor(Color.WHITE);
        if(selectButton == 1) text.setColor(Color.YELLOW);
        text.draw(batch, "exit", camera.position.x + Gdx.graphics.getWidth() / 2f - 7 * parameter.size,
                camera.position.y + 40 + parameter.size / 2f);

        text.setColor(Color.WHITE);
        text.draw(batch, "Alpha 0.1", camera.position.x + Gdx.graphics.getWidth() / 2f - 5 * parameter.size,
                camera.position.y - Gdx.graphics.getHeight() / 2f + parameter.size);

        batch.draw(blackSquare, blackSquarePositionX -= 13, 0);


        batch.end();

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        stage.act(delta); //Perform ui logic
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
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

    @Override
    public boolean keyDown(int keycode) {
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            selectButton += 1;
            if(selectButton > 1) selectButton = 0;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            selectButton -= 1;
            if(selectButton < 0) selectButton = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ENTER)) {
            if(selectButton == 0) {
                this.dispose();
                game.setScreen(new GameScene(game));
                menuMusic.stop();
                menuMusic.dispose();
            }
            if(selectButton == 1) {
                Gdx.app.exit();
            }
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
