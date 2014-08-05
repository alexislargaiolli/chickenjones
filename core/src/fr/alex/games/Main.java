package fr.alex.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.RubeSceneAsyncLoader;

import fr.alex.games.items.ItemManager;
import fr.alex.games.localization.LocalManager;
import fr.alex.games.saves.PlayerManager;
import fr.alex.games.screens.ScreenManager;
import fr.alex.games.screens.Screens;

public class Main extends Game {
	@Override
	public void create() {
		Gdx.app.log("Main", "init");
		GM.assetManager = new AssetManager();
		GM.assetManager.load("data/bg1.png", Texture.class);
		GM.assetManager.finishLoading();
		GM.assetManager.setLoader(RubeScene.class, new RubeSceneAsyncLoader(new InternalFileHandleResolver()));
		
		GM.skin = new Skin(Gdx.files.internal("uiskin.json"));
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().show(Screens.INTRO);
		LocalManager.get();
		PlayerManager.get();
		ItemManager.get();
		
		Gdx.input.setCatchBackKey(true);
		Gdx.app.log("Main", "init over");
		
		
		/*PlayerManager.get().addGold(100);
		PlayerManager.get().persist();*/
		
		/*Item i = new Item();
		i.setId(1);
		i.setName("item.1.name");
		i.setDesc("item.1.desc");
		i.setGold(100);
		i.setIcon("item_1_icon");
		PassiveSkill ps = new PassiveSkill();
		ps.setType(ModifType.ADD);
		ps.setCarac(Caracteristic.BOW_STRENGTH);
		ps.setBonus(1);
		i.getPassives().add(ps);
		
		Item i2 = new Item();
		i2.setId(2);
		i2.setName("item.2.name");
		i2.setDesc("item.2.desc");
		i2.setGold(100);
		i2.setIcon("item_2_icon");
		PassiveSkill ps2 = new PassiveSkill();
		ps2.setType(ModifType.ADD);
		ps2.setCarac(Caracteristic.JUMP_STRENGTH);
		ps2.setBonus(1);
		i2.getPassives().add(ps);
		
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(i);
		items.add(i2);
		Json json = new Json();
		FileHandle f = Gdx.files.local("/home/jahal/items/items.json");
		
		json.toJson(items, ArrayList.class, f);
		System.out.println(json.prettyPrint(items));*/
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
}
