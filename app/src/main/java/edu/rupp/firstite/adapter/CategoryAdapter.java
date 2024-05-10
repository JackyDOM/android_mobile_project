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
import edu.rupp.firstite.author_screen.AuthorInformationActivity;
import edu.rupp.firstite.databinding.ViewHolderCategory1Binding;
import edu.rupp.firstite.modals.CategoryBanner1;

public class CategoryAdapter extends ListAdapter<CategoryBanner1, CategoryAdapter.CategoryViewHolder> {

    public CategoryAdapter(){
        super(new DiffUtil.ItemCallback<CategoryBanner1>() {
            @Override
            public boolean areItemsTheSame(@NonNull CategoryBanner1 oldItem, @NonNull CategoryBanner1 newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CategoryBanner1 oldItem, @NonNull CategoryBanner1 newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public CategoryAdapter(AsyncDifferConfig<CategoryBanner1> config){
        super(config);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderCategory1Binding binding = ViewHolderCategory1Binding.inflate(inflater);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryBanner1 categoryBanner1 = getItem(position);
        holder.bingCategory(categoryBanner1);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCategory1Binding binding;

        public CategoryViewHolder(ViewHolderCategory1Binding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bingCategory(CategoryBanner1 categoryBanner1){
            Picasso.get().load(categoryBanner1.getBook_image()).into(binding.bannerImageCategory);
            this.binding.txtTitleCategory.setText(categoryBanner1.getTitle());

            // display the price
            String priceWithPrefix = "Price: " + categoryBanner1.getPrice() + " $";
            this.binding.txtPriceCategory.setText(priceWithPrefix);

            binding.bannerImageCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                    intent.putExtra("book_image_url",categoryBanner1.getBook_image());
                    intent.putExtra("book_title",categoryBanner1.getTitle());
                    intent.putExtra("book_price", categoryBanner1.getPrice());
                    intent.putExtra("book_publisher", categoryBanner1.getPublisher());
                    intent.putExtra("book_Category_Name", categoryBanner1.getCategory().getName());
                    intent.putExtra("author_name", categoryBanner1.getAuthor().getAuthor_name());
                    intent.putExtra("author_Decs", categoryBanner1.getAuthor().getAuthor_decs());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
