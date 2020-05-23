package com.example.popularmoviesapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapp.Objects.MovieReview;
import com.example.popularmoviesapp.R;

import org.w3c.dom.Text;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewHolder> {
    private List<MovieReview> mList;
    private Context mContext;

    public MovieReviewAdapter(List<MovieReview> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_layout, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {

        holder.author.setText("Author: " + mList.get(position).getReviewAuthor());
        holder.content.setText(mList.get(position).getReviewDetail());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        private TextView author;
        private TextView content;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author_name);
            content = itemView.findViewById(R.id.review_content);

        }
    }
}
