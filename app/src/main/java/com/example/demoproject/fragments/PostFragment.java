package com.example.demoproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.demoproject.Ingredient;
import com.example.demoproject.Instruction;
import com.example.demoproject.R;
import com.example.demoproject.Recipe;
import com.example.demoproject.Uploadable;
import com.example.demoproject.connection.ConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    // Initialize General Counter for Steps and Ingredients
    private int ingredientCounter = 2;
    private int stepCounter = 2;
    private int latestInstruct = 0;
    private int latestIngred = 0;
    private ImageView finishImageView;
    private TextView finishTextView;
    private ImageView stepImageView;
    private TextView stepTextView;
    private ConnectionRequest connectionRequest;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<TextView> textViewList = new ArrayList<>();
    private ArrayList<Uploadable>ingredientList  = new ArrayList<>();
    private ArrayList<Uploadable> instructionList = new ArrayList<>();


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
        //refresh latestdata
        Activity activity = requireActivity();
        connectionRequest = new ConnectionRequest(activity);
        getLatestData(connectionRequest);

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

        ImageView imageView1 = view.findViewWithTag("process_photo_1");
        TextView textView1 = view.findViewWithTag("upload_process_text_1");
        finishImageView = view.findViewById(R.id.finished_photo);
        finishTextView = view.findViewById(R.id.finished_text);
        imageViewList.add(imageView1);
        textViewList.add(textView1);
        Button publish_recipe_button = view.findViewById(R.id.publish_recipe_button);
        publish_recipe_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        finaladdList(view);
                        publishRecipe(connectionRequest,ingredientList);
                    }
                }
        );
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchChoosePictureIntent(view);
                Log.d("PostFragment", "onClick: "+imageView1.getTag());
            }
        });
        finishImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishChoosePictureIntent(view);
            }
        });
        //Dynamically Add Ingredients
        Button addIngredientButton = view.findViewById(R.id.add_ingredient_button);
        LinearLayoutCompat ingredientsLayout = view.findViewById(R.id.ingredients_layout);
        addIngredientButton.setOnClickListener(v -> {
            //addIngredToList(view);
            View ingredientView = addViewToLayout(ingredientsLayout, R.layout.ingredient_layout);
            updateIngredientView(ingredientView, ingredientCounter++);
        });

        //Dynamically Add Instructions
        Button addStepButton = view.findViewById(R.id.add_step_button);
        LinearLayoutCompat instructionsLayout = view.findViewById(R.id.instructions_layout);
        addStepButton.setOnClickListener(v -> {
            //addInstructToList(view);
            View stepView = addViewToLayout(instructionsLayout, R.layout.step_layout);
            updateStepView(stepView, stepCounter++,view);
        });
    }

    private View addViewToLayout(LinearLayoutCompat layout, int layoutResId) {
        LayoutInflater inflater = LayoutInflater.from(layout.getContext());
        View newView = inflater.inflate(layoutResId, layout, false);
        layout.addView(newView);
        return newView;
    }

    private void updateStepView(View stepView, int number,View view) {
        //Update Step Text
        TextView stepTextView = stepView.findViewById(R.id.step_text);
        stepTextView.setText("Step " + number);

        //Generate Unique Id
        ImageView processPhoto = stepView.findViewById(R.id.process_photo);
        TextView uploadProcessText = stepView.findViewById(R.id.upload_process_text);
        EditText stepInstruction = stepView.findViewById(R.id.step_instruction);
        EditText time = stepView.findViewById(R.id.time);
        EditText timeScale = stepView.findViewById(R.id.time_unit);

        processPhoto.setId(View.generateViewId());
        uploadProcessText.setId(View.generateViewId());

        //Set Tags
        processPhoto.setTag("process_photo_" + String.valueOf(number));
        uploadProcessText.setTag("upload_process_text_" + String.valueOf(number));
        stepInstruction.setTag("step_instruction_" + String.valueOf(number));
        time.setTag("time_" + String.valueOf(number));
        timeScale.setTag("time_unit_" + String.valueOf(number));

        //Set OnClickListener
        processPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchChoosePictureIntent(view);
                Log.d("PostFragment", "onClick: "+stepTextView.getTag());
            }
        });

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

    private void addIngredToList(View view,int counter,int latest){
        String nameTag = "ingredient_name_" + String.valueOf(counter);
        String amountTag = "ingredient_amount_" + String.valueOf(counter);
        String name = findStringByTag(nameTag,view);
        String amount = findStringByTag(amountTag,view);
        Ingredient ingredient = new Ingredient();
        ingredient.setIdMeal(latest+1);
        ingredient.setIdIng(counter);
        ingredient.setStrIng(name);
        ingredient.setStrAmount(amount);
        ingredientList.add(ingredient);
    }
    private void addInstructToList(View view,int counter,int latest){
        Instruction instruction = new Instruction();
        String instTag = "step_instruction_"+ String.valueOf(counter);
        String inst = findStringByTag(instTag,view);
        instruction.setIdMeal(latest+1);
        instruction.setNumStep(counter);
        instruction.setIntstruct(inst);
        //instruction.setStepImg(base64String);
        //instruction.setStepTime("1");
        instructionList.add(instruction);
    }
    private void finaladdList(View view){
        for (int i = 1; i < ingredientCounter; i++ ){
            addIngredToList(view, i,latestIngred);
        }
        Log.d("finaladd", "finaladdList: "+"ingred added");
        for (int i = 1; i < stepCounter; i++ ){
            addInstructToList(view, i,latestInstruct);
        }
        Log.d("finaladd", "finaladdList: "+"instrut added");
    }
    public String findStringByTag(String tag,View view){
        String editTextValue = "";
        EditText editText = view.findViewWithTag(tag);
        if (editText != null){
            editTextValue = editText.getText().toString();
            Log.d("getEditText", "findStringByTag: "+tag+": "+editTextValue);
        } else{
            Log.e("getEditText", "fail to findStringByTag: "+ tag);
        }
        return editTextValue;
    }
    private void getLatestData(ConnectionRequest connectionRequest){
        String ingredurl ="https://studev.groept.be/api/a23PT214/get_latest_instruct&ingred";
        connectionRequest.jsonGetRequest(ingredurl,
                new ConnectionRequest.MyRequestCallback<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray response) {
                        try {
                            //String responseString = "";
                            for( int i = 0; i < response.length(); i++ )
                            {
                                JSONObject curObject = response.getJSONObject( i );
                                latestIngred=curObject.getInt("idMeal_ing");
                                latestInstruct=curObject.getInt("idMeal_inst");
                                Log.d("PostFragment", "onSuccess: "+"latestIngred:"+latestIngred+"latestInstruct:"+latestInstruct);
                            }
                        }
                        catch( JSONException e )
                        {
                            Log.e( "PostFragment", e.getMessage(), e );
                        }
                    }
                    @Override
                    public void onError(String error) {
                        Log.e( "PostFragment", error );
                    }
                });
    }

    private void publishInstruction(ConnectionRequest connectionRequest, HashMap<String,String> params){
        String instrutUrl = "https://studev.groept.be/api/a23PT214/upload_instrut";
        connectionRequest.uploadPostRequest(instrutUrl, params,
                new ConnectionRequest.MyRequestCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("postinstruct", "onSuccess: "+response.toString());
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("postinstruct", "onError: "+error );
                    }
                });
    }
    private void publishIngred(ConnectionRequest connectionRequest, HashMap<String,String> params){
        String ingredUrl = "https://studev.groept.be/api/a23PT214/upload_ingred";
        connectionRequest.uploadPostRequest(ingredUrl, params,
                new ConnectionRequest.MyRequestCallback() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.d("postingred", "onSuccess: "+response.toString());
                    }
                    @Override
                    public void onError(String error) {
                        Log.e("postingred", "onError: "+error );
                    }
                });
    }
    private void publishRecipe(ConnectionRequest connectionRequest,ArrayList<Uploadable> ingredList){
        List<HashMap> ingredHashMapList = getHashMap(ingredList);
        //List<HashMap> instructHashMapList = getHashMap(instrutList);
        for (int i = 0; i<ingredList.size();i++){
            publishIngred(connectionRequest,ingredHashMapList.get(i));
        }
//        for (int i = 0; i<ingredList.size();i++){
//            publishInstruction(connectionRequest,instructHashMapList.get(i));
//        }
    }
    private List<HashMap> getHashMap(ArrayList<Uploadable> arrayList) {
        List<HashMap> hashMapList = new ArrayList<>();
        int length = arrayList.size();
        HashMap<String,String> params= new HashMap<>();
        for (int i = 0; i<length; i++){
            params = arrayList.get(i).getHashMap();
            hashMapList.add(params);
        }
        return hashMapList;
    }

    private final ActivityResultLauncher<Intent> pickPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
    result -> {
        if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
            Uri selectedImage = result.getData().getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                if (bitmap != null) {
                    Log.d("PostFragment", "Bitmap is not null");
                    stepImageView.setImageBitmap(bitmap);
                    stepTextView.setVisibility(View.INVISIBLE);
                    Log.d("PostFragment", "Bitmap set to ImageView");
                    Log.d("PostFragment", bitmap.toString());
                } else {
                    Log.e("PostFragment", "Bitmap is null");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    );
    private final ActivityResultLauncher<Intent> finishPictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        if (bitmap != null) {
                            Log.d("PostFragment", "Bitmap is not null");
                            finishImageView.setImageBitmap(bitmap);
                            finishTextView.setVisibility(View.INVISIBLE);
                            Log.d("PostFragment", "Bitmap set to ImageView");
                            Log.d("PostFragment", bitmap.toString());
                        } else {
                            Log.e("PostFragment", "Bitmap is null");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );
    private void dispatchChoosePictureIntent(View view) {
        setCurrent(view);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPictureLauncher.launch(intent);
    }
    private void finishChoosePictureIntent(View view) {
        setCurrent(view);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        finishPictureLauncher.launch(intent);
    }
    private void setCurrent(View view) {
        String imageTag = "process_photo_" +String.valueOf(stepCounter-1);
        String textTag = "upload_process_text_" +String.valueOf(stepCounter-1);
        stepImageView = view.findViewWithTag(imageTag);
        stepTextView = view.findViewWithTag(textTag);

        if (stepImageView == null) {
            Log.e("PostFragment", "stepImageView is null");
        }

        if (stepTextView == null) {
            Log.e("PostFragment", "stepTextView is null");
        }
    }




    public Bitmap getResizedBitmap(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = ((float) newWidth) / width;

        // We create a matrix to transform the image
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // Create the new bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    View.OnClickListener loadImage = new View.OnClickListener() {
        ImageView imageView;
        @Override
        public void onClick(View v) {
            //dispatchChoosePictureIntent();
            Log.d("PostFragment", "onClick: "+imageView.getTag());
        }
    };
}


