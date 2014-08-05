package fr.alex.games.screens;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;

public class LevelsScreen extends MenuScreen {

	ArrayList<String> levels;

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
		mainTable.add("Levels", "title").center().colspan(5).pad(10).expandX();
		mainTable.row().expand();
		levels = new ArrayList<String>();
		levels.add("scene-1.json");
		levels.add("s2.json");
		levels.add("s3.json");
		levels.add("s4.json");
		levels.add("s5.json");
		for (int i=0; i <levels.size(); ++i) {
			final String lvl = levels.get(i);
			TextButton bt = new TextButton(""+(i+1), GM.skin);
			bt.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					GM.sceneFile = lvl;
					ScreenManager.getInstance().show(Screens.LOADING);
					super.clicked(event, x, y);
				}
				
			});		
			mainTable.add(bt).size(60, 60);
			if(i >0 && i % 5 == 0){
				mainTable.row();
			}
		}
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}
}
