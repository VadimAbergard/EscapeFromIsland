package com.survivalonisland.game.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.survivalonisland.game.addition.AnimationFrames;
import com.survivalonisland.game.addition.Inventory;
import com.survivalonisland.game.item.Item;
import com.survivalonisland.game.item.ItemStack;
import com.survivalonisland.game.map.CustomWorld;
import com.survivalonisland.game.scene.GameScene;

import java.util.List;

public class Player implements Entity, InputProcessor {

    private Vector2 location;
    private boolean move;
    private float speed;
    private final int maxHealth = 100;
    private int health;
    private final int maxHunger = 100;
    private int hunger;
    private int damege;
    private final Texture animTexture;

    private TextureRegion frame;
    private final Animation<TextureRegion> animStay;
    private final Animation<TextureRegion> animWalk;
    private final Animation<TextureRegion> animAttack;
    private Animation<TextureRegion> animAttackWithAxe;
    private final Animation<TextureRegion> animDie;
    private float stateTime;

    private final int distanceToItem = 50;
    private final int distanceToEntity = 100;
    private final float maxCouldownSpawn = 3.0f;
    private float couldownSpawn = maxCouldownSpawn;

    private final float maxCouldownHunger = 5.0f;
    private float couldownHunger = maxCouldownHunger;
    private boolean isDead = false;
    private boolean isAttack = false;
    private OrthographicCamera camera;
    private final Inventory inventory = new Inventory(camera);
    private boolean inInventory = false;
    private int selectSlot = 0;
    private final float maxCouldownAttack = 0.3f;
    private float couldownAttack = maxCouldownAttack;
    private final GameScene gameScene;
    private int smoothCameraX = 0;
    private int smoothCameraY = 0;
    private final int MAX_SMOOTH = 40;
    private final int SPEED_CAMERA_SMOOTH = 2;

    private Body box;
    private Game game;
    private final Music soundWalk;
    private final Sound soundWave;
    private final Sound soundWaveSword;
    private final Sound soundHit;
    private final Sound soundSelectSlot;
    private final Sound soundUpItem;
    private final Sound soundDownItem;
    private final Sound soundEat;


    public Player(Game game) {
        this.game = game;
        gameScene = new GameScene(game);

        soundWalk = Gdx.audio.newMusic(Gdx.files.internal("sound\\grass.mp3"));
        soundWave = Gdx.audio.newSound(Gdx.files.internal("sound\\wave.mp3"));
        soundWaveSword = Gdx.audio.newSound(Gdx.files.internal("sound\\waveSword.ogg"));
        soundHit = Gdx.audio.newSound(Gdx.files.internal("sound\\hit.mp3"));
        soundSelectSlot = Gdx.audio.newSound(Gdx.files.internal("sound\\selectSlot.ogg"));
        soundUpItem = Gdx.audio.newSound(Gdx.files.internal("sound\\upItem.ogg"));
        soundDownItem = Gdx.audio.newSound(Gdx.files.internal("sound\\downItem.ogg"));
        soundEat = Gdx.audio.newSound(Gdx.files.internal("sound\\eat.ogg"));

        speed = 1f;
        health = maxHealth;
        hunger = maxHunger;
        location = new Vector2(10, 10);

        animTexture = new Texture("sprites\\entity\\player.png");
        AnimationFrames animationFrames = new AnimationFrames(animTexture, 6, 5);

        animStay = new Animation<>(1f, animationFrames.getFrames(6, 1));
        animWalk = new Animation<>(1f, animationFrames.getFrames(6, 2));
        animAttackWithAxe = new Animation<>(1f, animationFrames.getFrames(4, 3));
        animAttack = new Animation<>(1f, animationFrames.getFrames(4, 4));
        animDie = new Animation<>(1f, animationFrames.getFrames(3, 5));
    }

