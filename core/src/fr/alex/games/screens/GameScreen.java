package fr.alex.games.screens;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

import fr.alex.games.GM;
import fr.alex.games.GameCollisions;
import fr.alex.games.HUD;
import fr.alex.games.Main;
import fr.alex.games.StickyInfo;
import fr.alex.games.Utils;
import fr.alex.games.background.ParallaxBackground;
import fr.alex.games.entity.Arrow;
import fr.alex.games.entity.Chicken;
import fr.alex.games.entity.EffectManager;
import fr.alex.games.entity.SimpleSpatial;
import fr.alex.games.entity.UserData;
import fr.alex.games.items.ActivatedSkill;
import fr.alex.games.items.ActiveSkill;
import fr.alex.games.items.ModifType;
import fr.alex.games.items.PassiveSkill;
import fr.alex.games.saves.PlayerManager;

public class GameScreen implements Screen, InputProcessor {

	public enum State {
		STARTING, PLAYING, ENDING, WIN, LOOSE, PAUSE
	}

	/**
	 * Current game state
	 */
	private State state;

	/**
	 * Tmp vector
	 */
	private static final Vector2 tmp = new Vector2();

	/**
	 * Camera
	 */
	private OrthographicCamera camera;

	/**
	 * Box 2D debug renderer
	 */
	private Box2DDebugRenderer renderer;

	/**
	 * Game batch
	 */
	private SpriteBatch batch;

	/**
	 * Spine skeleton renderer
	 */
	private SkeletonRenderer skeletonRenderer;

	/**
	 * 	
	 */
	private Chicken chicken;

	/**
	 * Chicken position on previous step
	 */
	private Vector2 lastChickenPosition;

	/**
	 * Activated skills still active
	 */
	private Array<ActivatedSkill> activedSkills;

	/**
	 * Reloading skills
	 */
	private Array<ActivatedSkill> reloadingSkills;

	/**
	 * Array of arrows in the world
	 */
	private Array<Arrow> arrows;

	/**
	 * Array of mSpatial (box 2d elements from RUBE Editor)
	 */
	private Array<SimpleSpatial> mSpatials;

	/**
	 * Map of util texture regions
	 */
	private Map<String, TextureRegion> mRegionMap;

	/**
	 * Parallax background
	 */
	private ParallaxBackground background;

	/**
	 * In game interface
	 */
	private HUD hud;

	/**
	 * Counter before game start
	 */
	private float counter;

	/**
	 * X coordinate in world space to reach to win
	 */
	private float endX;

	/**
	 * Current camera viewport width
	 */
	public static float viewportWidth;

	/**
	 * Current camera viewport height
	 */
	public static float viewportHeight;

	/**
	 * Delta x for camera position relative to player position
	 */
	private float cameraDeltaX = 2f;

	/**
	 * Delta y for camera position relative to player position
	 */
	private float cameraDeltaY = 1f;

	/**
	 * Drag constant for arrow rotation
	 */
	private static final float dragConstant = 2f;

	/**
	 * Last screen touch position used to compute bow direction
	 */
	private Vector3 lastTouch;

	/**
	 * Texture of bow direction simulation
	 */
	private TextureRegion trajectoryTexture;

	/**
	 * Vector of velocity used to compute arrow trajectory simulation
	 */
	private Vector2 trajectoryVelocty;

	/**
	 * Start position to compute arrow direction simulation
	 */
	private Vector2 startTrajectory;

	/**
	 * Mouse click position before drag
	 */
	private Vector2 touchBeforeDrag;

	/**
	 * True to draw box 2d debug lines
	 */
	private boolean debug;
	
	private float maxBendSize;

