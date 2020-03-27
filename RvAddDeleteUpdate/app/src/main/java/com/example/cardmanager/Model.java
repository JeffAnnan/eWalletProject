package com.example.cardmanager;

public class Model {
    //classe qui modelise une carte pour la mettre dans le RecyclerView
    public int id;
    public String name;
    public String logoName;
    public Integer bddIdCard;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoName() {
        return logoName;
    }
    public void setLogoName(String logoName) {
        this.logoName = logoName;
    }

    public Integer getBddIdCard() {
        return bddIdCard;
    }
    public void setBddIdCard(Integer bddIdCard) {
        this.bddIdCard = bddIdCard;
    }
}
