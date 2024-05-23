package edu.rupp.firstite.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.Home_screen.BookDetailActivity;
import edu.rupp.firstite.Home_screen.BookPdfActivity;
import edu.rupp.firstite.databinding.ViewHolderYourBookBinding;
import edu.rupp.firstite.modals.YourBookModal;

public class YourBookAdapter extends ListAdapter<YourBookModal, YourBookAdapter.YourBookViewHolder> {

    public YourBookAdapter(){
        super(new DiffUtil.ItemCallback<YourBookModal>() {
            @Override
            public boolean areItemsTheSame(@NonNull YourBookModal oldItem, @NonNull YourBookModal newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull YourBookModal oldItem, @NonNull YourBookModal newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public YourBookAdapter(AsyncDifferConfig<YourBookModal> config){
        super(config);
    }

    @NonNull
    @Override
    public YourBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderYourBookBinding binding = ViewHolderYourBookBinding.inflate(inflater, parent, false);
        return new YourBookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull YourBookViewHolder holder, int position) {
        YourBookModal yourBookModal = getItem(position);
        holder.bingYourBook(yourBookModal);
    }

    public class YourBookViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderYourBookBinding binding;

        public YourBookViewHolder(ViewHolderYourBookBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bingYourBook(YourBookModal yourBookModal){
            Picasso.get().load(yourBookModal.getBook().getBook_image()).into(binding.CartBookImage);
            this.binding.txtTitle.setText(yourBookModal.getBook().getTitle());

            final String bookPdfUrl = yourBookModal.getBook().getBook_pdf();
            Log.d("YourBookAdapter", "Book PDF URL: " + bookPdfUrl);

            binding.CartBookImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BookPdfActivity.class);
                    intent.putExtra("book_pdf_url", yourBookModal.getBook().getBook_pdf());
                    v.getContext().startActivity(intent);
                }
            });

        }
    }
}


