package com.example.bss;




import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {



    private EditText email;
    private EditText password;
    private Button signup;
    private Button login;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private DatabaseReference RootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        email = findViewById(R.id.etemail);
        password = findViewById(R.id.etpassword);
        signup = findViewById(R.id.btnsignup);
        login = findViewById(R.id.btnlogin);
        loadingBar = new ProgressDialog(this);








        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mail = email.getText().toString();
                final String pwd = password.getText().toString();

                if(mail.isEmpty() || pwd.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"please verify  all field",
                            Toast.LENGTH_LONG).show();
                }
                else{

                    loadingBar.setTitle("create New Account");
                    loadingBar.setMessage("please wait, while we where creating new Account for you..");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();

                    mAuth.createUserWithEmailAndPassword(mail,
                            pwd)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                        //database in store values
                                        String currentUserID = mAuth.getCurrentUser().getUid();
                                        RootRef.child("Users").child(currentUserID).setValue("");

                                        sendUserToMainActivity();

                                        Toast.makeText(RegisterActivity.this,"register successfull",
                                                Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }else{
                                        Toast.makeText(RegisterActivity.this,task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });

                }



            }



            private void showMessage(String s) {


                Toast.makeText(RegisterActivity.this,s,Toast.LENGTH_LONG).show();
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });


    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void sendUserToLoginActivity() {



            Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(loginIntent);


    }

}
