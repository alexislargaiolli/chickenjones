package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import fr.alex.games.GM;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.items.ModifType;
import fr.alex.games.items.PassiveSkill;

public class Player {
	protected Vector2 jumpVector;
	private Body chicken;
	private boolean dead;
	private boolean jumping;
	private float speed = 2;
	private Bow bow;

	public Player(Body chicken) {
		this.chicken = chicken;
		this.chicken.setBullet(true);
		this.chicken.setUserData(this);
		this.chicken.getFixtureList().get(0).setUserData(this);
		bow = new Bow(new Vector2(chicken.getWorldCenter().x, chicken.getWorldCenter().y));
		jumpVector = new Vector2(0, 40);
		
		for(Item item : ItemManager.get().getPlayerItem()){
			for(PassiveSkill skill : item.getPassives()){
				handlePassiveSkill(skill);
			}
		}
	}
	
	private void handlePassiveSkill(PassiveSkill skill){
		switch(skill.getCarac()){
		case BOW_STRENGTH:
			bow.setStrength(handleBonus(bow.getStrength(), skill.getType(), skill.getBonus()));				
			break;
		case JUMP_STRENGTH:
			jumpVector.y = handleBonus(jumpVector.y, skill.getType(), skill.getBonus());
			break;					
		}
	}
	
	private float handleBonus(float val, ModifType type, float bonus){
		float res = val;
		switch(type){
		case ADD:
			res = val + bonus;
			break;
		case DIV:
			res = val / bonus;
			break;
		case MUL:
			res = val * bonus;
			break;
		case SUB:
			res = val - bonus;
			break;
		}
		return res;
	}

	public void render(SpriteBatch batch, float delta) {
		bow.setOrigin(chicken.getWorldCenter().x, chicken.getWorldCenter().y);
		if (!dead && chicken.getLinearVelocity().x < speed) {
			chicken.setLinearVelocity(speed, chicken.getLinearVelocity().y);
		}
		
		/*if(chicken.getLinearVelocity().y == 0){
			jumping = false;
		}*/
	}

	public void clearForce() {
		chicken.setLinearVelocity(0, 0);
	}

	public void jump() {
		if (!jumping) {
			System.out.println("jump");
			jumping = true;
			chicken.applyForceToCenter(new Vector2(jumpVector.x, jumpVector.y), true);
		}
	}

	public Bow getBow() {
		return bow;
	}

	public void setBow(Bow bow) {
		this.bow = bow;
	}

	public float getX() {
		return chicken.getPosition().x;
	}

	public float getY() {
		return chicken.getPosition().y;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		if(dead){
			chicken.setLinearDamping(10);
		}
		this.dead = dead;
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
		System.out.println(jumping);
	}

	public Body getChicken() {
		return chicken;
	}

	public void setChicken(Body chicken) {
		this.chicken = chicken;
	}

}
