package com.me.mygdxgame;

import java.util.Iterator;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Roots 
{
	private Image _root;
	private Image _root_back;
	private Image _root_filled;
	
	private Vector<Vector3> _root_locations; //z component is how full the root is (0->1)
	private int _root_grabbed = -1; //-1 = no root, else is index
	private int _currently_filling = -1; //index of the root that is currently being filled (one at at time)
	
	public Roots()
	{
		_root = new Image("img/root/root_container.png", 32, 32);
		_root_back = new Image("img/root/root_back.png", 32, 32);
		_root_filled = new Image("img/root/root_filled.png", 32, 32);
		
		_root_locations = new Vector<Vector3>();
	}
	
	public void Release()
	{
		_root.Release();
		_root_back.Release();
		_root_filled.Release();
	}
	
	public void AddRoot(float X, float Y)
	{
		_root_locations.add(new Vector3(X, Y, 0.0f));
	}
	
	public boolean Update(Indicators Check)
	{
		//returns if the root filled an indicator
		boolean filled = false;
		
		//if the mouse the mouse button is not down, release the root, but leave it filled
		if (!Gdx.input.isTouched())
			_root_grabbed = -1;
		
		//check if the user selects one of the roots
		if (Gdx.input.isTouched() && _root_grabbed == -1) 
			FindSelectedRoot();
		//else the user has released the root
		else if (_root_grabbed != -1) 
		{
			filled = CheckFilled(Check);
			if (filled)
				ReleaseRoot();
		} 
		
		FillRoots();
		
		return filled;
	}
	
	public void ReleaseRoot()
	{
		//no roots are currently grabbed
		if (_root_grabbed == -1) return;
		
		//reset the fill of the selected root then reset the grabbed root index
		_root_locations.get(_root_grabbed).z = 0.0f;
		_root_grabbed = -1;
	}
	
	private void FillRoots()
	{
		if (_currently_filling == -1)
			return;
		
		//fill up a root
		Vector3 pos = _root_locations.get(_currently_filling);
		pos.z += Gdx.graphics.getDeltaTime()*4.0f;
		
		//the root has been completely filled, pick a new root to start filling
		if (pos.z > 1.0f) 
		{
			pos.z = 1.0f;
			_currently_filling = -1;
		}
	}
	
	public boolean FillNewRoot()
	{
		//already filling another root
		if (_currently_filling != -1)
			return false;
		
		//start filling a new root
		_currently_filling = (int)(Math.random()*_root_locations.size());
		
		return true;
	}
	
	public boolean CheckFilled(Indicators Check)
	{
		//check if the root fills an indicator
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600;
		if (Check.IndicatorHit(mousex, mousey))
			return true;
		
		return false;
	}
	
	public void FindSelectedRoot()
	{
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600;
		
		Iterator<Vector3> next_root = _root_locations.iterator();
		for (int i=0; i<_root_locations.size(); i++) //will not be removing any roots
		{
			//check the positions and see if the user has selected the root
			Vector3 pos = next_root.next();
			if (pos.x <= mousex && pos.x+32.0f >= mousex && pos.z >= 1.0f) //the root must be filled 
			{
				if (pos.y <= mousey && pos.y+32.0f >= mousey) 
				{
					_root_grabbed = i;
					
					//selected root found, break out of the loop
					break;
				} //end y check
			} //end x check
		}
	}
	
	public Vector2 GetMouseRootPos()
	{
		float mousex = Gdx.input.getX();
		float mousey = -Gdx.input.getY() + 600; //match y orientation to libGDX
		
		return new Vector2(mousex-16, mousey-16);
	}
	
	public void Render(SpriteBatch Batch)
	{
		//loop through each root and draw it
		Iterator<Vector3> next_root = _root_locations.iterator();
		for (int i=0; i<_root_locations.size(); i++) //will not be removing any roots
		{
			Vector3 pos = next_root.next();
			
			//set the position to that of the selected root
			if (i == _root_grabbed)
				pos = new Vector3( GetMouseRootPos(), pos.z);
			
			_root_back.Render(Batch, pos.x, pos.y);
			
			//range of 0->0.8 and 1.0 (jump in transparency to indicate completion)
			float alpha = pos.z - 0.2f;
			if (alpha < 0.0f) alpha = 0.0f;
			if (alpha >= 0.8f) alpha = 1.0f;
			_root_filled.Render(Batch, pos.x, pos.y, alpha);
			
			_root.Render(Batch, pos.x, pos.y);
		}
	}
}
