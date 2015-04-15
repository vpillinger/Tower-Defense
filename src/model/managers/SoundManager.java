package model.managers;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class SoundManager {
	private Music music;


	private Sound win;

	private Sound lose;

	private Sound ding;
	
	public SoundManager(){
		
		//playing background music
		try {
			music = new Music("res/A_Perfect_Circle_-_Judith.ogg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		music.setVolume(0.5f);

		music.loop();
	}
}
