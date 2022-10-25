package com.macro.lector.Entidades;

import java.util.ArrayList;

/**
 * Created by Fran on 12/09/2018.
 */

public class Detalle {
    public int id_visita;
    public String imagen;
    public String descripcion;

    public Detalle() {
    }

    public Detalle(int id_visita, String descripcion) {
        this.id_visita = id_visita;
        this.descripcion = descripcion;
    }

    public int getId_visita() {
        return id_visita;
    }

    public void setId_visita(int id_visita) {
        this.id_visita = id_visita;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    private static int lastContactId = 0;

    public static ArrayList<Detalle> createContactsList(int numContacts) {
        ArrayList<Detalle> contacts = new ArrayList<Detalle>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Detalle(i,"desc" + i));
        }

        return contacts;
    }
}
