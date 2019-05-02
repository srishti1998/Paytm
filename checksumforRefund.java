package com.example.admin.paytm;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;

import com.example.admin.paytm.R;
import com.example.admin.paytm.jsonparser;
import com.example.admin.paytm.showTransactions;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

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
import java.util.UUID;

/**
 * Created by Admin on 19-04-2019.
 */

public class checksumforRefund
        extends AppCompatActivity  {
    String  mid="";
    URL urlrefund;
    SendRequestforRefund refundob;
    String refid , transac_id,orderId ,custid , orderIDC;
    public static String checksumhash;
    jsonparser jsonob;
    static showTransactions ob;
    private String generateString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.random);
        Bundle b = getIntent().getExtras();
        transac_id = b.getString("transac_id");
        orderIDC = generateString();
        orderId= b.getString("order_id");
        Log.e("Received parameters" , transac_id + orderId);
        refid = generateString();
        custid =generateString();
        mid = "eNnjXe00637647587210";
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        sendUserDetailTOServerddd dll = new sendUserDetailTOServerddd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        dll.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

//  asynch
    }
    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(checksumforRefund.this);
        String url = " https://www.blueappsoftware.com/payment/payment_paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        public String checksum = "";
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            jsonparser jsonParser = new jsonparser(checksumforRefund.this);
            String param =
                    "MID=" + mid +
                            "&ORDER_ID=" + orderIDC +
                            "&CUST_ID=" + custid +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=100&WEBSITE=WEBSTAGING" +
                            "&CALLBACK_URL=" + varifyurl + "&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // recieve checksum , order id and status
            if (jsonObject != null) {
                try {
                    checksumhash = jsonObject.getString("CHECKSUMHASH");
                    Log.e("CheckSum result >>", jsonObject.getString("CHECKSUMHASH"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("print","tra"+transac_id+"ref"+refid);
            }
            return checksumhash;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            checksumhash = result;
        }

    }

    public class sendUserDetailTOServerddd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(checksumforRefund.this);

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
            String refundAmount = "12.00";
            String transactionId = transac_id;
            String refId = refid;
            String comment = "comment string";
            Log.e("responseData",checksumhash + "");
            TreeMap<String, String> paytmParams = new TreeMap<String, String>();
            paytmParams.put("MID", merchantMid);
            paytmParams.put("REFID", refId);
            paytmParams.put("CHECKSUM", checksumhash);
            paytmParams.put("TXNID", transactionId);
            paytmParams.put("ORDERID", orderId);
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
                    Log.e("responseData", "Response Json = " + responseData);
                }
                Log.e("responseData", "Response Json = " + postData);
                responseReader.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return checksumforRefund.checksumhash;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" responseData ", "" + result);
        }

    }
}