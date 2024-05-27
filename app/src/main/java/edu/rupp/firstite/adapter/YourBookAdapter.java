package edu.rupp.firstite.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.rupp.firstite.Home_screen.BookPdfActivity;
import edu.rupp.firstite.databinding.ViewHolderYourBookBinding;
import edu.rupp.firstite.modals.YourBookModal;
import edu.rupp.firstite.service.ApiServiceYourBook;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YourBookAdapter extends ListAdapter<YourBookModal, YourBookAdapter.YourBookViewHolder> {

    private String accessToken;

    public YourBookAdapter(String accessToken) {
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
        this.accessToken = accessToken;
    }

    public YourBookAdapter(AsyncDifferConfig<YourBookModal> config) {
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
        holder.bindYourBook(yourBookModal);
    }

    public class YourBookViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderYourBookBinding binding;

        public YourBookViewHolder(ViewHolderYourBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindYourBook(YourBookModal yourBookModal) {
            Picasso.get().load(yourBookModal.getBook().getBook_image()).into(binding.CartBookImage);
            this.binding.txtTitle.setText(yourBookModal.getBook().getTitle());

            final String bookPdfUrl = yourBookModal.getBook().getBook_pdf();
            Log.d("YourBookAdapter", "Book PDF URL: " + bookPdfUrl);

            binding.CartBookImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BookPdfActivity.class);
                    intent.putExtra("book_pdf_url", bookPdfUrl);
                    v.getContext().startActivity(intent);
                }
            });

            binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBook(yourBookModal.getId(), getAdapterPosition());
                }
            });
        }
    }

    private void deleteBook(int bookId, int position) {
        Retrofit httpClient = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiServiceYourBook apiServiceYourBook = httpClient.create(ApiServiceYourBook.class);
        Call<Void> call = apiServiceYourBook.deleteYourBook("Bearer " + accessToken, bookId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    removeBook(position);
                } else {
                    Log.e("YourBookAdapter", "Failed to delete book: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("YourBookAdapter", "Failed to delete book", t);
            }
        });
    }

    public void removeBook(int position) {
        if (position >= 0 && position < getItemCount()) {
            List<YourBookModal> currentList = new ArrayList<>(getCurrentList());
            currentList.remove(position);
            submitList(currentList);
        }
    }
}
