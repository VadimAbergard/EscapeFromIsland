package com.survivalonisland.game;

import com.badlogic.gdx.*;
import com.survivalonisland.game.scene.*;

public class Core extends Game {



	@Override
	public void create () {
		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		setScreen(new Title(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
