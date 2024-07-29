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

public class activityRegister extends AppCompatActivity {

    dataBase db;

    Button btnRegister;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new dataBase(this);

        btnRegister = findViewById(R.id.registerUser);
        username = findViewById(R.id.usernameRegister);
        password = findViewById(R.id.passwordRegister);
    }

    public void registerUser(View v) {

        int verificarSiExiste = db.loginUserAndGetId(username.getText().toString(), password.getText().toString());


        if(username.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
            Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
        }
        else{
            if(verificarSiExiste != -1){
                Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show();
            }
            else{
                boolean addUser = db.addUser(username.getText().toString(), password.getText().toString());
                if(!addUser){
                    Toast.makeText(this, "Error al registrar usuario, intente nuevamente", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, activityLogin.class);
                    startActivity(intent);
                    finish();
                }
            }
        }




    }

}