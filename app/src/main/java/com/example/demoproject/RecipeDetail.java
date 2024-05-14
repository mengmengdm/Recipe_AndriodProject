package com.example.demoproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
        nameTextView.setText(recipe.getName());
        //RecyclerView ingredRecyView = findViewById(R.id.recyclerviewforingrediant);
        RecyclerView detailRecyView = findViewById(R.id.recyclerviewfordetail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //ingredRecyView.setLayoutManager(linearLayoutManager);
        detailRecyView.setLayoutManager(linearLayoutManager);
        //RecyclerAdapter ingredAdapter = new RecyclerAdapter(instructionList)
        RecipeDetailRecyclerAdapter detailAdapter = new RecipeDetailRecyclerAdapter(instructionList,this);
        getInformation(recipe,recipe.getIdMeal(),detailAdapter,this,detailRecyView);

        detailRecyView.setAdapter(detailAdapter);
        // 设置其他 TextView 显示其他信息...

        // 设置系统边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getInformation(Recipe recipe, int id, RecipeDetailRecyclerAdapter adapter, Context context,RecyclerView recyclerView){

        String instructionUrl = "https://studev.groept.be/api/a23PT214/get_insrtuction_byid/";
        instructionUrl = instructionUrl + String.valueOf(id);
        Log.d("start", "getInformation: "+instructionUrl);
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
                        Log.d("getins", "onSuccess:");
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
}
