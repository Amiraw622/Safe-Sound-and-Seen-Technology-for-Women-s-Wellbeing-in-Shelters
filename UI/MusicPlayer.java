package UI;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private final String[] playlist;
    private int currentIndex = 0;
    private Clip clip;
    private boolean manualStop = false;
    private boolean paused = false;

    public MusicPlayer(String[] playlist) {
        this.playlist = playlist;
    }

    public void playCurrent() {
    System.out.println("=== playCurrent CALLED ===");
    new Exception().printStackTrace();

    stopCurrentClipOnly();
    paused = false;

    try {
        File file = new File(playlist[currentIndex]);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            return;
        }

        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);

        manualStop = false;

        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                if (!manualStop && !paused
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

    public void pause() {
        if (clip != null && clip.isRunning()) {
            manualStop = true;
            paused = true;
            clip.stop();
            System.out.println("[MusicPlayer] paused = true");
        }
    }

    public void resume() {
        if (clip != null && paused) {
            manualStop = false;
            paused = false;
            clip.start();
            System.out.println("[MusicPlayer] resumed, paused = false");
        }
    }

    public void stop() {
        stopCurrentClipOnly();
        paused = false;
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

    public boolean isPaused() {
        return paused;
    }

    public String getCurrentSongName() {
        return new File(playlist[currentIndex]).getName();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}