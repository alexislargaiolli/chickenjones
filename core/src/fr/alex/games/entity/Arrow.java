package fr.alex.games.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Arrow extends SimpleSpatial {
	boolean dead;
	boolean dieing;
	float timeBeforeDeath = -1;
	PooledEffect effect;

	public Arrow(Body body, float rotationInDegrees, Vector2 size, Vector2 center, TextureRegion region, PooledEffect effect) {
		super(region, false, body, Color.WHITE, size, center, rotationInDegrees);
		this.effect = effect;
		dieing = false;
		dead = false;
	}

	public void render(SpriteBatch batch, float delta) {
		if (dieing) {
			timeBeforeDeath -= delta;
			if (timeBeforeDeath < 0) {
				dead = true;
			}
		}
		if (effect != null) {
			// effect.setPosition(getWorldX(), getWorldY());
		}
		super.render(batch, delta);
	}

	public PooledEffect getEffect() {
		return effect;
	}

	public void setEffect(PooledEffect effect) {
		this.effect = effect;
	}

	public void setTimeBeforeDeath(float timeBeforeDeath) {
		if (!dieing) {
			dieing = true;
			this.timeBeforeDeath = timeBeforeDeath;
		}
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

}
