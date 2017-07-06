package reproductormp3;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author Yeray Molina García
 */
public class FXMLDocumentController implements Initializable,BasicPlayerListener {
    
    private Label label;
    @FXML
    private MenuBar menuPrincipal;
    @FXML
    private TableView<Canción> tablaCancion;
    @FXML
    private TableColumn<Canción,String> numeroCancion;
    @FXML
    private TableColumn<Canción,String> nombreCancion;
    @FXML
    private TableColumn<Canción,String> duracionCancion;
    @FXML
    private Button playBoton;
    @FXML
    private Button atrasBoton;
    @FXML
    private Button siguienteBoton;
    @FXML
    private Slider estadoActual;
    @FXML
    private Button añadirCancion;
    @FXML
    private Button eliminarCancion;
    @FXML
    private ImageView imagen1;
    @FXML
    private Text temporizador;
    @FXML
    private ImageView portada;
    @FXML
    private Text tituloCancion;
    @FXML
    private MenuItem añadirMenu;
    @FXML
    private MenuItem eliminarMenu;
    @FXML
    private Button muteBoton;
    @FXML
    private Button stopBoton;
    @FXML
    private ImageView imageMute;
    @FXML
    private MenuItem salirMenu;
    
    private ObservableList<Canción> myData=FXCollections.observableArrayList();
    private BasicPlayer basicPlayer;
    public double bytesLength;
    public int secondsSong,secondsActual;
    public boolean activado = false;
    public boolean muteado = false;
    public File cancionSeleccionada ;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        

