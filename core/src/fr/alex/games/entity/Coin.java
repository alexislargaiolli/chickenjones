package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

import fr.alex.games.GM;

public class Coin implements UserData{
	private int value;
	private boolean dead;

	public Coin(int val) {
		this.value = val;
		this.dead = false;
	}

	public int getValue() {
		return value;
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void hit(Body body) {
		GM.gold += value;
		this.dead = true;
		EffectManager.get().goldEffect(body.getPosition().x, body.getPosition().y);
	}

	@Override
	public void playerContact(Body body, Player p) {
		hit(body);
	}
}
