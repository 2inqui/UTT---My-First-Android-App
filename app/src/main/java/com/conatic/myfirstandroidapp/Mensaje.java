package com.conatic.myfirstandroidapp;

/**
 * Created by inqui on 5/21/15.
 */
public class Mensaje {
    String usuario;
    String mensaje;

    public Mensaje(String usuario, String mensaje) {
        this.usuario = usuario;
        this.mensaje = mensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return usuario + " dijo: " + mensaje;
    }
}
