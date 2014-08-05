package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneLoader;

import fr.alex.games.GM;

public class LoadingScreen extends MenuScreen{
	
	private Label message;
	private boolean finishLoading;

	@Override
	public void render(float delta) {
		finishLoading = GM.assetManager.update();		
		if(finishLoading){
			message.setText("Cliquez pour commencer");
		}
		else{
			message.setText("Chargement: " + GM.assetManager.getProgress() + "%" );
		}
		if (Gdx.input.justTouched() && finishLoading) {
			//GM.scene = GM.assetManager.get("scenes/" + GM.sceneFile, RubeScene.class);
			GM.atlas = GM.assetManager.get("bow.atlas", TextureAtlas.class);
			GM.bgAtlas = GM.assetManager.get("backgrounds/egypt.atlas", TextureAtlas.class);
			((GameScreen) ScreenManager.getInstance().getScreen(Screens.GAME)).init();
			ScreenManager.getInstance().show(Screens.GAME);
		}
		super.render(delta);
	}

	@Override
	public void show() {
		RubeSceneLoader loader = new RubeSceneLoader();
		GM.scene = loader.loadScene(Gdx.files.internal("scenes/" + GM.sceneFile));
		GM.assetManager.load("scenes/scene1.atlas", TextureAtlas.class);
		//GM.assetManager.load("scenes/" + GM.sceneFile, RubeScene.class);
		GM.assetManager.load("bow.atlas", TextureAtlas.class);
		GM.assetManager.load("backgrounds/egypt.atlas", TextureAtlas.class);
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
