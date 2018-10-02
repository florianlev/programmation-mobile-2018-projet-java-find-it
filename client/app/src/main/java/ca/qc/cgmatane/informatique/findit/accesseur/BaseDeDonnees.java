package ca.qc.cgmatane.informatique.findit.accesseur;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDonnees  extends SQLiteOpenHelper {

    private static BaseDeDonnees instance = null;

    public static BaseDeDonnees getInstance(Context contexte)
    {
        instance = new BaseDeDonnees(contexte);
        return instance;
    }

    public static BaseDeDonnees getInstance()
    {
        return instance;
    }

    public BaseDeDonnees(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public BaseDeDonnees(Context contexte) {
        super(contexte, "findIt", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_UTILISATEUR = "create table utilisateur(utilisateur_id INTEGER PRIMARY KEY AUTOINCREMENT, pseudo TEXT, mdp TEXT,mail TEXT )";
        db.execSQL(CREATE_TABLE_UTILISATEUR);
        String CREATE_TABLE_SCORE = "create table score(score_id INTEGER PRIMARY KEY, valeur INTEGER )";
        db.execSQL(CREATE_TABLE_SCORE);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

        String DELETE = "delete from score where 1 = 1";
        db.execSQL(DELETE);

        String INSERT_1 = "insert into score(valeur) VALUES(200)";
        String INSERT_2 = "insert into score(valeur) VALUES(200)";
        String INSERT_3 = "insert into score(valeur) VALUES(200)";

        db.execSQL(INSERT_1);
        db.execSQL(INSERT_2);
        db.execSQL(INSERT_3);

        String INSERT_1UTILISATEUR = "insert into utilisateur( utilisateur_speudo ,utilisateur_mdp ,utilisateur_mail) VALUES('toto','test','toto@gmail.com')";

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        //String DETRUIRE_TABLE_SCORE = "drop table score";
        //db.execSQL(DETRUIRE_TABLE_SCORE);
        //String DETRUIRE_TABLE_SCORE = "drop table utilisateur";
        //db.execSQL(DETRUIRE_TABLE_SCORE);
        String CREATE_TABLE_UTILISATEUR = "create table utilisateur(utilisateur_id INTEGER PRIMARY KEY AUTOINCREMENT, pseudo TEXT,mdp TEXT,mail TEXT )";
        db.execSQL(CREATE_TABLE_UTILISATEUR);
        String CREATE_TABLE_SCORE = "create table score(score_id INTEGER PRIMARY KEY, valeur INTEGER )";
        db.execSQL(CREATE_TABLE_SCORE);
    }
}

