import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    Clip clip;
    AudioInputStream audioStream;

    public Sound(File audioFile)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        audioStream = AudioSystem.getAudioInputStream(audioFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);

    }

    public void playfromStart() {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pause() {
        clip.stop();
    }

    public void resume() {
        clip.start();
    }

    public void stop() {
        clip.stop();
        clip.setFramePosition(0);
    }

    public void close() {
        clip.close();
    }

    public void setVolume(float volume) {
        clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    public void setCurrentPostion(int position) {
        clip.setFramePosition(position);
    }

}
