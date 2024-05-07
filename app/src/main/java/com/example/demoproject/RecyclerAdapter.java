package com.example.demoproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoproject.connection.ConnectionRequest;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
private List<Recipe> recipeList;
private ConnectionRequest connectionRequest;

    public RecyclerAdapter(List<Recipe> recipeList, Context context){
    this.recipeList = recipeList;
    connectionRequest = new ConnectionRequest(context);
}

static class ViewHolder extends RecyclerView.ViewHolder{
    TextView text1;
    TextView text2;
    ImageView img1;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        text1 = (TextView) itemView.findViewById(R.id.testTextview1);
        text2 = (TextView) itemView.findViewById(R.id.testTextview2);
        img1 = (ImageView) itemView.findViewById(R.id.testImage);
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
        holder.text1.setText(recipe.getIdReal());
        holder.text2.setText(recipe.getImgUrl());
        holder.img1.setImageBitmap(recipe.getBitmap());
        //holder.img1.setImageBitmap();
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
}
