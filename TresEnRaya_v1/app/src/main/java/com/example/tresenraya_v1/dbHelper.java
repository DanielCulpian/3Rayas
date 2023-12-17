package com.example.tresenraya_v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbUserZ.db";
    private static final int DATABASE_VERSION = 1;

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crea la tabla "user" con las columnas "id", "nombre" y "password"
        String createTableUser = "CREATE TABLE user ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "password TEXT NOT NULL"
                + ")";
        db.execSQL(createTableUser); //Ejecuta la sentencia SQL
    }//onCreate()

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Actualiza la tabla si la versión de la base de datos cambia
        db.execSQL("DROP TABLE IF EXISTS user"); //Borra la antigua bbdd
        onCreate(db); //Crea una nueva
    }//onUpgrade()

    public void addUser(String nombre, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); //Fijamos la bbdd sobre la que queremos trabajar
        ContentValues values = new ContentValues(); //Para trabajar con los contenidos de la bbdd

        //Valores
        values.put("nombre", nombre);
        values.put("password", password);
        //Añadimos el usuario con los valores establecidos
        db.insert("user", null, values);
        db.close(); //Cerramos la bbdd una vez usada
    }//addUser()

    public Cursor getUsers() {
        //Para hacer selects de los usuarios
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM user";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }//getUser()

    public boolean checkUser(String nombre, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        //Situamos un cursor sobre el usuario a buscar
        Cursor cursor = db.query("user", null, "nombre=? AND password=?", new String[]{nombre, password}, null, null, null);

        //Para llevar la cuenta de los usuarios encontrados
        int count = cursor.getCount();
        cursor.close(); //Cerramos cursor
        db.close();

        if (count > 0)
            return true; // si hay al menos un registro que cumple con las condiciones, retorna true
        else
            return false; // si no hay registros que cumplan con las condiciones, retorna false

    }//checkUser()

    public boolean checkUserN(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("user", null, "nombre=?", new String[]{nombre}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true; //Si hay al menos un registro que cumple con las condiciones, retorna true
        else
            return false; //Si no hay registros que cumplan con las condiciones, retorna false

    }//checkUserN()

    public boolean checkPass(String password) {

        if ((password.length() > 8) && isValidPassword(password))
            return true; //Si se cumplen las condiciones, retorna true
        else
            return false; // Si no, retorna false

    }//CheckPass()

    public boolean isValidPassword(String pass) {
        int countLetters = 0;
        int countNums = 0;
        boolean tieneMayusc = false;

        for (int i = 0; i < pass.length(); i++) { //Miramos letra por letra
            char c = pass.charAt(i);

            if (Character.isAlphabetic(c)) {
                countLetters++;
                if (Character.isUpperCase(c)) {
                    tieneMayusc = true;
                }
            } else if (Character.isDigit(c)) {
                countNums++;
            }

            //Si ya hemos encontrado al menos 2 letras, 2 números y al menos una letra mayúscula, retornamos true
            if (countLetters >= 2 && countNums >= 2 && tieneMayusc) {
                return true;
            }
        }

        //Retornamos true solo si encontramos al menos 2 letras, 2 números y al menos una letra mayúscula
        return countLetters >= 2 && countNums >= 2 && tieneMayusc;
    }

}

