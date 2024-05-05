package com.example.demoproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
private List<Recipe> recipeList;

public RecyclerAdapter(List<Recipe> recipeList){
    this.recipeList = recipeList;
}

static class ViewHolder extends RecyclerView.ViewHolder{
    TextView testText;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        testText = (TextView) itemView.findViewById(R.id.testTextview2);
    }
}

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.testText.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
