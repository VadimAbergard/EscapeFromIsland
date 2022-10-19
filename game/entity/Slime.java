package com.survivalonisland.game.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.survivalonisland.game.Core;
import com.survivalonisland.game.addition.AnimationFrames;
import com.survivalonisland.game.item.ItemStack;
import com.survivalonisland.game.item.SlimeItem;
import com.survivalonisland.game.map.CustomWorld;
import com.survivalonisland.game.scene.GameScene;

public class Slime implements Entity {


    private final Vector2 defaultPositionSpawn = new Vector2(5836, 7602);
    private Vector2 location = defaultPositionSpawn;
    private float speed = 5f;
    private final float maxCouldownAttack = 2f;
    private float couldownAttack = maxCouldownAttack;
    private int health = 3;
    private final int distantionToPlayer = 290;
    private final Player player = new GameScene(new Core()).getPlayer();
    private final Texture animTexture;
    private TextureRegion frame;
    private final Animation<TextureRegion> animIdle;
    private final Animation<TextureRegion> animWalk;
    private final Animation<TextureRegion> animAttack;
    private final Animation<TextureRegion> animHit;
    private final Animation<TextureRegion> animDie;
    private float stateTime;
    private Body box;
    private boolean isAttack = false;
    private boolean isAttackPlayer = false;
    private boolean dead = false;
    private boolean reallydead = false;
    private boolean hit = false;
    private boolean useInpulse = false;
    private final GameScene gameScene;
    private final Game game;
    private final Music soundMove;
    private final Sound soundAttack;
    private final Sound soundHit;


    public Slime(Game game) {
        this.game = game;
        gameScene = new GameScene(game);

        soundMove = Gdx.audio.newMusic(Gdx.files.internal("sound\\slimeMove.ogg"));
        soundAttack = Gdx.audio.newSound(Gdx.files.internal("sound\\slimeAttack.ogg"));
        soundHit = Gdx.audio.newSound(Gdx.files.internal("sound\\slimeHit.ogg"));

        animTexture = new Texture("sprites\\entity\\slime.png");
        AnimationFrames animationFrames = new AnimationFrames(animTexture, 7, 5);

        animIdle = new Animation<>(1f, animationFrames.getFrames(4, 1));
        animWalk = new Animation<>(1f, animationFrames.getFrames(6, 2));
        animAttack = new Animation<>(1f, animationFrames.getFrames(7, 3));
        animHit = new Animation<>(1f, animationFrames.getFrames(3, 4));
        animDie = new Animation<>(1f, animationFrames.getFrames(5, 5));

        frame = animIdle.getKeyFrame(stateTime, true);
    }

    @Override
    public Vector2 getLocation() {
        return location;
    }

    @Override
    public void spawn(Vector2 positon) {
        if(positon != null) this.location = positon;
        else {
            Vector2[] randomSpawn = {
                    new Vector2(5836, 7602),
                    new Vector2(5223, 7473),
                    new Vector2(5607, 7002),
                    new Vector2(6129, 7158)
            };
            this.location = randomSpawn[(int)(Math.random() * randomSpawn.length)];
        }

        new CustomWorld().getEntitu().add(this);
    }

    public void setBox(Body box) {
        this.box = box;
    }

