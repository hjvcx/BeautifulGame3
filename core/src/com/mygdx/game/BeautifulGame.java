package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class BeautifulGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture boblinImg;
	private Texture spikeImg;
	private Texture floorImg;
	private OrthographicCamera camera;
	private Rectangle boblin;
	private Array<Rectangle> spikes;
	private long lastScoreTime = 0;
	private long lastSpawnTime;

	private int score;
	private String scoreName;
	private BitmapFont bitmapScore;

	private int highscore;
	private String highscoreName;
	private BitmapFont bitmapHighscore;

	private String nickname;
	private BitmapFont bitmapNickname;

	private float accel = 0;
	private float velocity = 9;
	private boolean isJump = false;

	private MyTextInputListener listener;


	@Override
	public void create () {

		setCamera();
		setInputListener();
		setTextures();
		setSprites();
		setBitmaps();

//		Gdx.app.log("BeautifulGame", "Hello world");
	}

	private void setCamera() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
	}

	private void setInputListener() {
		listener = new MyTextInputListener();
		Gdx.input.getTextInput(listener, "Enter your nickname", "Player", "Nickname");
	}

	private void setBitmaps() {
		score = -1;
		scoreName = "score: 0";
		bitmapScore = new BitmapFont();

		highscore = 0;
		highscoreName = "highscore: 0";
		bitmapHighscore = new BitmapFont();

		bitmapNickname = new BitmapFont();

		bitmapScore.setColor(Color.BLACK);
		bitmapScore.getData().setScale(2);
		bitmapHighscore.setColor(Color.BLACK);
		bitmapHighscore.getData().setScale(2);
		bitmapNickname.setColor(Color.BLACK);
		bitmapNickname.getData().setScale(2);

	}

	private void setTextures() {
		boblinImg = new Texture("boblin.png");
		spikeImg = new Texture("spike.png");
		floorImg = new Texture("floor.png");
	}

	private void setSprites() {
		batch = new SpriteBatch();

		boblin = new Rectangle();
		boblin.x = 100;
		boblin.y = 50;
		boblin.height = 50;
		boblin.width = 50;

		spikes = new Array<>();
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		setNickname();
		jumpRender();
		spikesRender();
		drawing();
	}

	private void setNickname() {
		nickname = listener.getText();
	}

	private void spikesRender() {
		if ((TimeUtils.nanoTime() > lastScoreTime + 1000000000) && !isNicknameEmpty()) {

			score++;
			if (score >= highscore) highscore = score;
			scoreName = "score: " + score;
			highscoreName = "highscore: " + highscore;
			lastScoreTime = TimeUtils.nanoTime();
		}

		if ((TimeUtils.nanoTime() > lastSpawnTime + MathUtils.random(900000000, 2000000000) && !isNicknameEmpty())) spawnSpike();

		Iterator<Rectangle> iter = spikes.iterator();
		while (iter.hasNext()) {

			Rectangle spike = iter.next();
			spike.x -= 300 * Gdx.graphics.getDeltaTime();
			if (spike.overlaps(boblin)) score = -1;
			if (spike.x < -50 || spike.overlaps(boblin)) iter.remove();
		}
	}

	private void drawing() {
		batch.begin();

		batch.draw(boblinImg, boblin.x, boblin.y);
		for (Rectangle s : spikes) {
			batch.draw(spikeImg, s.x, s.y);
		}
		for (int i = 0; i < 800; i += 50) {
			batch.draw(floorImg, i, 0);
		}
		bitmapScore.draw(batch, scoreName, 20, 460);
		bitmapHighscore.draw(batch, highscoreName, 20, 430);
		bitmapNickname.draw(batch, nickname, 350, 450);

		batch.end();
	}

	private void jumpRender() {
		if (Gdx.input.isTouched()) {

			isJump = true;
		}
		if(isJump) {
			boblin.y += velocity;
			velocity -= 0.04 * accel;
			accel++;
			if (boblin.y <= 50) {
				boblin.y = 50;
				velocity = 9;
				accel = 0;
				isJump = false;
			}
		}
	}

	@Override
	public void dispose () {

		batch.dispose();
		boblinImg.dispose();
		spikeImg.dispose();
		floorImg.dispose();
	}

	private boolean isNicknameEmpty() {
		return nickname.equals("");
	}

	private void spawnSpike() {

		Rectangle spike = new Rectangle();

		spike.x = 800 + 50;
		spike.y = 50;
		spike.width = 50;
		spike.height = 50;

		spikes.add(spike);

		lastSpawnTime = TimeUtils.nanoTime();
	}
}
