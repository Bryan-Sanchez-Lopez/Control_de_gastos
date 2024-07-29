package com.example.controldegastos;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Inicio extends AppCompatActivity {

    dataBase db;


    //Declaramos las variables globales que necesitaremos en el activity inicio
    private int userId;
    private int id_income, id_expense;
    private double totalIncomes, totalExpenses;

    Button verIngresosGenerales, verGastosGenerales, agregarIngreso, agregarGasto;
    TextView dineroTotal;

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


        //Inicializamos la base de datos
        db = new dataBase(this);


        //Obtenemmos los botones para ver los ingresos y los gastos generales
        verIngresosGenerales = findViewById(R.id.verIngresosGenerales);
        verGastosGenerales = findViewById(R.id.verGastosGenerales);

        //Obtenemos los botones para agregar ingresos y gastos
        agregarIngreso = findViewById(R.id.agregarIngreso);
        agregarGasto = findViewById(R.id.agregarGasto);

        //Obtenemos el TextView para mostrar el dinero total
        dineroTotal = findViewById(R.id.dineroTotal);


        //Obtenemos el id del usuario, lo recibimos del activity login
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);


        //Obtenemos los totales de ingresos y gastos generales del usuario
        totalIncomes = db.getSumOfIncomes(userId);
        totalExpenses = db.getSumOfExpenses(userId);


        //Mostramos los totales en los TextView correspondientes
        verIngresosGenerales.setText(totalIncomes+"");
        verGastosGenerales.setText(totalExpenses+"");


        //Mostramos el dinero total en el TextView correspondiente
        dineroTotal.setText(totalIncomes-totalExpenses+"");

    }


    //Metodo para agregar ingresos
    public void agregarIngreso(View view) {

        //Creamos un intent para ir a la activity addIncome
        Intent intent = new Intent(this, addIncome.class);

        //Si el id del usuario es valido, agregamos el id del usuario al intent
        if (userId != -1) {
            intent.putExtra("userId", userId);
        }else{
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);

    }


    //Metodo para agregar gastos
    public void agregarGasto(View view) {

        //Creamos un intent para ir a la activity addExpense
        Intent intent = new Intent(this, addExpense.class);

        //Si el id del usuario es valido, agregamos el id del usuario al intent
        if (userId != -1) {
            intent.putExtra("userId", userId);
        }else{
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);

    }


    //Metodo para ver los ingresos generales
    public void verIngresosGenerales(View view) {

        try {
            //Creamos un cursor para obtener los ingresos del usuario en forma de lista
            Cursor cursor = db.getIncomes(userId);
            ArrayList<String> incomes = new ArrayList<>();

            //Validamos que el cursor no sea nulo
            if(cursor != null){

                //Mientras haya un registro en el cursor, lo agregamos a la lista
                if(cursor.moveToFirst()){
                    do{
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        Double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

                        id_income = cursor.getInt(cursor.getColumnIndexOrThrow("income_id"));
                        incomes.add(id_income+" - "+"   $"+amount);
                    }while (cursor.moveToNext());
                }
                cursor.close();
            }

            //Creamos un intent para ir a la activity seeIncomes
            Intent intent = new Intent(this, seeIncomes.class);
            intent.putStringArrayListExtra("incomes", incomes);
            intent.putExtra("id_income", id_income);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }

    }


    //Metodo para ver los gastos generales
    public void verGastosGenerales(View view) {

        try {
            //Creamos un cursor para obtener los gastos del usuario en forma de lista
            Cursor cursor = db.getExpenses(userId);
            ArrayList<String> expenses = new ArrayList<>();

            //Validamos que el cursor no sea nulo
            if(cursor != null){

                //Mientras haya un registro en el cursor, lo agregamos a la lista
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

            //Creamos un intent para ir a la activity seeExpenses
            Intent intent = new Intent(this, seeExpenses.class);
            intent.putStringArrayListExtra("expenses", expenses);
            intent.putExtra("id_expense", id_expense);
            startActivity(intent);

        }catch (Exception e){
            Toast.makeText(this, "Intente mas tarde", Toast.LENGTH_SHORT).show();
        }

    }












    //Cuando la actividad se destruye, cerramos la base de datos y establecemos el id del usuario en -1
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
        userId = -1;
    }

    //Cuando la actividad se pausa, cerramos la base de datos y establecemos el id del usuario en -1
    @Override
    public void onPause(){
        super.onPause();
        db.close();
        userId = -1;

    }



    //Cuando la actividad se reanuda, inicializamos la base de datos y recuperamos el id del usuario
    @Override
    public void onResume(){
        super.onResume();
        db = new dataBase(this);
        userId = getIntent().getIntExtra("userId", -1);

        totalIncomes = db.getSumOfIncomes(userId);
        verIngresosGenerales.setText(totalIncomes+"");

        totalExpenses = db.getSumOfExpenses(userId);
        verGastosGenerales.setText(totalExpenses+"");

        dineroTotal.setText(totalIncomes-totalExpenses+"");

    }

}