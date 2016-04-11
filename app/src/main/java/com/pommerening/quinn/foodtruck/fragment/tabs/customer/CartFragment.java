package com.pommerening.quinn.foodtruck.fragment.tabs.customer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.CalculateTotal;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.LocationData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartFragment extends ListFragment {

    private String mUsername;
    private ArrayList<HashMap<String, String>> mOrderList;
    private static final String TAG_PRODUCTID = "_ProductID";
    private static final String TAG_TRUCKNAME = "_TruckName";
    private static final String TAG_USERNAME = "_Username";
    private static final String TAG_PRODUCTPRICE = "_ProductPrice";
    private static final String TAG_PRDUCTNAME = "_PRODUCTNAME";
    private static final String URL = "http://192.168.1.72:80/webservice/addorder.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static OrderAdapter orderAdapter;
    public static ListView listView;
    private Button mOrderButton;
    private TextView mTotalTV;
    private String mTotal;
    private ProgressDialog pDialog;

    public static CartFragment newInstance(String username) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString("username");
        }

        try {
            mOrderList = LocationData.getTicketOrder();
            orderAdapter = new OrderAdapter(mOrderList);
        } catch (NullPointerException e) {
            mOrderList = new ArrayList<>();
            Toast.makeText(getActivity(), R.string.cart_empty_toast, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        CalculateTotal calculateTotal = new CalculateTotal(mOrderList);
        mTotal = calculateTotal.findTotal();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);

        mOrderButton = (Button) view.findViewById(R.id.cart_order_button);
        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(HashMap<String, String> hashMap: mOrderList) {
                    String sendID = "";
                    String sendTruck = "";
                    String sendUser = "";
                    for(Map.Entry<String, String> entry : hashMap.entrySet()) {
                        if(entry.getKey().equals(TAG_PRODUCTID)) {
                            sendID = entry.getValue();
                        } else if(entry.getKey().equals(TAG_TRUCKNAME)) {
                            sendTruck = entry.getValue();
                        } else if (entry.getKey().equals(TAG_USERNAME)) {
                            sendUser = entry.getValue();
                        }
                    }
                    new SendOrder().execute(sendID, sendTruck, sendUser);
                }

            }
        });

        mTotalTV = (TextView) view.findViewById(R.id.total_cost_tv);
        mTotalTV.setText(mTotal);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            mOrderList = LocationData.getTicketOrder();
            orderAdapter = new OrderAdapter(mOrderList);
            listView.setAdapter(orderAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private class OrderAdapter extends ArrayAdapter<HashMap<String, String>> {

        public OrderAdapter(ArrayList<HashMap<String, String>> orderList) {
            super(getActivity(), 0, orderList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.cart_list_view,
                        null);
            }

            final HashMap<String, String> entry = getItem(position);

            TextView productName = (TextView) convertView.findViewById(R.id.
                    cart_prod_name);
            productName.setText(entry.get(TAG_PRDUCTNAME));

            TextView productPrice = (TextView) convertView.findViewById(R.id.
                    cart_prod_price);
            productPrice.setText(entry.get(TAG_PRODUCTPRICE));

            TextView truckName = (TextView) convertView.findViewById(R.id.
                    cart_truck_name);

            truckName.setText(entry.get(TAG_TRUCKNAME));

            ImageButton removeButton = (ImageButton) convertView
                    .findViewById(R.id.cart_remove_button);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap <String, String> item = mOrderList.get(position);
                    mOrderList.remove(item);
                    orderAdapter.notifyDataSetChanged();
                    CalculateTotal ct = new CalculateTotal(mOrderList);
                    mTotalTV.setText(ct.findTotal());
                }
            });

            return convertView;
        }
    }

    private class SendOrder extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String productID = params[0];
            String truckName = params[1];
            String username = params[2];

            return runJSON(productID, truckName, username);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();

        }

        private String runJSON(String productID, String truckName, String username ) {

            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("truck_name", truckName));
                params.add(new BasicNameValuePair("prod_id", productID));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

                success = json.getInt(TAG_SUCCESS);
                Log.d("Success: ", json.getString(TAG_SUCCESS));

                if(success == 1) {
                    return json.getString(TAG_MESSAGE);
                } else {
                    return json.getString(TAG_MESSAGE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
