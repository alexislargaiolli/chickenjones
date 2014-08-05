package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fr.alex.games.GM;

public class IntroScreen extends MenuScreen {

	public IntroScreen() {
		super();
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.justTouched()) {
			ScreenManager.getInstance().show(fr.alex.games.screens.Screens.MAIN_MENU);
		}
		super.render(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void init() {
		mainTable.add("Chicken Jones", "title");
	}

	@Override
	protected void onBack() {
		
	}

}
