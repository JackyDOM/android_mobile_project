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
import edu.rupp.firstite.databinding.ViewHolderAuthorBookBinding;
import edu.rupp.firstite.modals.AuthorBookDisplay;

public class BookAuthorAdapter extends ListAdapter<AuthorBookDisplay, BookAuthorAdapter.ViewHolderBookAuthor> {
    public BookAuthorAdapter(){
        super(new DiffUtil.ItemCallback<AuthorBookDisplay>() {
            @Override
            public boolean areItemsTheSame(@NonNull AuthorBookDisplay oldItem, @NonNull AuthorBookDisplay newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull AuthorBookDisplay oldItem, @NonNull AuthorBookDisplay newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public BookAuthorAdapter(AsyncDifferConfig<AuthorBookDisplay> config){
        super(config);
    }

    @NonNull
    @Override
    public ViewHolderBookAuthor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderAuthorBookBinding binding = ViewHolderAuthorBookBinding.inflate(inflater);
        return new ViewHolderBookAuthor(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBookAuthor holder, int position) {
        AuthorBookDisplay authorBookDisplay = getItem(position);
        holder.bindAuthorBook(authorBookDisplay);
    }

    public class ViewHolderBookAuthor extends RecyclerView.ViewHolder {
        private final ViewHolderAuthorBookBinding binding;

        public ViewHolderBookAuthor(ViewHolderAuthorBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindAuthorBook(AuthorBookDisplay authorBookDisplay){
            Picasso.get().load(authorBookDisplay.getBook_image()).into(binding.bookAuthorImage);
            this.binding.txtBookAuthorTitle.setText(authorBookDisplay.getTitle());

            binding.bookAuthorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAuthorBook = new Intent(v.getContext(), BookDetailActivity.class);
                    intentAuthorBook.putExtra("book_image_url",authorBookDisplay.getBook_image());
                    intentAuthorBook.putExtra("book_title",authorBookDisplay.getTitle());
                    intentAuthorBook.putExtra("book_price", authorBookDisplay.getPrice());
                    intentAuthorBook.putExtra("book_publisher", authorBookDisplay.getPublisher());
                    intentAuthorBook.putExtra("book_Category_Name", authorBookDisplay.getCategory().getName());
                    intentAuthorBook.putExtra("author_name", authorBookDisplay.getAuthor().getAuthor_name());
                    intentAuthorBook.putExtra("description", authorBookDisplay.getDescription());
                    intentAuthorBook.putExtra("book_id", authorBookDisplay.getId());
                    intentAuthorBook.putExtra("book_pdf", authorBookDisplay.getBook_pdf());
                    v.getContext().startActivity(intentAuthorBook);
                }
            });
        }

    }
}
