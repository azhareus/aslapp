package com.example.aslapp.ReviewItems;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aslapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    Context context;
    List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View quoteView = LayoutInflater.from(context).inflate(R.layout.fragment_review_cards, parent, false);
        return new ViewHolder(quoteView);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("ReviewAdapter", "onBindViewHolder" + position);
        //Get the quote at the passed in position
        Review review = reviews.get(position);
        //Bind the quotes data into the VH
        holder.bind(review);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView reviewAns;
        public ImageView iv2;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            reviewAns = itemView.findViewById(R.id.reviewAns);
            iv2 =  itemView.findViewById(R.id.iv2);
        }

        public void bind(Review review) {
            reviewAns.setText(review.getTranslation());
            iv2.setImageResource(review.getSign());
        }
    }
}

