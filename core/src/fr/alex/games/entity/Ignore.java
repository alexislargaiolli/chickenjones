package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

public class Ignore implements UserData{

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void hit(Body body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerContact(Body body, Player p) {
		// TODO Auto-generated method stub
		
	}

}
