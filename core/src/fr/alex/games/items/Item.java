package fr.alex.games.items;

import java.util.ArrayList;

import fr.alex.games.saves.PlayerManager;

public class Item {	
	private int id;
	private String name;
	private String desc;
	private int gold;
	private String icon;
	private ArrayList<PassiveSkill> passives = new ArrayList<PassiveSkill>();

	public boolean isBuyable(){
		return gold <= PlayerManager.get().getGold();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public ArrayList<PassiveSkill> getPassives() {
		return passives;
	}

	public void setPassives(ArrayList<PassiveSkill> passives) {
		this.passives = passives;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
