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
import com.google.firebase.auth.FirebaseAuth;

public class forgotPage extends AppCompatActivity {

    Button login;
    EditText email;
    Button resetlink;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_page);

        email =  findViewById(R.id.femail);
        resetlink = findViewById(R.id.fsendlink);
        login =  findViewById(R.id.flogin);

        firebaseAuth = FirebaseAuth.getInstance();

        resetlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mail = email.getText().toString();

                if(mail.isEmpty())
                {
                    showMessage("please verify  all field");
                }
                else {
                    resetpwd(mail);
                }
            }

            private void resetpwd(String mail) {
                firebaseAuth.sendPasswordResetEmail(mail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(forgotPage.this,
                                    "Password reset eamil  sent!",Toast.LENGTH_LONG).show();
                            finish();
                            startActivity(new Intent(forgotPage.this,loginPage.class));

                        }else{
                            Toast.makeText(forgotPage.this,task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }

            private void showMessage(String s) {
                Toast.makeText(forgotPage.this,s,Toast.LENGTH_LONG).show();
            }
        });















        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( forgotPage.this,loginPage.class);
                startActivity(intent);

            }
        });

    }
}
