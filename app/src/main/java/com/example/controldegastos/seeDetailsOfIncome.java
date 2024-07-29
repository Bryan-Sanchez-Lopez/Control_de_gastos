package com.example.controldegastos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class seeDetailsOfIncome extends AppCompatActivity {

    TextView cantidad, descripcion, fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_details_of_expense);


        cantidad = findViewById(R.id.cantidad);
        descripcion = findViewById(R.id.descripcion);
        fecha = findViewById(R.id.fecha);

        Intent intent = getIntent();
        Double cantidad = intent.getDoubleExtra("amount", 0);
        String descripcion = intent.getStringExtra("description");
        String fecha = intent.getStringExtra("date");

        this.cantidad.setText(cantidad+"");
        this.descripcion.setText(descripcion);
        this.fecha.setText(fecha);








    }
}