package com.techcoderz.ruchira.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.utills.CustomAsyncTask;
import com.techcoderz.ruchira.utills.ViewUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class DashBoardFragment extends RuchiraFragment {
    private final static String TAG = "DashBoardFragment";
    String url = "http://techcoderz.com/ratingApp/catagory_list.php";
    Fragment toLaunchFragment = null;
    GetNewsListTask getNewsListTask;
    TextView view_more_txt;

    public DashBoardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setupActionBar();
        initialize(rootView);
        action();
//        fetchDataFromServer();
        fetchDataFromServer2();
        return rootView;
    }

    private void setupActionBar() {
        ownerActivity.getSupportActionBar().show();
    }

    private void initialize(View rootView) {

        view_more_txt = (TextView)rootView.findViewById(R.id.view_more_txt);
    }

    private void action(){
        view_more_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewDetailsFragment();
            }
        });
    }

    private void fetchDataFromServer() {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.GET, url, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, response.toString());
                execute(response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(ownerActivity).add(jsonRequest);

    }



    private void fetchDataFromServer2() {
        if (getNewsListTask == null) {
            getNewsListTask = new GetNewsListTask(ownerActivity);
            getNewsListTask.execute();
        }
    }

    public String getAllTrendingNews() {
        try {
            URL serverUrl = new URL(url);

            HttpURLConnection con = (HttpURLConnection) serverUrl.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer responseBuffer = new StringBuffer();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                responseBuffer.append(inputLine);
            }

            bufferedReader.close();

            return responseBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    class GetNewsListTask extends CustomAsyncTask<String, Void, String> {

        Context mContext;

        public GetNewsListTask(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {

            return getAllTrendingNews();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "result " + result);
            processResult2(result);
        }
    }

    private void processResult2(String result) {
        Toast.makeText(ownerActivity, result, Toast.LENGTH_SHORT).show();
    }

    private void execute(String result) {
        Toast.makeText(ownerActivity, result, Toast.LENGTH_SHORT).show();
    }

    private void processResult(String result) {
    }

    private void processResult() {

    }


    private void openViewDetailsFragment() {
        toLaunchFragment = new ViewDetailsFragment();
        if (toLaunchFragment != null) {
            ViewUtils.launchFragmentKeepingInBackStack(ownerActivity, toLaunchFragment);
            toLaunchFragment = null;
        }
    }
}