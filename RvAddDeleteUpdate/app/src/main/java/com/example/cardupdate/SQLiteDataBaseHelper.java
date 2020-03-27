package com.example.cardupdate;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {
    //classe peremttant de définir la table qui sera produite
    public static final String TABLE_NAME = "card_table";
    public static final String COL_IDCARD = "IDCARD";
    public static final String COL_CARDNAME = "CARDNAME";
    public static final String COL_BARCODENUMBER = "BARCODENUMBER";
    public static final String COL_ADRLOGO = "ADRLOGO";

    public static  final String CREATE_BDD = "CREATE TABLE "+TABLE_NAME+" ("
            +COL_IDCARD+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +COL_CARDNAME+" TEXT NOT NULL, "
            +COL_BARCODENUMBER+" TEXT NOT NULL, "
            +COL_ADRLOGO+" TEXT NOT NULL);";
    public SQLiteDataBaseHelper(Context context, String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_BDD);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
}