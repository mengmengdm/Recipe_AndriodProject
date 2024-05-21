package com.example.demoproject.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.demoproject.R;
import com.example.demoproject.Recipe;
import com.example.demoproject.RecyclerAdapter;
import com.example.demoproject.connection.ConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_NAME = "username";
    private static final String ARG_EMAIL_ADDRESS = "emailaddress";
    private static final String ARG_USER_ID = "userid";

    // TODO: Rename and change types of parameters
    private String username;
    private String emailaddress;
    private String userid;
    private List<Recipe> recipeLikeList = new ArrayList<>();
    private ArrayList<Integer> likeIdList = new ArrayList<>();
    public MyRecipeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyRecipeFragment newInstance(String username, String emailaddress, String userid) {
        MyRecipeFragment fragment = new MyRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_NAME, username);
        args.putString(ARG_EMAIL_ADDRESS, emailaddress);
        args.putString(ARG_USER_ID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USER_NAME);
            emailaddress = getArguments().getString(ARG_EMAIL_ADDRESS);
            userid = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_recipe, container, false);
        Activity activity = requireActivity();
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerviewMyRecipe);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_myRecipe);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerAdapter adapter = new RecyclerAdapter(recipeLikeList, activity, userid);
        if (recipeLikeList.isEmpty()){
            findLikeRecipe(connectionRequest,String.valueOf(userid),adapter);

        }
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }
    private void findLikeRecipe(ConnectionRequest connectionRequest,String id,RecyclerAdapter adapter){
        String likeUrl = "https://studev.groept.be/api/a23PT214/get_user_fav/";
        likeUrl= likeUrl+id;
        connectionRequest.jsonGetRequest(likeUrl,
                new ConnectionRequest.MyRequestCallback<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray response) {
                        try {
                            for( int i = 0; i < response.length(); i++ ) {
                                JSONObject curObject = response.getJSONObject( i );
                                likeIdList.add(curObject.getInt("user_fav"));
                            }
                            if(!likeIdList.isEmpty()){
                                initLike(connectionRequest,adapter);
                            }
                            Log.d("Database", "onSuccess: "+String.valueOf(likeIdList));
                        }
                        catch (JSONException e){
                            Log.e("Database", "fail: "+e );
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("Database", "fail: "+error );
                    }
                });
    }
    private void initLike(ConnectionRequest connectionRequest, RecyclerAdapter adapter){
        for(int i = 0; i< likeIdList.size();i++)
        {
            String imgUrl = "https://studev.groept.be/api/a23PT214/get_meal_info_byid/"+likeIdList.get(i);

            Log.d("Database", "initLike: "+likeIdList.size() );
            connectionRequest.jsonGetRequest(imgUrl, new ConnectionRequest.MyRequestCallback<JSONArray>() {
                @Override
                public void onSuccess(JSONArray response) {
                    try {
                        //String responseString = "";
                        for( int i = 0; i < response.length(); i++ )
                        {
                            Recipe recipe = new Recipe();
                            JSONObject curObject = response.getJSONObject( i );
                            recipe.setIdMeal(curObject.getInt("idMeal"));
                            recipe.setStrName(curObject.getString("strName"));
                            recipe.setStrCategory(curObject.getString("strCategory"));
                            recipe.setNumIng(curObject.getInt("numIng"));
                            recipe.setNumInst(curObject.getInt("numInst"));
                            recipe.setBitmap(curObject.getString("bitImg"));
                            //Log.d("recipe", "onSuccess:"+curObject.getString("stepImg"));
                            recipeLikeList.add(recipe);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    catch( JSONException e )
                    {
                        Log.e( "Database", e.getMessage(), e );
                    }
                }
                @Override
                public void onError(String error) {
                    Log.e("initurl", "onError: "+error );
                }
            });
        }
        adapter.notifyDataSetChanged();

    }

}