package com.example.rakshit.bss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginPage extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        email = findViewById(R.id.lemail);
        password = findViewById(R.id.lpassword);
        login = findViewById(R.id.llogin);

        firebaseAuth = FirebaseAuth.getInstance();

        ///login btn action here

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mail = email.getText().toString();
                final String pwd = password.getText().toString();

                if(mail.isEmpty() || pwd.isEmpty())
                {
                    showMessage("please verify  all field");
                }else
                {
                    Signin(mail,pwd);
                }





            }

            private void Signin(String mail, String pwd) {


                firebaseAuth.signInWithEmailAndPassword(mail,pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(loginPage.this,homePage.class);
                            startActivity(intent);
                        }else
                        {    Toast.makeText(loginPage.this,task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            private void showMessage(String s) {

                Toast.makeText(loginPage.this,s,Toast.LENGTH_LONG).show();
            }
        });



        ///forgot click action here
        findViewById(R.id.lforgot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginPage.this,forgotPage.class);
                startActivity(intent);

            }
        });

    }
}
