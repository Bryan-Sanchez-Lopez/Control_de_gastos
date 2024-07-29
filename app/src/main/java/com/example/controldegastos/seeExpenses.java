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

public class seeExpenses extends AppCompatActivity {

    ListView expensesView;
    dataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_expenses);

        db = new dataBase(this);


        expensesView = findViewById(R.id.expenses);


        // Obtener la lista de gastos desde el intent
        Intent intent = getIntent();
        ArrayList<String> expenses = intent.getStringArrayListExtra("expenses");


        // Crear un adaptador para la lista de gastos
        ArrayAdapter<String> adapterExpenses = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, expenses);

        // Establecer el adaptador en la lista de gastos
        expensesView.setAdapter(adapterExpenses);


        // Configurar el escuchador de clics en la lista de gastos

        //Se configura el loong click para eliminar un gasto
        expensesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String expenseItem = expenses.get(position);
                // Extraer el ID del ingreso del texto del elemento (antes del " - ")
                int expenseId = Integer.parseInt(expenseItem.split(" - ")[0]);

                // Mostrar un diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(seeExpenses.this)
                    .setTitle("Eliminar Ingreso")
                    .setMessage("¿Está seguro de que desea eliminar este ingreso?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteExpense(expenseId);
                            expenses.remove(position);
                            adapterExpenses.notifyDataSetChanged();
                            Toast.makeText(seeExpenses.this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
                return true;
            }



        });


        //Se configura el click para ver los detalles del gasto
        expensesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String expenseItem = expenses.get(position);
            int expenseId = Integer.parseInt(expenseItem.split(" - ")[0]);
            double amount = 0;
            String description = "";
            String date = "";

            try {

                Cursor cursor = db.getExpense(expenseId);
                if (cursor != null) {

                    if (cursor.moveToFirst()) {
                        do {

                            amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                            description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                            date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                        }while (cursor.moveToNext());
                    }
                    cursor.close();

                    Intent intent = new Intent(seeExpenses.this, seeDetailsOfExpense.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("description", description);
                    intent.putExtra("date", date);
                    startActivity(intent);

                }else{
                    Toast.makeText(seeExpenses.this, "Error al obtener los detalles del gasto", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Toast.makeText(seeExpenses.this, "Error al obtener el ID del gasto", Toast.LENGTH_SHORT).show();
            }

        }
        });

    }
}