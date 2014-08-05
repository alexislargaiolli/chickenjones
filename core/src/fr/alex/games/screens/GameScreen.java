package fr.alex.games.screens;

import java.util.HashMap;
import java.util.Map;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

import fr.alex.games.GM;
import fr.alex.games.GameCollisions;
import fr.alex.games.HUD;
import fr.alex.games.Utils;
import fr.alex.games.background.ParallaxBackground;
import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.Coin;
import fr.alex.games.entity.Destroyable;
import fr.alex.games.entity.EffectManager;
import fr.alex.games.entity.Mortal;
import fr.alex.games.entity.Player;
import fr.alex.games.entity.SimpleSpatial;
import fr.alex.games.entity.UserData;
import fr.alex.games.saves.PlayerManager;

public class GameScreen implements Screen, InputProcessor {

	private enum State {
		STARTING, PLAYING, ENDING
	}

	private State state;

	private static final Vector2 mTmp = new Vector2();
	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;
	private SpriteBatch batch;
	private Player player;
	private Vector2 lastPlayerPosition;
	private Array<Arrow> arrows;
	private Array<SimpleSpatial> mSpatials;
	private Map<String, Texture> mTextureMap;
	private Map<String, TextureRegion> mRegionMap;
	private ParallaxBackground background;
	private HUD hud;
	private float counter;
	private boolean loose;
	private float endX;
	private RayHandler rayHandler;
	

	public GameScreen() {

	}

	public void init() {
		GM.world = GM.scene.getWorld();
		
		arrows = new Array<Arrow>();
		mTextureMap = new HashMap<String, Texture>();
		mRegionMap = new HashMap<String, TextureRegion>();
		mSpatials = new Array<SimpleSpatial>();
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		GM.ratio = 400f / (float) Gdx.graphics.getHeight();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() * GM.ratio * .01f, 4f);
		camera.position.set(1f, 1.9f, 0f);
		hud = new HUD(this);
		state = State.STARTING;
		counter = 0;
		loose = false;

		Array<Body> tmp = GM.scene.getNamed(Body.class, "chicken");
		player = new Player(tmp.get(0));
		lastPlayerPosition = new Vector2(player.getChicken().getPosition());
		mRegionMap.put("arrow", GM.atlas.findRegion("arrow"));
		createSpatialsFromRubeImages(GM.scene);
		
		Json json = new Json();
		background = json.fromJson(ParallaxBackground.class, Gdx.files.internal("backgrounds/egypt.json"));
		
		tmp = GM.scene.getNamed(Body.class, "end");
		endX = tmp.get(0).getPosition().x;
		GM.gold = 0;
		
		rayHandler = new RayHandler(GM.world);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.setAmbientLight(.1f, .1f, 1f, .4f);
		
		new PointLight(rayHandler, 1000, new Color(1, .8f, .2f, .8f), 200, endX * .5f, 20);
		
		PlayerManager.get();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	private void update(float delta) {
		if (state == State.STARTING) {
			if (counter > 0) {
				counter -= delta;
				hud.setMessage(Math.floor(counter) + "");
			} else if (counter <= 0) {
				state = State.PLAYING;
				hud.hideMessage();
			}
		} else if (state == State.PLAYING) {
			checkGameState();
		} else if (state == State.ENDING) {
			counter += delta;
			if (counter > 2) {
				hud.setMessage(HUD.CONTINUE_MESSAGE);
				if (Gdx.input.isTouched()) {
					GM.scene.clear();
					if (loose) {
						ScreenManager.getInstance().show(Screens.LOOSE);
					} else {
						ScreenManager.getInstance().show(Screens.WIN);
					}

				}
			}
		}
		camera.position.x = player.getX() + 2;
		camera.position.y = player.getY() + .8f;
		background.setSpeed((player.getX() - lastPlayerPosition.x) * 2, (player.getY() - lastPlayerPosition.y) * 2);
		camera.update();
		lastPlayerPosition.set(player.getChicken().getPosition());
		GM.scene.step();
		
		hud.update(delta);
		//rayHandler.setCombinedMatrix(camera.combined);
	}

