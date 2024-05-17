package edu.rupp.firstite.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.rupp.firstite.Home_screen.BookDetailActivity;
import edu.rupp.firstite.R;
import edu.rupp.firstite.modals.Searching;

public class SearchingAdapter extends RecyclerView.Adapter<SearchingAdapter.ViewHolder> {
    private List<Searching> searchResults;
    private Context context; // Add context

    public SearchingAdapter(List<Searching> searchResults, Context context) { // Update constructor
        this.searchResults = searchResults;
        this.context = context;
    }

    public void updateData(List<Searching> newResults) {
        this.searchResults = newResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Searching searching = searchResults.get(position);
        holder.titleTextView.setText(searching.getTitle());
        Glide.with(holder.bookImageView.getContext()).load(searching.getBook_image()).into(holder.bookImageView);

        // Set click listener for the book image
        holder.bookImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra("book_image_url", searching.getBook_image());
            intent.putExtra("book_title", searching.getTitle());
            intent.putExtra("book_price", searching.getPrice());
            intent.putExtra("book_publisher", searching.getPublisher());
            intent.putExtra("book_Category_Name", searching.getCategory().getName());
            intent.putExtra("author_name", searching.getAuthor().getAuthor_name());
            intent.putExtra("author_Decs", searching.getAuthor().getAuthor_decs());
            intent.putExtra("book_id", searching.getId());
            intent.putExtra("book_pdf", searching.getBook_pdf());// Assuming you have a getBookId method
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookImageView;
        public TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.imageSearching);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}
