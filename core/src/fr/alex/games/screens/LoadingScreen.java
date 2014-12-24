package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gushikustudios.rube.loader.RubeSceneLoader;

import fr.alex.games.GM;
import fr.alex.games.Main;

public class LoadingScreen extends MenuScreen {

	private Label message;
	private boolean finishLoading;

	@Override
	public void render(float delta) {
		finishLoading = GM.assetManager.update();
		if (finishLoading) {
			message.setText("Cliquez pour commencer");
		} else {
			message.setText("Chargement: " + MathUtils.round(GM.assetManager.getProgress()) + "%");
		}
		if (Gdx.input.justTouched() && finishLoading) {
			GM.commonAtlas = GM.assetManager.get(Main.COMMON_ATLAS_PATH, TextureAtlas.class);
			GM.bgAtlas = GM.assetManager.get(Main.BACKGROUND_ATLAS_PATH + GM.level.getBackgroundFile(), TextureAtlas.class);

			ScreenManager.getInstance().show(Screens.GAME);
		}
		super.render(delta);
	}

	@Override
	public void show() {
		finishLoading = false;
		RubeSceneLoader loader = new RubeSceneLoader();
		GM.scene = loader.loadScene(Gdx.files.internal(Main.SCENES_PATH + GM.level.getSceneFile()));
		GM.assetManager.load(Main.SCENES_ATLAS_PATH + GM.level.getSceneAtlasFile(), TextureAtlas.class);
		GM.assetManager.load(Main.COMMON_ATLAS_PATH, TextureAtlas.class);
		GM.assetManager.load(Main.BACKGROUND_ATLAS_PATH + GM.level.getBackgroundFile(), TextureAtlas.class);
		GM.assetManager.load("chicken/chicken.atlas", TextureAtlas.class);
		GM.assetManager.load("chicken/bow.atlas", TextureAtlas.class);
		super.show();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		message = new Label("", GM.skin);
		mainTable.add(message);
	}

	@Override
	protected void onBack() {

	}

}
