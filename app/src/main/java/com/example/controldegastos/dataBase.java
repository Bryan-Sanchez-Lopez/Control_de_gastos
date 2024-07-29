package com.example.controldegastos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense_manager.db";
    private static final int DATABASE_VERSION = 1;


    //Tablas de la base de datos
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String TABLE_INCOMES = "incomes";


    //Columnas de las tabla de usuarios
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    // columnas de las tablas de ingresos y gastos
    private static final String COLUMN_EXPENSE_ID = "expense_id";
    private static final String COLUMN_INCOME_ID = "income_id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_DESCRIPTION = "description";



    //////////////////////////////////////////////////Creacion de las tablas////////////////////////////////////////////////////////////////
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " ("
        + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_USERNAME + " TEXT, "
        + COLUMN_PASSWORD + " TEXT)";

    private static final String CREATE_TABLE_EXPENSES = "CREATE TABLE " + TABLE_EXPENSES + " ("
        + COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_USER_ID + " INTEGER, "
        + COLUMN_AMOUNT + " REAL, "
        + COLUMN_CATEGORY + " TEXT, "
        + COLUMN_DATE + " TEXT, "
        + COLUMN_DESCRIPTION + " TEXT, "
        + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    private static final String CREATE_TABLE_INCOMES = "CREATE TABLE " + TABLE_INCOMES + " ("
        + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COLUMN_USER_ID + " INTEGER, "
        + COLUMN_AMOUNT + " REAL, "
        + COLUMN_DATE + " TEXT, "
        + COLUMN_DESCRIPTION + " TEXT, "
        + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";

    public dataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EXPENSES);
        db.execSQL(CREATE_TABLE_INCOMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INCOMES);
        onCreate(db);
    }


/////////////////////////////////////////////////////////    CRUD DE USUARIOS    ////////////////////////////////////////////////////////////////////////////

    //AGREGAR USUARIO
    public boolean addUser(String username, String password){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;

    }

    //LOGIN, VALIDA LAS CREDENCIALES DEL USUARIO Y RETORNA SU ID
    public int loginUserAndGetId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT "+COLUMN_USER_ID+" FROM "+TABLE_USERS+" WHERE "+COLUMN_USERNAME+" = ? AND "+COLUMN_PASSWORD+ " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
        }
        cursor.close();
        db.close();
        return userId;
    }


    //////////////////////////////////////////////     CRUD DE GASTOS    ////////////////////////////////////////////////////////////////////////


    //AGREGAR GASTO
    public boolean addExpense(int userId, double amount, String category, String date, String description, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;
    }

    //EDITAR GASTO
    public boolean editExpense(int expenseId, double amount, String category, String date, String description, String photoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.update(TABLE_EXPENSES, values, COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(expenseId)});
        db.close();
        return result != -1;
    }


    //ELIMINAR GASTO
    public boolean deleteExpense(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_EXPENSES, COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(expenseId)});
        db.close();
        return result != -1;
    }


    //OBTENER EL TOTAL DE GASTOS DE UN USUARIO
    public double getSumOfExpenses(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") AS total FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        double totalExpenses = -1;
        if (cursor.moveToFirst()) {
            totalExpenses = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        db.close();
        return totalExpenses;

    }


    //OBTENER TODOS LOS GASTOS DE UN USUARIO
    public Cursor getExpenses(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});

    }

    //OBTENER UN GASTO EN ESPECIFICO
    public Cursor getExpense(int expenseId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_EXPENSE_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(expenseId)});
    }




    ///////////////////////////////////////////////    CRUD DE INGRESOS    /////////////////////////////////////////////////////////////////////////


    //AGREGAR INGRESO
    public boolean addIncome(int userId, double amount, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_INCOMES, null, values);
        db.close();
        return result != -1;
    }

    //EDITAR INGRESO
    public boolean editIncome(int incomeId, double amount, String category, String date, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.update(TABLE_INCOMES, values, COLUMN_INCOME_ID + "=?", new String[]{String.valueOf(incomeId)});
        db.close();
        return result != -1;
    }

    //ELIMINAR INGRESO
    public boolean deleteIncome(int incomeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_INCOMES, COLUMN_INCOME_ID + "=?", new String[]{String.valueOf(incomeId)});
        db.close();
        return result != -1;
    }

    //OBTENER TODOS LOS INGRESOS DE UN USUARIO
    public Cursor getIncomes(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INCOMES + " WHERE " + COLUMN_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(userId)});

    }

    //OBTENER UN INGRESO EN ESPECIFICO
    public Cursor getIncome(int incomeId){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_INCOMES + " WHERE " + COLUMN_INCOME_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(incomeId)});
    }

    //OBTENER LA SUMA DEL TOTAL DE INGRESOS DE UN USUARIO
    public double getSumOfIncomes(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") AS total FROM " + TABLE_INCOMES + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        double totalIncomes = -1;
        if (cursor.moveToFirst()) {
            totalIncomes = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
        }
        cursor.close();
        db.close();
        return totalIncomes;

    }


}
