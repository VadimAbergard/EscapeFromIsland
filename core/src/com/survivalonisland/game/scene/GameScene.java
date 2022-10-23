package com.survivalonisland.game.scene;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.survivalonisland.game.entity.Entity;
import com.survivalonisland.game.entity.Slime;
import com.survivalonisland.game.entity.Player;
import com.survivalonisland.game.gameobj.Boat;
import com.survivalonisland.game.item.*;
import com.survivalonisland.game.map.CustomWorld;
import com.survivalonisland.game.map.GameMap;

public class GameScene implements Screen {

    private OrthographicCamera camera;
    private OrthographicCamera cameraUI;
    private float zoomCamera = 1f;
    private SpriteBatch batch;
    private static Player player;
    private int rotatePlayer = 1;
    private final CustomWorld customWorld = new CustomWorld();

    private static World world;
    private Pixmap pixmap;
    private Texture spriteButtonE;
    private Texture spriteHeart;
    private Texture spriteHunger;
    private Texture spriteSelectSlot;
    private Texture spriteNullSprite;
    private final Boat boat = new Boat(new Vector2(4524, 7746));

    private int mirrorTexture = 1;
    private final Texture inventoryTexture = new Texture("sprites\\inventory\\inventory.png");
    private final int maxPosInv = 450;
    private int posInv = 0;
    private BitmapFont text;
    private GameMap map;
    private final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts\\PixelFont.ttf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    private final Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    private Texture blackSquare;
    private int blackSquarePositionX;
    private Texture blackSquarePause;
    private Texture tutorial;
    private static boolean returnInMenu = false;
    private static boolean pause = false;
    private static int selectButton = 0;
    private final Game game;
    private final Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music\\game\\field.ogg"));

    public GameScene(Game game) {
        this.game = game;
    }


