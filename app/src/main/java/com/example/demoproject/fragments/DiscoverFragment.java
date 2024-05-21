package com.example.demoproject.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoproject.R;
import com.example.demoproject.Recipe;
import com.example.demoproject.RecipeDetail;
import com.example.demoproject.RecyclerAdapter;
import com.example.demoproject.connection.ConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "userid";

    private String userid;

    private List<Recipe> recipeList = new ArrayList<>();
    // TODO: Rename and change types of parameters

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String userid) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userid = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //find the mainActivity
        Activity activity = requireActivity();
        //new instance of connectionrequest
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        //find navController
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);
        //find view and Widget inside
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);


        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        //linear and staggergid layout of recyclerview(choose one)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //set layout
        recyclerView.setLayoutManager(linearLayoutManager);
        //transfer the data into the adapter
        RecyclerAdapter adapter = new RecyclerAdapter(recipeList, activity, userid);
        if (recipeList.isEmpty()){
            initUrl(connectionRequest,adapter,activity);
        }
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Context context = getContext();
                Intent intent = new Intent(context, RecipeDetail.class);
                intent.putExtra("recipe", recipeList.get(position));
                context.startActivity(intent);
            }
        });
        //set recyclerView
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
    private void initUrl(ConnectionRequest connectionRequest, RecyclerAdapter adapter, Context context){
        String imgUrl = "https://studev.groept.be/api/a23PT214/get_meal_info";
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
                        recipeList.add(recipe);
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
}