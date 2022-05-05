package com.example.CookingCoach.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CookingCoach.Models.ExtendedIngredient;
import com.example.CookingCoach.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IngrAdapt extends RecyclerView.Adapter<ingrHolder>{

    Context info;
    List<ExtendedIngredient> list;
    //make a constructor for both of them to get the info

    public IngrAdapt(Context info, List<ExtendedIngredient> list) {
        this.info = info;
        this.list = list;
    }

    @NonNull
    @Override
    public ingrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ingrHolder(LayoutInflater.from(info).inflate(R.layout.meal_ingredients, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ingrHolder holder, int position) {
        //get info for text name and quantity from the list
        holder.ingreName.setText(list.get(position).name);
        //holder.ingreName.setSelected(true);
        holder.ingrQuantity.setText(list.get(position).original);
        //holder.ingrQuantity.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class ingrHolder extends RecyclerView.ViewHolder
{
    TextView ingrQuantity, ingreName;
    //ImageView ingrImage;
    RecyclerView recycler_instruction_steps;

    public ingrHolder(@NonNull View itemView) {
        super(itemView);

        //initialize the ids
        ingrQuantity = itemView.findViewById(R.id.ingrQuantity);
        ingreName = itemView.findViewById(R.id.ingreName);
        //ingrImage = itemView.findViewById(R.id.ingrImage);
    }
}