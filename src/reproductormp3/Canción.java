/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reproductormp3;

import java.io.File;

/**
 *
 * @author Yeray
 */
public class Canción {
    public String titulo;
    public String duracion;
    public String numero;
    public File cancion;
    

    public Canción(String titulo, String duracion,File cancion,String numero) {
        this.titulo = titulo;
        this.duracion = duracion;
        this.numero = numero;
        this.cancion = cancion;
        
    }
    public String getTitulo(){
        return titulo;
    }
    public String getDuracion(){
        
        return duracion;
    }
    public String getNumero(){
        return numero;
    }


       
}
