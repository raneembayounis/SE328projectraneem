package com.example.SE328projectraneem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FireBaseActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference ref;

    EditText first_name;
    EditText last_name;
    EditText email_user;
    EditText user_id;
    EditText phone_number;

    Button button_insert;
    Button button_delete;
    Button button_update;
    Button button_select;

    RequestQueue rq;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base);

        try {
            database = FirebaseDatabase.getInstance();
            ref = database.getReference("users");
            if(ref == null) {
                Toast.makeText(FireBaseActivity.this, "Exception.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
//            Toast.makeText(FireBaseActivity.this,"Exception."+e,Toast.LENGTH_LONG).show();

        }
//        finally{
//            Toast.makeText(FireBaseActivity.this,"Exception.",Toast.LENGTH_LONG).show();
//        }

        rq= Volley.newRequestQueue(this);
        rq.add(Helper.weather(this));

        button_insert=findViewById(R.id.bttn_insert);
        button_delete=findViewById(R.id.bttn_delete);
        button_select=findViewById(R.id.bttnx_select);
        button_update=findViewById(R.id.bttnx_update);

//        email_user=findViewById(R.id.xemail);
//        user_id=findViewById(R.id.xuid);
//        button_select=findViewById(R.id.xselect);
//        button_update=findViewById(R.id.xupdate);

        first_name=findViewById(R.id.inp_fire_fname);
        last_name=findViewById(R.id.inp_fire_lname);
        phone_number=findViewById(R.id.inp_fire_phone);
        email_user=findViewById(R.id.inp_fire_email);
        user_id=findViewById(R.id.inp_fire_uid);

        user = new User();

        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FireBaseActivity.this,FirebaseList.class));
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uid=user_id.getText()+"";
                if (uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }

                delete(Integer.valueOf(uid));
            }
        });

        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=first_name.getText()+"";
                String lname=last_name.getText()+"";
                String phone=phone_number.getText()+"";
                String email=email_user.getText()+"";
                String uid=user_id.getText()+"";

                if (fname.isEmpty() || lname.isEmpty() || phone.isEmpty() || email.isEmpty() || uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"All fields are required.",Toast.LENGTH_SHORT).show();
                    return; }
                insertUser(fname,email,lname,phone,Integer.valueOf(uid));

            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname=first_name.getText()+"";
                String lname=last_name.getText()+"";
                String phone=phone_number.getText()+"";
                String email=email_user.getText()+"";
                String uid=user_id.getText()+"";

                HashMap<String,Object> myMap = new HashMap<>();

                if (uid.isEmpty()){
                    Toast.makeText(FireBaseActivity.this,"University ID field is required.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!fname.isEmpty()){
                    myMap.put("firstName",fname);
                }
                if (!lname.isEmpty()){
                    myMap.put("lastName",lname);
                }
                if (!phone.isEmpty()){
                    myMap.put("phoneNumber",phone);
                }
                if (!email.isEmpty()){
                    myMap.put("emailAddress",email);
                }
                if (!uid.isEmpty()){
                    myMap.put("userId",Integer.valueOf(uid));
                }
                update(Integer.valueOf(uid),myMap);
            }
        });

    }
    


    private void insertUser(String fName,String email,String lName,String phone,int userId){

        
try {

        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Toast.makeText(FireBaseActivity.this,"adding.",Toast.LENGTH_LONG).show();

                for (DataSnapshot child:snapshot.getChildren()){

                    if (child.child("userId").getValue(Integer.class)==userId){

                        Toast.makeText(FireBaseActivity.this,"A record with UID "+userId+" was found.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int counterx = (int)snapshot.getChildrenCount();
                Toast.makeText(FireBaseActivity.this,+counterx+"",Toast.LENGTH_SHORT).show();
                while(snapshot.hasChild(counterx+"")){
                    counterx++;
                }

                DatabaseReference insertRef = ref.child(counterx+"");

                insertRef.child("emailAddress").setValue(email);
                insertRef.child("firstName").setValue(fName);
                insertRef.child("lastName").setValue(lName);
                insertRef.child("phoneNumber").setValue(phone);
                insertRef.child("userId").setValue(userId);
//                Toast.makeText(FireBaseActivity.this,"Data inserted successfully.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FireBaseActivity.this,"Error inserting data.",Toast.LENGTH_LONG).show();

            }
        });
}catch (Exception e){
    Toast.makeText(FireBaseActivity.this,"Error inserting data.",Toast.LENGTH_LONG).show();

}
    }

    private void updateUserAllDetails(String email,String fName,String lName,String phone,int userId){

        Map<String,Object> stringObjMap = new HashMap<>();
        stringObjMap.put("firstName",fName);
        stringObjMap.put("lastName",lName);
        stringObjMap.put("emailAddress",email);
        stringObjMap.put("phoneNumber",phone);
        stringObjMap.put("userId",userId);
        update(userId,stringObjMap);
    }


    private void update(int uid,Map<String,Object> keyValMap){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){

                        ref.child(child.getKey()).updateChildren(keyValMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBaseActivity.this,"Record updated successfully.",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBaseActivity.this,"Error Detected: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBaseActivity.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void delete(int uid){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child:snapshot.getChildren()){
                    if (child.child("userId").getValue(Integer.class)==uid){
                        ref.child(child.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FireBaseActivity.this,"Record deleted successfully.",Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FireBaseActivity.this,"Error Detected: "+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                        return;
                    }
                }
                Toast.makeText(FireBaseActivity.this,"No such record found.",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}


@IgnoreExtraProperties
class User{


    int userId;
    String emailAddress;
    String phoneNumber;
    String firstName;
    String lastName;

    public User(){
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}