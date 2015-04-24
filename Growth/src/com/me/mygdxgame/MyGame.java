package com.me.mygdxgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame implements ApplicationListener 
{
	private OrthographicCamera _camera;
	private SpriteBatch _batch;
	
	private float SCREENW = 800;
	private float SCREENH = 600;
	
	private Background _bg;
	private Game _game;
	private Menu _menu;
	
	private GameState _currentstate = GameState.MENU;
	
	@Override
	public void create() 
	{		
		//initialize the camera
		_camera = new OrthographicCamera();
		_camera.setToOrtho(false, SCREENW, SCREENH);
		_batch = new SpriteBatch();
		
		_bg = new Background();
		_menu = new Menu();
		_game = new Game();
	}

	@Override
	public void dispose() 
	{
		//release the sprite batch
		_batch.dispose();
		
		_bg.Release();
		_menu.Release();
	}
	
	public void BeginDraw()
	{
		//render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		_batch.setProjectionMatrix(_camera.combined);
		_batch.begin();
		
		//render the background (no home button if in the main menu)
		boolean renderhome = true;
		if (_currentstate == GameState.MENU)
			renderhome = false;
		_bg.RenderBackground(_batch, renderhome);
	}
	
	public void EndDraw()
	{	
		_bg.RenderForeground(_batch);
		_batch.end();
	}

	@Override
	public void render() 
	{				
		//update the clock
		Clock.Update();
		
		//check if the user selects the home button
		if (_currentstate != GameState.MENU)
			_currentstate = _bg.Update(_currentstate);
		
		//check whether or not to restart the game
		if (_currentstate == GameState.RETRY) 
		{
			_game.Reset();
			_currentstate = GameState.GAME;
		} else if (_currentstate == GameState.NEXT) 
		{
			_game.NextLevel();
			_currentstate = GameState.GAME;
		}
		
		//call the appropriate function for the current game state
		if (_currentstate == GameState.MENU)
			MENU();		
		else if (_currentstate == GameState.GAME)
			GAME();
		else
			GAMEOVER();
	}
	
	public void GAME()
	{
		_currentstate = _game.Update();
		
		BeginDraw();
		
		_game.Render(_batch);
		
		EndDraw();
	}
	
	public void MENU()
	{
		_currentstate = _menu.Update();
		if (_currentstate == GameState.GAME)
			_game.Reset();
			
		BeginDraw();
		
		_menu.Render(_batch);
		
		EndDraw();
	}
	
	public void GAMEOVER()
	{
		if (_currentstate == GameState.GAMEOVER)
			_currentstate = _menu.UpdateGameOver();
		else if (_currentstate == GameState.SUCCESS)
			_currentstate = _menu.UpdateSuccess();
		
		BeginDraw();
		
		_game.Render(_batch);
		
		if (_currentstate == GameState.GAMEOVER)
			_menu.RenderGameOver(_batch);
		else if (_currentstate == GameState.SUCCESS)
			_menu.RenderSuccess(_batch);
		
		EndDraw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
