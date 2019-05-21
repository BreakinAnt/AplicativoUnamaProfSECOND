package com.example.aplicativounamaprofsecond;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainLogin extends AppCompatActivity {

    private EditText editEmailLogar, editSenhaLogar;
    private Button btnEntrar, btnCadastrar;
    private TextView txtCadastro;
    private String HOST = "http://bangadinhosbr2.000webhostapp.com/kinu_stuff/db/";
    String URL = HOST + "/logar.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmailLogar = (EditText) findViewById(R.id.editEmailLogar);
        editSenhaLogar = (EditText) findViewById(R.id.editSenhaLogar);
        btnEntrar = (Button) findViewById(R.id.btnLogar);
        txtCadastro = (TextView)findViewById(R.id.txtCadastro);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent abreCad = new Intent(MainLogin.this, MainCadastro.class);
               startActivity(abreCad);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  String email = editEmailLogar.getText().toString();
                  String senha = editSenhaLogar.getText().toString();

                if(email.isEmpty() || senha.isEmpty()) {
                    Toast.makeText(MainLogin.this, "Todos os campos são obrigatorios", Toast.LENGTH_LONG).show();
                } else {
                Ion.with(MainLogin.this)
                        .load(URL)
                        //enviando os dados do APP para o PHP

                        .setBodyParameter("email_app", email)
                        .setBodyParameter("senha_app", senha)

                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try {
                                    String RETORNO = result.get("LOGIN").getAsString();

                                    if(RETORNO.equals("ERRO")) {
                                        Toast.makeText(MainLogin.this, "Login ou senha incorretos", Toast.LENGTH_LONG).show();
                                    } else if(RETORNO.equals("SUCESSO")){

                                        Intent abrePrincipal = new Intent(MainLogin.this, MainPrincipal.class);
                                        startActivity(abrePrincipal);

                                    } else {
                                        Toast.makeText(MainLogin.this, "Ocorreu um erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception erro) {
                                    Toast.makeText(MainLogin.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }


    }
});



    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}