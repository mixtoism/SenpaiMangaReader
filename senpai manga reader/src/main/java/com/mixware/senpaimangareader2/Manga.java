/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mixware.senpaimangareader2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author pargon
 */
public class Manga implements Serializable {
    String enlace;
    String nombre;
    ArrayList<Capitulo> caps;

    public Manga(String enlace, String nombre) {
        this.enlace = enlace;
        this.nombre = nombre;
        caps = new ArrayList<Capitulo> ();
    }

    @Override
    public String toString() {
        return "Manga{" + "enlace=" + enlace + ", nombre=" + nombre + '}';
    }
    
    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void addCapitulo(Capitulo c) {
        this.caps.add(c);
    }
    
    public ArrayList<Capitulo> getCapitulos() {
        return this.caps;
    }
     public void addCapitulo(ArrayList<Capitulo> c) {
        this.caps.addAll(c);
    }
    public int getNumCapitulos() {return this.caps.size();}
}
