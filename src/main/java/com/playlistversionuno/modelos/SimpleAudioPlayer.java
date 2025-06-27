/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.playlistversionuno.modelos;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 *
 * @author yacog
 * This class is a simple audio player that can play, pause, resume, stop, and jump to specific times in songs.
 * It uses JavaFX's MediaPlayer to handle audio playback.
 */

public class SimpleAudioPlayer {
    private MediaPlayer mediaPlayer;
    private String currentSong;
    //private ListIterator<Cancion> iterator;// or could change this //  changing for cancion
    private List<Cancion> songs;
    private int currentIndex = -1;
    private boolean isManualStop = false;

    public SimpleAudioPlayer(DoublyLinkedList<Cancion> lista) {
        this.songs = new ArrayList<>();
        for (Cancion c : lista) {
            songs.add(c);
        }
        playNextSong(); // Start with the first song
    }
    
public void playSong(String filePath) {
    stop();
    
    // Normalizar rutas para recursos
    String normalizedPath = filePath.startsWith("/") ? filePath : "/" + filePath;
    URL resource = getClass().getResource(normalizedPath);
    
    if (resource == null) {
        System.out.println("❌ Resource not found: " + normalizedPath);
        return;
    }

    Media media = new Media(resource.toExternalForm());
    mediaPlayer = new MediaPlayer(media);
    currentSong = filePath;
    isManualStop = false;

    mediaPlayer.setOnReady(() -> {
        System.out.println("▶ Now playing: " + filePath);
        mediaPlayer.play();
    });

    mediaPlayer.setOnEndOfMedia(() -> {
        if (!isManualStop) {
            playNextSong();
        }
    });
}

    public void playNextSong() {
        if (songs.isEmpty()) {
            System.out.println("🎵 Playlist empty.");
            return;
        }
        currentIndex = (currentIndex + 1) % songs.size();
        Cancion next = songs.get(currentIndex);
        playSong(next.getTitulo());
    }



    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            System.out.println("Paused.");
        }
    }

    public void resumeAudio() {
        if (mediaPlayer == null && !songs.isEmpty()) {
            // Si no hay mediaPlayer pero hay canciones, carga la primera
            playSong(songs.get(0).getTitulo()); // Asumiendo que playSong existe
        } else if (mediaPlayer != null) {
            mediaPlayer.play();
            System.out.println("Play.");
        }
    }

    public void restart() {
        if (mediaPlayer != null) {
            mediaPlayer.seek(javafx.util.Duration.ZERO);
            mediaPlayer.play();
            System.out.println("🔁 Restarted.");
        }
    }

    public void stop() {
        isManualStop = true;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            System.out.println("⏹ Stopped.");
        }
    }

    public void jump(double seconds) {
        if (mediaPlayer != null) {
            javafx.util.Duration duration = javafx.util.Duration.seconds(seconds);
            mediaPlayer.seek(duration);
            System.out.println("⏩ Jumped to: " + seconds + " seconds.");
        }
    }

    public void gotoChoice(int c) {
        switch (c) {
            case 1: pause(); break;
            case 2: resumeAudio(); break;
            case 3: restart(); break;
            case 4: stop(); break;
            case 5:
                System.out.print("Enter time (seconds): ");
                java.util.Scanner sc = new java.util.Scanner(System.in);
                double time = sc.nextDouble();
                jump(time);
                break;
            case 6: playNextSong(); break;
            case 7: playPreviousSong(); break;
            default: System.out.println("❌ Invalid choice.");
        }
    }
    /**
     * Expose the playlist as a standard iterator. This does not affect the
     * internal playback index so the player remains in sync.
     */
    public ListIterator<Cancion> listIterator() {
        return songs.listIterator();
    }
    /**
     * Returns the currently playing song or null if none is loaded.
     */


    public void loadFirstSong() {
        if (!songs.isEmpty()) {
            currentIndex = 0;
            currentSong = songs.get(currentIndex).getTitulo();
            // No llames a playSong() aquí, solo prepara la canción
        }
    }

    public void playPreviousSong() {
        if (songs.isEmpty()) return;
        
        currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
        playSong(songs.get(currentIndex).getTitulo());
    }

    public Cancion getCurrentSong() {
        if (currentIndex >= 0 && currentIndex < songs.size()) {
            return songs.get(currentIndex);
        }
        return null;
    }

    // Asegúrate que este método existe
    public boolean isPlaying() {
        return mediaPlayer != null && 
               mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void loadFirstSongWithoutPlay() {
    if (!songs.isEmpty()) {
        currentIndex = 0;
        currentSong = songs.get(currentIndex).getTitulo();
        // Carga el Media pero NO llama a play()
        URL resource = getClass().getResource("/" + currentSong);
        if (resource != null) {
            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
        }
    }
}

public boolean isMediaLoaded() {
    return mediaPlayer != null;
}
    

}
