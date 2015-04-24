package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Menu 
{
	private Image _title;
	private Image _credits;
	private Image _back;
	
	private boolean _on_credits = false;
	private float _credits_alpha = 0.0f;
	private float _back_trans = 0.5f;
	private float _back_drop = 900.0f;
	
	private int _selection = 0;
	private boolean _can_change = true; //only on key press
	private boolean _playgame = false;
	private boolean _restartgame = false;
	private boolean _nextlevel = false;
	
	private Image _play;
	private Image _about;
	private Image _quit;
	private Image _replay;
	private Image _next;
	
	private Image _play_active;
	private Image _about_active;
	private Image _quit_active;
	private Image _replay_active;
	private Image _next_active;
	
	private Image _gameover;
	private Image _overlay;
	private Image _success;	
	
	private Sound _menu;
	
	public Menu()
	{
		_title		= new Image("img/menu/title.png", 262, 70);
		_credits	= new Image("img/menu/credits.png", 396, 208);
		_back		= new Image("img/menu/back.png", 32, 32);
		
		_play		= new Image("img/menu/play.png", 117, 67);
		_about		= new Image("img/menu/about.png", 182, 49);
		_quit		= new Image("img/menu/quit.png", 127, 61);
		_replay		= new Image("img/menu/replay.png", 193, 65);
		_next		= new Image("img/menu/next.png", 137, 49);
		
		_play_active	= new Image("img/menu/play_active.png", 117, 67);
		_about_active	= new Image("img/menu/about_active.png", 182, 49);
		_quit_active	= new Image("img/menu/quit_active.png", 127, 61);
		_replay_active	= new Image("img/menu/replay_active.png", 193, 65);
		_next_active	= new Image("img/menu/next_active.png", 137, 49);
		
		_gameover	= new Image("img/menu/gameover.png", 304, 48);
		_overlay	= new Image("img/menu/overlay.png", 488, 600);
		_success	= new Image("img/menu/success.png", 242, 48);
		
		_menu = Gdx.audio.newSound(Gdx.files.internal("audio/menu.wav"));
	}
	
	public void Release()
	{
		_title.Release();
		_credits.Release();
		_back.Release();
		
		_play.Release();
		_quit.Release();
		_about.Release();
		_replay.Release();
		_next.Release();
		
		_play_active.Release();
		_about_active.Release();
		_quit_active.Release();
		_replay_active.Release();
		_next_active.Release();
		
		_gameover.Release();
		_overlay.Release();
		_success.Release();
		
		_menu.dispose();
	}
	
	public GameState Update()
	{	
		if (!_on_credits) 
		{
			int last_select = _selection;
			ReadKeyBoard(0, 2, false);
			ReadMouse();
			
			//play an audio cue when the user changes selection
			if (last_select != _selection)
				_menu.play();
		} else UpdateCredits();
		
		if (_playgame) 
		{
			_playgame = false; //for next time in menu
			return GameState.GAME;
			
		} else return GameState.MENU; //stay in the main menu
	}
	
	public GameState UpdateGameOver()
	{
		UpdateBackdrop();
		
		//wait for the backdrop to fall
		if (_back_drop <= 0.0f)
		{
			int last_select = _selection;
			ReadKeyBoard(2, 3, true);
			ReadMouseGameOver(false);
			
			//play an audio cue when the user changes selection
			if (last_select != _selection)
				_menu.play();
		}
		
		if (_restartgame) 
		{
			//reset the back drop
			_back_drop = 900.0f;
			
			_restartgame = false;
			return GameState.RETRY;
		} else
			return GameState.GAMEOVER;
	}
	
	public GameState UpdateSuccess()
	{
		UpdateBackdrop();
		
		//wait for the backdrop to fall
		if (_back_drop <= 0.0f)
		{
			int last_select = _selection;
			ReadKeyBoard(2, 4, true);
			ReadMouseGameOver(true);
			
			//play an audio cue when the user changes selection
			if (last_select != _selection)
				_menu.play();
		}
		
		if (_restartgame) 
		{
			//reset the back drop
			_back_drop = 900.0f;
			
			_restartgame = false;
			return GameState.RETRY;
		} else if (_nextlevel)
		{
			//reset the back drop
			_back_drop = 900.0f;
			
			_nextlevel = false;
			return GameState.NEXT;
		} else
			return GameState.SUCCESS;
	}
	
	private void UpdateBackdrop()
	{
		_back_drop -= Gdx.graphics.getDeltaTime()*1000.0f;
		if (_back_drop < 0.0f) _back_drop = 0.0f;
	}
	
	private void UpdateCredits()
	{
		if (Gdx.input.isKeyPressed(Keys.ESCAPE))
			_on_credits = false;
		else if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			_on_credits = false;
		
		//get the mouse position
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600; //match y orientation to libGDX
		
		float xpos = 44.0f;
		float ypos = 300.0f-16.0f;
		
		//check if the user is hitting the back button to return to the menu
		boolean mouseover = false;
		if (mousex >= xpos && mousex <= xpos+32.0f) 
		{
			if (mousey >= ypos && mousey <= ypos+32.0f)
			{
				//adjust the transparency of the back button
				mouseover = true;
				_back_trans -= Gdx.graphics.getDeltaTime()*2.0f;
				if (_back_trans <= 0.0f) _back_trans = 0.0f;
				
				//on click return to the menu
				if (Gdx.input.isTouched())
					_on_credits = false;
			}
		}
		
		//readjust the transparency of the back button
		if (!mouseover && _back_trans < 0.5f)
		{
			_back_trans += Gdx.graphics.getDeltaTime()*2.0f;
			if (_back_trans >= 0.5f) _back_trans = 0.5f;
		}
	}
	
	private void ReadKeyBoard(int MinSelection, int MaxSelection, boolean FlipDirection)
	{
		//if the user is no longer holding a key, allow input again
		if (!Gdx.input.isKeyPressed(Keys.UP) && !Gdx.input.isKeyPressed(Keys.DOWN))
			_can_change = true;
				
		//get keyboard input
		if (Gdx.input.isKeyPressed(Keys.DOWN) && _can_change) {
			if (FlipDirection)
				_selection--;
			else _selection++;
			
			_can_change = false;
		} else if (Gdx.input.isKeyPressed(Keys.UP) && _can_change) {
			if (FlipDirection)
				_selection++;
			else _selection--;
			
			_can_change = false;
		} 
		
		//clamp the selection variable
		if (_selection < MinSelection) 
			_selection = MinSelection;
		else if (_selection > MaxSelection) 
			_selection = MaxSelection;
		
		//process the selection on enter key
		if (Gdx.input.isKeyPressed(Keys.ENTER))
			ProcessSelection();
	}
	
	private void ReadMouse()
	{
		//set selection based on mouse position
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600; //match y orientation to libGDX
		boolean mouseover = false;
		boolean mousemove = false;
		
		if (Gdx.input.getDeltaX() != 0.0f || Gdx.input.getDeltaY() != 0.0f)
			mousemove = true;
		
		float xpos = 400.0f - 117.0f/2.0f;
		if (mousex >= xpos && mousex <= xpos+117.0f)
			if (mousey >= 332.0f && mousey <= 332.0f+67.0f)
			{ 
				if (mousemove) //allow the keyboard to have the focus
					_selection = 0; 
				mouseover = true; 
			}
		
		xpos = 400.0f - 182.0f/2.0f;
		if (mousex >= xpos && mousex <= xpos+182.0f)
			if (mousey >= 274.0f && mousey <= 274.0f+49.0f)
			{ 
				if (mousemove) //allow the keyboard to have the focus
					_selection = 1; 
				mouseover = true; 
			}
		
		xpos = 400.0f - 127.0f/2.0f;
		if (mousex >= xpos && mousex <= xpos+127.0f)
			if (mousey >= 190.0f && mousey <= 190.0f+61.0f)
			{ 
				if (mousemove) //allow the keyboard to have the focus
					_selection = 2; 
				mouseover = true; 
			}
		
		if (Gdx.input.isTouched() && mouseover)
			ProcessSelection();
	}
	
	private void ReadMouseGameOver(boolean IncludeNext)
	{
		//set selection based on mouse position
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600; //match y orientation to libGDX
		boolean mouseover = false;
		boolean mousemove = false;
		
		if (Gdx.input.getDeltaX() != 0.0f || Gdx.input.getDeltaY() != 0.0f)
			mousemove = true;
		
		float xpos = 400.0f - 137.0f/2.0f;
		if (IncludeNext)
		{
			if (mousex >= xpos && mousex <= xpos+137.0f)
				if (mousey >= 347.0f && mousey <= 347.0f+49.0f)
				{ 
					if (mousemove) //allow the keyboard to have the focus
						_selection = 4; 
					mouseover = true; 
				}
		}
		
		xpos = 400.0f - 193.0f/2.0f;
		if (mousex >= xpos && mousex <= xpos+193.0f)
			if (mousey >= 260.0f && mousey <= 260.0f+65.0f)
			{ 
				if (mousemove) //allow the keyboard to have the focus
					_selection = 3; 
				mouseover = true; 
			}
		
		xpos = 400.0f - 127.0f/2.0f;
		if (mousex >= xpos && mousex <= xpos+127.0f)
			if (mousey >= 190.0f && mousey <= 190.0f+61.0f)
			{ 
				if (mousemove) //allow the keyboard to have the focus
					_selection = 2; 
				mouseover = true; 
			}
		
		if (Gdx.input.isTouched() && mouseover)
			ProcessSelection();
	}
	
	private void ProcessSelection()
	{
		if (_selection == 0)
			_playgame = true;
		
		else if (_selection == 1)
			_on_credits = true;
		
		else if (_selection == 2)
			Gdx.app.exit();
		
		else if (_selection == 3)
			_restartgame = true;
			
		else if (_selection == 4)
			_nextlevel = true;
	}
	
	private void CheckCreditsAlpha()
	{
		//transition between the menu and credits
		if (_credits_alpha > 0.0f && !_on_credits)
			_credits_alpha -= Gdx.graphics.getDeltaTime()*2.0f;
		else if (_credits_alpha < 1.0f && _on_credits)
			_credits_alpha += Gdx.graphics.getDeltaTime()*2.0f;
		
		//clamp the range of the credits alpha
		if (_credits_alpha < 0.0f) _credits_alpha = 0.0f;
		else if (_credits_alpha > 1.0f) _credits_alpha = 1.0f;
	}
	
	public void Render(SpriteBatch Batch)
	{
		CheckCreditsAlpha();
		float xpos = 400.0f - 262/2.0f;
		_title.Render(Batch, xpos, 48);
		
		RenderMenu(Batch);
		RenderCredits(Batch);
	}
	
	private void RenderMenu(SpriteBatch Batch)
	{
		float MenuAlpha = 1.0f-_credits_alpha;
		
		//render the menu
		float xpos = 400.0f - 117.0f/2.0f;
		if (_selection == 0)
			_play_active.Render(Batch, xpos, 332.0f, MenuAlpha);
		else _play.Render(Batch, xpos, 332.0f, MenuAlpha);
		
		xpos = 400.0f - 182.0f/2.0f;
		if (_selection == 1)
			_about_active.Render(Batch, xpos, 274.0f, MenuAlpha);
		else _about.Render(Batch, xpos, 274.0f, MenuAlpha);
		
		xpos = 400.0f - 127.0f/2.0f;
		if (_selection == 2)
			_quit_active.Render(Batch, xpos, 190.0f, MenuAlpha);
		else _quit.Render(Batch, xpos, 190.0f, MenuAlpha);
	}
	
	private void RenderCredits(SpriteBatch Batch)
	{
		//render the credits screen
		float xpos = 400.0f - 396.0f/2.0f;
		float ypos = 300.0f - 208.0f/2.0f;
		_credits.Render(Batch, xpos, ypos, _credits_alpha);
		
		xpos = 44.0f;
		ypos = 300.0f-16.0f;
		float alpha = _credits_alpha-_back_trans;
		if (alpha < 0.0f) alpha = 0.0f;
		
		_back.Render(Batch, xpos, ypos, alpha);
	}
	
	public void RenderGameOver(SpriteBatch Batch)
	{
		float xpos = 400.0f - 488/2.0f;
		_overlay.Render(Batch, xpos, _back_drop + 0.0f);
		
		xpos = 400.0f - 193.0f/2.0f;
		if (_selection == 3)
			_replay_active.Render(Batch, xpos, _back_drop + 260.0f);
		else _replay.Render(Batch, xpos, _back_drop + 260.0f);
		
		xpos = 400.0f - 127.0f/2.0f;
		if (_selection == 2)
			_quit_active.Render(Batch, xpos, _back_drop + 190.0f);
		else _quit.Render(Batch, xpos, _back_drop + 190.0f);
		
		xpos = 400.0f - 304.0f/2.0f;
		_gameover.Render(Batch, xpos, _back_drop + 500.0f);
	}
	
	public void RenderSuccess(SpriteBatch Batch)
	{
		float xpos = 400.0f - 488/2.0f;
		_overlay.Render(Batch, xpos, _back_drop + 0.0f);
		
		xpos = 400.0f - 137.0f/2.0f;
		if (_selection == 4)
			_next_active.Render(Batch, xpos, _back_drop + 347.0f);
		else _next.Render(Batch, xpos, _back_drop + 347.0f);
		
		xpos = 400.0f - 193.0f/2.0f;
		if (_selection == 3)
			_replay_active.Render(Batch, xpos, _back_drop + 260.0f);
		else _replay.Render(Batch, xpos, _back_drop + 260.0f);
		
		xpos = 400.0f - 127.0f/2.0f;
		if (_selection == 2)
			_quit_active.Render(Batch, xpos, _back_drop + 190.0f);
		else _quit.Render(Batch, xpos, _back_drop + 190.0f);
		
		xpos = 400.0f - 242.0f/2.0f;
		_success.Render(Batch, xpos, _back_drop + 500.0f);
	}
}
