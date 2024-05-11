package edu.rupp.firstite.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.databinding.ViewHolderCartBinding;
import edu.rupp.firstite.modals.Book;

public class CartAdapter extends ListAdapter<Book, CartAdapter.CartViewHolder> {

    public CartAdapter(){
        super(new DiffUtil.ItemCallback<Book>() {
            @Override
            public boolean areItemsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Book oldItem, @NonNull Book newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public CartAdapter(AsyncDifferConfig<Book> config){
        super(config);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderCartBinding binding = ViewHolderCartBinding.inflate(inflater, parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Book book = getItem(position);
        holder.bingCart(book);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCartBinding binding;

        public CartViewHolder(ViewHolderCartBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bingCart(Book book){
            Picasso.get().load(book.getBook_image()).into(binding.CartBookImage);
        }
    }
}
