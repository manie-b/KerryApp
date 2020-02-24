package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mapolbs.kerryapp.Adapter.CustomAdapter;
import com.mapolbs.kerryapp.Model.Model;

import java.util.ArrayList;
import java.util.List;

public class CustomerPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public TextView txtPickupsheetno;
    List<Model> modelArrayList;

    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        //init
        recyclerView=findViewById(R.id.recyclerview_custom);
        txtPickupsheetno=findViewById(R.id.txt_pickupsheetno);

        /*Intent intent=getIntent();
        pickup=intent.getStringExtra("pickup");*/
        Bundle bundle=this.getIntent().getExtras();
        //list data-Declare variable
        String pickup=bundle.getString("pickup");
        // Set the string into TextView
        txtPickupsheetno.setText(pickup);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelArrayList=new ArrayList<>();

        //adding some items to our list
        modelArrayList.add(new Model("Ashok Leyland Ltd"));
        modelArrayList.add(new Model("Brakes India Pvt Ltd"));
        modelArrayList.add(new Model("Isuzu Motors India Pvt Ltd"));
        modelArrayList.add(new Model("MRF Ltd"));
        modelArrayList.add(new Model("Nelcast Ltd"));
        modelArrayList.add(new Model("Royal Enfield (Unit of Eicher Motors)"));
        modelArrayList.add(new Model("Tractors and Farm Equipment Ltd (TAFE)"));
        modelArrayList.add(new Model("WABCO India Ltd"));

        CustomAdapter adapter=new CustomAdapter(this,modelArrayList);
        recyclerView.setAdapter(adapter);

    }


}
