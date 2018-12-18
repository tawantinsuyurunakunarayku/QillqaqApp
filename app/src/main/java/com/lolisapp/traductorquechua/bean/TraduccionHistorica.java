package com.lolisapp.traductorquechua.bean;

/**
 * Created by USUARIO on 01/11/2017.
 */

public class TraduccionHistorica {


    private Integer id;

    private String original;
    private String traduccion;
    private Integer isFavorite;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTraduccion() {
        return traduccion;
    }

    public void setTraduccion(String traduccion) {
        this.traduccion = traduccion;
    }

    public Integer getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Integer favorite) {
        isFavorite = favorite;
    }
}
