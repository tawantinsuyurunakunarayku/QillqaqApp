package com.lolisapp.traductorquechua.bean;

/**
 * Created by USUARIO on 07/12/2017.
 */

public class Pais {
    private Long idPais;
    private String nombre;

    public Long getIdPais() {
        return idPais;
    }

    public void setIdPais(Long idPais) {
        this.idPais = idPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
