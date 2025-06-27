package com.playlistversionuno;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.playlistversionuno.controllers.PlayListController;
import com.playlistversionuno.modelos.Cancion;
import com.playlistversionuno.modelos.DoublyLinkedList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class SecondaryController {
    @FXML
    private ListView<String> listCanciones;
    @FXML
    private TextField textIdAgregar;
    @FXML
    private TextField textIdEliminar;
    @FXML
    private TextField nombreCancionField;
    @FXML
    private Button btnRutaImagen;
    @FXML
    private Button btnRutaCancion;
    
    private String ultimaRutaCancion;
    private String ultimaRutaImagen;

    private PlayListController playlistController = new PlayListController();
    private DoublyLinkedList<Cancion> listaCanciones = App.getListaCancionesGlobal();
    @FXML
    private void initialize() {
        if (listaCanciones != null) {
            actualizarListaCanciones();
        } else {
            mostrarAlerta("Error", "No se pudo cargar la lista de canciones");
        }
    }

    private void actualizarListaCanciones() {
        listCanciones.getItems().clear();
        if (listaCanciones != null && !listaCanciones.isEmpty()) {
            int index = 1;
            for (Cancion cancion : listaCanciones) {
                if (cancion != null && cancion.getTitulo() != null) {
                    String nombreLimpio = obtenerNombreArchivo(cancion.getTitulo());
                    listCanciones.getItems().add(index++ + ". " + nombreLimpio);
                }
            }
        }
    }

    private String obtenerNombreArchivo(String ruta) {
        return (ruta == null) ? "Desconocido" : 
               ruta.substring(ruta.lastIndexOf("/") + 1);
    }


  @FXML
private void agregarCancion() {
        // Validaciones básicas
        if (nombreCancionField.getText().isEmpty() || 
            ultimaRutaCancion == null || 
            ultimaRutaImagen == null) {
            mostrarAlerta("Error", "Complete todos los campos");
            return;
        }

        // Crear canción usando el constructor original
        Cancion nueva = new Cancion(
            0,  // ID se generará automáticamente en la BD
            nombreCancionField.getText(),  // Nombre exacto del campo
            "recursos/" + ultimaRutaImagen // Ruta de la imagen (se guarda en 'foto')
        );

        // Guardar en base de datos
        PlayListController controller = new PlayListController();
        int resultado = controller.Create(nueva);
        
        if (resultado > 0) {
            // Actualizar la lista global
            App.actualizarListaGlobal();
            
            // Actualizar la lista visual
            listaCanciones = App.getListaCancionesGlobal();
            actualizarListaCanciones();
            
            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "Canción agregada correctamente", Alert.AlertType.INFORMATION);
            
            // Limpiar campos para nueva entrada
            limpiarCampos();
        } else {
            mostrarAlerta("Error", "No se pudo guardar en la base de datos");
        }
        
}

// Método auxiliar para limpiar campos
private void limpiarCampos() {
    nombreCancionField.clear();
    textIdAgregar.clear();
    ultimaRutaCancion = null;
    ultimaRutaImagen = null;
}


private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
    Alert alert = new Alert(tipo);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(mensaje);
    alert.showAndWait();
}





