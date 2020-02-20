package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TypeSelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerType_selection;
    ListView listPickup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);


        spinnerType_selection=findViewById(R.id.spin_type);

        spinnerType_selection.setOnItemSelectedListener(this);

        //Drop down elements
        List<String> selectionArray=new ArrayList<>();
        selectionArray.add("Select");
        selectionArray.add("Pickup Sheet");
        selectionArray.add("Normal");

        // Creating adapter for spinner
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,selectionArray);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerType_selection.setAdapter(adapter);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        listPickup=findViewById(R.id.list_pickup);

        ArrayList<String> pickupArray=new ArrayList<>();
        pickupArray.add("ABCD12345");
        pickupArray.add("EFGH67890");
        pickupArray.add("IJKL1112");
        pickupArray.add("MNOP1314");
        pickupArray.add("QRST1516");

        // On selecting a spinner item
        String item=parent.getItemAtPosition(position).toString();

        if (item.compareToIgnoreCase("pickupArray")==0)
        {
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,pickupArray);
            listPickup.setAdapter(adapter);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
