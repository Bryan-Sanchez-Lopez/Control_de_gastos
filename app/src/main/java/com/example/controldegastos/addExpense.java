package com.example.controldegastos;

import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class addExpense extends AppCompatActivity {

    Button btnAddExpense;
    EditText incomeAmount, incomeDescription, incomeDate;
    private int userId;
    dataBase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = new dataBase(this);

        btnAddExpense = findViewById(R.id.addExpense);
        incomeAmount = findViewById(R.id.incomeAmount);
        incomeDescription = findViewById(R.id.incomeDescription);
        incomeDate = findViewById(R.id.incomeDate);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);

    }

    public void addExpense(View view) {
        String amount = incomeAmount.getText().toString();
        String description = incomeDescription.getText().toString();
        String date = incomeDate.getText().toString();
        String category = "";

        double amountDouble = Double.parseDouble(amount);

        db = new dataBase(this);
        double totalIncomes = db.getSumOfIncomes(userId);
        double totalExpenses = db.getSumOfExpenses(userId);

        totalExpenses += amountDouble;

        if (amount.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();

        }
        else {
            if (totalExpenses > totalIncomes) {
                Toast.makeText(this, "No puedes agregar un gasto mayor al total de ingresos", Toast.LENGTH_SHORT).show();

            } else {
                if (userId != -1) {
                    if (!db.addExpense(userId, Float.parseFloat(amount), category, date, description, "")) {
                        Toast.makeText(this, "Error al agregar el ingreso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Ingreso agregado correctamente", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Inicio.class);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Error, no se pudo obtener el id del usuario", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


}
