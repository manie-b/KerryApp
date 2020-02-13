package com.mapolbs.kerryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.mapolbs.kerryapp.Adapter.CustomAdapter;
import com.mapolbs.kerryapp.Model.Model;

import java.util.ArrayList;
import java.util.List;

public class CustomerPageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<Model> modelArrayList;

    private CustomAdapter customAdapter;
    private String[] Customerlist=new String[]{"Britania","vizibee","Kerry","L&T","Ucal","Murugan","Natarajan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        //init
        recyclerView=findViewById(R.id.recyclerview_custom);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        modelArrayList=new ArrayList<>();

        //adding some items to our list
        modelArrayList.add(new Model("Kerry"));
        modelArrayList.add(new Model("Murugan"));
        modelArrayList.add(new Model("Natarajan"));
        modelArrayList.add(new Model("Mani"));
        modelArrayList.add(new Model("Mapol"));

        CustomAdapter adapter=new CustomAdapter(this,modelArrayList);
        recyclerView.setAdapter(adapter);

    }


}
