package com.example.controldegastos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //Base de datos
    //Intents
    //Diferentes layouts
    //Preferencias
    //Ciclo de vida
    //Eventos
    //Sensores (opcional)
    //listview

    //Aplicación de Control de Gastos:
    //Base de datos: El usuario se debe registrat e iniciar sesion, almacena los ingresos, gastos por categorías, fechas y cualquier otra información relevante.
    //Intentos: Permite a los usuarios moverse entre pantallas para agregar, editar y eliminar gastos e ingresos.
    //Diferentes Layouts: Usa diferentes layouts para la lista de gastos, la vista detallada de un gasto y la creación de un nuevo gasto.
    //Preferencias: Permite a los usuarios ver en la pantalla de inicio el total de ingresos y gastos.
    //Ciclo de vida: Guarda los datos de los gastos cuando la aplicación se pausa o cierra.
    //Eventos: Responde a eventos como la adición de un nuevo gasto, la edición de un gasto existente, etc.
    //Sensores: Permite a los usuarios tomar foto de un gasto y guardarlo en la base de datos.

    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
    }

    //Esta funcion se le agrega al boton de login y envia a la actividad de login

    public void login(View view) {
        Intent intent;
        intent = new Intent(this, activityLogin.class);
        startActivity(intent);
    }

    //Esta funcion se le agrega al boton de register y envia a la actividad de register

    public void register(View view) {
        Intent intent;
        intent = new Intent(this, activityRegister.class);
        startActivity(intent);
    }

}