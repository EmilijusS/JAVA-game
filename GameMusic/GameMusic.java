//=========================================================================
//
//   Kurso "Objektinis programavimas" (PS) 2015/16 m.m. pavasario (2) sem.
//   Projektinis darbas. Variantas Nr. 6
//   Darbą atliko: Emilijus Stankus
//
//=========================================================================

package GameMusic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GameMusic implements Runnable {
	private final String STR_FILE_NAME = "music.wav";

	public void run() {
		try{
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(GameMusic.class.getResource(STR_FILE_NAME));
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		clip.loop(1000);
		clip.start();
		} catch (Exception e){}
	}
}