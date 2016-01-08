package pt.iul.poo.games.systems;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

public class SoundLoader {

	// Música do main-menu
	private static String m_m_path = "/sounds/main_menu/main_track.wav";
	public static SoundLoader menuClip = new SoundLoader(m_m_path);

	// Som dos botões
	private static String m_b_path = "/sounds/main_menu/buttons/button_track.wav";
	public static SoundLoader butClip = new SoundLoader(m_b_path);

	private static String track1_p = "/music/track_1.wav";
	public static SoundLoader track1 = new SoundLoader(track1_p);

	private static String track2_p = "/music/track_2.wav";
	public static SoundLoader track2 = new SoundLoader(track2_p);

	private static String track3_p = "/music/track_3.wav";
	public static SoundLoader track3 = new SoundLoader(track3_p);

	private static String track4_p = "/music/track_4.wav";
	public static SoundLoader track4 = new SoundLoader(track4_p);

	private static String track5_p = "/music/track_5.wav";
	public static SoundLoader track5 = new SoundLoader(track5_p);

	private static String track6_p = "/music/track_6.wav";
	public static SoundLoader track6 = new SoundLoader(track6_p);

	private Clip clip;
	private BooleanControl mute;
	private boolean running = false;
	private String path;

	public SoundLoader(String path) {
		this.path = path;
		loadClip(path);
	}

	private void loadClip(String path) {

		try {
			AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResource(path));
			clip = AudioSystem.getClip();
			clip.open(in);

			mute = (BooleanControl) clip.getControl(BooleanControl.Type.MUTE);

		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			JOptionPane.showMessageDialog(null, "Erro - O ficheiro: " + path + " é inexistente ou está danificado.", "Erro!", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void start(boolean loop) {
		if (clip == null)
			JOptionPane.showMessageDialog(null, "Erro - O ficheiro: " + path + " é inexistente ou está danificado.", "Erro!", JOptionPane.ERROR_MESSAGE);

		else if (!running) {
			clip.setMicrosecondPosition(0);
			clip.setFramePosition(0);
			clip.start();
			if (loop)
				clip.loop(Clip.LOOP_CONTINUOUSLY);

			running = true;
		}
	}

	public void mute() {
		mute.setValue(true);
	}

	public void unmute() {
		mute.setValue(false);
	}

	public void stop() {
		if (running) {
			clip.stop();
			running = false;
		}
	}

	public boolean isRunning() {
		if (clip.isRunning())
			return true;
		else
			return false;
	}
	
	public String getTitle(){
		return path;
	}

}
