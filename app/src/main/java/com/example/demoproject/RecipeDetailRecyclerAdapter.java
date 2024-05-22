package com.example.demoproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    TextView countdownText;
    CountDownTimer countDownTimer;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        stepnumbertextview = (TextView) itemView.findViewById(R.id.stepnumbertextview);
        steppicimageview = (ImageView) itemView.findViewById(R.id.steppicimageview);
        stepdetailtextview = (TextView) itemView.findViewById(R.id.stepdetailtextview);
        timetextview = (TextView) itemView.findViewById(R.id.timetextview);
        countdownText = (TextView) itemView.findViewById(R.id.countdownText);
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
        String timeScale = instruction.getTimeScale();
        String stepTime = instruction.getStepTime();
        if (!instruction.getTimeScale().equals("null")){
            holder.timetextview.setText(instruction.getStepTime()+instruction.getTimeScale());
        }
        else{
            holder.countdownText.setVisibility(View.GONE);
        }
        holder.countdownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.countDownTimer != null) {
                    holder.countDownTimer.cancel();
                }

                if (stepTime != null && !stepTime.equals("null") && !stepTime.isEmpty()) {
                    int time = Integer.parseInt(stepTime);
                    int timeInMillis = 0;

                    switch (timeScale.toLowerCase()) {
                        case "second":
                            timeInMillis = time * 1000;
                            break;
                        case "minute":
                            timeInMillis = time * 60 * 1000;
                            break;
                        case "hour":
                            timeInMillis = time * 60 * 60 * 1000;
                            break;
                        default:
                            timeInMillis = 0;
                            break;
                    }

                    if (timeInMillis > 0) {
                        holder.countDownTimer = new CountDownTimer(timeInMillis, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                int minutes = (int) (millisUntilFinished / 1000) / 60;
                                int seconds = (int) (millisUntilFinished / 1000) % 60;
                                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                                holder.countdownText.setText(timeFormatted);
                            }

                            @Override
                            public void onFinish() {
                                holder.countdownText.setText("Time's up!");
                            }
                        }.start();
                    }
                }
            }
        });
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
