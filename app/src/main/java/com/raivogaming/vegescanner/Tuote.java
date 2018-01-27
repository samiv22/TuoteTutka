package com.raivogaming.vegescanner;

public class Tuote {

    String nimi, kuvaus;
    long viivakoodi;
    float arvostelut, arvosteluKerrat;
    boolean vegaani, gluteeniton;

    public Tuote(){

    }

    public Tuote(String nimi, String kuvaus, long viivakoodi, float arvostelut, float arvosteluKerrat, boolean vegaani, boolean gluteeniton){
        this.nimi = nimi;
        this.kuvaus = kuvaus;
        this.viivakoodi = viivakoodi;
        this.arvostelut = arvostelut;
        this.arvosteluKerrat = arvosteluKerrat;
        this.vegaani = vegaani;
        this.gluteeniton = gluteeniton;
    }

    public float getArvosteluKerrat() {
        return arvosteluKerrat;
    }

    public void setArvosteluKerrat(float arvosteluKerrat) {
        this.arvosteluKerrat = arvosteluKerrat;
    }

    public long getViivakoodi() {
        return viivakoodi;
    }

    public void setViivakoodi(long viivakoodi) {
        this.viivakoodi = viivakoodi;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public float getArvostelut() {
        return arvostelut;
    }

    public void setArvostelut(float arvostelu) {
        this.arvostelut = arvostelu;
    }

    public boolean isVegaani() {
        return vegaani;
    }

    public void setVegaani(boolean vegaani) {
        this.vegaani = vegaani;
    }

    public boolean isGluteeniton() {
        return gluteeniton;
    }

    public void setGluteeniton(boolean gluteeniton) {
        this.gluteeniton = gluteeniton;
    }
}
