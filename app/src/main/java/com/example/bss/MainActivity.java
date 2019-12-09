package com.example.bss;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ViewPager mviewPager;
    private TabLayout mtabLayout;
    private TabsAccessorAdapter tabsAccessorAdapter;
    private FirebaseUser currentuser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //database connectivity
        RootRef = FirebaseDatabase.getInstance().getReference();

        // toolbar set in any page code is here

        mtoolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("BSS");

        //this code is the tab layout set in app

        mviewPager = findViewById(R.id.main_tabs_pager);
        tabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(tabsAccessorAdapter);
        mtabLayout = findViewById(R.id.main_tabs);
        mtabLayout.setupWithViewPager(mviewPager);

        //this code is for the firebase auth and user

        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_find_friends_option)
        {

        }
        if(item.getItemId() == R.id.main_create_group_option)
        {
            RequestNewgroup();

        }
        if(item.getItemId() == R.id.main_settings_option)
        {
            senduserTOSettionActivity();
        }

        if(item.getItemId() == R.id.main_Logout_option)
        {
            mAuth.signOut();
            senduserTOLoginActivity();
        }
        return true;
    }

    private void RequestNewgroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);

        builder.setTitle("Enter group Name :");


        final EditText groupNameField = new EditText(MainActivity.this);

        groupNameField.setHint("e.g BSS");

        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                {
                    String gname = groupNameField.getText().toString();

                    if(TextUtils.isEmpty(gname))
                    {
                        Toast.makeText(MainActivity.this,"Please write group name..", Toast.LENGTH_LONG).show();
                    }else
                    {
                            CreateNewGroup(gname);
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                {
                    dialog.cancel();
                }
            }
        });
        builder.show();

    }

    private void CreateNewGroup(final String groupname) {

        RootRef.child("Groups").child(groupname).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, groupname+" is created", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void senduserTOLoginActivity() {

        Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
    private void senduserTOSettionActivity() {

        Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(settingIntent);


    }


    @Override
    protected void onStart() {
        super.onStart();
      if(currentuser == null)
        {

            Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginIntent);


        }else
          {
            verifyUserExistance();
          }
    }

    private void verifyUserExistance() {
            String currentUserID = mAuth.getCurrentUser().getUid();

            RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if ((dataSnapshot.child("name").exists()))
                    {


                    }else
                    {
                        senduserTOSettingActivity();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void senduserTOSettingActivity() {

        Intent mainIntent = new Intent(MainActivity.this,SettingActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }
}
