/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mixware.senpaimangareader;

import java.io.Serializable;

/**
 *
 * @author pargon
 */
public class Capitulo implements Serializable {
    private String enlace;
    private String Capitulo;
    private boolean readed;

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getCapitulo() {
        return Capitulo;
    }

    public void setCapitulo(String Capitulo) {
        this.Capitulo = Capitulo;
    }

    public Capitulo(String enlace, String Capitulo) {
        this.enlace = enlace;
        this.Capitulo = Capitulo;
    }
    
     public Capitulo(String enlace) {
        this.enlace = enlace;
        String enlac [] = enlace.split("/");
        this.Capitulo = "Capítulo " + (enlac[enlac.length-1]);
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public boolean isReaded() {
        return readed;
    }

    @Override
    public boolean equals(Object o) {
        Capitulo c;
       c =  o instanceof Capitulo ? ((Capitulo) o) : null;
      return  c!= null && c.enlace.equals(this.enlace) && c.Capitulo.equals(this.Capitulo);
    }
}
