package fr.alex.games.screens;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.alex.games.GM;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;
import fr.alex.games.widget.CustomList;
import fr.alex.games.widget.CustomListItem;
import fr.alex.games.widget.ItemSelectedEvent;

public class ShopScreen extends MenuScreen {
	private Label playerGold;
	private Table selectedItemTable;
	private Image iconSelected;
	private Label selectedName;
	private Label selectedDesc;
	private Label selectedGold;
	private TextButton btBuy;
	private Item selected;

	public ShopScreen() {
		super();
	}

	@Override
	public void show() {
		playerGold.setText("Gold: " + PlayerManager.get().getGold());
		super.show();
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	protected void init() {
		
		
		mainTable.add("Item Shop", "title").expandX();
		mainTable.row();
		playerGold = new Label("Gold: " + PlayerManager.get().getGold(), GM.skin);
		mainTable.add(playerGold);
		mainTable.row();
		Table t = new Table(GM.skin);
		t.debug();
		List<Item> items = ItemManager.get().getShopItem();
		for (final Item item : items) {
			
			Image icon = new Image(GM.skin.getRegion("Help"));
			t.add(icon).padTop(10);
			VerticalGroup v = new VerticalGroup();				
			v.addActor(new Label(item.getName(), GM.skin, "title"));
			v.addActor(new Label(item.getDesc(), GM.skin));
			t.add(v).padTop(10);
			t.row();
			t.add("Prix: " + item.getGold());
			final TextButton bt = new TextButton("ACHETER", GM.skin);
			t.add(bt).right();
			bt.addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (ItemManager.get().buyItem(item)) {
						selectedItemTable.setVisible(false);
						playerGold.setText("Gold: " + PlayerManager.get().getGold());
						bt.setVisible(false);
					}
					super.clicked(event, x, y);
				}
				
			});
			t.row();
		}
		
		ScrollPane container = new ScrollPane(t, GM.skin);
		
		mainTable.add(container).fill(true).pad(10);
		mainTable.debug();
		
		/*final CustomList l = new CustomList(GM.skin);
		l.setItems(ItemManager.get().getShopItem());
		l.setSelectionEvent(new ItemSelectedEvent() {

			@Override
			public void onSelected(CustomListItem item) {
				selected = item.getItem();
				iconSelected = new Image(GM.skin.getRegion("Help"));
				selectedName.setText(item.getItem().getName());
				selectedDesc.setText(item.getItem().getDesc());
				selectedGold.setText(LocalManager.get().getHudBundle().format("shop.selected.gold", item.getItem().getGold()));
				btBuy.setDisabled(!selected.isBuyable());
				selectedItemTable.setVisible(true);
			}
		});
				
		selectedItemTable = new Table(GM.skin);
		
		selectedItemTable.row();
		iconSelected = new Image(GM.skin.getRegion("Help"));
		selectedItemTable.add(iconSelected).size(64, 64);
		
		selectedName = new Label("", GM.skin);
		selectedItemTable.add(selectedName).expandX();
		selectedItemTable.row();
		
		selectedDesc = new Label("", GM.skin);
		selectedDesc.setWrap(true);	
		
		
		selectedItemTable.add(selectedDesc).colspan(2).width(380).height(150);
		

		selectedItemTable.row();
		selectedGold = new Label("dsf", GM.skin);
		selectedItemTable.add(selectedGold);

		btBuy = new TextButton(LocalManager.get().getHUDString("shop.selected.buy"), GM.skin);
		btBuy.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (ItemManager.get().buyItem(selected)) {
					selectedItemTable.setVisible(false);
					l.setItems(ItemManager.get().getShopItem());
					playerGold.setText("Gold: " + PlayerManager.get().getGold());
				}
				super.clicked(event, x, y);
			}

		});
		btBuy.pad(0, 10, 10, 10);
		selectedItemTable.add(btBuy);

		selectedItemTable.setVisible(false);
		SplitPane pan = new SplitPane(l.getPan(), selectedItemTable, false, GM.skin);
		mainTable.add(pan);*/
		//selectedItemTable.debug();
	}

	@Override
	protected void onBack() {
		ScreenManager.getInstance().show(Screens.MAIN_MENU);
	}

}
