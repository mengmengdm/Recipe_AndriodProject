package com.example.demoproject.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.demoproject.Ingredient;
import com.example.demoproject.Instruction;
import com.example.demoproject.R;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Initialize General Counter for Steps and Ingredients
    private int ingredientCounter = 2;
    private int stepCounter = 2;
    private List<Ingredient>ingredientList  = new ArrayList<>();
    private List<Instruction> instructionList = new ArrayList<>();


    public PostFragment() {
        // Required empty public constructor
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                if (position == 0) {//The First Item Is A Prompt
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

        Button publish_recipe_button = view.findViewById(R.id.publish_recipe_button);

        publish_recipe_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = "ingredient_name_" + String.valueOf(ingredientCounter-1);
                EditText testedit = view.findViewWithTag(tag);
                if (testedit != null) {
                    String text = testedit.getText().toString();
                    Log.d("uploadall", "onClick: " + text);
                } else {
                    Log.d("uploadall", "onClick: EditText with tag " + tag + " not found");
                }
            }
        });
        //Dynamically Add Ingredients
        Button addIngredientButton = view.findViewById(R.id.add_ingredient_button);
        LinearLayoutCompat ingredientsLayout = view.findViewById(R.id.ingredients_layout);
        addIngredientButton.setOnClickListener(v -> {
            addIngredToList()
            View ingredientView = addViewToLayout(ingredientsLayout, R.layout.ingredient_layout);
            updateIngredientView(ingredientView, ingredientCounter++);
        });

        //Dynamically Add Steps
        Button addStepButton = view.findViewById(R.id.add_step_button);
        LinearLayoutCompat instructionsLayout = view.findViewById(R.id.instructions_layout);
        addStepButton.setOnClickListener(v -> {
            View stepView = addViewToLayout(instructionsLayout, R.layout.step_layout);
            updateStepView(stepView, stepCounter++);
        });
    }

    private View addViewToLayout(LinearLayoutCompat layout, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        View newView = inflater.inflate(layoutResId, layout, false);
        layout.addView(newView);
        return newView;
    }

    private void updateStepView(View stepView, int number) {
        //Update Step Text
        TextView stepTextView = stepView.findViewById(R.id.step_text);
        stepTextView.setText("Step " + number);

        //Generate Unique Id
        ImageView processPhoto = stepView.findViewById(R.id.process_photo);
        TextView uploadProcessText = stepView.findViewById(R.id.upload_process_text);
        EditText stepInstruction = stepView.findViewById(R.id.step_instruction);

        processPhoto.setId(View.generateViewId());
        uploadProcessText.setId(View.generateViewId());

        //Set Tags
        processPhoto.setTag("process_photo_" + number);
        uploadProcessText.setTag("upload_process_text_" + number);
        stepInstruction.setTag("step_instruction_" + number);

        //Set Ratio Of ImageView
        ConstraintLayout.LayoutParams photoParams = (ConstraintLayout.LayoutParams) processPhoto.getLayoutParams();
        photoParams.dimensionRatio = "W,4:3";
        processPhoto.setLayoutParams(photoParams);

        //Set Constraints And Visibility Of Upload Text
        ConstraintLayout.LayoutParams uploadTextParams = (ConstraintLayout.LayoutParams) uploadProcessText.getLayoutParams();
        uploadTextParams.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
        uploadTextParams.height = 0;
        uploadTextParams.topToTop = processPhoto.getId();
        uploadTextParams.startToStart = processPhoto.getId();
        uploadTextParams.endToEnd = processPhoto.getId();
        uploadTextParams.bottomToBottom = processPhoto.getId();
        uploadProcessText.setLayoutParams(uploadTextParams);
        uploadProcessText.setVisibility(View.VISIBLE);
        uploadProcessText.setGravity(Gravity.CENTER);
    }

    private void updateIngredientView(View ingredientView, int number) {
        EditText ingredientName = ingredientView.findViewById(R.id.ingredient_name);
        EditText ingredientAmount = ingredientView.findViewById(R.id.ingredient_amount);

        //Set Tags
        ingredientName.setTag("ingredient_name_" + String.valueOf(number));
        ingredientAmount.setTag("ingredient_amount_" + String.valueOf(number));
        Log.d("uploadall", "updateIngredientView with tag"+ingredientName.getTag());
    }
    private void addIngredToList(View view){
        String tag = "ingredient_name_" + String.valueOf(ingredientCounter-1);
        EditText  = view.findViewWithTag(tag);
        EditText
        if (testedit != null) {
            String text = testedit.getText().toString();
            Log.d("uploadall", "onClick: " + text);
        } else {
            Log.d("uploadall", "onClick: EditText with tag " + tag + " not found");
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setIdIng(ingredientCounter-1);
        ingredient.setStrIng(name);
        ingredient.setStrAmount(amount);
        ingredientList.add(ingredient);
    }
    private void addInstructToList(String detail, String base64String){
        Instruction instruction = new Instruction();
        instruction.setNumStep(stepCounter-1);
        //instruction.setStepImg(base64String);
        //instruction.setStepTime("1");
        instructionList.add(instruction);
    }
    public String findStringByTag(String tag,View view){
        String editTextValue = "";
        EditText editText = view.findViewWithTag(tag);
        if (editText != null){
            editTextValue = editText.getText().toString();
            Log.d("getEditText", "findStringByTag: ");
        } else{

        }
        return editTextValue;
    }

}


