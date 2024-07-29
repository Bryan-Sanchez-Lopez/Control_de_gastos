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

public class addIncome extends AppCompatActivity {

    Button btnAddIncome;
    EditText incomeAmount, incomeDescription, incomeDate;
    private int userId;
    dataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_income);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new dataBase(this);

        btnAddIncome = findViewById(R.id.addExpense);
        incomeAmount = findViewById(R.id.incomeAmount);
        incomeDescription = findViewById(R.id.incomeDescription);
        incomeDate = findViewById(R.id.incomeDate);

        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);


    }


    public void addIncome(View view) {
        String amount = incomeAmount.getText().toString();
        String description = incomeDescription.getText().toString();
        String date = incomeDate.getText().toString();

        if (amount.isEmpty() || description.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            if(userId != -1) {
                if (!db.addIncome(userId, Float.parseFloat(amount), date, description)){
                    Toast.makeText(this, "Error al agregar el ingreso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Ingreso agregado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Inicio.class);
                    finish();

                }
            }
            else {
                Toast.makeText(this, "Error, no se pudo obtener el id del usuario", Toast.LENGTH_SHORT).show();
            }
        }

    }

}