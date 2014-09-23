package fr.alex.games.entity;

import com.badlogic.gdx.physics.box2d.Body;

import fr.alex.games.GM;

public class UserData {
	int life = 1;
	int coins = 0;
	boolean destroyable;
	boolean mortal;
	boolean coin;
	boolean stick;
	SimpleSpatial spatial;
	
	public UserData(SimpleSpatial spatial){
		this.spatial = spatial;
	}
	
	public boolean isDead(){
		return life == 0;
	}
	
	public void hit(Body body){
		if(coins > 0){
			GM.gold += coins;
			EffectManager.get().goldEffect(body.getPosition().x, body.getPosition().y);
		}
		if(destroyable){
			life--;
			spatial.nextImage();
			if(isDead()){
				EffectManager.get().dustEffect(body.getPosition().x, body.getPosition().y);
			}
		}		
	}
	
	public void playerContact(Body body, Chicken p){
		if(mortal){
			p.setDead(true);
		}
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public boolean isDestroyable() {
		return destroyable;
	}

	public void setDestroyable(boolean destroyable) {
		this.destroyable = destroyable;
	}

	public boolean isMortal() {
		return mortal;
	}

	public void setMortal(boolean mortal) {
		this.mortal = mortal;
	}

	public boolean isCoin() {
		return coin;
	}

	public void setCoin(boolean coin) {
		this.coin = coin;
	}

	public SimpleSpatial getSpatial() {
		return spatial;
	}

	public void setSpatial(SimpleSpatial spatial) {
		this.spatial = spatial;
	}

	public boolean isStick() {
		return stick;
	}

	public void setStick(boolean stick) {
		this.stick = stick;
	}
}
