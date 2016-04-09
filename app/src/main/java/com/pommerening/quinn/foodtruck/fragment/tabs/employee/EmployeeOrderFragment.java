package com.pommerening.quinn.foodtruck.fragment.tabs.employee;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.pojo.CalculateTotal;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.LocationData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmployeeOrderFragment extends ListFragment {
    private String mUsername;
    private ProgressDialog pDialog;
    private static final String URL = "http://192.168.1.72:80/webservice/loadorder.php";
    private static final String REMOVE_URL = "http://192.168.1.72:80/webservice/removeorder.php";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODNAME = "prod_name";
    private static final String TAG_CUSTOMER = "customer_name";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_DATE = "order_date";
    private static final String TAG_ORDERID = "order_id";

    public static Adapter adapter;
    public static ListView listView;

    private JSONArray mInventory = null;
    private ArrayList<HashMap<String, String>> mInventoryList;

    public static EmployeeOrderFragment newInstance(String username) {
        EmployeeOrderFragment f = new EmployeeOrderFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");
        new LoadOrders().execute(mUsername);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_order, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);
        return view;
    }

    private class Adapter extends ArrayAdapter<HashMap<String, String>> {
        public Adapter(ArrayList<HashMap<String, String>> orderList) {
            super(getActivity(), 0, orderList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.load_orders_view_list,
                        null);
            }

            final HashMap<String, String> entry = getItem(position);
            TextView productName = (TextView) convertView.findViewById(R.id.order_prod_name);
            productName.setText(entry.get(TAG_PRODNAME));
            TextView productPrice = (TextView) convertView.findViewById(R.id.order_customer_name);
            productPrice.setText(entry.get(TAG_CUSTOMER));
            TextView truckName = (TextView) convertView.findViewById(R.id.order_date);
            truckName.setText(entry.get(TAG_DATE));

            ImageButton removeButton = (ImageButton) convertView
                    .findViewById(R.id.orders_remove_button);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> item = mInventoryList.get(position);
                    new RemoveOrder().execute(item.get(TAG_ORDERID));
                    mInventoryList.remove(item);
                    adapter.notifyDataSetChanged();

                }
            });

            Switch doneButton = (Switch) convertView
                    .findViewById(R.id.orders_cooked_button);
            return convertView;
        }
    }

    private class LoadOrders extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Orders...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            runJSON(username);
            return null;
        }

        private void runJSON(String username) {
            mInventoryList = new ArrayList<HashMap<String, String>>();
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("username", username));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(URL, "POST", params);
                mInventory = json.getJSONArray(TAG_POSTS);

                success = json.getInt(TAG_SUCCESS);
                if(success == 1) {
                    for (int i = 0; i < mInventory.length(); i++) {
                        JSONObject c = mInventory.getJSONObject(i);


                        String prodName = c.getString(TAG_PRODNAME);
                        String customer = c.getString(TAG_CUSTOMER);
                        String date = c.getString(TAG_DATE);
                        String orderID = c.getString(TAG_ORDERID);

                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put(TAG_PRODNAME, prodName);
                        map.put(TAG_CUSTOMER, customer);
                        map.put(TAG_DATE, date);
                        map.put(TAG_ORDERID, orderID);
                        mInventoryList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            setData();
            pDialog.dismiss();

        }

        private void setData() {
            adapter = new Adapter(mInventoryList);
            listView.setAdapter(adapter);
        }
    }

    private class RemoveOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String order_id = params[0];

            return runJSON(order_id);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }

        private String runJSON(String order_id) {
            int success;
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("order_id", order_id));
                JSONParser jParser = new JSONParser();
                Log.d("Request", "Starting");
                JSONObject json = jParser.makeHttpRequest(REMOVE_URL, "POST", params);

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
