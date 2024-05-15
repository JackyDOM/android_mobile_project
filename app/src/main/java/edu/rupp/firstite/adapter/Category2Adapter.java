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
import edu.rupp.firstite.databinding.ViewHolderCategory2Binding;
import edu.rupp.firstite.modals.CategoryBanner1;
import edu.rupp.firstite.modals.CategoryBanner2;

public class Category2Adapter extends ListAdapter<CategoryBanner2, Category2Adapter.Category2ViewHolder> {

    public Category2Adapter(){
        super(new DiffUtil.ItemCallback<CategoryBanner2>() {
            @Override
            public boolean areItemsTheSame(@NonNull CategoryBanner2 oldItem, @NonNull CategoryBanner2 newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull CategoryBanner2 oldItem, @NonNull CategoryBanner2 newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public Category2Adapter(AsyncDifferConfig<CategoryBanner2> config){
        super(config);
    }

    @NonNull
    @Override
    public Category2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderCategory2Binding binding = ViewHolderCategory2Binding.inflate(inflater);
        return new Category2ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Category2ViewHolder holder, int position) {
        CategoryBanner2 categoryBanner2 = getItem(position);
        holder.bingCategory2(categoryBanner2);

    }

    public class Category2ViewHolder extends RecyclerView.ViewHolder {
        private final ViewHolderCategory2Binding binding;

        public Category2ViewHolder(ViewHolderCategory2Binding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bingCategory2(CategoryBanner2 categoryBanner2){
            Picasso.get().load(categoryBanner2.getBook_image()).into(binding.bannerImageCategory2);
            this.binding.txtTitleCategory2.setText(categoryBanner2.getTitle());

            //display the price
            String priceWithPrefix = "Price: " + categoryBanner2.getPrice() + " $";
            this.binding.txtPriceCategory2.setText(priceWithPrefix);

            binding.bannerImageCategory2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
                    intent.putExtra("book_image_url", categoryBanner2.getBook_image());
                    intent.putExtra("book_title", categoryBanner2.getTitle());
                    intent.putExtra("book_price", categoryBanner2.getPrice());
                    intent.putExtra("book_publisher", categoryBanner2.getPublisher());
                    intent.putExtra("book_Category_Name", categoryBanner2.getCategory().getName());
                    intent.putExtra("author_name", categoryBanner2.getAuthor().getAuthor_name());
                    intent.putExtra("description", categoryBanner2.getDescription());
                    intent.putExtra("book_id", categoryBanner2.getId());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
