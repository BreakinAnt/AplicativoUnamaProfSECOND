package com.example.aplicativounamaprofsecond;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class MainPrincipal extends AppCompatActivity {

    String mensagemProf, nomeProf, dataProf;
    TextView txtNome, txtMensagem;
    private String HOST = "http://bangadinhosbr2.000webhostapp.com/kinu_stuff/db/";
    String URL = HOST + "/verificar_mensagem.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_principal);

        txtNome = (TextView) findViewById(R.id.txtNome);
        txtMensagem = (TextView) findViewById(R.id.txtMensagem);

        SharedPreferences pref = getSharedPreferences("info", MODE_PRIVATE);

        String nomeEncrypt = pref.getString(encrypt("login"), "");

        String login = decrypt(nomeEncrypt);

        //txtNome.setText("Nome:" + nome);

        VerificarMensagem(login);

    }

    public void VerificarMensagem (String loginVerificacao){
        Ion.with(MainPrincipal.this)
                .load(URL)
                //enviando os dados do APP para o PHP

                .setBodyParameter("login_app", loginVerificacao)

                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        try {

                            String RETORNO = result.get("MENSAGEM").getAsString();

                            if (RETORNO.equals("ERRO")) {
                                Toast.makeText(MainPrincipal.this, "Nenhuma mensagem encontrada", Toast.LENGTH_LONG).show();
                                txtMensagem.setText("...");
                            } else if (RETORNO.equals("SUCESSO")) {
                                //recebendo os valores do php
                                mensagemProf = result.get("MENSAGEMPROF").getAsString();
                                nomeProf = result.get("NOME").getAsString();
                                dataProf = result.get("DATA").getAsString();

                                //colocando os valores pegados do php no txtMensagem
                                txtMensagem.setText("Professor: " + nomeProf + "." + System.getProperty ("line.separator")
                                        +  mensagemProf  + System.getProperty ("line.separator")
                                        + "Data: " + dataProf);


                            } else {
                                Toast.makeText(MainPrincipal.this, "Ocorreu um erro, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception erro) {
                            Toast.makeText(MainPrincipal.this, "Erro: " + erro, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public String encrypt(String palavra) {

        return Base64.encodeToString(palavra.getBytes(), Base64.DEFAULT);

    }

    public String decrypt (String palavra) {

        return new String(Base64.decode(palavra, Base64.DEFAULT));

    }
}