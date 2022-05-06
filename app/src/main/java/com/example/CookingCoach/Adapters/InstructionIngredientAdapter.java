package com.example.CookingCoach.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CookingCoach.Models.Ingredient;
import com.example.CookingCoach.R;
import android.content.Context;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionIngredientAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder>{

    Context context;
    List<Ingredient> list;

    public InstructionIngredientAdapter(Context context, List<Ingredient> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_instructions_step_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {

        holder.textView_instructions_step_item.setText(list.get(position).name);
        holder.textView_instructions_step_item.setSelected(true);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder{

    TextView textView_instructions_step_item;
    public InstructionIngredientsViewHolder(@NonNull View itemView){
        super(itemView);
        textView_instructions_step_item = itemView.findViewById(R.id.textView_instructions_step_item);
    }
}