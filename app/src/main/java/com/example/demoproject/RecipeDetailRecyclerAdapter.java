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

public class RecipeDetailRecyclerAdapter extends RecyclerView.Adapter<RecipeDetailRecyclerAdapter.ViewHolder> {
private List<Instruction> instructionList;
private ConnectionRequest connectionRequest;
private OnItemClickListener listener;
private int itemPosition;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public RecipeDetailRecyclerAdapter(List<Instruction> instructionList, Context context){
    this.instructionList = instructionList;
    connectionRequest = new ConnectionRequest(context);
    }

static class ViewHolder extends RecyclerView.ViewHolder{
    TextView stepnumbertextview;
    ImageView steppicimageview;
    TextView stepdetailtextview;
    TextView timetextview;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        stepnumbertextview = (TextView) itemView.findViewById(R.id.stepnumbertextview);
        steppicimageview = (ImageView) itemView.findViewById(R.id.steppicimageview);
        stepdetailtextview = (TextView) itemView.findViewById(R.id.stepdetailtextview);
        timetextview = (TextView) itemView.findViewById(R.id.timetextview);
    }
}

    @NonNull
    @Override
    public RecipeDetailRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecipeDetailRecyclerAdapter.ViewHolder holder, int position) {
        //itemPosition = holder.getBindingAdapterPosition();
        Instruction instruction = instructionList.get(position);
        holder.stepnumbertextview.setText("STEP"+instruction.getNumStep()+":");
        holder.steppicimageview.setImageBitmap(instruction.getStepImg());
        holder.stepdetailtextview.setText(instruction.getIntstruct());
        holder.timetextview.setText(instruction.getStepTime());
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
        return instructionList.size();
    }
}
