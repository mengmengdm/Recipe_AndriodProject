package com.example.demoproject.fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demoproject.R;
import com.example.demoproject.connection.ConnectionRequest;

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
        //load in the xml file the corresponding page
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        TextView textView = view.findViewById(R.id.testText);
        ImageView imageView = view.findViewById(R.id.testImage);
        //set network connections
        Activity activity = requireActivity();
        ConnectionRequest connectionRequest = new ConnectionRequest(activity);
        String testurl = "https://studev.groept.be/api/a23PT214/test";
        String imagetesturl ="https://www.themealdb.com/images/media/meals/wuvryu1468232995.jpg";
        connectionRequest.stringGetRequest(testurl,new ConnectionRequest.MyRequestCallback<String>(){
            @Override
            public void onSuccess(String response) {
                textView.setText(response);
                Log.d(TAG, "onSuccess: "+response);
            }

            @Override
            public void onError(String error) {
                textView.setText("response failed");
            }
        } );

        connectionRequest.imageGetRequest(imagetesturl,
                new ConnectionRequest.MyRequestCallback<Bitmap>() {
                    @Override
                    public void onSuccess(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }

                    @Override
                    public void onError(String error) {
                        Log.e(TAG, "onError: "+error);
                    }
                });
        return view;
    }
}