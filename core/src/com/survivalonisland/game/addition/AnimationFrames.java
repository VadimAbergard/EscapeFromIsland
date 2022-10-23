package com.survivalonisland.game.addition;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationFrames {

    private final Texture texture;
    private final int FRAME_COLS_IN_TEXSTURE, FRAME_ROWS_IN_TEXSTURE;

    public AnimationFrames(final Texture texture, final int FRAME_COLS, final int FRAME_ROWS) {
        this.texture = texture;
        this.FRAME_COLS_IN_TEXSTURE = FRAME_COLS;
        this.FRAME_ROWS_IN_TEXSTURE = FRAME_ROWS;
    }

    public TextureRegion[] getFrames(final int FRAME_COLS, final int FRAME_ROW) {
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS_IN_TEXSTURE,
                texture.getHeight() / FRAME_ROWS_IN_TEXSTURE);

        TextureRegion[] frames = new TextureRegion[FRAME_COLS * FRAME_ROW];
        int index = 0;
        for (int i = 0; i < FRAME_ROW; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[FRAME_ROW - 1][j];
            }
        }

        return frames;
    }
}
