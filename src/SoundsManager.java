import javax.sound.sampled.*;
import java.io.IOException;

// https://docs.oracle.com/javase/tutorial/sound/playing.html
// https://www.codejava.net/coding/how-to-play-back-audio-in-java-with-examples
public class SoundsManager {

    private Clip sound;

    public SoundsManager(String soundToPlay) {
        try {
            sound = AudioSystem.getClip();
            sound.open(
                    AudioSystem.getAudioInputStream(
                            getClass().getResource(HelperClass.sounds + soundToPlay + ".wav")
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
        // LOOP_CONTINUOUSLY found here:
        // https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/Clip.html
        sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopSound() {
        sound.stop();
    }
}
