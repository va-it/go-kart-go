import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

// https://docs.oracle.com/javase/tutorial/sound/playing.html
// https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
public class SoundsManager {

    private static final String CRASH = "crash";

    Clip sound;

    public SoundsManager(String soundToPlay) {
        try {
            sound = AudioSystem.getClip();
            sound.open(
                    AudioSystem.getAudioInputStream(
                            getClass().getResource("sounds" + File.separator + soundToPlay + ".wav")
                    )
            );
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

    public void playSound() {
        sound.start();
    }

    public void playSoundInLoop(){
        sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopSound() {
        sound.stop();
    }
}
