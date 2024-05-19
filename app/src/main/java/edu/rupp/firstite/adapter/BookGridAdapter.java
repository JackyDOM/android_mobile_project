package edu.rupp.firstite.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.Home_screen.BookDetailActivity;
import edu.rupp.firstite.databinding.ViewHolderBookGridBinding;
import edu.rupp.firstite.modals.BookGridDisplay;

public class BookGridAdapter extends ListAdapter<BookGridDisplay, BookGridAdapter.ViewHolderBookGrid> {

    public BookGridAdapter() {
        super(new DiffUtil.ItemCallback<BookGridDisplay>() {
            @Override
            public boolean areItemsTheSame(@NonNull BookGridDisplay oldItem, @NonNull BookGridDisplay newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull BookGridDisplay oldItem, @NonNull BookGridDisplay newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public BookGridAdapter(@NonNull AsyncDifferConfig<BookGridDisplay> config) {
        super(config);
    }

    @NonNull
    @Override
    public ViewHolderBookGrid onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderBookGridBinding binding = ViewHolderBookGridBinding.inflate(inflater, parent, false);
        return new ViewHolderBookGrid(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBookGrid holder, int position) {
        BookGridDisplay bookGridDisplay = getItem(position);
        holder.bindBookGrid(bookGridDisplay);
    }

    public static class ViewHolderBookGrid extends RecyclerView.ViewHolder {
        private final ViewHolderBookGridBinding binding;

        public ViewHolderBookGrid(ViewHolderBookGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindBookGrid(BookGridDisplay bookGridDisplay) {
            Picasso.get().load(bookGridDisplay.getBook_image()).into(binding.imageViwBookGrid);
            binding.txtBookGrid.setText(bookGridDisplay.getTitle());

            binding.imageViwBookGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentBookGrid = new Intent(v.getContext(), BookDetailActivity.class);
                    intentBookGrid.putExtra("book_image_url",bookGridDisplay.getBook_image());
                    intentBookGrid.putExtra("book_title",bookGridDisplay.getTitle());
                    intentBookGrid.putExtra("book_price", bookGridDisplay.getPrice());
                    intentBookGrid.putExtra("book_publisher", bookGridDisplay.getPublisher());
                    intentBookGrid.putExtra("book_Category_Name", bookGridDisplay.getCategory().getName());
                    intentBookGrid.putExtra("author_name", bookGridDisplay.getAuthor().getAuthor_name());
                    intentBookGrid.putExtra("description", bookGridDisplay.getDescription());
                    intentBookGrid.putExtra("book_id", bookGridDisplay.getId());
                    intentBookGrid.putExtra("book_pdf", bookGridDisplay.getBook_pdf());
                    v.getContext().startActivity(intentBookGrid);
                }
            });
        }
    }
}
