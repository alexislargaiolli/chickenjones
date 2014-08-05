package fr.alex.games.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;

public class MainMenuScreen extends MenuScreen{
	
	public MainMenuScreen(){
		super();
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
		mainTable.add("Chicken Jones", "title").expandX();
		mainTable.row().expand();
		TextButton btPlay = new TextButton("Jouer", GM.skin);
		btPlay.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.LEVEL_MENU);
				
				super.clicked(event, x, y);
			}
			
		});
		btPlay.pad(0, 10, 10, 10);
		mainTable.add(btPlay);
		mainTable.row().expand();
		
		TextButton bt = new TextButton("Shop", GM.skin);
		bt.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.SHOP);
				super.clicked(event, x, y);
			}
			
		});
		bt.pad(0, 10, 10, 10);
		mainTable.add(bt);
		mainTable.row();
	}

	@Override
	protected void onBack() {
		// TODO Auto-generated method stub
		
	}

}
