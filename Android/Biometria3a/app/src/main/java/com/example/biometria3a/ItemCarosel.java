package com.example.biometria3a;

public class ItemCarosel {
    private int imageResId;
    private String text;
    private String title; // Nuevo atributo para el título

    // Constructor modificado para incluir el título
    public ItemCarosel(int imageResId, String title, String text) {
        this.imageResId = imageResId;
        this.title = title; // Inicializar el título
        this.text = text;   // Inicializar el texto
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title; // Getter para el título
    }


}

