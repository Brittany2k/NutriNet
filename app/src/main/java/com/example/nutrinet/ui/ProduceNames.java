package com.example.nutrinet.ui;

public class ProduceNames {
    private String produceName;

    public ProduceNames() {
    }
    public String getProduceName() {
        return this.produceName;
    }
    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    private String id;
    private String image;
    private String password;
    private String info;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getNutrition(){return info;}
    public void setNutrition(String info){this.info = info;}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
