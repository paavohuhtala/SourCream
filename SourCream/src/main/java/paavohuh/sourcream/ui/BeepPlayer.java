
package paavohuh.sourcream.ui;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Mixer;
import paavohuh.sourcream.Resource;
import paavohuh.sourcream.configuration.Configuration;

/**
 * Plays a looping, single cycle square wave beep.
 */
public class BeepPlayer {
    private final Configuration config;
    private AudioInputStream stream;
    private Clip currentClip;
    
    /**
     * Creates a new beep player.
     * @param config The configuration.
     */
    public BeepPlayer(Configuration config) {
        this.config = config;
        
        try {
            InputStream beepStream = Resource.class.getResourceAsStream("/beep.wav");
            InputStream bufferedBeep = new BufferedInputStream(beepStream);
            stream = AudioSystem.getAudioInputStream(bufferedBeep);
            currentClip = AudioSystem.getClip();
            currentClip.open(stream);
            
            FloatControl volume = (FloatControl) currentClip.getControl(FloatControl.Type.MASTER_GAIN);
            volume.setValue(-10.0f);
            
        } catch (Exception ex) {
            System.out.println("Creating BeepPlayer failed.");
            System.out.println(ex);
        }
    }
    
    /**
     * Starts the beep. If the sound is already playing or sound is disabled
     * in the configuration, does nothing.
     */
    public void startBeep() {
    
        if (!config.getEmulatorConfig().getSound().isEnabled()) {
            return;
        }
        
        if (stream != null) {
            try {
                if (currentClip != null) {
                    currentClip.stop();
                }
                
                if (currentClip != null && currentClip.isActive()) {
                    return;
                }
                
                stream.reset();
                currentClip.loop(Clip.LOOP_CONTINUOUSLY);
                currentClip.start();
            } catch (Exception ex) { }
        }
    }
    
    /**
     * Stops the beep. If the sound is not playng, does nothing.
     */
    public void endBeep() {
        if (currentClip != null) {
            currentClip.stop();
        }
    }

}
