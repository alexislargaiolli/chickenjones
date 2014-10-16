package fr.alex.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Simple class for spatial (image) rendering. If a body reference is included,
 * its position will serve as the local coordinate system.
 * 
 * @author tescott
 * 
 */
public class SimpleSpatial {
	protected Sprite mSprite;
	protected int imageCount = 0;
	protected int curImage = 0;
	protected TextureRegion[] images;
	protected Body mBody;
	protected final Vector2 mCenter = new Vector2();
	protected final Vector2 mHalfSize = new Vector2();
	protected float mRotation;
	private static final Vector2 mTmp = new Vector2();
	private UserData userData;

	public SimpleSpatial(Texture texture, boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {
		mSprite = new Sprite(texture);
		defineSpatial(flip, body, color, size, center, rotationInDegrees);
	}

	public SimpleSpatial(TextureRegion region, boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {
		mSprite = new Sprite(region);
		defineSpatial(flip, body, color, size, center, rotationInDegrees);
	}

	public void nextImage() {
		if (curImage < imageCount-1) {			
			mSprite.setRegion(images[curImage]);
			curImage++;
		}
	}

	public void defineSpatial(boolean flip, Body body, Color color, Vector2 size, Vector2 center, float rotationInDegrees) {
		mBody = body;
		mSprite.flip(flip, false);
		mSprite.setColor(color);
		mRotation = rotationInDegrees;
		mSprite.setSize(size.x, size.y);
		mSprite.setOrigin(size.x / 2, size.y / 2);
		mHalfSize.set(size.x / 2, size.y / 2);
		mCenter.set(center);

		if (body != null) {
			mTmp.set(body.getPosition());
			mSprite.setPosition(mTmp.x - size.x / 2, mTmp.y - size.y / 2);

			float angle = mBody.getAngle() * MathUtils.radiansToDegrees;
			mTmp.set(mCenter).rotate(angle).add(mBody.getPosition()).sub(mHalfSize);
			mSprite.setRotation(mRotation + angle);
		} else {
			mTmp.set(center.x - size.x / 2, center.y - size.y / 2);
			mSprite.setRotation(rotationInDegrees);
		}

		mSprite.setPosition(mTmp.x, mTmp.y);
	}

	public void render(SpriteBatch batch, float delta) {
		// if this is a dynamic spatial...
		if (mBody != null) {
			// use body information to render it...
			float angle = mBody.getAngle() * MathUtils.radiansToDegrees;
			mTmp.set(mCenter).rotate(angle).add(mBody.getPosition()).sub(mHalfSize);
			mSprite.setPosition(mTmp.x, mTmp.y);
			mSprite.setRotation(mRotation + angle);
			mSprite.draw(batch);
		} else {
			// else just draw it wherever it was defined at initialization
			mSprite.draw(batch);
		}
	}

	public Body getmBody() {
		return mBody;
	}

	public void setmBody(Body mBody) {
		this.mBody = mBody;
	}

	public UserData getUserData() {
		return userData;
	}

	public void setUserData(UserData userData) {
		this.userData = userData;
	}

	public TextureRegion[] getImages() {
		return images;
	}

	public void setImages(TextureRegion[] images) {
		this.images = images;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}
}
