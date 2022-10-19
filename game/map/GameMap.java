package com.survivalonisland.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameMap {

    private final OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;

    private final String fileMap;

    public GameMap(OrthographicCamera cameraShow, String sourseFileMap) {
        camera = cameraShow;
        fileMap = sourseFileMap;
    }
    public void renderer() {
        renderer.render();
        renderer.setView(camera);
    }

    public void show() {
        TmxMapLoader mapLoader =  new TmxMapLoader();
        map = mapLoader.load(fileMap);

        renderer = new OrthogonalTiledMapRenderer(map, 4);
    }
}
