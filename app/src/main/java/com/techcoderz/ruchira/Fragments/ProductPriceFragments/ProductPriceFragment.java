package com.techcoderz.ruchira.Fragments.ProductPriceFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.techcoderz.ruchira.All_product_price_Adaptor;
import com.techcoderz.ruchira.ModelClasses.Area;
import com.techcoderz.ruchira.Profile_listview_adapter;
import com.techcoderz.ruchira.R;
import com.techcoderz.ruchira.Adapters.ProductAdapter;
import com.techcoderz.ruchira.Adapters.PromotionCompanySpinnerAdapter;
import com.techcoderz.ruchira.Application.RuchiraApplication;
import com.techcoderz.ruchira.Fragments.OtherFragments.RuchiraFragment;
import com.techcoderz.ruchira.ModelClasses.Company;
import com.techcoderz.ruchira.ModelClasses.ProductList;
import com.techcoderz.ruchira.Utils.AppConfig;
import com.techcoderz.ruchira.Utils.NetworkUtils;
import com.techcoderz.ruchira.Utils.TaskUtils;
import com.techcoderz.ruchira.Utils.UserPreferences;
import com.techcoderz.ruchira.Utils.ViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahriar on 9/14/2016.
 */
public class ProductPriceFragment extends RuchiraFragment {
    private final static String TAG = "ProductPriceFragment";
    private Fragment toLaunchFragment = null;

    private List<ProductList> productList;
    private List<Company> companyList;
    private List<Area> DemoList;

    private AppCompatSpinner beat_spinner;
    private PromotionCompanySpinnerAdapter promotionCompanySpinnerAdapter;

    private RecyclerView report_rcview;
    private ProductAdapter productAdapter;
    private LinearLayoutManager manager;
    private TextView company_title_txt;

    ///////////////////////////////////////////////
    ListView listView;
    String[] Name ={"abc",
            "def",
            "sha",
            "xcnkf"};

    String[] Price ={"123",
            "556",
            "45",
            "6879"};
    ///////////////////////////////////////////////


    public ProductPriceFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product_price, container, false);
        setupToolbar();
   //     initialize(rootView);
//        action();
        if(NetworkUtils.hasInternetConnection(mFragmentContext)) {
            fetchDataFromServer();
        }
        return rootView;
    }

    private void setupToolbar() {
        mFragmentContext.getSupportActionBar().show();
        mFragmentContext.getSupportActionBar().setTitle("All Product With Price");
    }

    private void initialize(View rootView) {

        beat_spinner = (AppCompatSpinner) rootView.findViewById(R.id.beat_spinner);
    //    report_rcview = (RecyclerView) rootView.findViewById(R.id.report_rcview);
    //    company_title_txt = (TextView) rootView.findViewById(R.id.company_title_txt);

        ///////////////////////////////////////////////////
        DemoList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.report_rcview);

        All_product_price_Adaptor adapter = new All_product_price_Adaptor(getContext() , Name , Price);
        listView.setAdapter(adapter);

        //////////////////////////////////////////////////
/*
        productList = new ArrayList<>();
        companyList = new ArrayList<>();


        productList.add(new ProductList("a", "Milk" , 100 ,"","ab" , 1 ,"abc" ));
        productList.add(new ProductList("b", "oil" , 200 ,"","ab" , 1 ,"abc" ));
        productList.add(new ProductList("c", "Medicine" , 500 ,"","ab" , 1 ,"abc" ));
        productList.add(new ProductList("d", "Mutton" , 400 ,"","ab" , 1 ,"abc" ));


        companyList.add(new Company("1" , "ACI"));
        companyList.add(new Company("2" , "AOS"));
        companyList.add(new Company("3" , "ABC"));
        companyList.add(new Company("4" , "AII"));

        promotionCompanySpinnerAdapter = new PromotionCompanySpinnerAdapter(mFragmentContext, R.layout.beat_list, companyList);
        promotionCompanySpinnerAdapter.setDropDownViewResource(R.layout.beat_list);

        manager = new LinearLayoutManager(mFragmentContext,LinearLayoutManager.VERTICAL,false);
        productAdapter = new ProductAdapter(mFragmentContext, productList);


        //report_rcview.setHasFixedSize(true);
        report_rcview.setLayoutManager(manager);
        report_rcview.setAdapter(productAdapter);*/

    }

    private void action() {
    //    beat_spinner.setAdapter(promotionCompanySpinnerAdapter);
        beat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //        company_title_txt.setText(companyList.get(position).getCompanyName());
                if(NetworkUtils.hasInternetConnection(mFragmentContext) &&
                        !companyList.get(position).getCompanyName().matches("Select a Company")) {
                    fetchDataFromServerForProduct(companyList.get(position).getCompanyId());
                }
                Log.e(TAG, " Company Id: " + companyList.get(position).getCompanyId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fetchDataFromServerForProduct(final String id) {
        UserPreferences.saveCompanyId(mFragmentContext, id);
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_product";
        final ProgressDialog finalProgressDialog = progressDialog;

    }


    private void fetchDataFromServer() {
        ProgressDialog progressDialog = null;
        progressDialog = new ProgressDialog(mFragmentContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String tag_string_req = "req_order";
        final ProgressDialog finalProgressDialog = progressDialog;


    }


    private void executeForCompany(String result) {
        companyList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                Company company = new Company();
                company.setCompanyName("Select a Company");
                companyList.add(company);
                companyList.addAll(TaskUtils.setCompany(result));
                promotionCompanySpinnerAdapter.notifyDataSetChanged();

                return;

            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void processResultForPromotion(String result) {
        Log.d(TAG, result.toString());
        productList.clear();
        try {
            JSONObject obj = new JSONObject(result);
            int responseResult = obj.getInt("success");
            Log.d(TAG, result.toString());
            if (responseResult == 1) {
                productList.addAll(TaskUtils.setProductList(result));
                productAdapter.notifyDataSetChanged();
                return;
            } else {
                ViewUtils.alertUser(mFragmentContext, "Server Error");
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}