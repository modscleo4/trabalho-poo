package Engine;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;

public class Sound {
    private Clip c;
    private boolean loop;
    private long time;

    public Sound(AudioInputStream ais, int audioMixer, boolean loop) throws LineUnavailableException, IOException {
        this.loop = loop;
        this.c = AudioSystem.getClip(AudioSystem.getMixerInfo()[audioMixer]);

        this.c.open(ais);

        FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(-50f);
    }

    public void play() {
        if (this.time != 0) {
            this.c.setMicrosecondPosition(this.time);
            this.time = 0;
        }

        this.c.start();

        if (this.loop) {
            this.c.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void pause() {
        if (this.c.isRunning()) {
            this.time = this.c.getMicrosecondPosition();
            this.c.stop();
        }
    }

    public void stop() {
        this.c.stop();
    }
}