    @Override
    public Body getBox() {
        return this.box;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void removeHealth(int value) {
        stateTime = 0;
        couldownAttack = maxCouldownAttack;
        speed = 5;
        isAttack = false;
        useInpulse = false;
        isAttackPlayer = false;
        health -= value;
        if(health > 0) {
            hit = true;
            int VectorX = 1;
            if(this.getLocation().x < player.getLocation().x) VectorX = -1;
            box.applyLinearImpulse(VectorX * 100000, 0, box.getPosition().x, box.getPosition().y * 10, true);
        }
        soundHit.play();
    }

    @Override
    public void addHealth(int value) {}

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void update(float delta) {
        // animation
        final int SPEED_ANIMATION = 9;
        stateTime += Gdx.graphics.getDeltaTime() * SPEED_ANIMATION;

        if(gameScene.isPause()) {
            stopBox();
            return;
        }

        this.getLocation().set(box.getPosition());

        if(this.isHit()) {
            frame = animHit.getKeyFrame(stateTime, true);
            if(stateTime > 2) {
                stopBox();
                hit = false;
                return;
            }
            return;
        }

        if(this.isDead()) {
            if(reallydead) {
                new CustomWorld().getEntitu().remove(this);
                gameScene.spawnEntity(new Slime(game));
                return;
            }
            stopBox();
            frame = animDie.getKeyFrame(stateTime, true);
            if(stateTime > 4) {
                this.location = new Vector2(0, 0);
                reallydead = true;
            }
            return;
        }

        if(isAttack) {
            soundMove.stop();
            if(couldownAttack < 0) {
                couldownAttack = maxCouldownAttack;
                speed = 5;
                isAttack = false;
                useInpulse = false;
                isAttackPlayer = false;
            } else {
                if(couldownAttack > 0f && couldownAttack < 0.6f) {
                    speed = 200;
                    int vectorX = 1, vectorY = 1;
                    if (this.getLocation().x > player.getLocation().x) {
                        vectorX = -1;
                    }
                    if (this.getLocation().y > player.getLocation().y) {
                        vectorY = -1;
                    }
                    if(!useInpulse) {
                        box.applyLinearImpulse(vectorX * 100000, vectorY * 100000, box.getPosition().x, box.getPosition().y * 10, true);
                        soundAttack.play();
                        useInpulse = true;
                        stateTime = 0;
                    }
                    frame = animAttack.getKeyFrame(stateTime, true);
                    if(distantion(this.getLocation(), player.getLocation()) < distantionToPlayer) {
                        if(!isAttackPlayer) {
                            player.removeHealth(7);
                            isAttackPlayer = true;
                        }
                    }
                } else {
                    stopBox();
                }
                if(stateTime < 2 && !useInpulse) {
                    frame = animAttack.getKeyFrame(stateTime, true);
                }
            }
            couldownAttack -= delta;
            return;
        }

        if(distantion(this.getLocation(), player.getLocation()) < distantionToPlayer / 2f) {
            stopBox();
            attack();
            return;
        }

        if(distantion(this.getLocation(), player.getLocation()) < distantionToPlayer) {
            frame = animWalk.getKeyFrame(stateTime, true);

            // logic
            if (this.getLocation().x > player.getLocation().x) {
                move(-speed, 0);
            } else {
                move(speed, 0);
            }

            if (this.getLocation().y > player.getLocation().y) {
                move(0, -speed);
            } else {
                move(0, speed);
            }
            return;
        } else {
            stopBox();
        }
        soundMove.stop();
        frame = animIdle.getKeyFrame(stateTime, true);
    }

    private void attack() {
        isAttack = true;
        stateTime = 0;
    }

    private void move(final float vectorX, final float vectorY) {
        box.applyLinearImpulse(vectorX * 100000, vectorY * 100000, box.getPosition().x,  box.getPosition().y * 10, true);

        Vector2 vel = box.getLinearVelocity();
        if(vectorX != 0) {
            if (vel.x >= speed) {
                vel.x = speed;
            } else if (vel.x <= speed) {
                vel.x = speed * -1;
            }
        }
        if(vectorY != 0) {
            if (vel.y >= speed) {
                vel.y = speed;
            } else if (vel.y <= speed) {
                vel.y = speed * -1;
            }
        } else {
            vel.y = 0;
        }
        box.setLinearVelocity(vel);

        if(!soundMove.isPlaying() && !this.isAttack) {
            soundMove.play();
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
        dead = true;
        player.getInventory().addItem(new ItemStack(new SlimeItem()));
    }

    @Override
    public TextureRegion getFrame() {
        return this.frame;
    }

    @Override
    public boolean isMove() {
        return false;
    }
    @Override
    public boolean isDead() {
        return this.dead;
    }

    @Override
    public boolean isHit() {
        return this.hit;
    }

    @Override
    public boolean isAttack() {
        return false;
    }

    @Override
    public void dispose() {
        animTexture.dispose();
    }

    private double distantion(Vector2 ObjOne, Vector2 ObjTwo) {
        return Math.sqrt(Math.pow((ObjOne.x - ObjTwo.x), 2) + Math.pow((ObjOne.y - ObjTwo.y), 2));
    }


}