    public void setBox(Body box) {
        this.box = box;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void spawn(Vector2 positon) {
        this.location = positon;
        new CustomWorld().getEntitu().add(this);
    }
    //@TODO сделать Броню
    @Override
    public Vector2 getLocation() {
        return location;
    }
    @Override
    public void update(float delta) {
        // animation
        int SPEED_ANIMATION = 10;
        stateTime += Gdx.graphics.getDeltaTime() * SPEED_ANIMATION;

        if(gameScene.isPause()) {
            stopBox();
            return;
        }

        // when dead
        if(isDead) {
            if(couldownSpawn < 0) {
                isDead = false;
                couldownSpawn = maxCouldownSpawn;
                this.hunger = maxHunger;
                this.health = maxHealth;
            } else {
                couldownSpawn -= delta;
                if(stateTime >= 3) return;
                frame = animDie.getKeyFrame(stateTime, false);
                stopBox();
            }
            return;
        }
        // player hunger

        if(couldownHunger < 0) {
            couldownHunger = maxCouldownHunger;
            removeHunger(0.7f);
            if(this.hunger <= 0) {
                removeHealth(7);
            }
        } else {
            couldownHunger -= delta;
        }

        // when attack
        if(isAttack) {
            if (couldownAttack < 0) {
                isAttack = false;
                couldownAttack = maxCouldownAttack;
            } else {
                couldownAttack -= delta;
                // none weapon
                frame = animAttack.getKeyFrame(stateTime, false);
                // use axe
                if(inventory.getSlot(selectSlot) != null) {
                    if (inventory.getSlot(selectSlot).getId() == 2)
                        frame = animAttackWithAxe.getKeyFrame(stateTime, false);
                }


            }
            stopBox();
            return;
        }

        // moving player
        if(inInventory) {
            frame = animStay.getKeyFrame(stateTime, true);
            stopBox();
            return;
        }


        move = false;

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            move(-speed, 0);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            move(speed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            move(0, speed);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            move(0, -speed);
        }

        if(!move) {
            frame = animStay.getKeyFrame(stateTime, true);
            stopBox();


            if(smoothCameraX != 0 || smoothCameraY != 0) {
                if (smoothCameraX < 0) {
                    smoothCameraX += SPEED_CAMERA_SMOOTH;
                } else if(smoothCameraX <= MAX_SMOOTH) {
                    smoothCameraX -= SPEED_CAMERA_SMOOTH;
                }

                if (smoothCameraY < 0) {
                    smoothCameraY += SPEED_CAMERA_SMOOTH;
                } else if(smoothCameraY <= MAX_SMOOTH) {
                    smoothCameraY -= SPEED_CAMERA_SMOOTH;
                }
            }
        }

        camera.position.x = box.getPosition().x + smoothCameraX + this.frame.getRegionWidth() / 2f;
        camera.position.y = box.getPosition().y + smoothCameraY + this.frame.getRegionHeight() / 2f;
    }

    private void move(float speedX, float speedY) {
        frame = animWalk.getKeyFrame(stateTime, true);
        move = true;

        if(speedX != 0) smoothCameraX -= ((speedX < 0) ? -1 : 1) * SPEED_CAMERA_SMOOTH;
        if(speedY != 0) smoothCameraY -= ((speedY < 0) ? -1 : 1) * SPEED_CAMERA_SMOOTH;
        if(smoothCameraX >= MAX_SMOOTH) {
            smoothCameraX = MAX_SMOOTH;
        } else if(smoothCameraX <= -MAX_SMOOTH) {
            smoothCameraX = -MAX_SMOOTH;
        }
        if(smoothCameraY >= MAX_SMOOTH) {
            smoothCameraY = MAX_SMOOTH;
        } else if(smoothCameraY <= -MAX_SMOOTH) {
            smoothCameraY = -MAX_SMOOTH;
        }

        box.applyLinearImpulse(speedX * 100000, speedY * 100000, box.getPosition().x,  box.getPosition().y * 10, true);


        final int maxSpeed = 30;

        Vector2 vel = box.getLinearVelocity();
        if(speedX != 0) {
            if (vel.x >= maxSpeed) {
                vel.x = maxSpeed;
            } else if (vel.x <= maxSpeed) {
                vel.x = maxSpeed * -1;
            }
        } else {
            if(!(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) &&
                    !(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                vel.x = 0;
            }
        }
        if(speedY != 0) {
            if (vel.y >= maxSpeed) {
                vel.y = maxSpeed;
            } else if (vel.y <= maxSpeed) {
                vel.y = maxSpeed * -1;
            }
        } else {
            vel.y = 0;
        }
        box.setLinearVelocity(vel);

        this.location =  box.getPosition();
        if(!soundWalk.isPlaying()) {
            soundWalk.setVolume((float)(Math.random()) + 0.35f);
            soundWalk.play();
        }
    }
    private void stopBox() {
        Vector2 vel = box.getLinearVelocity();
        vel.x = 0f;
        vel.y = 0f;
        box.setLinearVelocity(vel);
    }

    @Override
    public void kill() {
        stateTime = 0;
        isDead = true;
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
    }

    @Override
    public boolean isMove() {
        return move;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void removeHealth(int value) {
        if(isDead()) return;

        health -= value;
        if(health < 0) {
            health = 0;
            this.kill();
        }
        this.removeHunger(1f);
    }

    @Override
    public void addHealth(int value) {
        health += value;
        if(health > maxHealth) {
            health = maxHealth;
        }
    }

    @Override
    public boolean isDead() {
        return this.isDead;
    }

    @Override
    public boolean isHit() {
        return false;
    }

    @Override
    public boolean isAttack() {
        return this.isAttack;
    }

    @Override
    public Body getBox() {
        return this.box;
    }

    @Override
    public void dispose() {
        animTexture.dispose();
    }

    @Override
    public int getHealth() {
        return health;
    }

    public int getHunger() {
        return hunger;
    }

    public void addHunger(final float value) {
        hunger += value;
        if(hunger > maxHunger) {
            hunger = maxHunger;
        }
    }

    public void removeHunger(final float value) {
        hunger -= value;
        if(hunger < 0) {
            hunger = 0;
        }
    }


    @Override
    public boolean keyDown(int ignored) {
        if(isDead) return false;

        if (Gdx.input.isKeyPressed(Input.Keys.E) && !gameScene.isPause()) {
            final List<Item> itemList = new CustomWorld().getItem();

            for(int i = 0;i < itemList.size();i++) {
                if(distantion(this.getLocation(), itemList.get(i).getLocation()) < distanceToItem) {
                    inventory.addItem(new ItemStack(itemList.get(i)));
                    itemList.remove(i);
                    soundUpItem.play();
                    return true;
                }
            }

            if(distantion(this.getLocation(), gameScene.getBoat().getPosition()) < 165) {
                for(int i = 0;i < inventory.getSize();i++) {
                    if(inventory.getSlot(i) == null) continue;
                    if(inventory.getSlot(i).getId() == 1 /*SlimeItem*/) {
                        gameScene.getBoat().addAmountPurpose(inventory.getSlot(i).getCount());
                        inventory.deleteItemInSlot(i);
                        soundDownItem.play();
                        if(gameScene.getBoat().getAmountPurpose() >= 10) {
                            gameScene.setReturnInMenu(true);
                        }
                        break;
                    }
                }

            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.I) && !gameScene.isPause()) {
            inInventory = !inInventory;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gameScene.setPause(!gameScene.isPause());
        }
        // use item
        if (Gdx.input.isKeyPressed(Input.Keys.F) && !gameScene.isPause()) {
            if(this.getSelectSlot() == null) return false;

            if(this.getSelectSlot().isFood()) {
                this.addHunger(this.getSelectSlot().getSaturation());
                this.getSelectSlot().removeCount(1);

                if(this.getSelectSlot().getCount() == 0) {
                    inventory.deleteItemInSlot(selectSlot);
                }
                soundEat.play();
            }
        }

        // select slot
        if(!isInInventory()) {
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
                selectSlot = 0;
                soundSelectSlot.play(1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
                selectSlot = 1;
                soundSelectSlot.play();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
                selectSlot = 2;
                soundSelectSlot.play();
            }
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
                selectSlot = 3;
                soundSelectSlot.play();
            }
        }

        // in pause
        if(gameScene.isPause()) {
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                gameScene.addSelectButton(1);
                if (gameScene.getSelectButton() > 1) gameScene.setSelectButton(0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                gameScene.addSelectButton(-1);
                if (gameScene.getSelectButton() < 0) gameScene.setSelectButton(1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER) || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_ENTER)) {
                if (gameScene.getSelectButton() == 0) {
                    gameScene.setPause(false);
                }
                if (gameScene.getSelectButton() == 1) {
                    gameScene.setReturnInMenu(true);
                    gameScene.setPause(false);
                }
            }
            System.out.println(gameScene.getSelectButton());
        }

        return false;
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
        if(isDead || isAttack) return false;

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            // attack
            if(!isInInventory()) {
                // animation
                isAttack = true;
                stateTime = 0;
                System.out.println(isAttack);

                // attack entity
                List<Entity> entityWorld = new CustomWorld().getEntitu();
                for (Entity entity : entityWorld) {
                    if (!(entity instanceof Player)) {
                        int _distanceToEntity = distanceToEntity;

                        damege = 2;

                        if(inventory.getSlot(selectSlot) != null) {
                            // use axe
                            if (inventory.getSlot(selectSlot).getId() == 2) {
                                damege = 4;
                                _distanceToEntity += 30;
                            }
                        }
                        if (distantion(this.getLocation(), entity.getLocation()) >= _distanceToEntity) continue;
                        if (entity.isDead()) continue;

                        entity.removeHealth(damege);

                        if (entity.getHealth() <= 0) {
                            entity.kill();

                            gameScene.getWorld().destroyBody(entity.getBox());

                            if(inventory.getSlot(selectSlot) != null) {
                                if (inventory.getSlot(selectSlot).getId() == 2) {
                                    soundHit.play();
                                    this.removeHunger(1f + damege / 5f);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            playSoundsHit();
            this.removeHunger(1f + damege / 6f);

        }
        return true;
    }
    private void playSoundsHit() {
        if(inventory.getSlot(selectSlot) != null) {
            if (inventory.getSlot(selectSlot).getId() == 2) {
                soundWaveSword.play();
                return;
            }
        }
        soundWave.play();
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
        selectSlot += amountY;
        if(selectSlot < 0) {
            selectSlot = 3;
        } else if (selectSlot > 3) {
            selectSlot = 0;
        }
        soundSelectSlot.play();
        return true;
    }

    private double distantion(Vector2 ObjOne, Vector2 ObjTwo) {
        return Math.sqrt(Math.pow((ObjOne.x - ObjTwo.x), 2) + Math.pow((ObjOne.y - ObjTwo.y), 2));
    }

    public boolean isInInventory() {
        return this.inInventory;
    }

    public ItemStack getSelectSlot() {
        return this.inventory.getSlot(selectSlot);
    }

    public int getSelectSlotInt() {
        return this.selectSlot;
    }

    public int getDistanceToItem() {
        return this.distanceToItem;
    }

}
