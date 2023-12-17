package com.example.tresenraya_v1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {
    dbHelper db = new dbHelper(this); //Recogemos la bbdd

    //Elementos que vamos a tratar
    EditText username;
    EditText password;
    Button loginButton;
    TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        //Inicializamos los elementos
        username = findViewById(R.id.user);
        password = findViewById(R.id.password);
        signUpText = findViewById(R.id.signUpText);
        loginButton = findViewById(R.id.loginButton);

        //Para cuandos e haga click en el boton de login
        loginButton.setOnClickListener(view -> {
            //Booleana que comprueba si el usuario y la contraseña estan ya o no en la bbdd
            boolean find = db.checkUser(username.getText().toString(), password.getText().toString());

            if(find){
                //Si lo encuentra, avisa de que el login es correcto
                Toast.makeText(LogIn.this, "Login Correcto!!", Toast.LENGTH_SHORT).show();
                try{
                    //Si el login es correcto, espero 1 segundo y medio e inicia la actividad siguiente
                    Thread.sleep(1500);
                    Intent intent = new Intent(getApplicationContext(), TresEnRaya.class);

                    //Aqui, le indicamos que el intent tendra un dato extra, el nombre de usuario
                    intent.putExtra("Username", username.getText().toString());
                    startActivity(intent);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else
                //Si no lo encuentra o la contraseña es incorrecta se lo avisa al usuario
                Toast.makeText(LogIn.this, "No estas registrado!", Toast.LENGTH_SHORT).show();


        });

        //Para permitirle al usuario un registro
        signUpText.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SingUp.class);
            startActivity(intent);
        });


    }//onCreate()
}