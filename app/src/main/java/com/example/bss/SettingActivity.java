package com.example.bss;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.CircularPropagation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText username,userstatus;
    private CircleImageView userprofileImage;
    private String currentuserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        currentuserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializaFields();
    }

    private void InitializaFields() {
        updateAccountSettings = findViewById(R.id.update_settings_button);
        username = findViewById(R.id.set_user_name);
        userstatus = findViewById(R.id.update_status);
        userprofileImage = findViewById(R.id.profile_image);

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                udpatesettings();
            }
        });


       // RetrieveUserInfo();
    }

    /*private void RetrieveUserInfo() {

        RootRef.child("Users").child(currentuserID).addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });

     }

 */
    private void udpatesettings() {

        String setUsername = username.getText().toString();
        String setstatus = userstatus.getText().toString();
            if(TextUtils.isEmpty(setUsername) || TextUtils.isEmpty(setstatus))
            {
                Toast.makeText(SettingActivity.this,"please enter the user and status",Toast.LENGTH_LONG).show();
            }else
            {
                HashMap<String,String> profileMap = new HashMap<>();
                        profileMap.put("uid",currentuserID);
                        profileMap.put("name", setUsername);
                        profileMap.put("status",setstatus);

                 RootRef.child("Users").child(currentuserID).setValue(profileMap)
                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task)
                     {
                            if(task.isSuccessful())
                            {
                                sendUserToMainActivity();
                                Toast.makeText(SettingActivity.this,"profile update successfuly",Toast.LENGTH_LONG).show();
                            }else
                            {
                                String message  = task.getException().toString();
                                Toast.makeText(SettingActivity.this,"Error"+message,Toast.LENGTH_LONG).show();

                            }
                     }
                 });
            }

    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(SettingActivity.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
}
