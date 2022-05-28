package com.example.SE328projectraneem;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class SQLSelect extends AppCompatActivity {


    ArrayList<User> myUsers;
    DBHelper sql_dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_q_l_select);

        sql_dbHelper = new DBHelper(this);

        EditText inp_uid = findViewById(R.id.inp_sql_select_uid);
        Button select = findViewById(R.id.bttn_sqlselect);
        ListView listx = findViewById(R.id.sql_list);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = inp_uid.getText()+"";

                Cursor c;

                if (uid.isEmpty()){
                    c = sql_dbHelper.selectAll();
                }
                else{
                    c = sql_dbHelper.selectByUID(Integer.valueOf(uid));
                }

                if (c==null){
                    Toast.makeText(SQLSelect.this,"No match found.",Toast.LENGTH_LONG).show();
                    return;
                }

                myUsers.clear();

                do {
                    User myUser = new User();
                    myUser.firstName = c.getString(0);
                    myUser.lastName = c.getString(1);
                    myUser.phoneNumber = c.getString(2);
                    myUser.emailAddress = c.getString(3);
                    myUser.userId = c.getInt(4);
                    myUsers.add(myUser);
                }while (c.moveToNext());

                ((BaseAdapter)listx.getAdapter()).notifyDataSetChanged();
            }
        });

        myUsers=new ArrayList<>();

        listx.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return myUsers.size();
            }

            @Override
            public Object getItem(int position) {
                return myUsers.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                User x = myUsers.get(position);

                TableLayout table=(TableLayout) LayoutInflater.from(SQLSelect.this).inflate(R.layout.list_item,parent,false);

                TextView output_uid=table.findViewById(R.id.out_fb_uid);
                TextView output_name=table.findViewById(R.id.out_fb_name);
                TextView output_phone=table.findViewById(R.id.out_fb_phone);
                TextView output_email=table.findViewById(R.id.out_fb_email);

                output_uid.setText(""+x.userId);
                output_name.setText(x.firstName+" "+x.lastName);
                output_phone.setText(x.phoneNumber);
                output_email.setText(x.emailAddress);

                return table;
            }
        });
        listx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u= myUsers.get(position);

                Toast.makeText(SQLSelect.this,u.getFirstName()+" "+ u.getLastName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}