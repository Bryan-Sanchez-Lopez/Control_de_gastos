package com.example.controldegastos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity {

    dataBase db;

    private int userId;
    private int id_income, id_expense;
    private double totalIncomes, totalExpenses;

    Button verIngresosGenerales, verGastosGenerales, agregarIngreso, agregarGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new dataBase(this);

        verIngresosGenerales = findViewById(R.id.verIngresosGenerales);
        verGastosGenerales = findViewById(R.id.verGastosGenerales);
        agregarIngreso = findViewById(R.id.agregarIngreso);
        agregarGasto = findViewById(R.id.agregarGasto);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

        totalIncomes = db.getSumOfIncomes(userId);
        verIngresosGenerales.setText(totalIncomes+"");

        totalExpenses = db.getSumOfExpenses(userId);
        verGastosGenerales.setText(totalExpenses+"");
    }

    public void agregarIngreso(View view) {
        Intent intent = new Intent(this, addIncome.class);
        if (userId != -1) {
            intent.putExtra("userId", userId);
        }else{
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);

    }

    public void agregarGasto(View view) {
        Intent intent = new Intent(this, addExpense.class);
        if (userId != -1) {
            intent.putExtra("userId", userId);
        }else{
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);

    }


    public void verIngresosGenerales(View view) {

        try {
            Cursor cursor = db.getIncomes(userId);
            ArrayList<String> incomes = new ArrayList<>();

            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        Double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

                        id_income = cursor.getInt(cursor.getColumnIndexOrThrow("income_id"));
                        incomes.add(id_income+" - "+"   $"+amount+" \n \t\t\t\t\t"+description+"\n");
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }

            Intent intent = new Intent(this, seeIncomes.class);
            intent.putStringArrayListExtra("incomes", incomes);
            intent.putExtra("id_income", id_income);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }

    }


    public void verGastosGenerales(View view) {

        try {
            Cursor cursor = db.getExpenses(userId);
            ArrayList<String> expenses = new ArrayList<>();

            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        Double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

                        id_expense = cursor.getInt(cursor.getColumnIndexOrThrow("expense_id"));
                        expenses.add(id_expense+" - "+"   $"+amount);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }

            Intent intent = new Intent(this, seeExpenses.class);
            intent.putStringArrayListExtra("expenses", expenses);
            intent.putExtra("id_expense", id_expense);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }

    }












    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
        userId = -1;
    }

    @Override
    public void onPause(){
        super.onPause();
        db.close();
        userId = -1;

    }



    @Override
    public void onResume(){
        super.onResume();
        db = new dataBase(this);
        userId = getIntent().getIntExtra("userId", -1);

        totalIncomes = db.getSumOfIncomes(userId);
        verIngresosGenerales.setText(totalIncomes+"");

        totalExpenses = db.getSumOfExpenses(userId);
        verGastosGenerales.setText(totalExpenses+"");

    }

}