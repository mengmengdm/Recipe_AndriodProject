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
import android.widget.TextView;

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
    private static final String OPENAI_API = "https://api.openai.com/v1/chat/completions";
    private String API_KEY = "sk-proj-9BGeCKgoArLuQVE9TC7FT3BlbkFJDqTCbgQAwDwOBXqCj9MI";

    private List<Recipe> recipeList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String content;

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
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        Activity activity = requireActivity();
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);
        TextView apiReturnText = view.findViewById(R.id.apiReturnText);
        connectionRequest.apiPostRequest("tomato, potato" + "give me a recipe with ingredient and instruction",
                new ConnectionRequest.MyRequestCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            content = response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                            Log.d("api", "onSuccess: " + content);
                            apiReturnText.setText(content);
                            getJsonData(connectionRequest);
                        } catch (JSONException e) {
                            Log.e("api", "fail: " + e);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("api", "error: " + error);
                    }
                });

        return view;
    }
    public void getJsonData(ConnectionRequest connectionRequest){
        String apiRequest = "i want to return it with all ingredients and instructions in json,it contains recipetitle, all ingredients, instructions, and ingredients are json array, containg ingredientname and quantity. the instructions are jsonarray too, each step contain instruction, time and timescale";
        connectionRequest.apiPostRequest(content+apiRequest,
                new ConnectionRequest.MyRequestCallback<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            String content = response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                            Log.d("api", "onSuccess: " + content);
//                            apiReturnText.setText(content);
                        } catch (JSONException e) {
                            Log.e("api", "fail: " + e);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("api", "error: " + error);
                    }
                });

    }

}