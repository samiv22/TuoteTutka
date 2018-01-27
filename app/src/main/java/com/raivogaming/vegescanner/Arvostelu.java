package com.raivogaming.vegescanner;


public class Arvostelu {

    private String arvostelija, arvostelu, uid;
    private float arvosana;
    private boolean suosittelee;

    public Arvostelu(){

    }

    public Arvostelu(String arvostelija, String arvostelu, float arvosana, boolean suosittelee){
        this.arvostelija = arvostelija;
        this.arvostelu = arvostelu;
        this.arvosana = arvosana;
        this.suosittelee = suosittelee;
    }

    public Arvostelu(String arvostelija, String arvostelu, String uid, float arvosana, boolean suosittelee){
        this.arvostelija = arvostelija;
        this.arvostelu = arvostelu;
        this.uid = uid;
        this.arvosana = arvosana;
        this.suosittelee = suosittelee;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setArvosana(float arvosana) {
        this.arvosana = arvosana;
    }

    public float getArvosana(){
        return arvosana;
    }

    public boolean isSuosittelee() {
        return suosittelee;
    }

    public void setSuosittelee(boolean suosittelee) {
        this.suosittelee = suosittelee;
    }

    public String getArvostelija() {
        return arvostelija;
    }

    public void setArvostelija(String arvostelija) {
        this.arvostelija = arvostelija;
    }

    public String getArvostelu() {
        return arvostelu;
    }

    public void setArvostelu(String arvostelu) {
        this.arvostelu = arvostelu;
    }
}
