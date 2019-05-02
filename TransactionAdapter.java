package com.example.admin.paytm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Admin on 15-04-2019.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder>{
    ArrayList<String> this_transac_id ;
    ArrayList<String> this_order_id;
    public  static Context context;
    //checksumforRefund ob;
    public TransactionAdapter(showTransactions showTransactions, ArrayList<String> transac_id, ArrayList<String> order_id) {
        this_transac_id = transac_id;
        this_order_id =order_id ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction,parent,false);
        context = v.getContext();
        return  new ViewHolder(v);}


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String transc_id= this_transac_id.get(position);
        final String transc_orderid = this_order_id.get(position);
        holder.transaction_transid.setText(transc_id);
        holder.transaction_orderid.setText(transc_orderid);
        holder.refund.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Log.e("Reached","REached here");
                  Intent in = new Intent(v.getContext(), com.example.admin.paytm.checksumforRefund.class);
                     in.putExtra("transac_id",transc_id);
                   in.putExtra("order_id",transc_orderid);
                  v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this_transac_id!=null)
        {return this_transac_id.size();}
        else
            return  0;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView transaction_transid;
        public  TextView transaction_orderid;
        public Button refund;
        public ViewHolder(View view) {
            super(view);
            transaction_transid = (TextView)view.findViewById(R.id.transaction_transacid);
            transaction_orderid =(TextView)view.findViewById(R.id.transaction_orderid);
            refund =(Button)view.findViewById(R.id.refund);
        }

    }
}
