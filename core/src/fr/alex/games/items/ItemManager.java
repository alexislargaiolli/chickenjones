package fr.alex.games.items;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;

public class ItemManager {

	private static ItemManager instance;

	public static ItemManager get() {
		if (instance == null)
			instance = new ItemManager();
		return instance;
	}

	private List<Item> items = new ArrayList<Item>();
	private List<Item> playerItem = new ArrayList<Item>();

	@SuppressWarnings("unchecked")
	private ItemManager() {
		Json json = new Json();
		Gdx.app.log("ItemManager", "Read file");
		items = json.fromJson(ArrayList.class, Gdx.files.internal("items/items3.json"));
		Gdx.app.log("ItemManager", "File read");
		Gdx.app.log("ItemManager", "Localized items");
		for (Item i : items) {
			i.setName(LocalManager.get().getObjectString(i.getName()));
			i.setDesc(LocalManager.get().getObjectString(i.getDesc()));			
		}
		
		refreshList();
		
	}
	
	private void refreshList(){
		Gdx.app.log("ItemManager", "Refreshing list");
		playerItem.clear();
		for (Item i : items) {
			if (PlayerManager.get().hasItem(i.getId())) {
				playerItem.add(i);
			}
		}
		Gdx.app.log("ItemManager", "List refreshed");
	}
	
	public boolean buyItem(Item item){
		if(PlayerManager.get().getGold() >= item.getGold()){
			playerItem.add(item);
			PlayerManager.get().subGold(item.getGold());
			PlayerManager.get().addItem(item.getId());
			refreshList();
			return true;
		}
		return false;
	}
	
	public List<Item> getShopItem(){
		Gdx.app.log("ItemManager", "Build shop item");
		List<Item> result = new ArrayList<Item>(items);
		result.removeAll(playerItem);
		Gdx.app.log("ItemManager", "Shop item built");
		return result;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public List<Item> getPlayerItem() {
		return playerItem;
	}

	public void setPlayerItem(List<Item> playerItem) {
		this.playerItem = playerItem;
	}

}