	private void draw(float delta) {
		background.render(delta);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < mSpatials.size; i++) {
			SimpleSpatial s = mSpatials.get(i);
			s.render(batch, 0);
			if (s.getmBody() != null && s.getmBody().getFixtureList().size > 0) {
				Object userData = s.getmBody().getFixtureList().get(0).getUserData();
				if (userData instanceof UserData) {
					UserData ud = (UserData) userData;
					if (ud.isDead()) {
						mSpatials.removeIndex(i);
						GM.scene.getWorld().destroyBody(s.getmBody());
					}
				}
			}
		}
		for (int i = 0; i < arrows.size; i++) {
			Arrow a = arrows.get(i);
			a.render(batch, delta);
			if (a.isDead()) {
				GM.world.destroyBody(a.getmBody());
				arrows.removeIndex(i);
			}
		}
		
		
		
		player.render(batch, delta);

		batch.end();
		
		EffectManager.get().draw(camera.combined.cpy().scale(Utils.WORLD_TO_BOX, Utils.WORLD_TO_BOX, 0), delta);
		//renderer.render(GM.scene.getWorld(), camera.combined);
		//rayHandler.updateAndRender();
		hud.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	private void checkGameState() {

		if (player.isDead()) {
			state = State.ENDING;
			loose = true;
			GM.world.clearForces();
			hud.setMessage(HUD.LOOSE_MESSAGE);
			hud.showMessage();
		}
		if (player.getX() > endX) {
			state = State.ENDING;
			player.setDead(true);
			GM.world.clearForces();
			hud.setMessage(HUD.WIN_MESSAGE);
			hud.showMessage();
		}

	}

	private void createSpatialsFromRubeImages(RubeScene scene) {
		Array<RubeImage> images = scene.getImages();
		mSpatials = new Array<SimpleSpatial>();
		
		String altasName = (String) scene.getCustom(GM.world, "atlas");
		TextureAtlas atlas = GM.assetManager.get("scenes/"+altasName, TextureAtlas.class);
		if ((images != null) && (images.size > 0)) {

			for (int i = 0; i < images.size; i++) {
				RubeImage image = images.get(i);
				mTmp.set(image.width, image.height);
				String textureFileName = image.file.replace(".png", "");
				SimpleSpatial spatial = new SimpleSpatial(atlas.findRegion(textureFileName), image.flip, image.body, image.color, mTmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);
				mSpatials.add(spatial);
			}
		}
		Array<Fixture> destroyables = scene.getNamed(Fixture.class, "destroyable");
		if (destroyables != null) {
			for (Fixture f : destroyables) {
				f.setUserData(new Destroyable(1));
			}
		}

		Array<Fixture> coins = scene.getNamed(Fixture.class, "coin");
		if (coins != null) {
			for (Fixture f : coins) {
				f.setUserData(new Coin(1));
			}
		}

		Array<Fixture> mortals = scene.getNamed(Fixture.class, "mortal");
		if (mortals != null) {
			for (Fixture f : mortals) {
				f.setUserData(new Mortal());
			}
		}
	}

	@Override
	public void show() {
		GM.scene.getWorld().setContactListener(new GameCollisions());
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(hud.getStage());
		multi.addProcessor(this);

		Gdx.input.setInputProcessor(multi);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		//GM.atlas.dispose();
		//GM.world.dispose();
		
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Q) {
			player.jump();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX < 180 && screenY > 220) {
			//player.jump();
			return true;
		} else {
			Vector3 p = camera.unproject(new Vector3(screenX, screenY, 0));
			Vector2 direction = new Vector2(p.x, p.y).sub(player.getBow().getOrigin().x, player.getBow().getOrigin().y).nor();

			/*
			 * PooledEffect effect = GM.effectPool.obtain();
			 * effects.add(effect);
			 */

			Arrow a = player.getBow().fire(direction, mRegionMap.get("arrow"), null);
			arrows.add(a);
			return true;
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
