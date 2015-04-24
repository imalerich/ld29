package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;

public class Clock 
{
	private static double _time = 0.0f;
	
	public static double GetTime()
	{
		return _time;
	}
	
	public static void Reset()
	{
		_time = 0.0f;
	}
	
	public static void Update()
	{
		_time += Gdx.graphics.getDeltaTime();
	}
}
