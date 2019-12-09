package com.example.bss;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SimpleTimeZone;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton sendmessagebutton;
    private EditText usermessageInput;
    private ScrollView scrollView;
    private TextView displayTextmessage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef ,GroupNameRef,GroupMessageKeyRef;
    private String currentGroupName,currentUserId,courrentUserName,currentDate,currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);



        InitializeFields();
        GetuserInfo();
        sendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageDatabase();
                usermessageInput.setText("");
                scrollView.fullScroll(scrollView.FOCUS_DOWN);
            }
        });

    }

            @Override
            protected void onStart() {
                super.onStart();
                GroupNameRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists())
                        {
                            DisplayMessage(dataSnapshot);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        if(dataSnapshot.exists())
                        {
                            DisplayMessage(dataSnapshot);
                        }
                    }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void saveMessageDatabase() {

        String message = usermessageInput.getText().toString();
        String messagekey =GroupNameRef.push().getKey();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this,"please write message",Toast.LENGTH_LONG).show();

        }else {
            Calendar ccalfordate = Calendar.getInstance();
            SimpleDateFormat currentdateFormat = new SimpleDateFormat("MM DD, YYYY");
            currentDate = currentdateFormat.format(ccalfordate.getTime());

            Calendar ccalfortime = Calendar.getInstance();
            SimpleDateFormat currenttimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currenttimeFormat.format(ccalfortime.getTime());


            HashMap<String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);
            GroupMessageKeyRef= GroupNameRef.child(messagekey);
            HashMap<String,Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name",courrentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentDate);
            messageInfoMap.put("time",currentTime);
            GroupMessageKeyRef.updateChildren(messageInfoMap);

        }
    }

    private void GetuserInfo() {

        UserRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    courrentUserName = dataSnapshot.child("name").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void InitializeFields() {
        toolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentGroupName);
        sendmessagebutton = findViewById(R.id.send_message_button);
        usermessageInput = findViewById(R.id.input_group_message);
        scrollView = findViewById(R.id.my_scroll_view);
        displayTextmessage = findViewById(R.id.group_chat_text_display);
    }

   private void DisplayMessage(DataSnapshot dataSnapshot) {
        Iterator iterator  = dataSnapshot.getChildren().iterator();
        while(iterator.hasNext())
        {
            String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String)((DataSnapshot)iterator.next()).getValue();
            displayTextmessage.append(chatName+"\n" + chatMessage +" \n" + chatDate+"\n" + chatTime +"\n\n\n");

            scrollView.fullScroll(scrollView.FOCUS_DOWN);

        }
    }

}
