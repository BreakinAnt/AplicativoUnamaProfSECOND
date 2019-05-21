package com.example.aplicativounamaprofsecond;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainCadastro extends AppCompatActivity {

    private EditText editNomeCad, editEmailCad, editSenhaCad, editSenhaConf;
    private Button btnCadastrar;
    private String HOST = "http://bangadinhosbr2.000webhostapp.com/kinu_stuff/db/";
    String URL = HOST + "/cadastrar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cadastro);

        editNomeCad = (EditText) findViewById(R.id.editNomeCad);
        editEmailCad = (EditText) findViewById(R.id. editEmailCad);
        editSenhaCad = (EditText) findViewById(R.id.editSenhaCad);
        editSenhaConf = (EditText) findViewById(R.id.editSenhaConf);
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome = editNomeCad.getText().toString();
                String email = editEmailCad.getText().toString();
                String senha = editSenhaCad.getText().toString();
                String confirma = editSenhaConf.getText().toString();

                if(confirma.equals(senha)) {

                  if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                      Toast.makeText(MainCadastro.this, "Todos os campos são obrigatorios", Toast.LENGTH_LONG).show();
                  } else {
                      Ion.with(MainCadastro.this)
                              .load(URL)
                              //enviando os dados do APP para o PHP
                              .setBodyParameter("nome_app", nome)
                              .setBodyParameter("email_app", email)
                              .setBodyParameter("senha_app", senha)

                              .asJsonObject()
                              .setCallback(new FutureCallback<JsonObject>() {
                                  @Override
                                  public void onCompleted(Exception e, JsonObject result) {
                                      try {
                                          String RETORNO = result.get("CADASTRO").getAsString();

                                          if(RETORNO.equals("EMAIL_ERRO")) {
                                              Toast.makeText(MainCadastro.this, "Email já está cadastrado", Toast.LENGTH_LONG).show();
                                          } else if(RETORNO.equals("SUCESSO")){
                                              Toast.makeText(MainCadastro.this, "Cadastrado com sucesso", Toast.LENGTH_LONG).show();
                                              Intent abrePrincipal = new Intent(MainCadastro.this, MainPrincipal.class);
                                              startActivity(abrePrincipal);
                                          } else {
                                              Toast.makeText(MainCadastro.this, "Ocorreu um erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                                          }

                                      } catch (Exception erro) {
                                          Toast.makeText(MainCadastro.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                                      }

                                  }
                              });
                  }
                } else {
                    Toast.makeText(MainCadastro.this, "Senhas não conferem", Toast.LENGTH_LONG).show();
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
