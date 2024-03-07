package com.example.games;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdaptadorJuego extends RecyclerView.Adapter<AdaptadorJuego.CardViewHolder> {

    private List<String> cardName;
    private List<String> description;
    private List<Integer> imageList;



    public AdaptadorJuego(List<String> cardName, List<String> description, List<Integer> imageList) {
        this.cardName = cardName;
        this.description = description;
        this.imageList = imageList;
    }



    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String data = cardName.get(position);
        holder.textViewTitle.setText(data);
        holder.textViewDescription.setText(description.get(position));
        holder.imageView.setImageResource(imageList.get(position));
    }


    @Override
    public int getItemCount() {
        return cardName.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        TextView textViewTitle, textViewDescription;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.descripcionJuego);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }
}
