package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class AnimImage 
{
	public float _time;
	private int _rows;
	private int _columns;
	private int _width;
	private int _height;
	
	private Texture _spritesheet;
	private Animation[] _animations;
	private TextureRegion _currentframe;
	
	public AnimImage(String SpriteSheet, int Rows, int Columns, int AnimCount)
	{
		//load assets
		_spritesheet = new Texture(Gdx.files.internal(SpriteSheet));
		_animations = new Animation[AnimCount];
		
		//set data needed for the animation
		_rows = Rows;
		_columns = Columns;
		_width = _spritesheet.getWidth()/_columns;
		_height = _spritesheet.getHeight()/_rows;
	}
	
	public void NewAnimation(int Index, int FrameCount, int Start, int End, float TimeStep)
	{
		TextureRegion[] frames = new TextureRegion[FrameCount];
		TextureRegion[][] tmp = TextureRegion.split(_spritesheet, _width, _height);
		
		int k = 0;
		for (int i=Start; i<End+1; i++)
		{
			int row = i/_columns;
			frames[k++] = tmp[row][i - row*_columns];
		}
		
		_animations[Index] = new Animation(TimeStep, frames);
		_time = 0.0f;
	}
	
	public void Render(SpriteBatch Batch, int Index, Vector2 Coords)
	{
		_time += Gdx.graphics.getDeltaTime();
		_currentframe = _animations[Index].getKeyFrame(_time, true);
		
		Batch.draw(_currentframe, Coords.x, Coords.y);
	}
	
	public void Release()
	{
		_spritesheet.dispose();
	}
}
