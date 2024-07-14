package com.apps.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public int Position;
    Button button;
    TextView partNumber, srmNumber, location, spareTitle;
    Spinner GunId, Spare;
    DBHelper myDbHelper;
    String gun_id, spare_name;
    String[] spare = {"Bracket Fix", "Bracket Moving", "Gun Arm Fix", "Gun Arm Moving", "Arm Sleeve Fix", "Arm Sleeve Moving", "Shank Fix", "Shank Moving", "Link Fix", "Link Moving", "Seal Kit", "Insulation Kit", "Cylinder", "Rotation Assy", "Transformer"};

    public int getPosition() {
        return Position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myDbHelper = new DBHelper(this);
        try {
            myDbHelper.createDatabase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDatabase();

        } catch (SQLException sqle) {

            throw sqle;
        }
        button = findViewById(R.id.button);
        partNumber = findViewById(R.id.part_number);
        srmNumber = findViewById(R.id.srm_number);
        location = findViewById(R.id.location);
        spareTitle = findViewById(R.id.spare_title);
        GunId = findViewById(R.id.gun_id);
        Spare = findViewById(R.id.spare);

        ArrayAdapter spareAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spare);
        spareAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spare.setAdapter(spareAdapter);
        Spare.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Position = Spare.getSelectedItemPosition();
                spare_name = Spare.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GunId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gun_id = GunId.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Integer i = new Integer(Position);
         String pos = String.valueOf(i);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<String> GunID = dbHelper.getList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, GunID);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GunId.setAdapter(arrayAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String PartNumber = myDbHelper.getPartNo(gun_id);
//                String SRMNumber = myDbHelper.getSrmNo(gun_id);
//                String Location = myDbHelper.getLocation(gun_id);

//                partNumber.setText(PartNumber);
//                srmNumber.setText(SRMNumber);
//                location.setText(Location);
//                spareTitle.setText(gun_id + " : " + spare_name);
//            }
//        });
//    }
//}
                Cursor result = myDbHelper.getPartNo(gun_id);
                if (result.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entry", Toast.LENGTH_SHORT).show();
                }else {
                    String string1 = "";
                    while (result.moveToNext()) {
                        string1 = (result.getString(Position+1));
                        }
                    Cursor result1 = myDbHelper.getSrmNo(gun_id);
                    if (result1.getCount()==0){
                        Toast.makeText(MainActivity.this, "No Entry", Toast.LENGTH_SHORT).show();
                    }else {
                        String string2 = "";
                        while (result1.moveToNext()) {
                            string2=(result1.getString(Position+1));
                        }
                        Cursor result2 = myDbHelper.getLocation(gun_id);
                        if (result2.getCount()==0){
                            Toast.makeText(MainActivity.this, "No Entry", Toast.LENGTH_SHORT).show();
                        }else {
                            String string3 = "";
                            while (result2.moveToNext()) {
                                string3=(result2.getString(Position+1));
                            }
                    partNumber.setText(string1);
                    srmNumber.setText(string2);
                    location.setText(string3);
                    spareTitle.setText(gun_id + " : " + spare_name);
                }
            }
        }

    }
 });
    }
}
