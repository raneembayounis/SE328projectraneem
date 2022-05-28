package com.example.SE328projectraneem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SQLITEActivity extends AppCompatActivity {


    EditText first_name;
    EditText last_name;
    EditText phone_number;
    EditText email_address;
    EditText user_id;

    Button button_insert;
    Button button_update;
    Button button_delete;
    Button button_insert_firebase;
    Button button_select_options;

    FirebaseDatabase database;
    DatabaseReference ref;

    private void setup() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_i_t_e);
        setup();
        DBHelper dbHelper = new DBHelper(this);

        first_name = findViewById(R.id.inp_sql_fname);
        last_name = findViewById(R.id.inp_sql_lname);
        phone_number = findViewById(R.id.inp_sql_phone);
        email_address = findViewById(R.id.inp_sql_email);
        user_id = findViewById(R.id.inp_sql_uid);

        button_insert = findViewById(R.id.bttn_sql_insert);
        button_update = findViewById(R.id.bttn_sql_update);
        button_delete = findViewById(R.id.btn_sql_delete);
        button_select_options = findViewById(R.id.btn_sql_select_options);
        button_insert_firebase = findViewById(R.id.bttn_sql_fire);


        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fname = first_name.getText() + "";
                String lname = last_name.getText() + "";
                String phone = phone_number.getText() + "";
                String email = email_address.getText() + "";
                String uid = user_id.getText() + "";

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() || uid.isEmpty()) {

                    Toast.makeText(SQLITEActivity.this, "All field required.", Toast.LENGTH_SHORT).show();

                }

                int x = dbHelper.insert(fname, lname, phone, email, Integer.valueOf(uid));

                if (x != -1) {
                    Toast.makeText(SQLITEActivity.this, "Record inserted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error inserting .", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = first_name.getText() + "";
                String lname = last_name.getText() + "";
                String phone = phone_number.getText() + "";
                String email = email_address.getText() + "";
                String uid = user_id.getText() + "";

                if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || phone.isEmpty() || uid.isEmpty()) {

                    Toast.makeText(SQLITEActivity.this, "All field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int y = dbHelper.updateUser(fname, lname, phone, email, Integer.valueOf(uid));
                if (y > 0) {
                    Toast.makeText(SQLITEActivity.this, "Record updated successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error updating", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = user_id.getText() + "";

                if (uid.isEmpty()) {
                    Toast.makeText(SQLITEActivity.this, "University ID field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int z = dbHelper.deleteByUID(Integer.valueOf(uid));

                if (z > 0) {
                    Toast.makeText(SQLITEActivity.this, "Record deleted successfully.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SQLITEActivity.this, "Error in deletion", Toast.LENGTH_SHORT).show();

                }
            }
        });

        button_insert_firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid = user_id.getText() + "";

                if (uid.isEmpty()) {
                    Toast.makeText(SQLITEActivity.this, "University ID field required.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot childData : snapshot.getChildren()) {
                            if (childData.child("userId").getValue(Integer.class) == Integer.valueOf(uid)) {
                                User u = snapshot.child(childData.getKey()).getValue(User.class);
                                int k = dbHelper.insert(u);

                                if (k != -1) {
                                    Toast.makeText(SQLITEActivity.this, "Record inserted successfully.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SQLITEActivity.this, "Error inserting.", Toast.LENGTH_SHORT).show();
                                }
                                return;
                            }
                        }
                        Toast.makeText(SQLITEActivity.this, "No such University ID found.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



        button_select_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SQLITEActivity.this, SQLSelect.class));
            }
        });



    }


}