    public Player getPlayer() {
        return player;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean value) {
        pause = value;
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, 0), true);

        menuMusic.setVolume(0.3f);
        menuMusic.setLooping(true);
        menuMusic.play();

        batch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = zoomCamera;
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        cameraUI = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cameraUI.zoom = zoomCamera;
        cameraUI.position.set(cameraUI.viewportWidth / 2f, cameraUI.viewportHeight / 2f, 0);
        cameraUI.update();

        player = new Player(game);
        player.setCamera(camera);
        player.spawn(new Vector2(4719, 7575));
        player.setSpeed(5);


        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.BLACK);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        blackSquare = new Texture(pixmap);
        pixmap.dispose();

        pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA4444);
        pixmap.setColor(0, 0, 0 ,0.6f);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        blackSquarePause = new Texture(pixmap);
        pixmap.dispose();



        map = new GameMap(camera, "map\\island.tmx");
        map.show();

        parameter.size = 22;
        text = generator.generateFont(parameter);
        generator.dispose();

        Body box = createBox(BodyDef.BodyType.DynamicBody, player.getLocation(), 20, 20);
        player.setBox(box);

        //table
        createBox(BodyDef.BodyType.StaticBody, new Vector2(4962, 7647), 29, 23);
        //rock
        createBox(BodyDef.BodyType.StaticBody, new Vector2(4833, 7326), 30, 30);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5475, 6939), 30, 30);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5475, 7514), 30, 30);
        //bush
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5250, 7615), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5375, 7615), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5500, 7615), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5625, 7615), 55, 55);

        createBox(BodyDef.BodyType.StaticBody, new Vector2(5570, 6849), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5695, 6849), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5820, 6849), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5945, 6849), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(6070, 6849), 55, 55);

        createBox(BodyDef.BodyType.StaticBody, new Vector2(4547, 7604), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(4674, 7039), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5062, 6840), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(6150, 7613), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5376, 7488), 55, 55);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5952, 7101), 55, 55);
        //barrier island
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5350, 7730), 950, 10);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(5350, 6750), 950, 10);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(4430, 7239), 10, 500);
        createBox(BodyDef.BodyType.StaticBody, new Vector2(6250, 7239), 10, 500);

        spawnEntity(new Slime(game));

        spriteButtonE = new Texture("sprites\\button\\E.png");
        spriteHeart = new Texture("sprites\\UI\\heart.png");
        spriteHunger = new Texture("sprites\\UI\\hunger.png");
        spriteSelectSlot = new Texture("sprites\\inventory\\selectSlot.png");
        spriteNullSprite = new Texture("sprites\\UI\\nullSprite.png");
        tutorial = new Texture("sprites\\gameObjects\\tutorial.png");

        new Apple().spawn(new Vector2(850, 2064));
        new Axe().spawn(new Vector2(4884, 7575));
        //@TODO сделать так чтобы при спавне предметов которых больше 1, разбрасывались.

        Gdx.input.setInputProcessor(player);
    }

    public void spawnEntity(Entity entity) {
        entity.spawn(null);
        Body box2 = createBox(BodyDef.BodyType.DynamicBody, new Vector2(entity.getLocation().x, entity.getLocation().y), 20, 20);
        entity.setBox(box2);
    }

    private Body createBox(BodyDef.BodyType type, Vector2 position, int width, int height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        Body body = world.createBody(bodyDef);
        body.setTransform(position, 0);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        body.createFixture(poly, 0);
        poly.dispose();

        return body;
    }

    private float accumulator = 0;

    public void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= 1/60f) {
            world.step(1/60f, 6, 2);
            accumulator -= 1/60f;
        }
    }

    public Boat getBoat() {
        return boat;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,  0.631f , 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // игровая логика
        doPhysicsStep(0.1f);

        // карта
        map.renderer();
        //new Disposable();






        batch.begin();
        // cameras
        batch.setProjectionMatrix(camera.combined);
        camera.update();

        // draw boat and purpose
        batch.draw(boat.getTexture(), boat.getPosition().x, boat.getPosition().y,
                boat.getTexture().getWidth() * 7, boat.getTexture().getHeight() * 7);
        batch.draw(boat.getPurposeSlimeTexture(), boat.getPosition().x + 50, boat.getPosition().y + 80,
                boat.getPurposeSlimeTexture().getWidth() * 2, boat.getPurposeSlimeTexture().getHeight() * 2);

        if(boat.getAmountPurpose() >= 10) text.setColor(Color.GREEN);
        else if(boat.getAmountPurpose() >= 5) text.setColor(Color.YELLOW);
        else text.setColor(Color.RED);
        text.draw(batch, "" + boat.getAmountPurpose() + "/10", boat.getPosition().x + 100, boat.getPosition().y + 105);


        for(Item item : customWorld.getItemInWorld()) {
            batch.draw(item.getTexture(), item.getLocation().x, item.getLocation().y,
                    item.getTexture().getWidth() * 3, item.getTexture().getHeight() * 3);

            // draw button
            if(distantion(player.getLocation(), item.getLocation()) < player.getDistanceToItem()) {
                batch.draw(spriteButtonE, player.getLocation().x - 5,
                        player.getLocation().y + 120,
                        spriteButtonE.getWidth() * 5, spriteButtonE.getHeight() * 5);
            }

        }
        for(Entity entity : customWorld.getEntityInWorld()) {
            entity.update(delta);
            mirrorTexture = 1;

            if(entity instanceof Player) {
                if(!entity.isDead() && !player.isInInventory()) {
                    if(!isPause()) {
                        if (entity.isMove() && (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                            mirrorTexture = -1;
                            rotatePlayer = mirrorTexture;
                        } else if (entity.isMove() && (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                            mirrorTexture = 1;
                            rotatePlayer = mirrorTexture;
                        }
                    }
                }

                batch.draw(entity.getFrame(), entity.getLocation().x - entity.getFrame().getRegionWidth()
                                * rotatePlayer + 10,
                        entity.
                                getLocation().y,
                        entity.getFrame().getRegionWidth() * 5 * rotatePlayer, entity.getFrame().getRegionHeight() * 5);
                continue;
            }

            if(!entity.isDead()) {
                if (player.getLocation().x < entity.getLocation().x) {
                    mirrorTexture = -1;
                } else if (player.getLocation().x > entity.getLocation().x) {
                    mirrorTexture = 1;
                }
            }

            batch.draw(entity.getFrame(), entity.getLocation().x - entity.getFrame().getRegionWidth() * mirrorTexture, entity.getLocation().y,
                    entity.getFrame().getRegionWidth() * 4 * mirrorTexture, entity.getFrame().getRegionHeight() * 4);
        }

        // button E on boat
        if(distantion(player.getLocation(), new Vector2(4587, 7699)) < 130) {
            batch.draw(spriteButtonE, player.getLocation().x - 5,
                    player.getLocation().y + 120,
                    spriteButtonE.getWidth() * 5, spriteButtonE.getHeight() * 5);
        }

        // tutorial
        if(distantion(player.getLocation(), new Vector2(4962, 7647)) < 120) {
            batch.draw(tutorial, 4820, 7690,
                    tutorial.getWidth() * 4, tutorial.getHeight() * 4);
        }

        // UI draw
         // camera
        batch.setProjectionMatrix(cameraUI.combined);
        cameraUI.update();

        // health player
        pixmap = new Pixmap(player.getHealth() * 3, 35, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.RED);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture lineBarHealth = new Texture(pixmap);
        pixmap.dispose();

        batch.draw(lineBarHealth,  cameraUI.position.x - Gdx.graphics.getWidth() / 2f + 10,
                cameraUI.position.y + Gdx.graphics.getHeight() / 2.3f * cameraUI.zoom);

        batch.draw(spriteHeart,  cameraUI.position.x - Gdx.graphics.getWidth() / 2f + 100 * 3 + 25,
                cameraUI.position.y + Gdx.graphics.getHeight() / 2.3f - 0.5f,
                spriteHeart.getWidth() + 30, spriteHeart.getHeight() + 30);

        // hunger player
        pixmap = new Pixmap(player.getHunger() * 3, 35, Pixmap.Format.RGBA4444);
        pixmap.setColor(Color.BROWN);
        pixmap.fillRectangle(0, 0, pixmap.getWidth(), pixmap.getHeight());
        Texture lineBarHunger = new Texture(pixmap);
        pixmap.dispose();

        batch.draw(lineBarHunger,  cameraUI.position.x - Gdx.graphics.getWidth() / 2f + 10,
                cameraUI.position.y + Gdx.graphics.getHeight() / 2.3f * cameraUI.zoom - 50);


        batch.draw(spriteHunger,  cameraUI.position.x - Gdx.graphics.getWidth() / 2f + 100 * 3 + 25,
                cameraUI.position.y + Gdx.graphics.getHeight() / 2.3f - 10 - 40,
                spriteHunger.getWidth() + 30, spriteHunger.getHeight() + 30);


        // draw inventory and logic
        final int SPEED_MOVE = 30;
        if(player.isInInventory()) {
            if(posInv >= -maxPosInv) {
                posInv -= SPEED_MOVE;
            }
        } else {
            if(posInv < 0) {
                posInv += SPEED_MOVE;
            } else posInv = 0;
        }

            // main inventory
        batch.draw(inventoryTexture, cameraUI.position.x + Gdx.graphics.getWidth() / 2f  - 90 + posInv,
                cameraUI.position.y - Gdx.graphics.getHeight() / 4f,
                inventoryTexture.getWidth() * 5.5f, inventoryTexture.getHeight() * 5.5f);

            // select slot
        batch.draw(spriteSelectSlot, cameraUI.position.x + Gdx.graphics.getWidth() / 2f - 81 + posInv,
                cameraUI.position.y - Gdx.graphics.getHeight() / 4f + 357 - 87.55f * player.getSelectSlotInt(),
                spriteSelectSlot.getWidth() * 5.1f, spriteSelectSlot.getHeight() * 5.1f);

        //@TODO move item in inventory

        int slotX = 0;
        int slotY = 0;
        for(int i = 0;i < player.getInventory().getSize();i++) {
            ItemStack slot = player.getInventory().getSlot(i);

            if(slotY == 4) {
                slotY = 0;
                slotX++;
            }
            if(slot == null) {
                batch.draw(spriteNullSprite, cameraUI.position.x + Gdx.graphics.getWidth() / 2f  - 78 + (83 * slotX) + posInv,
                        cameraUI.position.y - Gdx.graphics.getHeight() / 4f + 350 - (85 * slotY),
                        70, 70);
            } else {
                batch.draw(slot.getTexture(), cameraUI.position.x + Gdx.graphics.getWidth() / 2f  - 78 + (83 * slotX) + posInv,
                        cameraUI.position.y - Gdx.graphics.getHeight() / 4f + 350 - (85 * slotY),
                        70, 70);

                text.setColor(Color.BLACK);
                text.setUseIntegerPositions(false);
                text.draw(batch, "" + slot.getCount(),
                        cameraUI.position.x + Gdx.graphics.getWidth() / 2f  - 78 + (78 * slotX) + posInv,
                        cameraUI.position.y - Gdx.graphics.getHeight() / 4f + 380 - (85 * slotY) );
            }

            slotY++;
        }


        // pause menu
        if(pause) {
            batch.draw(blackSquarePause, 0, 0);

            text.setColor(Color.WHITE);
            if(selectButton == 0) text.setColor(Color.YELLOW);
            text.draw(batch, "Return", cameraUI.position.x - 40,
                    cameraUI.position.y);

            text.setColor(Color.WHITE);
            if(selectButton == 1) text.setColor(Color.YELLOW);
            text.draw(batch, "Exit in menu", cameraUI.position.x - 70,
                    cameraUI.position.y - 40);
        }

        // draw black square
        if(returnInMenu) {
            if(blackSquarePositionX > 0) {
                customWorld.getEntityInWorld().clear();
                customWorld.getItemInWorld().clear();
                menuMusic.stop();
                this.dispose();
                game.setScreen(new MainMenu(game));
            }
            batch.draw(blackSquare, blackSquarePositionX += 13, 0);
        }
        else if(blackSquarePositionX > Gdx.graphics.getWidth() * -1) {
            batch.draw(blackSquare, blackSquarePositionX -= 13, 0);
        }

        batch.end();

        lineBarHealth.dispose();
        lineBarHunger.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportHeight = height;
        camera.viewportWidth = width;
        camera.zoom = zoomCamera;

        cameraUI.viewportHeight = height;
        cameraUI.viewportWidth = width;
        cameraUI.zoom = zoomCamera;
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

    public World getWorld() {
        return world;
    }

    public CustomWorld getCustomWorld() {
        return this.customWorld;
    }

    private double distantion(Vector2 ObjOne, Vector2 ObjTwo) {
        return Math.sqrt(Math.pow((ObjOne.x - ObjTwo.x), 2) + Math.pow((ObjOne.y - ObjTwo.y), 2));
    }

    public int getSelectButton() {
        return selectButton;
    }

    public void addSelectButton(int value) {
        selectButton += value;
    }

    public void setSelectButton(int value) {
        selectButton = value;
    }

    public void setReturnInMenu(boolean value) {
        returnInMenu = value;
    }
}
