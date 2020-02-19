package com.elab.yourvoice;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class GroupchatActivity extends AppCompatActivity {
    private Toolbar mtoolbar;
    private ImageButton sendbtn;
    private EditText usermsg;
    private ScrollView mscroll;
    private TextView displaytextmsgs;
    private FirebaseAuth mauth;
    private DatabaseReference userref, grpnameref, grpmsgkeyref;
    private String currentgroupname, currentuserid, currentusername, currentdate, currenttime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        currentgroupname = getIntent().getExtras().get("groupname").toString();
        mauth = FirebaseAuth.getInstance();
        currentuserid = mauth.getCurrentUser().getUid();
        userref = FirebaseDatabase.getInstance().getReference().child("Users");
        grpnameref = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroupname);
        initializefields();
        getusetrinfo();
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmsgtodatabse();
                usermsg.setText("");
                mscroll.fullScroll(ScrollView.FOCUS_DOWN);

            }


        });

        //Toast.makeText(GroupchatActivity.this,currentgroupname,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        grpnameref.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    Displaymsgs(dataSnapshot);
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Displaymsgs(dataSnapshot);

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


    private void initializefields() {
        mtoolbar = (Toolbar) findViewById(R.id.group_chat_bar_layout);
        //currentgroupname=getIntent().getExtras().get("groupname").toString();
        //Toast.makeText(GroupchatActivity.this,currentgroupname,Toast.LENGTH_LONG).show();
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentgroupname);
        sendbtn = (ImageButton) findViewById(R.id.sendbutton);
        usermsg = (EditText) findViewById(R.id.input_group_msg);
        displaytextmsgs = (TextView) findViewById(R.id.group_chat_text_display);
        mscroll = (ScrollView) findViewById(R.id.myscroll_view);
    }

    private void getusetrinfo() {
        userref.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentusername = dataSnapshot.child("name").getValue().toString();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendmsgtodatabse() {
        String msg = usermsg.getText().toString();
        String msgkey = grpnameref.push().getKey();
        if (TextUtils.isEmpty(msg)) {
            Toast.makeText(GroupchatActivity.this, "please write message first", Toast.LENGTH_LONG).show();
        } else {
            Calendar calfordate = Calendar.getInstance();
            SimpleDateFormat currentdateformat = new SimpleDateFormat("MMM dd,yyyy");
            currentdate = currentdateformat.format(calfordate.getTime());
            Calendar calfortime = Calendar.getInstance();
            SimpleDateFormat currenttimeformat = new SimpleDateFormat("hh:mm a");
            currenttime = currenttimeformat.format(calfortime.getTime());
            HashMap<String, Object> grpmsgkey = new HashMap<>();
            grpnameref.updateChildren(grpmsgkey);
            grpmsgkeyref = grpnameref.child(msgkey);
            HashMap<String, Object> msginfomap = new HashMap<>();
            msginfomap.put("name", currentusername);
            msginfomap.put("message", msg);

            msginfomap.put("Date", currentdate);

            msginfomap.put("Time", currenttime);
            grpmsgkeyref.updateChildren(msginfomap);


        }
    }

    private void Displaymsgs(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatdate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chattime = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatmsg = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatname = (String) ((DataSnapshot) iterator.next()).getValue();


            displaytextmsgs.append(chatname + ":\n" + chatmsg + "\n" + chattime + "  " + chatdate + "\n\n\n");
            mscroll.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }


        }




