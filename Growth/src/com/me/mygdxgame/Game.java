package com.me.mygdxgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game 
{
	private Water _water;
	private Tree _tree;
	private int _level = 0;
	private int _rootcount = 1;
	
	private Image _root_tip;
	private Image _branch_tip;
	private Image _water_tip;
	
	public Game()
	{
		_root_tip = new Image("img/howto/tooltip0.png", 292, 136);
		_branch_tip = new Image("img/howto/tooltip1.png", 324, 134);
		_water_tip = new Image("img/howto/tooltip2.png", 304, 140);
		
		_water = new Water();
		LoadLevel(0, 1);
	}
	
	public void Release()
	{
		_water.Release();
		_tree.Release();
		
		_root_tip.Release();
		_branch_tip.Release();
		_water_tip.Release();
	}
	
	public void Reset()
	{
		_water.Reset();
		LoadLevel(_level, _rootcount);
	}
	
	public void NextLevel()
	{
		_rootcount++;
		if (_rootcount > 4+_level && _level < 3)
		{
			_level++;
			_rootcount = _level+1;
		}
		
		_water.Reset();
		LoadLevel(_level, _rootcount);
	}
	
	public void LoadLevel(int Level, int RootCount)
	{
		//reset the clock
		Clock.Reset();
		
		//clamp and set appropriate level parameters
		if (Level < 0) Level = 0;
		else if (Level > 3) Level = 3;
		if (RootCount > 4+Level) RootCount = 4+Level;
		else if (RootCount < Level+1) RootCount = Level+1;
		
		_level = Level;
		_rootcount = RootCount;
		
		if (Level == 0)
		{		
			//load the tree and its canopy
			_tree = new Tree("img/tree_0/tree.png", 445, 536);
			_tree.LoadCanopy("img/tree_0/buds.png", "img/tree_0/bloom.png", "img/tree_0/leaves.png");
			
			//set root locations
			_tree.AddRootLocation(32, 96);
			if (RootCount > 1)	
				_tree.AddRootLocation(407, 95);
			if (RootCount > 2)
				_tree.AddRootLocation(153, 14);
			if (RootCount > 3)
				_tree.AddRootLocation(324, 32);
			
			//set indicator locations
			_tree.AddIndicatorLocation(105, 352);
			if (RootCount > 1)
				_tree.AddIndicatorLocation(230, 459);
			if (RootCount > 2)
				_tree.AddIndicatorLocation(134, 459);
			if (RootCount > 3)
				_tree.AddIndicatorLocation(279, 352);
			
			//set parameters for difficulty
			_water.SetSpeed(10.0f);
			_tree.SetWaterBonus(20.0f);
			_tree.SetCanopyIncrement(0.1f);
		}
		
		else if (Level == 1)
		{		
			//load the tree and its canopy
			_tree = new Tree("img/tree_1/tree.png", 433, 492);
			_tree.LoadCanopy("img/tree_1/buds.png", "img/tree_1/bloom.png", "img/tree_1/leaves.png");
			
			//set root locations
			_tree.AddRootLocation(0, 62);
			if (RootCount > 1)	
				_tree.AddRootLocation(401, 62);
			if (RootCount > 2)
				_tree.AddRootLocation(85, 19);
			if (RootCount > 3)
				_tree.AddRootLocation(308, 16);
			if (RootCount > 4)
				_tree.AddRootLocation(199, 0);
			
			//set indicator locations
			_tree.AddIndicatorLocation(38, 271);
			if (RootCount > 1)
				_tree.AddIndicatorLocation(340, 271);
			if (RootCount > 2)
				_tree.AddIndicatorLocation(93, 385);
			if (RootCount > 3)
				_tree.AddIndicatorLocation(280, 385);
			if (RootCount > 4)
				_tree.AddIndicatorLocation(188, 458);
			
			//set parameters for difficulty
			_water.SetSpeed(10.5f);
			_tree.SetWaterBonus(21.0f);
			_tree.SetCanopyIncrement(0.07f);
		} else if (Level == 2)
		{
			//load the tree and its canopy
			_tree = new Tree("img/tree_2/tree.png", 635, 567);
			_tree.LoadCanopy("img/tree_2/buds.png", "img/tree_2/bloom.png", "img/tree_2/leaves.png");
			
			//set root locations
			_tree.AddRootLocation(0, 90);
			if (RootCount > 1)	
				_tree.AddRootLocation(597, 90);
			if (RootCount > 2)
				_tree.AddRootLocation(138, 42);
			if (RootCount > 3)
				_tree.AddRootLocation(303, 44);
			if (RootCount > 4)
				_tree.AddRootLocation(441, 44);
			if (RootCount > 5)
				_tree.AddRootLocation(549, 15);
			
			//set indicator locations
			_tree.AddIndicatorLocation(188, 276);
			if (RootCount > 1)
				_tree.AddIndicatorLocation(421, 282);
			if (RootCount > 2)
				_tree.AddIndicatorLocation(56, 345);
			if (RootCount > 3)
				_tree.AddIndicatorLocation(487, 479);
			if (RootCount > 4)
				_tree.AddIndicatorLocation(101, 446);
			if (RootCount > 5)
				_tree.AddIndicatorLocation(266, 485);
			
			//set parameters for difficulty
			_water.SetSpeed(10.0f);
			_tree.SetWaterBonus(22.0f);
			_tree.SetCanopyIncrement(0.04f);
		} else 
		{
			//load the tree and its canopy
			_tree = new Tree("img/tree_3/tree.png", 678, 573);
			_tree.LoadCanopy("img/tree_3/buds.png", "img/tree_3/bloom.png", "img/tree_3/leaves.png");
			
			//set root locations
			_tree.AddRootLocation(26, 122);
			if (RootCount > 1)
				_tree.AddRootLocation(596, 104);
			if (RootCount > 2)	
				_tree.AddRootLocation(87, 54);
			if (RootCount > 3)
				_tree.AddRootLocation(485, 30);
			if (RootCount > 4)
				_tree.AddRootLocation(227, 63);
			if (RootCount > 5)
				_tree.AddRootLocation(374, 83);
			if (RootCount > 6)
				_tree.AddRootLocation(336, 35);
			
			//set indicator locations
			_tree.AddIndicatorLocation(90, 311);
			if (RootCount > 1)
				_tree.AddIndicatorLocation(545, 329);
			if (RootCount > 2)
				_tree.AddIndicatorLocation(266, 332);
			if (RootCount > 3)
				_tree.AddIndicatorLocation(402, 336);
			if (RootCount > 4)
				_tree.AddIndicatorLocation(78, 443);
			if (RootCount > 5)
				_tree.AddIndicatorLocation(299, 498);
			if (RootCount > 6)
				_tree.AddIndicatorLocation(515, 500);
			
			//set parameters for difficulty
			_water.SetSpeed(10.0f);
			_tree.SetWaterBonus(22.0f);
			_tree.SetCanopyIncrement(0.04f);
		}
	}
	
	public GameState Update()
	{
		GameState state = _tree.Update(_water);
		_water.Update();
		
		return state;
	}
	
	private void RenderTutorial(SpriteBatch Batch)
	{
		float time = (float)Clock.GetTime();
		
		if (time <= 5.0f)
		{
			//fade out on exit
			float alpha = (time-4.0f);
			if (alpha < 0.0f) alpha = 0.0f;
			alpha = -alpha + 1.0f; //flip direction
			
			_root_tip.Render(Batch, 80.0f, 126.0f, alpha);
		} //end root tool tip
		
		if (time > 2.0f && time < 10.0f)
		{
			//fade in
			float alpha = 1.0f;
			if (time < 3.0f)
				alpha = time-2.0f;

			else if (time>+9.0f)
			{
				//fade out
				alpha = time-9.0f;
				if (alpha < 0.0f) alpha = 0.0f;
				alpha = -alpha + 1.0f;
			}
			
			_branch_tip.Render(Batch, 80.0f, 453.0f, alpha);
		} //end branch tool tip
		
		if (time > 6.0f && time < 14.0f)
		{
			//fade in
			float alpha = 1.0f;
			if (time < 7.0f)
				alpha = time-6.0f;
			
			else if (time>13.0f)
			{
				//fade out
				alpha = time-13.0f;
				if (alpha < 0.0f) alpha = 0.0f;
				alpha = -alpha+1.0f;
			}

			_water_tip.Render(Batch, 480.0f, 156.0f, alpha);
		} //end water tool tip
	}
	
	public void Render(SpriteBatch Batch)
	{
		if (_level == 0 && _rootcount == 1)
			RenderTutorial(Batch);
		
		_water.Render(Batch);
		_tree.Render(Batch);
	}
}
