package fr.alex.games;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import fr.alex.games.screens.GameScreen;

public class HUD {
	protected static final int VIRTUAL_WIDTH = 800, VIRTUAL_HEIGHT = 400;
	protected Stage stage;
	protected Table mainTable;
	private Label lbInfo;
	private Label scoreLabel;
	private TextButton btJump;
	public static String SCORE_MESSAGE = "GOLD: ";
	public static String WIN_MESSAGE = "You win !";
	public static String LOOSE_MESSAGE = "You loose...";
	public static String CONTINUE_MESSAGE = "Click to continue";

	public HUD(final GameScreen game) {
		stage = new Stage(new ExtendViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
		mainTable = new Table(GM.skin);
		mainTable.setFillParent(true);
		
		scoreLabel = new Label(SCORE_MESSAGE + GM.gold, GM.skin);
		mainTable.add(scoreLabel).pad(10).left().expandX();
		mainTable.row().expand();
				
		lbInfo = new Label("", GM.skin);
		mainTable.add(lbInfo).expand();
		
		mainTable.row();
		btJump = new TextButton("JUMP", GM.skin);
		btJump.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.getPlayer().jump();

				super.clicked(event, x, y);
			}
		});
		//mainTable.add(btJump).left().pad(10).expandX();
		
		stage.addActor(mainTable);
	}

	public void update(float delta) {
		scoreLabel.setText(SCORE_MESSAGE + GM.gold);
		stage.act(delta);
	}

	public void draw() {
		stage.draw();
	}

	public void setMessage(String message) {
		lbInfo.setText(message);
	}

	public void showMessage() {
		lbInfo.addAction(alpha(1, 0));
		
	}

	public void hideMessage() {
		lbInfo.addAction(alpha(0, 1));
	}

	public Stage getStage() {
		return stage;
	}

}
