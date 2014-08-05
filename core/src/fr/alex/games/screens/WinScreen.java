package fr.alex.games.screens;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;

public class WinScreen extends MenuScreen {	
	private Label earnedGoldLabel, totalGoldLabel;
	private TextButton btReplay, btNext;
	private float animTime1, animTime2, startGold, endGold;
	private static final float ANIM_DURATION = 2;

	@Override
	public void show() {
		
		startGold = PlayerManager.get().getGold();
		totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", startGold));
		endGold = startGold + GM.gold;
		PlayerManager.get().addGold(GM.gold);	
		earnedGoldLabel.setText("0");
		animTime1 = 0;
		animTime2 = 0;
		
		btReplay.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(4, Actions.alpha(1f, .5f))));
		btNext.addAction(Actions.sequence(Actions.alpha(0, 0), Actions.delay(4.5f, Actions.alpha(1f, .5f))));
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
		mainTable.add(LocalManager.get().getHUDString("win.title"), "title").expandX().colspan(2).row();
		earnedGoldLabel= new Label("", GM.skin);
		mainTable.add(earnedGoldLabel).colspan(2);
		mainTable.row();
		
		totalGoldLabel = new Label("", GM.skin);
		mainTable.add(totalGoldLabel).colspan(2);
		mainTable.row().expand();
		
		btReplay = new TextButton(LocalManager.get().getHUDString("win.retry"), GM.skin);
		btReplay.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.getInstance().show(Screens.LOADING);
				super.clicked(event, x, y);
			}

		});
		
		btReplay.pad(0, 10, 10, 10);
		mainTable.add(btReplay).width(180);
		btNext = new TextButton(LocalManager.get().getHUDString("win.next"), GM.skin);
		btNext.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {				
				ScreenManager.getInstance().show(fr.alex.games.screens.Screens.LEVEL_MENU);		
				super.clicked(event, x, y);
			}

		});
		btNext.pad(0, 10, 10, 10);
		mainTable.add(btNext).width(180);
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

	@Override
	public void render(float delta) {
		if(animTime1 <= ANIM_DURATION){
			int score = (int) Math.floor(Interpolation.pow2.apply(0, GM.gold, animTime1 / ANIM_DURATION));
			earnedGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.earned", score));
			animTime1 += delta;
			if(animTime1 >= ANIM_DURATION){
				earnedGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.earned", GM.gold));
			}
		}
		if(animTime1 >= ANIM_DURATION && animTime2 <= ANIM_DURATION){
			int score = (int) Math.floor(Interpolation.pow2.apply(startGold, endGold, animTime2 / ANIM_DURATION));
			totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", score));
			animTime2 += delta;
			if(animTime2 >= ANIM_DURATION){
				totalGoldLabel.setText(LocalManager.get().getHudBundle().format("win.gold.total", endGold));
			}
		}
		super.render(delta);
	}

}