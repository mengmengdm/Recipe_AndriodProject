package com.example.demoproject.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
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
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Recipe> recipeList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        //find the mainActivity
        Activity activity = requireActivity();
        //new instance of connectionrequest
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        //find navController
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragment_container);
        //find view and Widget inside
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        //linear and staggergid layout of recyclerview(choose one)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //set layout
        recyclerView.setLayoutManager(linearLayoutManager);
        //transfer the data into the adapter

        RecyclerAdapter adapter = new RecyclerAdapter(recipeList, activity);
        if (recipeList.isEmpty()){
            initUrl(connectionRequest,adapter,activity);
        }


        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Recipe selectedRecipe = recipeList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("testString", String.valueOf(position));
                Log.d("test", "onItemClick: "+position);
                navController.navigate(R.id.recipeFragment,bundle);
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

    //test method of recyclerview

//    private void intintest() {
//        for (int i = 0; i < 100; i++) {
//            String str = String.valueOf(i);
//            Recipe item = new Recipe(i);
//            recipeList.add(item);
//        }
//    }
    //importing the recipe list into the discovery page
    private void initUrl(ConnectionRequest connectionRequest, RecyclerAdapter adapter, Context context){
        String imgUrl = "https://studev.groept.be/api/a23PT214/get_meal_info";
        connectionRequest.jsonGetRequest(imgUrl, new ConnectionRequest.MyRequestCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    //String responseString = "";
                    for( int i = 0; i < response.length(); i++ )
                    {
                        Recipe recipe = new Recipe(context);
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