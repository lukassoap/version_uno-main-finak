package com.playlistversionuno;

// import com.mycompany.proyectoplaylist.controladores.PlayListController;
// import com.mycompany.proyectoplaylist.modelos.Cancion;
// import com.mycompany.proyectoplaylist.modelos.DoublyLinkedList;
// import com.mycompany.proyectoplaylist.modelos.SimpleAudioPlayer;

// TODO: Uncomment and fix the import paths below if these classes exist in your project structure
// import your.correct.package.PlayListController;
// import your.correct.package.Cancion;
// import your.correct.package.DoublyLinkedList;
// import your.correct.package.SimpleAudioPlayer;
import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;

import com.playlistversionuno.controllers.PlayListController;
import com.playlistversionuno.modelos.Cancion;
import com.playlistversionuno.modelos.DoublyLinkedList;
import com.playlistversionuno.modelos.SimpleAudioPlayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PrimaryController {
    
    // @FXML
    // Button btnpausa;
    // @FXML
    // Button btnplay;
    @FXML
    Button btnPlayPause;
    @FXML
    Button btnreverse;
    @FXML
    Button btnforward;
    @FXML
    ImageView fotoAlbum;
    @FXML
    Label textTitulo; // this is not used but could be used to show the current song or something
    
    private String ultimaImagenMostrada;
    PlayListController Playlist = new PlayListController();
    DoublyLinkedList<Cancion> lista = App.getListaCancionesGlobal(); // might have to do this outside
    //could use iterators for adding without having to redo the iterator but need to ask
    // does adding items to the queue also updates an iterator made before or not
    //ListIterator<Cancion> inter = lista.listIterator(); // beed to work on this, maybe adding a controller to separate
    SimpleAudioPlayer player = new SimpleAudioPlayer(lista); // i think it plays the first song
    ListIterator<Cancion> rotafoto = lista.listIterator();
    //REVISAR SI ESTO NO ES UN ITERADOR DOBLE DE LA MISMA COSA
    //need to find current song
    // could change the iterator in the music playlist
    // acutually nvm CHANGE THE MEDIA PLAYER TO TAKE ITERATOR WITH MUSIC CLASS OBJECTS TSO IT WORKS CORRECTLY AND VOILA
    // could also say fuck it and make a photo controller or
    //makre a dedicated thiung in the class for audio player but it would feel pointless
    //PROBANDO ESTO

@FXML
private void initialize() throws IOException {
    System.out.println("Inicializando PrimaryController...");

    if (!lista.isEmpty() && rotafoto.hasNext()) {
        Cancion primeraCancion = rotafoto.next();
        actualizarVista(primeraCancion);
        player.loadFirstSong();
        btnPlayPause.setDisable(false);
    } else {
        btnPlayPause.setDisable(true);
    }

    updatePlayPauseButton();
}

private void updatePlayPauseButton() {
    if (player.isPlaying()) {
        btnPlayPause.setText("►");  // PONER BOTÓN DE PLAY
    } else {
        btnPlayPause.setText("‖");   // PONER BOTÓN DE PAUSE
    }
}
    

    @FXML
    private void switchToSecondary() throws IOException {
        player.pause();
        updatePlayPauseButton();
        App.setRoot("secondary");
    }



   @FXML
private void siguiente() throws IOException {
    player.playNextSong();
    updatePlayPauseButton(); // Actualiza el texto del botón
    
    if (rotafoto.hasNext()) {
        actualizarVista(rotafoto.next());
    } else {
        rotafoto = lista.listIterator();
        if (rotafoto.hasNext()) {
            actualizarVista(rotafoto.next());
        }
    }
}
    

@FXML
private void reversa() throws IOException {
    player.playPreviousSong();
    updatePlayPauseButton(); // Actualiza el texto del botón
    
    Cancion actual = player.getCurrentSong();
    if (actual != null) {
        rotafoto = lista.listIterator();
        while (rotafoto.hasNext()) {
            if (rotafoto.next().equals(actual)) {
                break;
            }
        }
        actualizarVista(actual);
    }
}


@FXML
private void togglePlayPause() {
    try {
        // Verifica si no hay canción y no hay más canciones disponibles
        if (player.getCurrentSong() == null && !rotafoto.hasNext()) {
            btnPlayPause.setDisable(true);
            textTitulo.setText("🎵 No hay canciones disponibles");
            return;
        }

        // ✅ Solo reactivar si hay canción actual o próxima
        btnPlayPause.setDisable(false);

        if (player.isPlaying()) {
            player.pause();
        } else {
            // Si no hay canción actual, toma la siguiente del iterador
            if (player.getCurrentSong() == null && rotafoto.hasNext()) {
                Cancion siguiente = rotafoto.next();
                player.playSong(siguiente.getTitulo());
                actualizarVista(siguiente);
            } else {
                player.resumeAudio(); // 🔁 Tu método actual funciona perfecto
            }
        }

        updatePlayPauseButton();

    } catch (Exception e) {
        System.err.println("Error en togglePlayPause: " + e.getMessage());
        e.printStackTrace();
    }
}

    private void cargarImagenPorDefecto() {
        try {
            URL defaultImgUrl = getClass().getResource("/recursos/default.png");
            if (defaultImgUrl != null) {
                System.out.println("Cargando imagen por defecto");
                fotoAlbum.setImage(new Image(defaultImgUrl.toExternalForm()));
            } else {
                fotoAlbum.setImage(null); // Limpiar ImageView
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen por defecto: " + e.getMessage());
        }
    }

private void actualizarVista(Cancion cancion) {
    // Limpiar vista primero
    fotoAlbum.setImage(null);
    textTitulo.setText("");
    
    if (cancion == null) {
        cargarImagenPorDefecto();
        textTitulo.setText("No hay canción seleccionada");
        return;
    }

    // Actualizar título (versión optimizada)
    String nombreCancion = limpiarNombreCancion(cancion.getTitulo());
    textTitulo.setText(nombreCancion);
    
    // Cargar imagen (versión mejorada)
    cargarImagenCancion(cancion.getFoto());
}

private void cargarImagenCancion(String rutaImagen) {
    if (rutaImagen == null || rutaImagen.isEmpty()) {
        cargarImagenPorDefecto();
        return;
    }

    String nombreArchivo = normalizarRutaImagen(rutaImagen);
    System.out.println("Intentando cargar imagen: " + nombreArchivo);
    
    try {
        URL imageUrl = getClass().getResource("/recursos/" + nombreArchivo);
        if (imageUrl != null) {
            Image imagen = new Image(imageUrl.toExternalForm());
            fotoAlbum.setImage(imagen);
            System.out.println("Imagen cargada exitosamente desde: " + imageUrl);
        } else {
            System.err.println("No se encontró la imagen: " + nombreArchivo);
            cargarImagenPorDefecto();
        }
    } catch (Exception e) {
        System.err.println("Error al cargar imagen: " + e.getMessage());
        cargarImagenPorDefecto();
    }
}


private String limpiarNombreCancion(String nombreOriginal) {
    if (nombreOriginal == null) return "Sin título";
    
    // Eliminar rutas y extensiones
    return nombreOriginal
        .replace("recursos/", "")
        .replaceAll("\\.(mp3|wav|ogg)$", "")
        .replace("_", " ")
        .trim();
}
private String normalizarRutaImagen(String ruta) {
    if (ruta == null) return "";
    
    // Eliminar rutas duplicadas y corregir extensiones
    return ruta
        .replace("recursos/recursos/", "recursos/")
        .replace("recursos/", "")
        .replaceAll("\\.(jpg|png)\\.(jpg|png)$", ".$1");
}




}


