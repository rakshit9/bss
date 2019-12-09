package com.example.bss;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseUser currentuser;
    private Button loginbtn,phonebtn;
    private EditText uemail,upassword;
    private TextView needNewAccountLink,forgotPasswordLink;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
            currentuser = mAuth.getCurrentUser();

        InitializeFileds();



        forgotPasswordLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgot = new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(forgot);

            }
        });

        needNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent createaccount = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(createaccount);
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = uemail.getText().toString();
                String pwd = upassword.getText().toString();

                if(mail.isEmpty() || pwd.isEmpty())
                {
                    Toast.makeText(LoginActivity.this,"please verify  all field",
                            Toast.LENGTH_LONG).show();
                }
                else {

                    loadingBar.setTitle("create New Account");
                    loadingBar.setMessage("please wait, while we where creating new Account for you..");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    mAuth.signInWithEmailAndPassword(mail, pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                            sendUserToMainActivity();
                                   /*     Toast.makeText(LoginActivity.this,"Login Successful",
                                                Toast.LENGTH_LONG).show();*/
                                        loadingBar.dismiss();
                                    }else
                                    {
                                        String message =task.getException().toString();
                                        Toast.makeText(LoginActivity.this,message,
                                                Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });

                }

            }
        });









    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentuser!=null)
        {
            sendUserToMainActivity();
        }
    }


    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void InitializeFileds() {

        loginbtn = findViewById(R.id.log_btn_login);
        phonebtn = findViewById(R.id.log_btn_phone);
        uemail =   findViewById(R.id.log_et_email);
        upassword= findViewById(R.id.log_et_password);
        needNewAccountLink = findViewById(R.id.log_tv_newaccout);
        forgotPasswordLink = findViewById(R.id.log_tv_forget);
        loadingBar = new ProgressDialog(this);


    }


}
