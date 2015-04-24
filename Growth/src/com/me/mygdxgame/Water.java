package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Water 
{
	private Image _water;
	private float _water_level = -151.0f;
	private float _target_water_level = -151.0f; //used for smooth transitions
	private float _water_speed = 10.0f;
	
	public Water()
	{
		//load assets
		_water = new Image("img/bg/water.png", 800, 151);
	}
	
	public void Release()
	{
		_water.Release();
	}
	
	public void SetSpeed(float Speed)
	{
		_water_speed = Speed;
	}
	
	public void Reset()
	{
		_water_level = -151.0f;
		_target_water_level = -151.0f;
		_water_speed = 10.0f;
	}
	
	public void IncrementWaterLevel(float Add)
	{
		_target_water_level += Add;
		
		if (_target_water_level > 0.0f) 
			_target_water_level = 0.0f;
		else if (_target_water_level < -151.0f)
			_target_water_level = -151.0f;
	}
	
	public float GetWaterLevel()
	{
		return _water_level;
	}
	
	public void Update()
	{
		//update the waters position
		_target_water_level += Gdx.graphics.getDeltaTime()*_water_speed;
		if (_target_water_level >= 0.0f) _target_water_level = 0.0f;
		
		//adjust the actual water level used for rendering according to the relative position of the target level
		if (_water_level < _target_water_level)
			_water_level += Gdx.graphics.getDeltaTime()*10.0f;
		else if (_water_level > _target_water_level)
			_water_level -= Gdx.graphics.getDeltaTime()*60.0f;
		if (_water_level >= 0.0f) _water_level = 0.0f;
		else if (_water_level < -151.0f) _water_level = -151.0f;
	}
	
	public void Render(SpriteBatch Batch)
	{
		_water.Render(Batch, 0.0f, _water_level);
	}
}
