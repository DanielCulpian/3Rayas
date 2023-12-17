package com.example.tresenraya_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SingUp extends AppCompatActivity {
    dbHelper db = new dbHelper(this); //Recogemos la bbdd

    //Elementos a utilizar
    EditText username;
    EditText password;
    Button loginButton;
    TextView loginUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //Inicializamos los elementos
        username = findViewById(R.id.user);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        loginUpText = findViewById(R.id.loginUpText);

        loginButton.setOnClickListener(view -> {
            //Si al clickar en el boton de login, el usuario no esta en la bbdd, la contraseña es apta y la contraseña es diferente de un valor null, registramos al usuario
            if((db.checkUserN(username.getText().toString()) == false) && (db.checkPass(password.getText().toString()) && (password.getText().toString() != null))){
                db.addUser(username.getText().toString(), password.getText().toString());
                Toast.makeText(SingUp.this, "Registrado!", Toast.LENGTH_SHORT).show();
            }
            else {
                //Indicamos al usuario que el registro es erroneo
                try {
                    Utilidades.mostrarToastConRetraso(
                            SingUp.this,
                            "Alguno de los parámetros no cumple las condiciones!",
                            Toast.LENGTH_SHORT,
                            "Comprueba que el nombre de usuario no esté ya en la base de datos y que la contraseña tenga al menos 8 dígitos, de los cuales 2 sean números y haya al menos 1 mayúscula.",
                            Toast.LENGTH_LONG,
                            2000 // 2000 milisegundos (2 segundos) de retraso
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //Para volver al login cuando el usuario desee
        loginUpText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LogIn.class);
            startActivity(intent);
        });
    }


}