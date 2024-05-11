package com.example.demoproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demoproject.R;
import com.example.demoproject.Recipe;
import com.example.demoproject.RecyclerAdapter;
import com.example.demoproject.connection.ConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Recipe> recipeList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = requireActivity();
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);

        //intintest();

        View view = inflater.inflate(R.layout.fragment_today, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL){

        });
        //transfer the data into the adapter
        RecyclerAdapter adapter = new RecyclerAdapter(recipeList, activity);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("test", "onItemClick: "+position);
                navController.navigate(R.id.recipeFragment);
            }
        });
        //set recyclerView
        recyclerView.setAdapter(adapter);
        //initUrl(connectionRequest,adapter,activity);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                adapter.notifyDataSetChanged();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    //test method of recyclerview

//    private void intintest() {
//        for (int i = 0; i < 100; i++) {
//            String str = String.valueOf(i);
//            Recipe item = new Recipe(i);
//            recipeList.add(item);
//    }
//}
    //importing real urls to the list
    private void initUrl(ConnectionRequest connectionRequest, RecyclerAdapter adapter, Context context){
        String imgUrl = "https://studev.groept.be/api/a23PT214/get_img";
        connectionRequest.jsonGetRequest(imgUrl, new ConnectionRequest.MyRequestCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {

            }

            @Override
            public void onError(String error) {
                Log.e("initurl", "onError: "+error );
            }
        });
    }
}