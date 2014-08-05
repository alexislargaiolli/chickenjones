package fr.alex.games.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import fr.alex.games.GM;

public class Bow {
	private Vector2 origin;
	private float strength;

	public Bow(Vector2 origin) {
		super();
		this.origin = origin;
		strength = 20;
	}

	public Arrow fire(Vector2 direction, TextureRegion region, PooledEffect effect) {
		Vector2 o = new Vector2(origin).add(new Vector2(direction).scl(1f));
		
		
		float width = .25f;
		float height = .05f;
		float x = o.x;
		float y = o.y;
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width, height);
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(x, y);
		bodyDef.allowSleep = false;
		bodyDef.gravityScale = .5f;
		Body body = GM.world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = .8f;
		fixtureDef.restitution = .3f;
		fixtureDef.friction = .2f;
		Fixture f = body.createFixture(fixtureDef);
		body.setTransform(x - width * .5f, y - height * .5f, direction.angleRad());
		Arrow arrow = new Arrow(body, direction.angleRad(), new Vector2(width*2f, height*2f), new Vector2(0, 0), region, effect);
		f.setUserData(arrow);
		body.applyForceToCenter(direction.scl(strength), true);
		return arrow;
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

}
