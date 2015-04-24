package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Background 
{
	private Image _background;
	private Image _foreground;
	private Image _ground;
	private AnimImage _grass;
	
	private Image _home;
	private float _homealpha = 0.3f;
	private boolean _cangohome = false;
	
	private Image _rain;
	private float _rainpos = 0.0f;
	
	private Image _cloud0; //make more clouds!
	private Vector3[] _cloud_positions; //z is cloud image index
	private int _cloud_count = 7;
	
	public Background()
	{
		//load assets
		_background = new Image("img/bg/background.png", 800, 600);
		_foreground = new Image("img/bg/frame.png", 800, 600);
		_ground		= new Image("img/bg/ground.png", 800, 151);
		_rain		= new Image("img/bg/rain.png", 800, 49);
		_cloud0		= new Image("img/bg/cloud_0.png", 342, 137);
		_home		= new Image("img/bg/home.png", 24, 27);
		
		//create the animated grass
		_grass = new AnimImage("img/bg/grass_spritesheet.png", 1, 4, 1);
		_grass.NewAnimation(0, 4, 0, 3, 3.0f);
		
		//generate the clouds
		_cloud_positions = new Vector3[_cloud_count];
		for (int i=0; i<_cloud_count; i++) 
		{
			//get the equally space x pos in the range 0-3200 (4 screen spaces)
			float xpos = ((float)i/_cloud_count) * 3200.0f;
			_cloud_positions[i] = InitCloud(xpos);
		}
	}
	
	public void Release()
	{
		_background.Release();
		_foreground.Release();
		_ground.Release();
		_rain.Release();
		_grass.Release();
		_cloud0.Release();
	}
	
	public GameState Update(GameState Current)
	{
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600; //match y orientation to libGDX
		boolean mouseover = false;
		
		//check if the mouse is over the home button
		float xpos = 800.0f-44.0f;
		float ypos = 600.0f-46.0f;
		if (mousex >= xpos && mousex <= xpos+24.0f)
			if (mousey >= ypos && mousey <= ypos+27.0f)
			{
				mouseover = true;
				
				//increase the alpha value of the home button
				_homealpha += Gdx.graphics.getDeltaTime()*2.0f;
				if (_homealpha > 0.7)
					_homealpha = 0.7f;
			}
		
		//reset the home button alpha
		if (!mouseover)
			_homealpha = _homealpha -= Gdx.graphics.getDeltaTime()*2.0f;
		if (_homealpha < 0.3f) _homealpha = 0.3f;
		
		//only go to the main menu on click (so they don't drag a root to the home button)
		if (mouseover && !Gdx.input.isTouched())
			_cangohome = true;
		else if (!mouseover)
			_cangohome = false;
		
		if (Gdx.input.isTouched() && mouseover && _cangohome) 
		{
			_cangohome = false;
			return GameState.MENU;
		} else
			return Current;
	}
	
	public void RenderBackground(SpriteBatch Batch, boolean HomeButton)
	{
		UpdateCloudPositions();
		_background.Render(Batch, 0, 0);
		
		//render the rain
		for (int i=0; i<600/49+1; i++)
		{
			//update the rains position
			_rainpos += Gdx.graphics.getDeltaTime()*10.0f;
			if (_rainpos >= 49.0f) _rainpos = 0.0f;
			
			_rain.Render(Batch, 0.0f, 49.0f*(i+1) - _rainpos);
		}
		
		//draw the ground above the rain
		_ground.Render(Batch, 0, 0);
		
		//render the grass
		int count = (int)(800.0f/64.0f)+1;
		for (int i=0; i<count; i++)
		{
			_grass.Render(Batch, 0, new Vector2(i*64.0f, 151.0f));
		}
		
		//render the clouds
		for (int i=0; i<_cloud_count; i++)
		{
			Vector3 cloud = _cloud_positions[i];
			_cloud0.Render(Batch, cloud.x, cloud.y);
		}
		
		//render the home button
		if (HomeButton)
			_home.Render(Batch, 800.0f-44.0f, 600.0f-46.0f, _homealpha);
	}
	
	public void RenderForeground(SpriteBatch Batch)
	{
		_foreground.Render(Batch, 0, 0);
	}
	
	private Vector3 InitCloud(float X)
	{
		//generate appropriate random values for position and texture used
		float xpos = X + ((float)Math.random()*2.0f - 1.0f) * 64.0f;
		float ypos = 300.0f + 256.0f * (float)Math.random();
		float img_index = 1;
		
		return new Vector3(xpos, ypos, img_index);
	}
	
	private void UpdateCloudPositions()
	{
		for (int i=0; i<_cloud_count; i++)
		{
			Vector3 cloud = _cloud_positions[i];
			cloud.x -= Gdx.graphics.getDeltaTime()*20.0f;
			
			if (cloud.x <= -800.0f)
			{
				cloud.x += 3200.0f; //reset its position to the far right
				cloud.y = 300.0f + 181.0f * (float)Math.random(); //a new random height
			}
		} //end iterator
	}
}
