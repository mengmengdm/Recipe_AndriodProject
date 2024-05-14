package com.example.demoproject.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;


import com.example.demoproject.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Initialize Step Counter
    private int stepcounter = 2;

    public PostFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
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
        //Inflate The Layout For This Fragment
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        //Get Spinner Control
        Spinner categorySpinner = rootView.findViewById(R.id.category_spinner);

        //Create Adapter And Set To Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(),
                R.array.recipe_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setSelection(0);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                if (position == 0) { //The First Item Is A Prompt
                    textView.setTextColor(Color.parseColor("#777777"));
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                textView.setTextSize(15);
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Dynamically Add Ingredients
        Button addIngredientButton = view.findViewById(R.id.add_ingredient_button);
        LinearLayoutCompat ingredientsLayout = view.findViewById(R.id.ingredients_layout);

        addIngredientButton.setOnClickListener(v -> addViewToLayout(ingredientsLayout, R.layout.ingredient_layout));

        ////Dynamically Add Steps
        Button addStepButton = view.findViewById(R.id.add_step_button);
        LinearLayoutCompat instructionsLayout = view.findViewById(R.id.instructions_layout);

        addStepButton.setOnClickListener(v -> {
            View stepView = addViewToLayout(instructionsLayout, R.layout.step_layout);
            updateStepView(stepView, stepcounter++);
        });
    }


    private View addViewToLayout(LinearLayoutCompat layout, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        View newView = inflater.inflate(layoutResId, layout, false);
        layout.addView(newView);
        return newView;
    }

    private void updateStepView(View stepView, int stepNumber) {
        //Update Step Text
        TextView stepTextView = stepView.findViewById(R.id.step_text);
        stepTextView.setText("Step " + stepNumber);

        //Generate Unique Id
        ImageView processPhoto = stepView.findViewById(R.id.process_photo);
        TextView uploadTextView = stepView.findViewById(R.id.upload_text);

        processPhoto.setId(View.generateViewId());
        uploadTextView.setId(View.generateViewId());

        //Set Ratio Of ImageView
        ConstraintLayout.LayoutParams photoParams = (ConstraintLayout.LayoutParams) processPhoto.getLayoutParams();
        photoParams.dimensionRatio = "W,4:3";
        processPhoto.setLayoutParams(photoParams);

        //Set Constraints And Visibility Of Upload Text
        ConstraintLayout.LayoutParams uploadTextParams = (ConstraintLayout.LayoutParams) uploadTextView.getLayoutParams();
        uploadTextParams.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        uploadTextParams.height = 0;
        uploadTextParams.topToTop = processPhoto.getId();
        uploadTextParams.startToStart = processPhoto.getId();
        uploadTextParams.endToEnd = processPhoto.getId();
        uploadTextParams.bottomToBottom = processPhoto.getId();
        uploadTextView.setLayoutParams(uploadTextParams);
        uploadTextView.setVisibility(View.VISIBLE);
        uploadTextView.setGravity(Gravity.CENTER);
    }
}
