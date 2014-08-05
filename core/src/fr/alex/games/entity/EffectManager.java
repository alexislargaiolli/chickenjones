package fr.alex.games.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

import fr.alex.games.Utils;

public class EffectManager {

	private static EffectManager instance;	

	public static EffectManager get() {
		if (instance == null)
			instance = new EffectManager();
		return instance;
	}

	private SpriteBatch batch;
	private Array<PooledEffect> effects = new Array<PooledEffect>();
	private ParticleEffectPool goldEffect;
	private ParticleEffectPool fireEffect;

	private EffectManager() {
		ParticleEffect fEffect = new ParticleEffect();
		fEffect.load(Gdx.files.internal("fire.p"), Gdx.files.internal(""));
		fireEffect = new ParticleEffectPool(fEffect, 1, 2);
		
		ParticleEffect bombEffect = new ParticleEffect();
		bombEffect.load(Gdx.files.internal("gold.p"), Gdx.files.internal(""));		
		goldEffect = new ParticleEffectPool(bombEffect, 1, 2);
		
		batch = new SpriteBatch();
	}

	public void draw(Matrix4 m, float delta) {
		batch.setProjectionMatrix(m);
		batch.begin();
		for (int i = effects.size - 1; i >= 0; i--) {
			PooledEffect effect = effects.get(i);
			effect.draw(batch, delta);
			if (effect.isComplete()) {
				effect.free();
				effects.removeIndex(i);
			}
		}
		batch.end();
	}

	public PooledEffect fireEffect(float x, float y) {
		PooledEffect effect = fireEffect.obtain();
		
		effect.setPosition(Utils.toWorld(x), Utils.toWorld(y));
		effects.add(effect);
		return effect;
	}

	public PooledEffect goldEffect(float x, float y){
		PooledEffect effect = goldEffect.obtain();
		effect.setPosition(Utils.toWorld(x), Utils.toWorld(y));
		effects.add(effect);
		return effect;
	}
	
}
