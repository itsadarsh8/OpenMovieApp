package com.example.popularmoviesapp.Adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.MovieDetailActivity;
import com.example.popularmoviesapp.R;

import java.util.ArrayList;
import java.util.List;

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.VideoViewHolder> {

    private ArrayList<String> movieLinkList;
    private Context mContext;

    public MovieVideoAdapter(ArrayList<String> movieLinkList, Context context) {
        this.movieLinkList = movieLinkList;
        mContext=context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trailor_layout,parent,false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder,final int position) {
        holder.trailorView.setText("Trailor"+String.valueOf(position+1));
        holder.trailorView.setOnClickListener(new View.OnClickListener() {
            String id= movieLinkList.get(position);
            @Override
            public void onClick(View v) {

                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
                try {
                    mContext.startActivity(webIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return movieLinkList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView trailorView;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            trailorView=itemView.findViewById(R.id.trailer_textView);


        }
    }{

    }
}
