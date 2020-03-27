package com.example.cardmanager;

public class Card {
    //la classe d'une carte pour manipuler des objets
    private Integer id;
    private String barCodeNumber;
    private String name;
    private String adrLogo; //nom du logo de l'enseigne drawable

    public Card(){};

    public Card(String barCodeNumber, String name, String adrLogo){
        this.barCodeNumber = barCodeNumber;
        this.name = name;
        this.adrLogo=adrLogo;
    }
    public Integer getIdCard() {
        return this.id;
    }

    public void setIdCard(Integer id) {
        this.id = id;
    }

    public String getBarcodeNumber() {
        return this.barCodeNumber;
    }

    public void setBarcodeNumber(String barCodeNumber) {
        this.barCodeNumber = barCodeNumber;
    }

    public String getCardName() {
        return this.name;
    }

    public void setCardName(String name) {
        this.name = name;
    }

    public String getAdrLogo() {
        return this.adrLogo;
    }

    public void setAdrLogo(String adrLogo) {
        this.adrLogo = adrLogo;
    }

    public String toString(){
        return "id :"+this.id+" barCodeNumber :"+this.barCodeNumber+" name :"+this.name+" adrLogo :"+this.adrLogo;
    }
}