        playBoton.setTooltip(new Tooltip("Iniciar cancíon."));
        siguienteBoton.setTooltip(new Tooltip("Siguiente cancíon."));
        atrasBoton.setTooltip(new Tooltip("Anterior cancíon."));
        añadirCancion.setTooltip(new Tooltip("Añadir cancíon."));
        eliminarCancion.setTooltip(new Tooltip("Eliminar cancíon."));
        
        
        numeroCancion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
        nombreCancion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        duracionCancion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracion()));
        tablaCancion.setItems(myData);
    }    

                
    @FXML
    private void añadirCancion(ActionEvent event){
         FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir fichero");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter(".wav"+"*,.mp3"+"*,.aac.", "*.wav", "*.mp3", "*.aac"));
        Window Window = null;
        File selectedFile = fileChooser.showOpenDialog(Window); 
        if (selectedFile != null) {
             try {
                 AudioFile f = AudioFileIO.read(selectedFile.getAbsoluteFile());
                 Tag tag = f.getTag();
                 AudioHeader a = f.getAudioHeader();
                 String duracion = "";
                 String minutos = String.valueOf(f.getAudioHeader().getTrackLength()/60);
                 String segundos = String.valueOf(f.getAudioHeader().getTrackLength()%60);
                 secondsSong = Integer.valueOf(minutos)*60 + Integer.valueOf(segundos);
                 estadoActual.setMax(f.getAudioHeader().getTrackLength());
                 if(Integer.valueOf(minutos)<10){
                     duracion += "0"+minutos+":";
                     
                 }else{
                     duracion += minutos + ":";
                 }
                 if(Integer.valueOf(segundos)<10){
                     duracion += "0"+segundos;
                 }else{
                     duracion += segundos;
                 }
                 
                 String titulo = tag.getFirst(FieldKey.COVER_ART);
                 if(titulo.equals("")){
                     titulo = selectedFile.getAbsoluteFile().getName();
                 }
                 myData.add(new Canción(titulo,duracion,selectedFile.getAbsoluteFile(),String.valueOf(myData.size()+1)));
                 tablaCancion.setItems(myData);
             } catch (CannotReadException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (IOException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (TagException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ReadOnlyFileException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             } catch (InvalidAudioFrameException ex) {
                 Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             }
           }
         
    }

    @FXML
    private void playCancion(ActionEvent event) throws BasicPlayerException {
    
    
    if(tablaCancion.getSelectionModel().isEmpty()){
        Alert alerta = new Alert(AlertType.WARNING);
        alerta.setTitle("Cuadro de Alerta");
        alerta.setHeaderText("Se ha detectado un problema.");
        alerta.setContentText("Debes seleccionar una canción.");
        alerta.showAndWait();
    }else{
        
        if(!activado && (cancionSeleccionada==null || cancionSeleccionada.equals(tablaCancion.getSelectionModel().getSelectedItem().cancion)) ){
            if(secondsActual == 0){
                basicPlayer = new BasicPlayer();
                imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/pause-circle-outline.png"));
                activado = true;
                basicPlayer.addBasicPlayerListener(this);
                File seleccion = tablaCancion.getSelectionModel().getSelectedItem().cancion;
                tituloCancion.setText(tablaCancion.getSelectionModel().getSelectedItem().titulo); 
                cancionSeleccionada = seleccion;
                basicPlayer.open(seleccion);
                basicPlayer.play();
                
            }else{     
            basicPlayer.resume();
            imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/pause-circle-outline.png"));
            activado = true;
            }
        }else{
            if(!cancionSeleccionada.equals(tablaCancion.getSelectionModel().getSelectedItem().cancion)){
                basicPlayer.stop();
                basicPlayer.open(tablaCancion.getSelectionModel().getSelectedItem().cancion);
                cancionSeleccionada = tablaCancion.getSelectionModel().getSelectedItem().cancion;
                tituloCancion.setText(tablaCancion.getSelectionModel().getSelectedItem().titulo); 
                basicPlayer.play();
                imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/pause-circle-outline.png"));
                activado = true;
            }else{
            imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/skip-next-circle-outline.png"));
            basicPlayer.pause();
            activado = false;
            }
        }
        }
    }
    @FXML
    private void siguienteCancion(ActionEvent event) throws BasicPlayerException {
        if(!tablaCancion.getSelectionModel().isEmpty()){
        basicPlayer.stop();
        activado = true;
        int act = tablaCancion.getSelectionModel().getSelectedIndex();
        tablaCancion.getSelectionModel().select((act+1)%myData.size());
        imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/pause-circle-outline.png"));
        File seleccion = tablaCancion.getSelectionModel().getSelectedItem().cancion;
        tituloCancion.setText(tablaCancion.getSelectionModel().getSelectedItem().titulo); 
        cancionSeleccionada = seleccion;
        basicPlayer.open(seleccion);
        basicPlayer.play();
    
        }
    }
    @FXML
    private void atrasCancion(ActionEvent event) throws BasicPlayerException {
        if(!tablaCancion.getSelectionModel().isEmpty()){
        int act = tablaCancion.getSelectionModel().getSelectedIndex();
        if((act - 1)< 0){
            act = (act-1) + myData.size();
        }else{
            act = act-1;
        }
        tablaCancion.getSelectionModel().select(act);
        basicPlayer.stop();
        activado = true;
        imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/pause-circle-outline.png"));
        File seleccion = tablaCancion.getSelectionModel().getSelectedItem().cancion;
        tituloCancion.setText(tablaCancion.getSelectionModel().getSelectedItem().titulo); 
        cancionSeleccionada = seleccion;
        basicPlayer.open(seleccion);
        basicPlayer.play();
        
        }
    }
    @FXML
    private void eliminarCancion(ActionEvent event) {
        myData.remove(tablaCancion.getSelectionModel().getSelectedItem());
        tablaCancion.setItems(myData);
    }

    @Override
    public void opened(Object arg0, Map arg1) {
        if (arg1.containsKey("audio.length.bytes")) {
            bytesLength = Double.parseDouble(arg1.get("audio.length.bytes").toString());
       }
    }

    @Override
    public void progress(int bytesread, long microseconds, byte[] pcmdata,  Map properties) {
        float progressUpdate = (float) (bytesread * 1.0f / bytesLength * 1.0f);
        int progressNow = (int) (bytesLength * progressUpdate);
        int segundos = (int) (microseconds / 1000000);
        estadoActual.setValue(segundos);
        estadoActual.setMax(secondsSong-1);
        secondsActual = segundos;
        String minutos = "";
        if(segundos == secondsSong-1 || segundos == secondsSong){
            imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/skip-next-circle-outline.png"));
        }
        if(segundos< 60){                   
            if(segundos < 10){
                minutos = "00" + ":" + "0"+segundos;
            }else{
                minutos = "00" + ":" + segundos;
            }
        }else{
            if(segundos/60 < 10){
                if(segundos%60<10){
                    minutos = "0"+segundos/60+":"+"0"+segundos%60;
                }else{
                minutos = "0" + segundos/60 +":"+ segundos%60;
                }
            }else{
                if(segundos%60<10){
                    minutos = "0"+segundos/60+":"+"0"+segundos%60;
                }else{
                    minutos = segundos/60 +":"+ segundos%60;
                }
            }
        }
        temporizador.setText(minutos);
    }

    @Override
    public void stateUpdated(BasicPlayerEvent bpe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setController(BasicController bc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @FXML
    private void mute(ActionEvent event) throws BasicPlayerException {
        if(!muteado){
            muteado = true;
            Image imagenMute = new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/volume-off.png");
            imageMute.setImage(imagenMute);
            basicPlayer.setGain(0.0);
        }else{
            basicPlayer.setGain(1.0);
            Image imagenActivo = new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/volume-high.png");
            imageMute.setImage(imagenActivo);
            muteado = false;
        }
            
    }

    @FXML
    private void stop(ActionEvent event) throws BasicPlayerException {
        basicPlayer.stop();
        activado = false;
        estadoActual.setValue(0.0);
        cancionSeleccionada = null;
        secondsActual = 0;
        temporizador.setText("00:00");
        imagen1.setImage(new Image("file:/C:/Users/Yeray/Desktop/ReproductorMP3/ReproductorMP3/src/media/skip-next-circle-outline.png"));
    }

    @FXML
    private void cerrarPrograma(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void abrirInformacion(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reproductormp3/InformacionFXML.fxml"));
        Parent root = (Parent) loader.load();
            
        Scene scene = new Scene(root);
        primaryStage.setTitle("Información");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    
}
  

