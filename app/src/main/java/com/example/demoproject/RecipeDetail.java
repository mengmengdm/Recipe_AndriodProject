package com.example.demoproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoproject.connection.ConnectionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {

    private Recipe recipe;
    private Instruction instruction;
    private List<Instruction> instructionList = new ArrayList<>();
    private List<Ingredient> ingredientList = new ArrayList<>();
    private ConnectionRequest connectionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_detail);
        connectionRequest = new ConnectionRequest(this);
        // get the recipe class pass from the previous fragment
        recipe = getIntent().getParcelableExtra("recipe");

        TextView nameTextView = findViewById(R.id.recipenametextview);
        ImageView mainpicimageview = findViewById(R.id.mainpicimageview);

        nameTextView.setText(recipe.getName());
        Bitmap bitmap = transBitmap(recipe.getBase64String());
        mainpicimageview.setImageBitmap(bitmap);

        RecyclerView ingredRecyView = findViewById(R.id.recyclerviewforingrediant);
        RecyclerView detailRecyView = findViewById(R.id.recyclerviewfordetail);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        ingredRecyView.setLayoutManager(linearLayoutManager1);
        detailRecyView.setLayoutManager(linearLayoutManager2);
        IngredientRecyclerAdapter ingredAdapter = new IngredientRecyclerAdapter(ingredientList,this);
        RecipeDetailRecyclerAdapter detailAdapter = new RecipeDetailRecyclerAdapter(instructionList,this);
        getInstruction(recipe,recipe.getIdMeal(),detailAdapter);
        getIngredient(recipe.getIdMeal(),ingredAdapter);
        detailRecyView.setAdapter(detailAdapter);
        ingredRecyView.setAdapter(ingredAdapter);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getInstruction(Recipe recipe, int id, RecipeDetailRecyclerAdapter adapter){

        String instructionUrl = "https://studev.groept.be/api/a23PT214/get_insrtuction_byid/";
        instructionUrl = instructionUrl + String.valueOf(id);
        Log.d("start", "getInstruction: "+instructionUrl);
        connectionRequest.jsonGetRequest(instructionUrl, new ConnectionRequest.MyRequestCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    for( int i = 0; i < response.length(); i++ )
                    {
                        Log.d("test??", "onSuccess: ");
                        Instruction instruction = new Instruction();
                        JSONObject curObject = response.getJSONObject( i );
                        instruction.setIdMeal(curObject.getInt("idMeal"));
                        instruction.setNumStep(curObject.getInt("numStep"));
                        instruction.setIntstruct(curObject.getString("strInstruct"));
                        instruction.setStepTime(curObject.getString("stepTime"));
                        instruction.setTimeScale(curObject.getString("timeScale"));
                        instruction.setStepImg(curObject.getString("stepImg"));
                        Log.d("getins", "onSuccess:"+instruction.getStepTime());
                        instructionList.add(instruction);
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
                Log.e("recipe", "onError: "+"creating a new recipe detail"+error );
            }
        });
    }
    private void getIngredient(int id, IngredientRecyclerAdapter adapter){

        String ingredientUrl = "https://studev.groept.be/api/a23PT214/get_ingredient_byid/";
        ingredientUrl = ingredientUrl + String.valueOf(id);
        Log.d("start", "getInformation: "+ingredientUrl);
        connectionRequest.jsonGetRequest(ingredientUrl, new ConnectionRequest.MyRequestCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray response) {
                try {
                    for( int i = 0; i < response.length(); i++ )
                    {
                        Log.d("test??", "onSuccess: ");
                        Ingredient ingredient = new Ingredient();
                        JSONObject curObject = response.getJSONObject( i );
                        ingredient.setIdMeal(curObject.getInt("idMeal"));
                        ingredient.setIdIng(curObject.getInt("idIng"));
                        ingredient.setStrIng(curObject.getString("strIng"));
                        ingredient.setStrAmount(curObject.getString("strAmount"));
                        Log.d("getingredient", "onSuccess:");
                        ingredientList.add(ingredient);
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
                Log.e("recipe", "onError: "+"creating a new recipe detail"+error );
            }
        });
    }
    public Bitmap transBitmap(String base64String){
        Bitmap bitmap;
        byte[] imageBytes = Base64.decode( base64String, Base64.DEFAULT );
        bitmap = BitmapFactory.decodeByteArray( imageBytes, 0, imageBytes.length );
        return bitmap;
    }
}

