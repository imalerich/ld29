package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Image 
{
	private Texture _img;
	private TextureRegion _reg;
	private Vector2 _dim;
	
	public Image(String FileName, int Width, int Height)
	{
		//load assets
		_img = new Texture(Gdx.files.internal(FileName));
		_reg = new TextureRegion(_img, Width, Height);
		_dim = new Vector2(Width, Height);
	}
	
	public Vector2 GetDimmensions()
	{
		return _dim;
	}
	
	public void Render(SpriteBatch Batch, float X, float Y)
	{
		Batch.draw(_reg, X, Y);
	}
	
	public void Render(SpriteBatch Batch, float X, float Y, float Alpha)
	{
		//render with alpha
		Batch.setColor(1.0f, 1.0f, 1.0f, Alpha);
		Batch.draw(_reg, X, Y);
		Batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void Release()
	{
		_img.dispose();
	}
}
