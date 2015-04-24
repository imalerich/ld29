package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Canopy 
{
	private Image _buds;
	private Image _bloom;
	private Image _leaves;
	
	private float _bloom_target = 0.0f; //increment by tree class
	private float _bloom_percent = 0.0f; //increment over time to reach target
	
	public void Load(String Buds, String Bloom, String Leaves, int Width, int Height)
	{
		_buds = new Image(Buds, Width, Height);
		_bloom = new Image(Bloom, Width, Height);
		_leaves = new Image(Leaves, Width, Height);
	}
	
	public void Release()
	{
		_buds.Release();
		_bloom.Release();
		_leaves.Release();
	}
	
	public float GetBloom()
	{
		return _bloom_target;
	}
	
	public void IncrementBloom(float Percent)
	{
		_bloom_target += Percent;
		
		//clamp the bloom target
		if (_bloom_target < 0.0f) 
			_bloom_target = 0.0f;
		else if (_bloom_target > 1.0f) 
			_bloom_target = 1.0f;
	}
	
	public void Render(SpriteBatch Batch, float X, float Y)
	{
		//increase bloom percent by time to approach the target (allows for smooth transitions)
		if (_bloom_percent < _bloom_target)
			_bloom_percent += Gdx.graphics.getDeltaTime()/10.0f;
		else if (_bloom_percent > _bloom_target)
			_bloom_percent -= Gdx.graphics.getDeltaTime()/10.0f;
		
		//clamp the bloom alpha
		if (_bloom_percent < 0.0f)
			_bloom_percent = 0.0f;
		else if (_bloom_percent > 1.0f)
			_bloom_percent = 1.0f;
		
		//calculate the alpha values for each layer, then render accordingly
		float alpha = _bloom_percent/0.33f;
		if (alpha > 1.0f) alpha = 1.0f;
		_buds.Render(Batch, X, Y, alpha);
		
		//render the bloom layer
		alpha = (_bloom_percent-0.33f)/0.33f;
		if (alpha > 1.0f) alpha = 1.0f;
		if (alpha < 0.0f) alpha = 0.0f;
		_bloom.Render(Batch, X, Y, alpha);
		
		//render the leaves layer
		alpha = (_bloom_percent-0.66f)/0.33f;
		if (alpha > 1.0f) alpha = 1.0f;
		if (alpha < 0.0f) alpha = 0.0f;
		_leaves.Render(Batch, X, Y, alpha);
		
	}
}
