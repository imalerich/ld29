package com.me.mygdxgame;

import java.util.Iterator;
import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Indicators 
{
	private Image _indicator;
	Vector<Vector3> _indicator_locations; //z component is alpha
	Vector<Vector2> _indicator_times; //x: time set alive - y: alive length (random off of _time_alive)
	private float _time_alive = 3.0f; //base unit for the time each indicator is alive
	
	public Indicators()
	{
		_indicator = new Image("img/root/indicator.png", 76, 76);
		_indicator_locations = new Vector<Vector3>();
		_indicator_times = new Vector<Vector2>();
	}
	
	public void Release()
	{
		_indicator.Release();
	}
	
	public void AddIndicator(float X, float Y)
	{
		float talive = _time_alive + (float)(Math.random()-0.5f);
		_indicator_locations.add( new Vector3(X, Y, 0.0f) );
		_indicator_times.add( new Vector2((float)Clock.GetTime(), talive));
	}
	
	public boolean IndicatorHit(float X, float Y)
	{
		//loop through each and see if the position is within one of the indicators
		Iterator<Vector3> next_indicator = _indicator_locations.iterator();
		while (next_indicator.hasNext())
		{
			//start at relative center (38, 38) with radius 14
			Vector3 pos = next_indicator.next();
			if (pos.z != 1.0f) continue; //skip if not completely visible
			
			//calculate the distance from the hit point to the center
			Vector2 center = new Vector2(pos.x+38.0f, pos.y+38.0f);
			float dist = (float)Math.sqrt( Math.pow(center.x-X, 2)  + Math.pow(center.y-Y, 2));
			
			if (dist <= 14.0f) 
			{
				pos.z = 0.0f; //reset alpha
				return true;
			}
		}
		
		return false;
	}
	
	public void Update(Canopy Check)
	{
		UpdateIndicators(Check);
	}
	
	private void UpdateIndicators(Canopy Check)
	{
		//update the indicators (active indicators are fully transparent)
		Iterator<Vector3> next_indicator	= _indicator_locations.iterator();
		Iterator<Vector2> next_time			= _indicator_times.iterator();
		while (next_indicator.hasNext() && next_time.hasNext())
		{
			Vector3 pos = next_indicator.next();
			Vector2 time = next_time.next();
			
			//if it is a live, increment alpha until it is full
			if(pos.z > 0.0f && pos.z < 1.0f) 
			{
				//don't start the death clock until it is fully visible
				time.x = (float)Clock.GetTime();
				pos.z += Gdx.graphics.getDeltaTime()*4.0f;		
			} 
			
			//clamp the alpha component
			if (pos.z > 1.0f) 
				pos.z = 1.0f;
			
			//if the time has passed the indicators clock, kill it
			if ((float)Clock.GetTime() > time.x + time.y && pos.z == 1.0f) 
			{
				Check.IncrementBloom(-0.1f);
				pos.z = 0.0f;
			}
		}
	}
	
	public boolean ActivateNewIndicator()
	{
		int index = (int)(Math.random()*_indicator_locations.size());
		Vector3 pos		= _indicator_locations.get(index);
		Vector2 time	= _indicator_times.get(index);
		
		//if already alive, break early and try again next frame
		if (pos.z != 0.0f) return false;

		//set initial properties for this indicator
		float talive = _time_alive + (float)(Math.random()-0.5f);
		pos.z = 0.1f; //initialize to a static non-zero alpha
		time.x = (float)Clock.GetTime(); //reset clock
		time.y = talive;
		
		return true;
	}
	
	public void Render(SpriteBatch Batch)
	{
		Iterator<Vector3> next_indicator = _indicator_locations.iterator();
		while (next_indicator.hasNext())
		{
			Vector3 pos = next_indicator.next();
			float alpha = pos.z;
			
			_indicator.Render(Batch, pos.x, pos.y, alpha);
		}
	}
}
