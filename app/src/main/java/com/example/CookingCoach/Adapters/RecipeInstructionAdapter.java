package com.example.CookingCoach.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CookingCoach.Models.RecipeInstructionsResponse;
import com.example.CookingCoach.R;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeInstructionAdapter extends RecyclerView.Adapter<InstructionsViewHolder>{

    Context context;
    List<RecipeInstructionsResponse> list;

    public RecipeInstructionAdapter(Context context, List<RecipeInstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout .recipe_instructions, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionsViewHolder holder, int position) {

        holder.textView_instructions_name.setText(list.get(position).name);
        holder.recycler_instruction_steps.setHasFixedSize(true);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionsViewHolder extends RecyclerView.ViewHolder{
    TextView textView_instructions_name;
    RecyclerView recycler_instruction_steps;
    public InstructionsViewHolder(@NonNull View itemView){
        super(itemView);
        textView_instructions_name = itemView.findViewById(R.id.textView_instructions_name);
        recycler_instruction_steps = itemView.findViewById(R.id.recycler_instruction_steps);

    }
}