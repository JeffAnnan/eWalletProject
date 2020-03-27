package com.example.cardupdate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class CardBDD {
    //classe qui gere l'insertion, la suppression, la modification et la recuperation de cartes dans la Base de Donnees
    private static final int VERSION_BDD=1;
    public static final String DATABASE_NAME = "card.db";

    public static final String TABLE_NAME="card_table";
    public static final String COL_IDCARD = "IDCARD";
    public static final int NUM_COL_IDCARD = 0;
    public static final String COL_CARDNAME = "CARDNAME";
    public static final int NUM_COL_CARDNAME = 1;
    public static final String COL_BARCODENUMBER = "BARCODENUMBER";
    public static final int NUM_COL_BARCODENUMBER = 2;
    public static final String COL_ADRLOGO = "ADRLOGO";
    public static final int NUM_COL_ADRLOGO = 3;

    private SQLiteDatabase db;
    private  SQLiteDataBaseHelper databaseSQLite;

    public CardBDD(Context context){
        //On crée la BDD et sa table
        databaseSQLite = new SQLiteDataBaseHelper(context, DATABASE_NAME, null, VERSION_BDD);
    }

    public void open(){
        //on ouvre la BDD en écriture
        db = databaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        db.close();
    }

    public SQLiteDatabase getDb(){
        return db;
    }

    public long insertCard(Card card){
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues values = new ContentValues();
        //on lui ajoute une valeur associée à une clé
        values.put(COL_CARDNAME, card.getCardName());
        values.put(COL_BARCODENUMBER, card.getBarcodeNumber());
        values.put(COL_ADRLOGO, card.getAdrLogo());
        //on insère l'objet dans la BDD via le ContentValues
        return db.insert(TABLE_NAME,null,values);
    }

    public int updateCard(int id, Card card) {
        //La mise à jour d'une carte
        //on précise quelle carte on doit mettre à jour grâce à l'ID
        ContentValues values = new ContentValues();
        values.put(COL_CARDNAME, card.getCardName());
        values.put(COL_BARCODENUMBER, card.getBarcodeNumber());
        values.put(COL_ADRLOGO, card.getAdrLogo());
        return db.update(TABLE_NAME,values,COL_IDCARD+" = "+id,null);
    }

    //methode pour selectionner le dernier ID insere dans la base, un lastInsertId
    public int getHighestID() {
        //on fait la requete
        String selectQuery = "SELECT * FROM card_table WHERE IDCARD = (SELECT MAX(IDCARD)  FROM card_table) ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        int ID = cursor.getInt(0);
        cursor.close();
        return ID;
    }

    public int removeCardWithID(int id){
        //Suppression d'une Carte de la BDD grâce à l'ID
        return db.delete(TABLE_NAME, COL_IDCARD+" = "+id,null);
    }

    public Card getCardWithId(int bddIdCard){
        //On récupère dans un Cursor les valeurs correspondant à une carte contenue dans la BDD
        //(ici on sélectionne la carte grâce à son ID en BDD)
        Cursor c = db.query(TABLE_NAME, new String[] {COL_IDCARD, COL_CARDNAME, COL_BARCODENUMBER,COL_ADRLOGO},
                COL_IDCARD+" LIKE \'"+bddIdCard+"\'",null,null,null,null);
        return cursorToCard(c);
    }

    //Cette méthode permet de convertir un cursor en une carte
    private Card cursorToCard(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if(c.getCount()==0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé une carte
        Card card = new Card();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        card.setIdCard(c.getInt(NUM_COL_IDCARD));
        card.setCardName(c.getString(NUM_COL_CARDNAME));
        card.setBarcodeNumber(c.getString(NUM_COL_BARCODENUMBER));
        card.setAdrLogo(c.getString(NUM_COL_ADRLOGO));
        //On ferme le cursor
        c.close();

        //On retourne la carte
        return card;
    }
}