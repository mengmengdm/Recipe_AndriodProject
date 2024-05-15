package com.example.demoproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoproject.connection.ConnectionRequest;

import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.ViewHolder> {
private List<Ingredient> ingredientList;
private ConnectionRequest connectionRequest;
private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public IngredientRecyclerAdapter(List<Ingredient> ingredientList, Context context){
    this.ingredientList = ingredientList;
    connectionRequest = new ConnectionRequest(context);
    }

static class ViewHolder extends RecyclerView.ViewHolder{
    TextView ingredientnametextview;
    TextView ingredientmeasuretextview;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        ingredientnametextview = (TextView) itemView.findViewById(R.id.ingredientnametextview);
        ingredientmeasuretextview = (TextView) itemView.findViewById(R.id.ingredientmeasuretextview);
    }
}

    @NonNull
    @Override
    public IngredientRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngredientRecyclerAdapter.ViewHolder holder, int position) {
        //itemPosition = holder.getBindingAdapterPosition();
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientnametextview.setText(ingredient.getStrIng());
        holder.ingredientmeasuretextview.setText(ingredient.getStrAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }
}
