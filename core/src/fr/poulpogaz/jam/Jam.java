package fr.poulpogaz.jam;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import fr.poulpogaz.jam.stage.Stage;
import fr.poulpogaz.jam.stage.Stages;
import fr.poulpogaz.jam.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static fr.poulpogaz.jam.Constants.*;

public class Jam implements ApplicationListener {

	public static final Jam INSTANCE = new Jam();

	public static final AssetManager manager = new AssetManager();

	private BitmapFont font22;
	private BitmapFont font42;

	private SpriteBatch batch;
	private ShapeRenderer shape;

	private MainMenuScreen mainMenuScreen;
	private GameScreen gameScreen;
	private WinScreen winScreen;

	private AbstractScreen currentScreen;
	private boolean loading = true;

	private OrthographicCamera ortho;
	private Matrix4 transform;

	private final List<String> debugInfos = new ArrayList<>();

	private Jam() {

	}

	@Override
	public void create () {
		if (Constants.DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_INFO);
		}

		Gdx.input.setCatchKey(Input.Keys.SPACE, true);

		ortho = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
		ortho.setToOrtho(false);

		transform = new Matrix4().scl(Constants.SCALE_FACTOR);

		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		loadFonts();

		batch.getProjectionMatrix().set(ortho.combined);
		shape.getProjectionMatrix().set(ortho.combined);
		batch.getTransformMatrix().set(transform);
		shape.getTransformMatrix().set(transform);

		gameScreen = new GameScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		winScreen = new WinScreen(this);

		setScreen(mainMenuScreen);
	}

	private void loadFonts() {
		manager.load("fonts/dialog_22.fnt", BitmapFont.class);
		manager.load("fonts/dialog_42.fnt", BitmapFont.class);
		manager.finishLoading();

		font22 = manager.get("fonts/dialog_22.fnt", BitmapFont.class);
		font42 = manager.get("fonts/dialog_42.fnt", BitmapFont.class);

		font22.getData().setScale(0.5f);
		font42.getData().setScale(0.5f);
	}

	@Override
	public void resize(int width, int height) {
		Gdx.app.debug("DEBUG", "Window size = " + width + "x" + height);

		//if (currentScreen != null && !loading) currentScreen.resize(width, height);
		Gdx.gl.glViewport((width - WINDOW_WIDTH) / 2,
				(height - WINDOW_HEIGHT) / 2,
				WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	@Override
	public void render() {
		batch.getProjectionMatrix().set(ortho.combined);
		shape.getProjectionMatrix().set(ortho.combined);
		batch.getTransformMatrix().set(transform);
		shape.getTransformMatrix().set(transform);

		if (manager.update()) {
			if (loading) {
				currentScreen.show();
				currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
			loading = false;
			currentScreen.render(Gdx.graphics.getDeltaTime());
		} else {
			ScreenUtils.clear(0, 0, 0, 1);
			float progress = manager.getProgress();

			batch.begin();
			font42.setColor(1, 1, 1, 1);
			font42.draw(batch, "Loading: " + Utils.round2(progress * 100) + "%", 0, HALF_HEIGHT,
					WIDTH, Align.center, true);
			batch.end();
		}

		if (DEBUG) {
			drawDebugInfo();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F2)){
			boolean fullScreen = Gdx.graphics.isFullscreen();
			Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

			if (fullScreen) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(currentMode);
			}
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
			DEBUG = !DEBUG;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {
			SHOW_HITBOX = !SHOW_HITBOX;
		}
	}

	private void drawDebugInfo() {
		batch.enableBlending();
		batch.begin();

		debugInfos.clear();
		debugInfos.add("FPS: " + Gdx.graphics.getFramesPerSecond());

		if (currentScreen != null && !loading) {
			currentScreen.getDebugInfo(debugInfos);
		}

		float h = font22.getLineHeight();
		font22.setColor(1, 1, 1, 1);

		float y = HEIGHT - font22.getAscent();
		for (String str : debugInfos) {
			font22.draw(batch, str, 0, y);

			y -= h;
		}

		batch.end();
		batch.disableBlending();
	}

	@Override
	public void pause() {
		if (currentScreen != null && !loading) currentScreen.pause();
	}

	@Override
	public void resume() {
		if (currentScreen != null && !loading) currentScreen.resume();
	}


	public void setScreen(AbstractScreen screen) {
		if (this.currentScreen != null) this.currentScreen.hide();
		this.currentScreen = screen;
		if (this.currentScreen != null) {
			this.currentScreen.preLoad();
			loading = true;
		}
	}

	@Override
	public void dispose () {
		manager.dispose();

		gameScreen.dispose();
		mainMenuScreen.dispose();
		winScreen.dispose();

		shape.dispose();
		batch.dispose();

		for (Stage s : Stages.STAGES) {
			s.getBackground().dispose();

			if (s.getEffect() != null) {
				s.getEffect().dispose();
			}
		}
	}

	public BitmapFont getFont() {
		return getFont22();
	}

	public BitmapFont getFont22() {
		return font22;
	}

	public BitmapFont getFont42() {
		return font42;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public ShapeRenderer getShape() {
		return shape;
	}

	public MainMenuScreen getMainMenuScreen() {
		return mainMenuScreen;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public WinScreen getWinScreen() {
		return winScreen;
	}

	public static <T> T getOrLoad(String name, Class<T> type) {
		if (manager.isLoaded(name, type)) {
			return manager.get(name, type);
		} else if (!manager.contains(name, type)) {
			manager.load(name, type);
		}

		return null;
	}

	public static <T> void loadIfNeeded(String name, Class<T> type) {
		if (!manager.isLoaded(name, type)) {
			manager.load(name, type);
		}
	}

	public static Texture getOrLoadTexture(String name) {
		String fullName = "textures/" + name;

		if (manager.isLoaded(fullName, Texture.class)) {
			return manager.get(fullName, Texture.class);
		} else if (!manager.contains(fullName, Texture.class)) {
			manager.load(fullName, Texture.class);
		}

		return null;
	}

	public static void loadIfNeededTexture(String name) {
		String fullName = "textures/" + name;
		if (!manager.isLoaded(fullName, Texture.class)) {
			manager.load(fullName, Texture.class);
		}
	}

	public static Texture getTexture(String name) {
		return manager.get("textures/" + name, Texture.class);
	}
}
