package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tree 
{
	private Image _tree;
	private Roots _roots;
	private Canopy _canopy;
	private Indicators _indicators;
	private float _increment = 0.1f;
	private float _water_bonus = 20.0f; //when a root fills branch, lower the water level
	
	public double _add_time = 2.0f;
	
	private Sound _waterdrop;
	
	public Tree(String FileName, int Width, int Height)
	{
		//load assets
		_tree = new Image(FileName, Width, Height);
		_roots = new Roots();
		_canopy = new Canopy();
		_indicators = new Indicators();
		
		_waterdrop = Gdx.audio.newSound(Gdx.files.internal("audio/drop.wav"));
	}
	
	public void Release()
	{
		_tree.Release();
		_roots.Release();
		_canopy.Release();
		_indicators.Release();
		_waterdrop.dispose();
	}
	
	public void LoadCanopy(String Buds, String Bloom, String Leaves)
	{
		int width	= (int)_tree.GetDimmensions().x;
		int height	= (int)_tree.GetDimmensions().y;
		
		_canopy.Load(Buds, Bloom, Leaves, width, height);
	}
	
	public void SetCanopyIncrement(float Increment)
	{
		_increment = Increment;
	}
	
	public void SetWaterBonus(float Bonus)
	{
		_water_bonus = Bonus;
	}
		
	public void AddRootLocation(int X, int Y)
	{
		/* -------------------------------------------------------------------
			x and y are relative to the trees position
			convert to screen coordinates for rendering
		 ------------------------------------------------------------------- */
		float xpos = X + (400-_tree.GetDimmensions().x/2.0f);
		float ypos = Y + (300-_tree.GetDimmensions().y/2.0f);
		
		_roots.AddRoot(xpos, ypos);
	}
	
	public void AddIndicatorLocation(int X, int Y)
	{
		/* -------------------------------------------------------------------
			x and y are relative to the trees position
			convert to screen coordinates for rendering
	 	------------------------------------------------------------------- */
		float xpos = X + (400-_tree.GetDimmensions().x/2.0f);
		float ypos = Y + (300-_tree.GetDimmensions().y/2.0f);
		
		_indicators.AddIndicator(xpos, ypos);
	}
	
	public GameState Update(Water WaterLevel)
	{
		//activates roots and indicators
		ActivateTree();
		
		_indicators.Update(_canopy);
		if (_roots.Update(_indicators))
		{
			_waterdrop.play();
			WaterLevel.IncrementWaterLevel(-_water_bonus);
			_canopy.IncrementBloom(_increment);
		}
		
		//check lose/win conditions
		if (WaterLevel.GetWaterLevel() == 0.0f) 
		{
			//at game over, make sure user is not holding a root
			_roots.ReleaseRoot();
			return GameState.GAMEOVER;
		} else if (_canopy.GetBloom() == 1.0f) 
		{
			//shouldn't be necessary here, but in for consistency
			_roots.ReleaseRoot();
			return GameState.SUCCESS;
		}
		
		return GameState.GAME; //stay in game
	}
	
	private void ActivateTree()
	{
		if (Clock.GetTime() > _add_time)
		{
			if (_indicators.ActivateNewIndicator())
				_roots.FillNewRoot();

			double next = (Math.random())+2.0f;
			_add_time = Clock.GetTime() + next;
		}
	}
	
	public void Render(SpriteBatch Batch)
	{
		//draw the tree centered on the screen
		float xpos = 400-_tree.GetDimmensions().x/2.0f;
		float ypos = 300-_tree.GetDimmensions().y/2.0f;
		_tree.Render(Batch, xpos, ypos);
		_canopy.Render(Batch, xpos, ypos);
		
		_indicators.Render(Batch);
		_roots.Render(Batch);
	}
}
