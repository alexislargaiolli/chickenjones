package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

public interface UserData {
	public boolean isDead();
	
	public void hit(Body body);
	
	public void playerContact(Body body, Player p);
}
