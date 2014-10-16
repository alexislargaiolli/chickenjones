package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.items.Item;
import fr.alex.games.items.ItemManager;
import fr.alex.games.items.ModifType;
import fr.alex.games.items.PassiveSkill;
import fr.alex.games.screens.GameScreen.State;

public class Chicken {
	protected Vector2 jumpVector;
	private Body chicken;
	private boolean dead;
	private boolean jumping;
	private float speed = 2;
	private float timeFactor = 1;
	private Bow bow;

	private float width, height = .75f, scale;

	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private BoneData root;

	public Chicken(Body chicken) {
		this.chicken = chicken;
		this.chicken.setBullet(true);
		this.chicken.setUserData(this);

		jumpVector = new Vector2(0, 40);

		TextureAtlas atlas = GM.assetManager.get("chicken/skeleton.atlas", TextureAtlas.class);
		SkeletonJson skeletonJson = new SkeletonJson(atlas);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("chicken/skeleton.json"));

		scale = height / skeletonData.getHeight();
		width = skeletonData.getWidth() * scale;

		stateData = new AnimationStateData(skeletonData);
		root = skeletonData.findBone("root");
		root.setScale(scale, scale);

		skeleton = new Skeleton(skeletonData);
		stateData.setMix("idle", "run", 0.2f);
		stateData.setMix("run", "jump", 0.4f);
		animState = new AnimationState(stateData);

		idle();

		bow = new Bow(this);

		for (Item item : ItemManager.get().getEquiped()) {
			for (PassiveSkill skill : item.getPassives()) {
				handlePassiveSkill(skill);
			}
		}
	}

	private void handlePassiveSkill(PassiveSkill skill) {
		switch (skill.getCarac()) {
		case BOW_STRENGTH:
			bow.setStrength(handleBonus(bow.getStrength(), skill.getType(), skill.getBonus()));
			break;
		case SPEED:
			break;
		case TIME_SPEED:
			break;
		default:
			break;
		}
	}

	private float handleBonus(float val, ModifType type, float bonus) {
		float res = val;
		switch (type) {
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

	/**
	 * Set the chicken animation to run
	 */
	public void run() {
		animState.setAnimation(0, "run", true);
	}

	/**
	 * Set the chicken animation to fall
	 */
	public void fall() {
		animState.setAnimation(0, "jump", true);
	}

	/**
	 * Set the chicken animation to idle
	 */
	public void idle() {
		animState.setAnimation(0, "idle", true);
	}

	public void update(State state, float delta) {
		skeleton.setPosition(getX(), getY() - height * .5f);
		animState.update(delta * timeFactor);
		animState.apply(skeleton);
		skeleton.updateWorldTransform();
		bow.setOrigin(chicken.getWorldCenter().x - width * .05f, chicken.getWorldCenter().y + height * .2f);
		bow.update(state, delta * timeFactor);

		if (state == State.PLAYING && !dead && chicken.getLinearVelocity().x < speed) {
			chicken.setLinearVelocity(speed, chicken.getLinearVelocity().y);
		}
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer) {
		skeletonRenderer.draw(batch, skeleton);
		bow.draw(batch, skeletonRenderer);
	}

	public float getSpeedX() {
		return chicken.getLinearVelocity().x;
	}

	public float getSpeedY() {
		return chicken.getLinearVelocity().y;
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
		if (dead) {
			stopRunning();
		}
		this.dead = dead;
	}

	public void stopRunning() {
		chicken.setLinearDamping(10);
	}

	public boolean isJumping() {
		return jumping;
	}

	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}

	public Body getChicken() {
		return chicken;
	}

	public void setChicken(Body chicken) {
		this.chicken = chicken;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(float timeFactor) {
		this.timeFactor = timeFactor;
	}

}
