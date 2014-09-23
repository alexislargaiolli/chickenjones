package fr.alex.games.saves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PlayerManager {
	private static PlayerManager instance;

	public static PlayerManager get() {
		if (instance == null)
			instance = new PlayerManager();
		return instance;
	}

	private static final String PLAYER_INFO_KEY = "playerInfo";
	private static final String GOLD_KEY = "gold";
	private static final String LEVEL_KEY = "level";
	private static final String ITEM_KEY = "ITEM";

	Preferences playerInfo;

	private PlayerManager() {
		playerInfo = Gdx.app.getPreferences(PLAYER_INFO_KEY);
		Gdx.app.log("PlayerManager", "Pref read");
	}

	private void persist() {
		playerInfo.flush();
	}

	public int getGold() {
		return playerInfo.getInteger(GOLD_KEY, 100);
	}

	public void addGold(int val) {
		playerInfo.putInteger(GOLD_KEY, getGold() + val);
		persist();
	}

	public void subGold(int val) {
		playerInfo.putInteger(GOLD_KEY, getGold() - val);
		persist();
	}

	public void setLevelFinish(int lvl) {
		playerInfo.putBoolean(LEVEL_KEY + lvl, true);
		persist();
	}

	public boolean hasFinishedLevel(int lvl) {
		return playerInfo.getBoolean(LEVEL_KEY + lvl, false);
	}

	public boolean hasItem(int itemId) {
		return playerInfo.getBoolean(ITEM_KEY + itemId, false);
	}

	public void addItem(int itemId) {
		playerInfo.putBoolean(ITEM_KEY + itemId, true);
		persist();
	}
}
