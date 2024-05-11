package edu.rupp.firstite.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.databinding.ViewHolderCartBookListBindingBinding;
import edu.rupp.firstite.modals.CartBookModal;

public class BookCartsAdapter extends ListAdapter<CartBookModal, BookCartsAdapter.CartBookViewHolder> {

    public BookCartsAdapter(){
        super(new DiffUtil.ItemCallback<CartBookModal>() {
            @Override
            public boolean areItemsTheSame(@NonNull CartBookModal oldItem, @NonNull CartBookModal newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CartBookModal oldItem, @NonNull CartBookModal newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public BookCartsAdapter(AsyncDifferConfig<CartBookModal> config){
        super(config);

    }

    @NonNull
    @Override
    public CartBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderCartBookListBindingBinding binding = ViewHolderCartBookListBindingBinding.inflate(inflater);
        return new CartBookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartBookViewHolder holder, int position) {
        CartBookModal cartBookModal = getItem(position);
        holder.bingBookCart(cartBookModal);
    }

    public class CartBookViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCartBookListBindingBinding binding;

        public CartBookViewHolder(ViewHolderCartBookListBindingBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bingBookCart(CartBookModal cartBookModal){
            Picasso.get().load(cartBookModal.getBook_image()).into(binding.CartBookImage);
            this.binding.xx.setText(cartBookModal.getBook().getPrice());

        }
    }
}
