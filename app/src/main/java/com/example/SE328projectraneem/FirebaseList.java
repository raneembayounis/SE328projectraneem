package com.example.SE328projectraneem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseList extends AppCompatActivity {

    ArrayList<User> users=new ArrayList<>();
    ArrayList<User> fetchedUsers=new ArrayList<>();

    ListView myList;
    EditText inp_uid_select;

    int uidDisplay=-1; //-1 means all
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_list);

        setup();


        inp_uid_select=findViewById(R.id.inp_uid_selct);

        TextWatcher tw =new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    uidDisplay=-1;
                }else{
                    uidDisplay=Integer.valueOf(s.toString());
                }
                update();
            }
        };


        inp_uid_select.addTextChangedListener(tw);

        myList=findViewById(R.id.lv_firebase);

        myList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return users.size();
            }

            @Override
            public Object getItem(int position) {
                return users.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User myUser = users.get(position);
                System.out.println(myUser);
                TableLayout table=(TableLayout) LayoutInflater.from(FirebaseList.this).inflate(R.layout.list_item,parent,false);

                TextView output_uid = table.findViewById(R.id.out_fb_uid);
                TextView output_name = table.findViewById(R.id.out_fb_name);
                TextView output_phone = table.findViewById(R.id.out_fb_phone);
                TextView output_email = table.findViewById(R.id.out_fb_email);

//                TextView output_uid=findViewById(R.id.out_fb_uid);
//                TextView output_name=findViewById(R.id.out_fb_name);
//                TextView output_phone=findViewById(R.id.out_fb_phone);
//                TextView output_email=findViewById(R.id.out_fb_email);

                output_uid.setText(""+myUser.userId);
                output_name.setText(myUser.firstName+" "+myUser.lastName);
                output_phone.setText(myUser.phoneNumber);
                output_email.setText(myUser.emailAddress);

                return table;

            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                fetchedUsers = snapshot.getValue(new GenericTypeIndicator<ArrayList<User>>() {});

                ArrayList<User> newUsersList = new ArrayList<>();
                for (User u:fetchedUsers){
                    if (u != null){
                        newUsersList.add(u);
                    }
                }

                fetchedUsers = newUsersList;
                update();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FETCH",error.getMessage());
                Toast.makeText(FirebaseList.this,"Error Detected: "+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    FirebaseDatabase database;
    DatabaseReference ref;


    private void setup(){
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users"); }


    private void update(){
        if (uidDisplay != -1){
            for (User u:fetchedUsers) {
                if (uidDisplay == u.getUserId()){
                    users.clear();
                    users.add(u);
                }
            }
        }
        else{
            users.clear();
            users.addAll(fetchedUsers);
        }

        ((BaseAdapter)myList.getAdapter()).notifyDataSetChanged();
    }


}