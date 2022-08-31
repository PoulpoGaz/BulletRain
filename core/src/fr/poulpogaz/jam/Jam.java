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
import fr.poulpogaz.jam.utils.Utils;

import static fr.poulpogaz.jam.Constants.*;

public class Jam implements ApplicationListener {

	public static final AssetManager manager = new AssetManager();

	private BitmapFont font;
	private SpriteBatch batch;
	private ShapeRenderer shape;

	private GameScreen gameScreen;

	private AbstractScreen currentScreen;
	private boolean loading = true;

	private OrthographicCamera ortho;
	private Matrix4 transform;

	@Override
	public void create () {
		if (Constants.DEBUG) {
			Gdx.app.setLogLevel(Application.LOG_DEBUG);
		} else {
			Gdx.app.setLogLevel(Application.LOG_INFO);
		}

		ortho = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
		ortho.setToOrtho(false);

		transform = new Matrix4().scl(Constants.SCALE_FACTOR);

		batch = new SpriteBatch();
		shape = new ShapeRenderer();

		manager.load("fonts/dialog_22.fnt", BitmapFont.class);
		manager.finishLoading();

		font = manager.get("fonts/dialog_22.fnt", BitmapFont.class);
		font.getData().setScale(0.5f);

		batch.getProjectionMatrix().set(ortho.combined);
		shape.getProjectionMatrix().set(ortho.combined);
		batch.getTransformMatrix().set(transform);
		shape.getTransformMatrix().set(transform);

		gameScreen = new GameScreen(this);
		gameScreen.setFont(font);
		gameScreen.setSpriteBatch(batch);
		gameScreen.setShapeRenderer(shape);

		setScreen(gameScreen);
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
			font.draw(batch, "Loading: " + Utils.round2(progress * 100), 0, HALF_HEIGHT,
					WIDTH, Align.center, true);
			batch.end();
		}
		
		if (Constants.DEBUG && Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.F11)){
			boolean fullScreen = Gdx.graphics.isFullscreen();
			Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

			if (fullScreen) {
				Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
			} else {
				Gdx.graphics.setFullscreenMode(currentMode);
			}
		}
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
		}
	}

	@Override
	public void dispose () {
		manager.dispose();

		gameScreen.dispose();

		shape.dispose();
		batch.dispose();
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
