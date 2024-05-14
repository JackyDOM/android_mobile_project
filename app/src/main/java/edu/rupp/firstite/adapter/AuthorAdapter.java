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

import edu.rupp.firstite.author_screen.AuthorInformationActivity;
import edu.rupp.firstite.databinding.ViewHolderAuthorListBinding;
import edu.rupp.firstite.modals.Author;

public class AuthorAdapter extends ListAdapter<Author, AuthorAdapter.AuthorViewHolder> {

    public AuthorAdapter(){
        super(new DiffUtil.ItemCallback<Author>() {
            @Override
            public boolean areItemsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Author oldItem, @NonNull Author newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public AuthorAdapter(AsyncDifferConfig<Author> config){
        super(config);
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderAuthorListBinding binding = ViewHolderAuthorListBinding.inflate(inflater, parent, false);
        return new AuthorViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull AuthorViewHolder holder, int position) {
        Author author = getItem(position);
        holder.bingAuthor(author);
    }

    public class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderAuthorListBinding binding;

        public AuthorViewHolder(ViewHolderAuthorListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bingAuthor(Author author){
            Picasso.get().load(author.getAuthor_image()).into(binding.AuthorImage);
            this.binding.txtAuthorName.setText(author.getAuthor_name());
            //this.binding.txtAuthorName.setText(author.getGender());
            //this.binding.txtAuthorName.setText(author.getAuthor_name());
            //this.binding.txtAuthorName.setText(author.getAuthor_decs());

            binding.AuthorImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), AuthorInformationActivity.class);
                    intent.putExtra("author_id", author.getId());
                    intent.putExtra("author_image_url",author.getAuthor_image());
                    intent.putExtra("author_gender",author.getGender());
                    intent.putExtra("author_name",author.getAuthor_name());
                    intent.putExtra("author_decs", author.getAuthor_decs());
                    v.getContext().startActivity(intent);
                }
            });
        }

    }
}
