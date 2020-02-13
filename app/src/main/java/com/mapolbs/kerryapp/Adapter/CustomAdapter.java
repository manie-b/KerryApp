package com.mapolbs.kerryapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mapolbs.kerryapp.BookingActivity;
import com.mapolbs.kerryapp.CustomerPageActivity;
import com.mapolbs.kerryapp.Model.Model;
import com.mapolbs.kerryapp.R;
import com.mapolbs.kerryapp.SignatureActivity;

import java.util.List;
import java.util.zip.Inflater;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    //private LayoutInflater inflater;
    private Context ctx;

    private List<Model> modelList;


    public CustomAdapter(Context ctx, List<Model> modelArrayList) {
        //this.inflater = inflater;
        this.ctx = ctx;
        this.modelList=modelArrayList;
    }


    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater= LayoutInflater.from(ctx);
        View view=inflater.inflate(R.layout.recycler_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        Model model=modelList.get(position);

        //binding the data with the viewholder views
        holder.txt_customerName.setText(model.getCustomername());
        holder.btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx,BookingActivity.class);
                ctx.startActivity(intent);
            }
        });

        holder.btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx,SignatureActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected Button btnSign,btnBooking;
        private TextView txt_customerName;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btnSign=itemView.findViewById(R.id.btn_signature);
            btnBooking=itemView.findViewById(R.id.btn_booking);
            txt_customerName=itemView.findViewById(R.id.customer_name);
            cardView=itemView.findViewById(R.id.card_item);

            btnBooking.setOnClickListener(this);
            btnSign.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (view.getId()==btnSign.getId())
            {
                Intent intent=new Intent(ctx, SignatureActivity.class);
                ctx.startActivity(intent);
            }else if (view.getId()==btnBooking.getId())
            {
                Intent intent=new Intent(ctx,BookingActivity.class);
                ctx.startActivity(intent);
            }

        }
    }
}
