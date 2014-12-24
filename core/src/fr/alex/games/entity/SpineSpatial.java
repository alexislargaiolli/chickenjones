package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;

import fr.alex.games.GM;

public class SpineSpatial extends SimpleSpatial{
	private Skeleton skeleton;
	private AnimationStateData stateData;
	private AnimationState animState;
	private Bone root;	

	public SpineSpatial(String spineFile, boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {		
		GM.assetManager.load(spineFile + ".atlas", TextureAtlas.class);
		GM.assetManager.finishLoading();
		TextureAtlas atlas = GM.assetManager.get(spineFile + ".atlas", TextureAtlas.class);
		SkeletonJson skeletonJson = new SkeletonJson(atlas);
		SkeletonData skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal(spineFile+".json"));
		
		float scale = size.y / skeletonData.getHeight();
		stateData = new AnimationStateData(skeletonData);
		
		
		
		skeleton = new Skeleton(skeletonData);
		root = skeleton.findBone("root");
		root.setScale(scale, scale);
		stateData.setMix("idle", "idle", 0.2f);
		
		animState = new AnimationState(stateData);
		animState.setAnimation(0, "idle", true);
		animState.getCurrent(0).setTime(Math.round(Math.random() * 10));
		
		defineSpatial(flip, body, color, size, center, rotationInDegrees);
		idle();
	}

	public void defineSpatial(boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {
		mBody = body;
		
		skeleton.setFlip(flip, false);
		mRotation = rotationInDegrees;
		
		mHalfSize.set(size.x / 2, size.y / 2);
		mCenter.set(center);

		/*if (body != null) {
			mTmp.set(body.getPosition());
			skeleton.setPosition(body.getPosition().x, body.getPosition().y);
			//skeleton.setPosition(mTmp.x - size.x / 2, mTmp.y - size.y / 2);

			float angle = mBody.getAngle() * MathUtils.radiansToDegrees;
			//mTmp.set(mCenter).rotate(angle).add(mBody.getPosition()).sub(mHalfSize);
			//root.setRotation(mRotation + angle);
		} else {
			//mTmp.set(center.x - size.x / 2, center.y - size.y / 2);
			root.setRotation(rotationInDegrees);
		}*/
	}

	public void flipX(){
		skeleton.setFlip(!skeleton.getFlipX(), skeleton.getFlipY());
	}
	
	public void render(SkeletonRenderer skeletonRenderer, SpriteBatch batch, float delta) {
		// if this is a dynamic spatial...
		
		if (mBody != null) {
			// use body information to render it...
			float angle = mBody.getAngle() * MathUtils.radiansToDegrees;
			mTmp.set(mCenter).rotate(angle).add(mBody.getPosition()).sub(mHalfSize);
			
			
			skeleton.setPosition(mBody.getPosition().x, mBody.getPosition().y);
			animState.update(delta);
			animState.apply(skeleton);
			root.setRotation(angle * (skeleton.getFlipX() ? -1 : 1));
			skeleton.updateWorldTransform();
			skeletonRenderer.draw(batch, skeleton);
		} else {
			// else just draw it wherever it was defined at initialization
			skeletonRenderer.draw(batch, skeleton);
		}
		if(properties != null){
			for(Property p : properties){
				p.update(delta);
				p.draw(batch);
			}
		}
		if(userData != null){
			userData.update(delta);			
		}
	}
	
	/**
	 * Set the chicken animation to idle
	 */
	public void idle() {
		animState.setAnimation(0, "idle", true);
	}
}
