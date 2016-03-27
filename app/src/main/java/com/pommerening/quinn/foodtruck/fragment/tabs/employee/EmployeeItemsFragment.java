package com.pommerening.quinn.foodtruck.fragment.tabs.employee;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.pommerening.quinn.foodtruck.R;
import com.pommerening.quinn.foodtruck.fragment.dialogs.AddInventoryDialog;
import com.pommerening.quinn.foodtruck.fragment.dialogs.EditInventoryDialog;
import com.pommerening.quinn.foodtruck.pojo.JSONParser;
import com.pommerening.quinn.foodtruck.pojo.RefreshScreenInterface;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EmployeeItemsFragment extends Fragment implements RefreshScreenInterface{
    private String mUsername;
    private ProgressDialog pDialog;
    private static final String URL = "http://192.168.1.72:80/webservice/loadempinv.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODNAME = "prodname";
    private static final String TAG_PRODPRICE = "prodprice";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_PRODID = "prodid";
    private ListView lv;
    private Button mAddButton;

    private JSONArray mInventory = null;
    private ArrayList<HashMap<String, String>> mInventoryList;

    public static EmployeeItemsFragment newInstance(String username) {
        EmployeeItemsFragment f = new EmployeeItemsFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUsername = getArguments().getString("username");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_items, container, false);
        lv = (ListView) view.findViewById(R.id.emp_list_view);

        mAddButton = (Button) view.findViewById(R.id.emp_inv_add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = AddInventoryDialog.newInstance(mUsername);
                newFragment.setTargetFragment(EmployeeItemsFragment.this, 1);
                newFragment.show(getActivity().getSupportFragmentManager(), "add dialog");
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("OnReumse: ", "OnResume was called");
        new LoadInformation().execute(mUsername);
    }

    @Override
    public void refreshScreen() {
        Log.d("Hope:", "JobScreen Refreshed");
        new LoadInformation().execute(mUsername);
    }


    public class LoadInformation extends AsyncTask<String, Void, Boolean> {
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
        protected Boolean doInBackground(String... args) {
            String username = args[0];
            getData(username);
            return null;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            setData(lv);
            pDialog.dismiss();
        }

    }

    public void getData(String username) {
        mInventoryList = new ArrayList<HashMap<String, String>>();
        int success;

        try {
            JSONParser jParser = new JSONParser();
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", username));

            Log.d("Request", "Starting");
            JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

            success = json.getInt(TAG_SUCCESS);
            Log.d("Success: ", json.getString(TAG_SUCCESS));

            if (success == 1) {
                mInventory = json.getJSONArray(TAG_POSTS);

                for (int i = 0; i < mInventory.length(); i++) {
                    JSONObject c = mInventory.getJSONObject(i);

                    String prodName = c.getString(TAG_PRODNAME);
                    String prodPrice = c.getString(TAG_PRODPRICE);
                    String prodID = c.getString(TAG_PRODID);
                    Log.d("Product name: ", prodName);

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_PRODNAME, prodName);
                    map.put(TAG_PRODPRICE, prodPrice);
                    map.put(TAG_PRODID, prodID);
                    mInventoryList.add(map);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setData(ListView lv) {
        final ListAdapter adapter = new SimpleAdapter(getActivity(), mInventoryList,
                R.layout.grid_list, new String[] {TAG_PRODNAME, TAG_PRODPRICE},
                new int[] {R.id.item1, R.id.item2});

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                long arrayPosition = adapter.getItemId(position);
                String nameSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODNAME);
                String priceSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODPRICE);
                String idSend = mInventoryList.get((int)arrayPosition).get(TAG_PRODID);

                DialogFragment newFragment = EditInventoryDialog.newInstance(nameSend,
                        priceSend, idSend);
                newFragment.setTargetFragment(EmployeeItemsFragment.this, 1);
                newFragment.show(getActivity().getSupportFragmentManager(), "edit dialog");
            }
        });
    }
}
