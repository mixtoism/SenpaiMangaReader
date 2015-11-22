/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mixware.senpaireader.Model;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author pargon
 */
public class Capitulo implements Serializable, Comparator<Capitulo> {
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
    public int compare(Capitulo lhs, Capitulo rhs) {
        return new Integer(Integer.parseInt(rhs.getCapitulo())).
                compareTo(new Integer(Integer.parseInt(lhs.getCapitulo())));
    }

    @Override
    public boolean equals(Object o) {
        Capitulo c;
       c =  o instanceof Capitulo ? ((Capitulo) o) : null;
      return  c!= null /*&& c.enlace.equals(this.enlace)*/ && c.Capitulo.equals(this.Capitulo);
    }
}
