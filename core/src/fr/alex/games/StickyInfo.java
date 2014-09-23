package fr.alex.games;

import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.UserData;

public class StickyInfo {
	private Arrow arrow;
	private UserData userData;

	public StickyInfo(Arrow arrow, UserData userData) {
		super();
		this.arrow = arrow;
		this.userData = userData;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public void setArrow(Arrow arrow) {
		this.arrow = arrow;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

}
