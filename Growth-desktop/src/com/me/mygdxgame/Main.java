package com.me.mygdxgame;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Bloom";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 600;
		cfg.resizable = false;
		cfg.x = -1;
		cfg.y = -1;
		cfg.addIcon("img/icon/icon_128.png", Files.FileType.Internal);
		cfg.addIcon("img/icon/icon_32.png", Files.FileType.Internal);
		cfg.addIcon("img/icon/icon_16.png", Files.FileType.Internal);
		
		new LwjglApplication(new MyGame(), cfg);
	}
}