@FXML
private void eliminarCancion() {
    try {
        int indice = Integer.parseInt(textIdEliminar.getText()) - 1;
        if (indice < 0 || indice >= listaCanciones.size()) {
            mostrarAlerta("Error", "Índice inválido");
            return;
        }

        Cancion cancionAEliminar = listaCanciones.get(indice);
        
        // 1. Eliminar de la base de datos
        int resultado = playlistController.delete(cancionAEliminar.getCodigo());
        
        if (resultado > 0) {
            // 2. Eliminar archivos físicos
            eliminarArchivoFisico(cancionAEliminar.getTitulo());  // Audio
            eliminarArchivoFisico(cancionAEliminar.getFoto());    // Imagen
            
            // 3. Actualizar lista global
            App.actualizarListaGlobal();
            
            // 4. Actualizar lista local
            listaCanciones = App.getListaCancionesGlobal();
            actualizarListaCanciones();
            
            mostrarAlerta("Éxito", "Canción eliminada correctamente", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo eliminar de la base de datos");
        }
    } catch (Exception e) {
        mostrarAlerta("Error", "Error al eliminar: " + e.getMessage());
    }
}

private void eliminarArchivoFisico(String rutaRelativa) {
    if (rutaRelativa == null || rutaRelativa.isEmpty()) {
        System.out.println("Ruta vacía - no se puede eliminar");
        return;
    }

    // 1. Normalizar el nombre del archivo (eliminar "recursos/" y corregir extensiones)
    String nombreArchivo = rutaRelativa.replace("recursos/", "")
                                      .replace(".jpg.png", ".jpg")
                                      .replace(".png.jpg", ".jpg");

    System.out.println("Intentando eliminar: " + nombreArchivo);

    // 2. Definir todas las posibles ubicaciones donde podría estar el archivo
    String[] posiblesUbicaciones = {
        "src/main/resources/recursos/" + nombreArchivo,
        "target/classes/recursos/" + nombreArchivo,
        "recursos/" + nombreArchivo,
        System.getProperty("user.dir") + "/target/classes/recursos/" + nombreArchivo
    };

    // 3. Intentar eliminar en todas las ubicaciones posibles
    boolean eliminado = false;
    for (String ubicacion : posiblesUbicaciones) {
        File archivo = new File(ubicacion);
        if (archivo.exists()) {
            System.out.println("Encontrado en: " + ubicacion);
            // Intentar liberar recursos primero
            System.gc();
            try {
                Thread.sleep(100); // Pequeña pausa
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            if (archivo.delete()) {
                System.out.println("¡Archivo eliminado con éxito!");
                eliminado = true;
                break;
            } else {
                System.err.println("Fallo al eliminar: " + ubicacion);
            }
        }
    }

    if (!eliminado) {
        System.err.println("No se pudo eliminar el archivo en ninguna ubicación");
        
        // 4. Búsqueda de emergencia - listar archivos similares
        System.out.println("\nBuscando archivos similares en recursos...");
        File directorio = new File("src/main/resources/recursos");
        File[] archivos = directorio.listFiles((dir, name) -> 
            name.toLowerCase().contains("reencuentro"));
        
        if (archivos != null && archivos.length > 0) {
            System.out.println("Archivos similares encontrados:");
            for (File f : archivos) {
                System.out.println("- " + f.getName() + " (" + f.length() + " bytes)");
            }
        } else {
            System.out.println("No se encontraron archivos similares");
        }
    }
}



 @FXML
private void seleccionarImagen() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Seleccionar Imagen");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.jpg", "*.png")
    );

    File archivo = fileChooser.showOpenDialog(null);
    if (archivo != null) {
        try {
            // Obtener nombre y asegurar extensión .jpg
            String nombreArchivo = archivo.getName();
            if (nombreArchivo.toLowerCase().endsWith(".png")) {
                nombreArchivo = nombreArchivo.substring(0, nombreArchivo.length() - 4) + ".jpg";
            }
            
            // Eliminar espacios y caracteres especiales
            nombreArchivo = nombreArchivo.replace(" ", "_")
                                        .replaceAll("[^a-zA-Z0-9._-]", "");
            
            // Copiar a recursos
            Path destino = Paths.get("src", "main", "resources", "recursos", nombreArchivo);
            Files.copy(archivo.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
            
            // Guardar solo el nombre del archivo (sin "recursos/")
            ultimaRutaImagen = nombreArchivo;
            
            System.out.println("Imagen guardada como: " + nombreArchivo);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo copiar la imagen: " + e.getMessage());
        }
    }
}

    @FXML
    private void seleccionarCancion() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio", "*.mp3"));
        File archivo = fileChooser.showOpenDialog(null);
        
        if (archivo != null) {
            try {
                // 1. Conservar el nombre exacto del archivo
                String nombreArchivo = archivo.getName();
                
                // 2. Copiar el archivo tal cual
                File destino = new File("src/main/resources/recursos/" + nombreArchivo);
                Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // 3. Guardar la ruta exacta
                ultimaRutaCancion = nombreArchivo;
                
                // 4. Mostrar nombre sugerido (opcional)
                if (nombreCancionField.getText().isEmpty()) {
                    nombreCancionField.setText(
                        nombreArchivo.substring(0, nombreArchivo.lastIndexOf('.'))
                    );
                }
                
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo copiar el archivo: " + e.getMessage());
            }
        }
    }





    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    private String obtenerNombreBase(String rutaArchivo) {
    if (rutaArchivo == null || rutaArchivo.isEmpty()) return "";
    
    // Ejemplo: convierte "recursos/mi_cancion.mp3" a "mi_cancion"
    String nombreArchivo = rutaArchivo.substring(rutaArchivo.lastIndexOf("/") + 1);
    return nombreArchivo.split("\\.")[0]; // Elimina la extensión
}





}