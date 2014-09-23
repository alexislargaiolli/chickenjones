package fr.alex.games;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gushikustudios.rube.RubeScene;

public class GM {
	public static AssetManager assetManager;
	public static World world;
	public static Skin skin;
	public static int gold;
	public static TextureAtlas commonAtlas;
	public static TextureAtlas bgAtlas;
	public static String sceneFile;
	public static int sceneIndex;
	public static RubeScene scene;
	public static float ratio;
	public static int arrowFiredCount;
	public static int hitCount;
}
