package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapolbs.kerryapp.Model.SpinnerData;

import java.util.ArrayList;
import java.util.List;

public class TypeSelectionActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerType_selection,spinneroutputType;
    ListView listPickup;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);

        //init
        spinnerType_selection=findViewById(R.id.spin_type);
        spinneroutputType=findViewById(R.id.spin_output_type);
        btnNext=findViewById(R.id.btn_next);

        spinnerType_selection.setOnItemSelectedListener(this);
        spinneroutputType.setOnItemSelectedListener(this);

        /*Drop down - start*/
        List<String> selectionArray=new ArrayList<>();
        selectionArray.add("Select");
        selectionArray.add("Pickup Sheet");
        selectionArray.add("Normal");

        // Creating adapter for spinner
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,selectionArray);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(R.layout.spinner);

        // attaching data adapter to spinner
        spinnerType_selection.setAdapter(adapter);
        /*Drop down elements - end*/


        /*listview parts - start*/
        // Store string resources into an Array
        List<SpinnerData> arrayofOTspin=new ArrayList<>();
        SpinnerData spinnerData=new SpinnerData("");
        SpinnerData spinnerData1=new SpinnerData("202122UV");
        SpinnerData spinnerData2=new SpinnerData("232425WX");
        SpinnerData spinnerData3=new SpinnerData("262728YZ");
        SpinnerData spinnerData4=new SpinnerData("293031ABCD");
        SpinnerData spinnerData5=new SpinnerData("32334EFGH");
        SpinnerData spinnerData6=new SpinnerData("12345ABCD");
        SpinnerData spinnerData7=new SpinnerData("67891EFGH");
        SpinnerData spinnerData8=new SpinnerData("11123IJKL");
        SpinnerData spinnerData9=new SpinnerData("14516MNOP");
        SpinnerData spinnerData10=new SpinnerData("17819QRST");
        arrayofOTspin.add(spinnerData);
        arrayofOTspin.add(spinnerData1);
        arrayofOTspin.add(spinnerData2);
        arrayofOTspin.add(spinnerData3);
        arrayofOTspin.add(spinnerData4);
        arrayofOTspin.add(spinnerData5);
        arrayofOTspin.add(spinnerData6);
        arrayofOTspin.add(spinnerData7);
        arrayofOTspin.add(spinnerData8);
        arrayofOTspin.add(spinnerData9);
        arrayofOTspin.add(spinnerData10);


        ArrayAdapter<SpinnerData> arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayofOTspin);
        arrayAdapter.setDropDownViewResource(R.layout.spinner);
        spinneroutputType.setAdapter(arrayAdapter);

        spinneroutputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerData spinnData= (SpinnerData) parent.getSelectedItem();
                displaySpinnerData(spinnData);
                //getSelectedUser(view);
                btnNext.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*send data to next activity*/
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TypeSelectionActivity.this,CustomerPageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("pickup",spinneroutputType.getSelectedItem().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void getSelectedUser(View view)
    {
        SpinnerData spinnerData= (SpinnerData) spinneroutputType.getSelectedItem();
        displaySpinnerData(spinnerData);
    }

    private void displaySpinnerData(SpinnerData spinnerData) {
        String pickupno=spinnerData.getPickupData();

        String data="Pickup No"+pickupno;
        //Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item=parent.getItemAtPosition(position).toString();

        if (parent.getItemAtPosition(position).equals("Select"))
        {
            spinneroutputType.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }else if (parent.getItemAtPosition(position).equals("Pickup Sheet"))
        {
            spinneroutputType.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
        }else if (parent.getItemAtPosition(position).equals("Normal"))
        {
            btnNext.setVisibility(View.GONE);
            Intent intent=new Intent(TypeSelectionActivity.this,BookingActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
