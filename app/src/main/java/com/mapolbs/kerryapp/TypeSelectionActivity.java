package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

        //init
        spinnerType_selection=findViewById(R.id.spin_type);

        spinnerType_selection.setOnItemSelectedListener(this);

        /*Drop down - start*/
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
        /*Drop down elements - end*/


        /*listview parts - start*/

        // Store string resources into an Array
        String[] pickupListArray=new String[]{"Galaxy S","Galaxy S2","Galaxy Note","Galaxy Beam","Galaxy Ace Plus"};
        listPickup=findViewById(R.id.list_pickup);

        // Bind array strings into an adapter
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                android.R.id.text1,pickupListArray);
        listPickup.setAdapter(arrayAdapter);

        //ListView item click
        listPickup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String pickup= (String) listPickup.getItemAtPosition(i);
                Intent intent=new Intent(TypeSelectionActivity.this,CustomerPageActivity.class);
                intent.putExtra("pickup",pickup);
                startActivity(intent);
            }
        });

        /*listview parts - end*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item=parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
