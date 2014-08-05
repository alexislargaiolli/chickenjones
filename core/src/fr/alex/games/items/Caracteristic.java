package fr.alex.games.items;

import fr.alex.games.localization.LocalManager;

public enum Caracteristic {
	BOW_STRENGTH("bow.strength.title", "bow.strength.desc"), JUMP_STRENGTH("jump.strength.title", "jump.strength.desc");

	private String name;
	private String description;

	private Caracteristic(String name, String description) {
		this.name = LocalManager.get().getObjectString(name);
		this.description = LocalManager.get().getObjectString(description);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
