package com.example.litemangareader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.litemangareader.MainActivity;
import com.example.litemangareader.Model.UserModel;
import com.example.litemangareader.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class Register extends AppCompatActivity {

    private EditText edt_name_register;
    private EditText edt_email_register;
    private EditText edt_senha_register;
    private EditText edt_confirmar_senha_register;
    private CheckBox ckb_exibir_senha_register;
    private Button btn_registrar_register;
    private Button btn_login_register;
    private ProgressBar login_progress_bar_register;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        edt_name_register = findViewById(R.id.edt_name_register);
        edt_email_register = findViewById(R.id.edt_email_register);
        edt_senha_register = findViewById(R.id.edt_senha_register);
        edt_confirmar_senha_register = findViewById(R.id.edt_confirmar_senha_register);
        ckb_exibir_senha_register = findViewById(R.id.ckb_exibir_senha_register);
        btn_registrar_register = findViewById(R.id.btn_registrar_register);
        btn_login_register = findViewById(R.id.btn_login_register);
        login_progress_bar_register = findViewById(R.id.login_progress_bar_register);


        ckb_exibir_senha_register.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edt_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                }else{
                    edt_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edt_confirmar_senha_register.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });



        btn_registrar_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserModel userModel = new UserModel();

                userModel.setEmail(edt_email_register.getText().toString());
                userModel.setNome(edt_name_register.getText().toString());
                String registerSenha = edt_senha_register.getText().toString();
                String registerConfirmarSenha = edt_confirmar_senha_register.getText().toString();

                if(!TextUtils.isEmpty(userModel.getEmail()) || !TextUtils.isEmpty(registerSenha)|| !TextUtils.isEmpty(userModel.getNome())  || !TextUtils.isEmpty(registerConfirmarSenha)){
                    if(registerSenha.equals(registerConfirmarSenha)){
                        login_progress_bar_register.setVisibility(View.VISIBLE);

                        mAuth.createUserWithEmailAndPassword(userModel.getEmail(),registerSenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    userModel.setId(mAuth.getUid());
                                    userModel.salvar();
                                    abrirTelaPrincipal();
                                }else{
                                    String error;
                                    try{
                                        throw task.getException();
                                    }catch (FirebaseAuthWeakPasswordException e){
                                        error = getString(R.string.password_exception);
                                        e.printStackTrace();
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        error = getString(R.string.invalid_email);
                                        e.printStackTrace();
                                    }catch (FirebaseAuthUserCollisionException e){
                                        error = getString(R.string.email_collision);
                                        e.printStackTrace();
                                    }catch (Exception e){
                                        error = getString(R.string.exception);
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(Register.this, error, Toast.LENGTH_SHORT).show();

                                }
                                login_progress_bar_register.setVisibility(View.INVISIBLE);

                            }
                        });

                    }else {
                        Toast.makeText(Register.this, getString(R.string.senha_diferente), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
}

    private void abrirTelaPrincipal() {
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
