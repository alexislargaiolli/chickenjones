package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.BoneData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;
import fr.alex.games.screens.GameScreen.State;

public class Bow {
	private Vector2 origin;
	private float strength;
	private Vector2 velocity;

	private Skeleton skeleton;
	private Animation animShot;
	private BoneData root;
	private float animTime = 0, animDuration = .5f;
	private float angle;

	public Bow(Chicken player) {
		super();
		origin = new Vector2();
		velocity = new Vector2();
		strength = 10f;

		TextureAtlas atlas = GM.assetManager.get("chicken/bow.atlas", TextureAtlas.class);
		SkeletonJson skeletonJson = new SkeletonJson(atlas);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("chicken/bow.json"));
		root = skeletonData.findBone("root");
		root.setScale(player.getScale(), player.getScale());
		root.setRotation(30);
		
		animShot = skeletonData.findAnimation("shot");

		skeleton = new Skeleton(skeletonData);
	}

	public void shot() {

	}

	public void update(State state, float delta) {
		animTime += delta;
		if (animTime <= animDuration) {
			animShot.apply(skeleton, animTime - delta, animTime, false, null);
		}
		skeleton.getRootBone().setRotation(angle);
		skeleton.setPosition(origin.x, origin.y);
		skeleton.updateWorldTransform();
	}

	public void draw(SpriteBatch batch, SkeletonRenderer skeletonRenderer) {
		skeletonRenderer.draw(batch, skeleton);
	}

	public Vector2 computeVelocity() {
		return velocity.set(strength * (float) Math.cos(getAngleRad()), strength * (float) Math.sin(getAngleRad()));
	}

	public Arrow fire(TextureRegion region, PooledEffect effect) {
		float width = .3f;
		float height = .04f;
		Body body = Arrow.createBody(origin.x, origin.y, width, height);
		body.setTransform(origin.x, origin.y, getAngleRad());

		body.setLinearVelocity(computeVelocity().cpy());
		body.setAngularDamping(2f);

		Arrow arrow = new Arrow(body, getAngleRad(), new Vector2(width, height), new Vector2(), region, effect);
		body.setUserData(arrow);

		animTime = 0;
		return arrow;
	}
	
	public float getAngleRad(){
		return MathUtils.degreesToRadians * angle;
	}

	public void setOrigin(float x, float y) {
		this.origin.x = x;
		this.origin.y = y;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	public Vector2 getOrigin() {
		return origin;
	}

	public void setOrigin(Vector2 origin) {
		this.origin = origin;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
