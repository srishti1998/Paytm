package com.example.admin.paytm;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Admin on 26-04-2019.
 */

public class SendRequestforRefund  extends AppCompatActivity {
    String Checksum , Mid , transid,refundid,order;
    public void execute(String CHECKSUMHASH, String mid, String transac_id, String refid, String orderid) {
       Checksum = CHECKSUMHASH;
       Mid=mid;
       transid =transac_id;
       refundid =refid;
       order= orderid;
    sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(SendRequestforRefund.this);
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            String transactionURL = "https://securegw-stage.paytm.in/refund/process";
            String merchantMid = "eNnjXe00637647587210";
            String transactionType = "REFUND";
            String refundAmount = "12";
            String transactionId = transid;
            String refId = refundid;
            String comment = "comment string";
            TreeMap<String, String> paytmParams = new TreeMap<String, String>();
            paytmParams.put("MID", merchantMid);
            paytmParams.put("REFID", refId);
            paytmParams.put("CHECKSUM", Checksum);
            paytmParams.put("TXNID", transactionId);
            paytmParams.put("ORDERID", order);
            paytmParams.put("REFUNDAMOUNT", refundAmount);
            paytmParams.put("TXNTYPE", transactionType);
            paytmParams.put("COMMENTS", comment);
            try {
                JSONObject obj = new JSONObject(paytmParams);
                String postData = "JsonData=" + obj.toString();
                URL url = new URL(transactionURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("contentType", "application/json");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
                requestWriter.writeBytes(postData);
                requestWriter.close();
                String responseData = "";
                InputStream is = connection.getInputStream();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
                if ((responseData = responseReader.readLine()) != null) {
                    System.out.append("Response Json = " + responseData);
                }
                System.out.append("Requested Json = " + postData + " ");
                responseReader.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return null;
        }
        }
}
