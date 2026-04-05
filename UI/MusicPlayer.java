package UI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private final String[] playlist;
    private int currentIndex = 0;
    private Clip clip;
    private boolean manualStop = false;

    public MusicPlayer(String[] playlist) {
        this.playlist = playlist;
    }

    public void playCurrent() {
        stopCurrentClipOnly();

        try {
            File file = new File(playlist[currentIndex]);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            manualStop = false;
            System.out.println("Path: " + file.getAbsolutePath());
            System.out.println("Exists: " + file.exists());
            System.out.println("Format: " + audioStream.getFormat());
            
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            System.out.println("Playing: " + clip.isRunning());

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    if (!manualStop
                            && clip != null
                            && clip.getMicrosecondPosition() >= clip.getMicrosecondLength()) {
                        nextSong();
                    }
                }
            });

            clip.setFramePosition(0);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Cannot play: " + playlist[currentIndex]);
        }
    }

    private void stopCurrentClipOnly() {
        if (clip != null) {
            manualStop = true;
            clip.stop();
            clip.close();
            clip = null;
        }
    }

    public void stop() {
        stopCurrentClipOnly();
    }

    public void nextSong() {
        currentIndex = (currentIndex + 1) % playlist.length;
        playCurrent();
    }

    public void previousSong() {
        currentIndex = (currentIndex - 1 + playlist.length) % playlist.length;
        playCurrent();
    }

    public void replay() {
        playCurrent();
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    public String getCurrentSongName() {
        return new File(playlist[currentIndex]).getName();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}