	public GameScreen() {
		trajectoryVelocty = new Vector2();
		startTrajectory = new Vector2();
		touchBeforeDrag = new Vector2();
		activedSkills = new Array<ActivatedSkill>();
		reloadingSkills = new Array<ActivatedSkill>();
		arrows = new Array<Arrow>();
		mRegionMap = new HashMap<String, TextureRegion>();
		renderer = new Box2DDebugRenderer();
		batch = new SpriteBatch();
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);
		lastTouch = new Vector3();
	}

	@Override
	public void show() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		GM.world = GM.scene.getWorld();
		GM.arrowFiredCount = 0;
		GM.hitCount = 0;
		GM.gold = 0;
		GM.stars = 0;
		GM.maxArrowCount = 10;
		GM.arrowCount = GM.maxArrowCount;
		maxBendSize = Gdx.graphics.getWidth() * .1f; 
		counter = 0;
		arrows.clear();
		activedSkills.clear();
		reloadingSkills.clear();
		lastTouch.set(1, 0, 0);

		viewportHeight = 4f; // 4 meters in box 2d world
		viewportWidth = viewportHeight * w / h;
		camera = new OrthographicCamera(viewportWidth, viewportHeight);
		cameraDeltaX = viewportWidth * .25f;
		GM.ratio = (float) Gdx.graphics.getHeight() / viewportHeight;

		Array<Body> tmp = GM.scene.getNamed(Body.class, "chicken");
		chicken = new Chicken(tmp.get(0));
		lastChickenPosition = new Vector2(chicken.getChicken().getPosition());
		mRegionMap.put("arrow", GM.commonAtlas.findRegion("arrow"));

		createSpatialsFromRubeImages(GM.scene);

		Json json = new Json();
		background = json.fromJson(ParallaxBackground.class, Gdx.files.internal("backgrounds/egypt.json"));

		tmp = GM.scene.getNamed(Body.class, "end");
		endX = tmp.get(0).getPosition().x;

		trajectoryTexture = GM.commonAtlas.findRegion("trajectory");

		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(true);

		hud = new HUD(this);
		GM.scene.getWorld().setContactListener(new GameCollisions());
		InputMultiplexer multi = new InputMultiplexer();
		multi.addProcessor(hud.getStage());
		multi.addProcessor(this);
		Gdx.input.setInputProcessor(multi);
		Gdx.input.setCatchBackKey(true);

		state = State.STARTING;
		updateCamera();
	}

	@Override
	public void hide() {

	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	private void update(float delta) {
		if (state == State.PAUSE) {

		} else {
			if (Gdx.input.isKeyPressed(Keys.Z)) {
				float angle = chicken.getBow().getAngle();
				angle += 2;
				chicken.getBow().setAngle(angle);
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				float angle = chicken.getBow().getAngle();
				angle -= 2;
				chicken.getBow().setAngle(angle);
			}
			updateWorld(delta);
			chicken.update(state, delta);
			if (state == State.STARTING) {
				if (counter > 0) {
					counter -= delta;
					hud.setMessage(Math.round(counter) + "");
				} else if (counter <= 0) {
					state = State.PLAYING;
					hud.hideMessage();
					chicken.run();
				}
			} else if (state == State.PLAYING) {
				updateSkills(delta);

				updateArrows(delta);

				if (isLost()) {
					state = State.ENDING;
					GM.world.clearForces();
				} else if (isWin()) {
					state = State.ENDING;
					GM.world.clearForces();
				}
			} else if (state == State.ENDING) {
				chicken.idle();
				if (isLost()) {
					hud.showLoose();
					state = State.LOOSE;
				} else {
					hud.showWin();
					PlayerManager.get().setLevelFinish(GM.level.getIndex(), GM.stars);
					PlayerManager.get().addGold(GM.gold);
					state = State.WIN;
				}
			}

			updateCamera();
			background.setSpeed((chicken.getX() - lastChickenPosition.x) * 4, (chicken.getY() - lastChickenPosition.y) * 4);
			lastChickenPosition.set(chicken.getChicken().getPosition());
		}

		hud.update(delta);

	}

	private void updateWorld(float delta) {
		GM.scene.step();
		// Handle arrow to stick
		for (StickyInfo info : GameCollisions.arrowToStick) {
			WeldJointDef def = new WeldJointDef();
			def.initialize(info.getArrow().getmBody(), info.getUserData().getSpatial().getmBody(), info.getArrow().getmBody().getWorldCenter());
			GM.scene.getWorld().createJoint(def);
		}

		GameCollisions.arrowToStick.clear();
	}

	private void updateArrows(float delta) {
		for (int i = 0; i < arrows.size; i++) {
			Arrow a = arrows.get(i);
			Body body = a.getmBody();
			float flightSpeed = new Vector2(body.getLinearVelocity()).nor().len();
			float bodyAngle = body.getAngle();
			Vector2 pointingDirection = new Vector2(MathUtils.cos(bodyAngle), -MathUtils.sin(bodyAngle));
			float flyingAngle = MathUtils.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x);

			Vector2 flightDirection = new Vector2(MathUtils.cos(flyingAngle), MathUtils.sin(flyingAngle));
			float dot = flightDirection.dot(pointingDirection);
			float dragForceMagnitude = (1 - Math.abs(dot)) * flightSpeed * flightSpeed * dragConstant * body.getMass();
			Vector2 arrowTailPosition = body.getWorldPoint(new Vector2(-.3f, 0));
			body.applyForce(new Vector2((dragForceMagnitude * -flightDirection.x), (dragForceMagnitude * -flightDirection.y)), arrowTailPosition, true);

			if (!isInScreen(a.getmBody().getPosition())) {
				a.setDead(true);
			}
			if (a.isDead()) {
				GM.world.destroyBody(a.getmBody());
				arrows.removeIndex(i);
			}
		}
	}

	private void updateSkills(float delta) {
		for (int i = 0; i < activedSkills.size; ++i) {
			ActivatedSkill as = activedSkills.get(i);
			as.update(delta);
			if (as.isOver()) {
				reloadingSkills.add(as);
				activedSkills.removeIndex(i);
				activeSkill(false, as.getSkill());

			}
		}
		for (int i = 0; i < reloadingSkills.size; ++i) {
			ActivatedSkill as = reloadingSkills.get(i);
			as.update(delta);
			hud.updateReloadingSkill(as.getSkill(), as.getCounterValue());
			if (as.isReloaded()) {
				reloadingSkills.removeIndex(i);
				hud.skillReloaded(as.getSkill());
			}
		}
	}

	private void draw(float delta) {
		if (state != State.PAUSE) {
			background.render(delta);
		}
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < mSpatials.size; i++) {
			SimpleSpatial s = mSpatials.get(i);
			s.render(batch, delta);
			if (s.getUserData() instanceof UserData) {
				UserData ud = s.getUserData();
				if (ud.isDead()) {
					mSpatials.removeIndex(i);
					GM.scene.getWorld().destroyBody(s.getmBody());
				}
			}
		}
		chicken.draw(batch, skeletonRenderer);

		// RayCastCallback callback;
		if (chicken.getBow().isBend()) {
			int nbPoint = Math.round(Interpolation.exp5.apply(1, 30, chicken.getBow().getBendSize() / 1));
			for (int i = 2; i < nbPoint; i++) { // three seconds at 60fps
				trajectoryVelocty.set(chicken.getBow().computeVelocity().cpy());
				startTrajectory.set(chicken.getBow().getOrigin());
				Vector2 trajectoryPosition = getTrajectoryPoint(startTrajectory, trajectoryVelocty, i);
				batch.draw(trajectoryTexture, trajectoryPosition.x, trajectoryPosition.y, .05f, .05f);
			}
		}
		for (int i = 0; i < arrows.size; i++) {
			Arrow a = arrows.get(i);
			a.render(batch, delta);
		}

		batch.end();

		EffectManager.get().draw(camera.combined.cpy().scale(Utils.WORLD_TO_BOX, Utils.WORLD_TO_BOX, 0), delta);
		hud.draw();

		if (debug) {
			renderer.render(GM.scene.getWorld(), camera.combined);
		}
	}

	private Vector2 getTrajectoryPoint(Vector2 startingPosition, Vector2 startingVelocity, float n) {
		// velocity and gravity are given per second but we want time step
		// values here
		float t = 1 / 60.0f; // seconds per time step (at 60fps)
		Vector2 stepVelocity = startingVelocity.scl(t); // m/s
		Vector2 stepGravity = GM.scene.getWorld().getGravity().cpy().scl(t).scl(t); // m/s/s

		return startingPosition.add(stepVelocity.scl(n)).add(stepGravity.scl(.58f * (n * n + n)));
	}

	@Override
	public void resize(int width, int height) {

	}

	private void updateCamera() {
		camera.position.x = chicken.getX() + cameraDeltaX;
		camera.position.y = chicken.getY() + cameraDeltaY;
		camera.update();
	}

	private boolean isLost() {
		return chicken.isDead();
	}

	private boolean isWin() {
		return chicken.getX() > endX;
	}

	private void createSpatialsFromRubeImages(RubeScene scene) {
		Array<RubeImage> images = scene.getImages();
		mSpatials = new Array<SimpleSpatial>();

		String altasName = (String) scene.getCustom(GM.world, "atlas");
		TextureAtlas atlas = GM.assetManager.get(Main.SCENES_ATLAS_PATH + altasName, TextureAtlas.class);
		if ((images != null) && (images.size > 0)) {

			for (int i = 0; i < images.size; i++) {
				RubeImage image = images.get(i);
				tmp.set(image.width, image.height);
				String textureFileName = image.file.replace(".png", "");
				System.out.println(textureFileName);
				TextureRegion region = atlas.findRegion(textureFileName);
				if (region == null) {
					region = GM.commonAtlas.findRegion(textureFileName);
				}
				SimpleSpatial spatial = new SimpleSpatial(region, image.flip, image.body, image.color, tmp, image.center, image.angleInRads * MathUtils.radiansToDegrees);

				Boolean active = (Boolean) GM.scene.getCustom(image, "active");
				if (active != null && active) {
					UserData userData = new UserData(spatial);
					spatial.setUserData(userData);

					Boolean destroyable = (Boolean) GM.scene.getCustom(image, "destroyable");
					if (destroyable != null && destroyable) {
						userData.setDestroyable(destroyable);
					}

					Boolean coin = (Boolean) GM.scene.getCustom(image, "coin");
					if (coin != null && coin) {
						userData.setCoin(coin);
					}

					Boolean mortal = (Boolean) GM.scene.getCustom(image, "mortal");
					if (mortal != null && mortal) {
						userData.setMortal(mortal);
					}

					Boolean stick = (Boolean) GM.scene.getCustom(image, "stick");
					if (stick != null && stick) {
						userData.setStick(stick);
					}

					Boolean star = (Boolean) GM.scene.getCustom(image, "star");
					if (star != null && star) {
						userData.setStar(star);
					}

					Integer coins = (Integer) GM.scene.getCustom(image, "coins");
					if (coins != null) {
						userData.setCoins(coins);
					}

					Integer life = (Integer) GM.scene.getCustom(image, "life");
					if (life != null) {
						userData.setLife(life);
					}

					Integer imgCount = (Integer) GM.scene.getCustom(image, "imageCount");
					if (imgCount != null) {
						spatial.setImages(new TextureRegion[imgCount - 1]);
						spatial.setImageCount(imgCount);
						for (int j = 0; j < imgCount - 1; ++j) {
							String key = "image" + (j + 1);
							String img = (String) GM.scene.getCustom(image, key);
							spatial.getImages()[j] = atlas.findRegion(img);
						}
					}
					image.body.setUserData(userData);
				}
				mSpatials.add(spatial);

			}
		}
	}

	public boolean isInScreen(float x, float y) {
		return x > (camera.position.x - camera.viewportWidth * .5f) && x < (camera.position.x + camera.viewportWidth * .5f);
	}

	public boolean isInScreen(Vector2 position) {
		return isInScreen(position.x, position.y);
	}

	public void activeSkill(boolean active, ActiveSkill skill) {
		if (active) {
			activedSkills.add(new ActivatedSkill(skill));
		}
		for (PassiveSkill ps : skill.getPassives()) {
			handlePassiveSkill(active, ps);
		}
	}

	private void handlePassiveSkill(boolean enable, PassiveSkill skill) {
		switch (skill.getCarac()) {
		case BOW_STRENGTH:
			chicken.getBow().setStrength(handleBonus(enable, chicken.getBow().getStrength(), skill.getType(), skill.getBonus()));
			break;
		case TIME_SPEED:
			GM.scene.setStepsPerSecond((int) handleBonus(enable, GM.scene.getStepsPerSecond(), skill.getType(), skill.getBonus()));
			chicken.setTimeFactor(handleBonus(enable, chicken.getTimeFactor(), ModifType.DIV, skill.getBonus()));
			break;
		case SPEED:
			break;
		default:
			break;
		}
	}

	private float handleBonus(boolean enable, float val, ModifType type, float bonus) {
		float res = val;
		switch (type) {
		case ADD:
			res = enable ? val + bonus : val - bonus;
			break;
		case DIV:
			res = enable ? val / bonus : val * bonus;
			break;
		case MUL:
			res = enable ? val * bonus : val / bonus;
			break;
		case SUB:
			res = enable ? val - bonus : val + bonus;
			break;
		}
		return res;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		GM.scene.getWorld().dispose();
		GM.scene.clear();
		renderer.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.Q) {
			chicken.jump();
			return true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.BACK || keycode == Keys.SPACE) {
			if (state == State.PLAYING) {
				state = State.PAUSE;
				hud.showPause();
			} else if (state == State.PAUSE) {
				hud.hidePause();
				state = State.PLAYING;
			}
		}
		if (keycode == Keys.D) {
			debug = !debug;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (state == State.PLAYING && !isLost() && !isWin()) {
			if (GM.arrowCount > 0) {
				chicken.getBow().bend(mRegionMap.get("arrow"), null);
				touchBeforeDrag.set(screenX, Gdx.graphics.getHeight() - screenY);
			}
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (state == State.PLAYING) {
			if (chicken.getBow().isBend()) {
				arrows.addAll(chicken.getBow().fire());
				GM.arrowFiredCount++;
				GM.arrowCount--;
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (chicken.getBow().isBend()) {

			float y = Gdx.graphics.getHeight() - screenY;
			tmp.set(touchBeforeDrag);
			float bendSize = Vector2.dst(screenX, y, touchBeforeDrag.x, touchBeforeDrag.y);
			tmp.sub(screenX, y);
			chicken.getBow().setAngle(tmp.angle());
			bendSize = Math.min(1, bendSize / maxBendSize);
			chicken.getBow().setBendSize(bendSize);

			return true;
		}
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

	public Chicken getPlayer() {
		return chicken;
	}

	public void setPlayer(Chicken player) {
		this.chicken = player;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Vector3 getLastTouch() {
		return lastTouch;
	}

	public void setLastTouch(Vector3 lastTouch) {
		this.lastTouch = lastTouch;
	}
}
