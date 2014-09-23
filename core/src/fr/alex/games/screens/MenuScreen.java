package fr.alex.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fr.alex.games.GM;

public abstract class MenuScreen implements Screen{
	
	protected static final int VIRTUAL_WIDTH = 800, VIRTUAL_HEIGHT = 400;
	protected Stage stage;
	protected Table mainTable;
	
	public MenuScreen(){
		stage = new Stage(new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
		
		mainTable = new Table(GM.skin);
		Image bg = new Image(GM.skin.getDrawable("background"));
		bg.setFillParent(true);
		stage.addActor(bg);
		
		mainTable.setFillParent(true);
		mainTable.debug();
		
		init();
		
		stage.addActor(mainTable);
	}
	
	protected abstract void init();

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
		//Table.drawDebug(stage);
		if(Gdx.input.isKeyPressed(Keys.BACK)){			
			onBack();
		}
	}
	
	protected abstract void onBack();

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}
	
	
}
