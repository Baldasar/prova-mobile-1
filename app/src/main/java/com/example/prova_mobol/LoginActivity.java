package com.example.prova_mobol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button btnEntrar;
    private EditText inputEmail, inputSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnEntrar = findViewById(R.id.btnEntrar);
        inputEmail = findViewById(R.id.inputEmail);
        inputSenha = findViewById(R.id.inputSenha);

        SharedPreferences sharedPreferences = getSharedPreferences("props", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        inputEmail.setText( sharedPreferences.getString("email", ""));
        inputSenha.setText( sharedPreferences.getString("senha", ""));

        Intent it = new Intent(LoginActivity.this, MainActivity.class);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                String senha = inputSenha.getText().toString();

                if(email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo e-mail não pode ser vazio", Toast.LENGTH_SHORT).show();
                }

                if(senha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Campo senha não pode ser vazio", Toast.LENGTH_SHORT).show();
                }

                if((email.equals("Administrador") && senha.equals("Administrador") || (email.equals("Adm") && senha.equals("Adm")) || (email.equals("Administrator") && senha.equals("pr4frentef0rever")))) {
                    Toast.makeText(LoginActivity.this, "Login Efetuado com sucesso!!", Toast.LENGTH_SHORT).show();

                    editor.putString("email", email);
                    editor.putString("senha", senha);
                    editor.apply();

                    startActivity(it);
                }
            }
        });
    }
}
