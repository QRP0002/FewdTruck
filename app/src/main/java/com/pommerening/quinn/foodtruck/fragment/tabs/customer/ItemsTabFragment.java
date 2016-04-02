package com.pommerening.quinn.foodtruck.fragment.tabs.customer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.fragment.dialogs.AddFavoriteDialog;
import com.pommerening.quinn.foodtruck.fragment.dialogs.EditInventoryDialog;
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

public class ItemsTabFragment extends Fragment {
    private String mUSername;
    private ListView lv;
    private ProgressDialog pDialog;
    private static final String URL = "http://192.168.1.72:80/webservice/custloadinv.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODNAME = "prodname";
    private static final String TAG_PRODPRICE = "prodprice";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_TRUCKNAME = "truckname";
    private static final String TAG_PRODID = "prodid";

    private JSONArray mInventory = null;
    private ArrayList<HashMap<String, String>> mInventoryList;

    public static ItemsTabFragment newInstance(String username) {
        ItemsTabFragment f = new ItemsTabFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUSername = getArguments().getString("username");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_tab, container, false);
        lv = (ListView) view.findViewById(R.id.cust_list_view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new CustomerInventory().execute();
    }

    public class CustomerInventory extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Inventory...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            getJSONData();
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            setJSONData();
            pDialog.dismiss();
        }

        public void getJSONData() {
            mInventoryList = new ArrayList<HashMap<String, String>>();
            ArrayList<String> truckNamesArray = LocationData.getTruckNames();
            for(String temp : truckNamesArray) {
                int success;
                try {
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("truckname", temp));
                    JSONParser jParser = new JSONParser();
                    Log.d("Request", "Starting");
                    JSONObject json = jParser.makeHttpRequest(URL, "POST", params);
                    mInventory = json.getJSONArray(TAG_POSTS);

                    success = json.getInt(TAG_SUCCESS);
                    if(success == 1) {

                        for (int i = 0; i < mInventory.length(); i++) {
                            JSONObject c = mInventory.getJSONObject(i);

                            String prodName = c.getString(TAG_PRODNAME);
                            String prodPrice = c.getString(TAG_PRODPRICE);
                            String prodID = c.getString(TAG_PRODID);
                            String truckName = c.getString(TAG_TRUCKNAME);

                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put(TAG_PRODNAME, prodName);
                            map.put(TAG_PRODPRICE, prodPrice);
                            map.put(TAG_PRODID, prodID);
                            map.put(TAG_TRUCKNAME, truckName);
                            mInventoryList.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setJSONData() {
            final ListAdapter adapter = new SimpleAdapter(getActivity(), mInventoryList,
                    R.layout.cust_list_view, new String[] {TAG_TRUCKNAME,
                    TAG_PRODNAME, TAG_PRODPRICE},
                    new int[] {R.id.cust_truck_name,
                            R.id.cust_prod_name, R.id.cust_prod_price});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    long arrayPosition = adapter.getItemId(position);
                    String prodNameSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODNAME);
                    String prodPriceSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODPRICE);
                    String truckNameSend = mInventoryList.get((int)arrayPosition).get(TAG_TRUCKNAME);
                    String productIDSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODID);

                    DialogFragment newFragment = AddFavoriteDialog.newInstance(mUSername,
                            productIDSend, prodNameSend, prodPriceSend, truckNameSend);

                    newFragment.setTargetFragment(ItemsTabFragment.this, 1);
                    newFragment.show(getActivity().getSupportFragmentManager(), "favorite dialog");
                }
            });
        }
    }

}
