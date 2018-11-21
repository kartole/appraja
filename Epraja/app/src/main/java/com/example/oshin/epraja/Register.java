package com.example.oshin.epraja;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register extends AppCompatActivity {

    Intent launchNextActivity;
    FloatingActionButton fabTec;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabTec = (FloatingActionButton) findViewById(R.id.fabTeC);
        fabTec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Register.this, TermsAndConditions.class);
                startActivity(intent);
            }
        });

        EditText edCPF = (EditText)findViewById(R.id.edCPF);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(edCPF, smf);
        edCPF.addTextChangedListener(mtw);

    }

    public void register_OnClick(View v){




            EditText edName = (EditText)findViewById(R.id.edUserName);
            EditText edCPF = (EditText)findViewById(R.id.edCPF);
            EditText edEmail = (EditText)findViewById(R.id.edEmail);
            EditText edConfEmail = (EditText)findViewById(R.id.edConfEmail);
            EditText edPassword = (EditText)findViewById(R.id.edPassword);
            EditText edConfPassword = (EditText)findViewById(R.id.edConfPassword);

            CheckBox cbAcceptTeC = (CheckBox)findViewById(R.id.cbAcceptTeC);

            if (edName.length() == 0){
                edName.setError("O campo Nome é obrigatório!");
                edName.requestFocus();
                return;
            }
            if (edCPF.length() == 0){
                edCPF.setError("Campo CPF é obrigatório!");
                edCPF.requestFocus();
                return;
            }
            if (edEmail.length() == 0){
                edEmail.setError("Campo Email é obrigatório!");
                edEmail.requestFocus();
                return;
            }
            if (edConfEmail.length() == 0 || !edConfEmail.getText().toString().equals(edEmail.getText().toString())){
                edConfEmail.setError("Os Emails informados não correspondem!");
                edConfEmail.requestFocus();
                return;
            }
            if (edPassword.length() == 0){
                edPassword.setError("Campo Senha é obrigatório!");
                edPassword.requestFocus();
                return;
            }
            if (edConfPassword.length() == 0 || !edConfPassword.getText().toString().equals(edPassword.getText().toString())){
                edConfPassword.setError("As senhas informados não correspondem!");
                edConfPassword.requestFocus();
                return;
            }
            if (cbAcceptTeC.isChecked() == false){
                Toast.makeText(getBaseContext(), "Você deve aceitar os termos e condições para se registrar!", Toast.LENGTH_SHORT).show();
                return;
            }


            RegisterModel registerModel = new RegisterModel(edName.getText().toString(),
                                                            edCPF.getText().toString(),
                                                            edEmail.getText().toString(),
                                                            edPassword.getText().toString());

            RegisterDAO registerDAO = new RegisterDAO();

           registerDAO.insertUser(registerModel.getName(), registerModel.getCpf(), registerModel.getEmail(), registerModel.getUserPassword());

        launchNextActivity = new Intent(Register.this, Login.class);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchNextActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle b = new Bundle();
        b.putInt("key", 1);
        //launchNextActivity.putExtra("backRegister", 1);
        startActivity(launchNextActivity);

           Toast.makeText(getBaseContext(), "Registrado com sucesso!", Toast.LENGTH_SHORT).show();

    }


}
