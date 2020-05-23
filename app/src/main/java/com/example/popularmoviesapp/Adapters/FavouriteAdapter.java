package com.example.popularmoviesapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.ArchitectureComponents.FavouritesViewModel;
import com.example.popularmoviesapp.Objects.FavouriteDetails;
import com.example.popularmoviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.favouriteViewHolder> {

    private List<FavouriteDetails> list = new ArrayList<>();

    @NonNull
    @Override
    public favouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_layout, parent, false);
        return new favouriteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull favouriteViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.date.setText("Release Date: "+list.get(position).getDate());
        holder.rating.setText("Ratings: "+list.get(position).getRating());
        holder.description.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<FavouriteDetails> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public FavouriteDetails getItemAt(int position) {
        return list.get(position);
    }

    public class favouriteViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView date;
        private TextView rating;

        public favouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.favourite_title);
            date = itemView.findViewById(R.id.favourite_date);
            description = itemView.findViewById(R.id.favourite_description);
            rating = itemView.findViewById(R.id.favourite_rating);
        }

    }
}
