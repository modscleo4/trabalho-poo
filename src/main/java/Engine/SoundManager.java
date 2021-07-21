package Engine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundManager {
    private static final Map<String, AudioInputStream> sounds = new HashMap<>();

    public static List<String> filesInDirectory() {
        return ResourceLoader.filesInDirectory("sounds/");
    }

    public static void loadAll(Consumer<Integer> c) {
        List<String> files = filesInDirectory();
        int i = 1;
        for (String f : files) {
            c.accept(i++);
            try {
                BufferedInputStream s = new BufferedInputStream(ResourceLoader.loadFile(String.format("sounds/%s", f)));
                sounds.put(f, AudioSystem.getAudioInputStream(s));
            } catch (IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getSystemAudioOutputMixer() {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
        DataLine.Info dataLineInfo = new DataLine.Info(null, format);

        for (int i = 0; i < mixerInfo.length; i++) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo[i]);
            if (mixer.isLineSupported(dataLineInfo)) {
                return i;
            }
        }

        return 0;
    }

    public static Sound playSound(final String file) {
        return playSound(file, false);
    }

    public static Sound playSound(final String file, boolean loop) {
        try {
            int audioMixer = Settings.audioOutputMixer;
            if (audioMixer == -1) {
                audioMixer = SoundManager.getSystemAudioOutputMixer();
            }

            AudioInputStream ais;
            if (sounds.containsKey(file)) {
                ais = sounds.get(file);
            } else {
                BufferedInputStream s = new BufferedInputStream(
                        ResourceLoader.loadFile(String.format("sounds/%s.wav", file)));
                ais = AudioSystem.getAudioInputStream(s);
                sounds.put(file, ais);
            }

            Sound sound = new Sound(ais, audioMixer, loop);

            BufferedInputStream s = new BufferedInputStream(
                    ResourceLoader.loadFile(String.format("sounds/%s.wav", file)));
            ais = AudioSystem.getAudioInputStream(s);
            sounds.put(file, ais);

            sound.play();

            return sound;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.printStackTrace();
            return null;
        }
    }
}
