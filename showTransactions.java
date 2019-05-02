package com.example.admin.paytm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class showTransactions extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    TransactionAdapter adapter;
    ArrayList<String> transac_id;
    ArrayList<String> order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_transactiobs);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Bundle b = getIntent().getExtras();
        if (null != b) {
            transac_id = b.getStringArrayList("transactions_transid");
            order_id = b.getStringArrayList("transactions_orderid");
            Log.e("List", "Passed Array List :: " + transac_id+order_id);
        }
        callTransactionShow(transac_id,order_id);
    }

    public void callTransactionShow(ArrayList<String> transac_id, ArrayList<String> order_id) {
        adapter = new TransactionAdapter(this, transac_id,order_id);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}