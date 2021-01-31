import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

// https://docs.oracle.com/javase/tutorial/sound/playing.html
// https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
public class SoundsMnager {

    public SoundsMnager() {

    }

    public void playSound() {
        try {
            Clip sound = AudioSystem.getClip();
            sound.open(
                AudioSystem.getAudioInputStream(
                    getClass().getResource("sounds" + File.separator + "arcade-music-loop.wav")
                )
            );
            sound.start();

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("ERROR: File not supported");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("ERROR: Cannot reserve system resources");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("ERROR: File cannot be played");
            ex.printStackTrace();
        }
    }
}
