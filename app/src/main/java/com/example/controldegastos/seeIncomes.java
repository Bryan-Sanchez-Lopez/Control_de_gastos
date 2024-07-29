package com.example.controldegastos;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class seeIncomes extends AppCompatActivity {

    ListView incomesView;
    dataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_incomes);

        db = new dataBase(this);


        incomesView = findViewById(R.id.incomes);


        Intent intent = getIntent();
        ArrayList<String> incomes = intent.getStringArrayListExtra("incomes");


        ArrayAdapter<String> adapterIncomes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, incomes);

        incomesView.setAdapter(adapterIncomes);


        incomesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String incomeItem = incomes.get(position);
                // Extraer el ID del ingreso del texto del elemento (antes del " - ")
                int incomeId = Integer.parseInt(incomeItem.split(" - ")[0]);

                // Mostrar un diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(seeIncomes.this)
                    .setTitle("Eliminar Ingreso")
                    .setMessage("¿Está seguro de que desea eliminar este ingreso?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteIncome(incomeId);
                            incomes.remove(position);
                            adapterIncomes.notifyDataSetChanged();
                            Toast.makeText(seeIncomes.this, "Ingreso eliminado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
                return true;
            }
        });


        incomesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String expenseItem = incomes.get(position);
                int incomeId = Integer.parseInt(expenseItem.split(" - ")[0]);
                double amount = 0;
                String description = "";
                String date = "";

                try {

                    Cursor cursor = db.getIncome(incomeId);
                    if (cursor != null) {

                        if (cursor.moveToFirst()) {
                            do {

                                amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                                description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                                date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                            }while (cursor.moveToNext());
                        }
                        cursor.close();

                        Intent intent = new Intent(seeIncomes.this, seeDetailsOfIncome.class);
                        intent.putExtra("amount", amount);
                        intent.putExtra("description", description);
                        intent.putExtra("date", date);
                        startActivity(intent);

                    }else{
                        Toast.makeText(seeIncomes.this, "Error al obtener los detalles del ingreso", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Toast.makeText(seeIncomes.this, "Error al obtener el ID del ingreso", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}