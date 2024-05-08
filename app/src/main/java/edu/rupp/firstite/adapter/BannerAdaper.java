package edu.rupp.firstite.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.databinding.ViewHolderBannerBinding;
import edu.rupp.firstite.modals.Banner;

public class BannerAdaper extends ListAdapter<Banner, BannerAdaper.BannerViewHolder> {
    public BannerAdaper(){
        super(new DiffUtil.ItemCallback<Banner>() {
            @Override
            public boolean areItemsTheSame(@NonNull Banner oldItem, @NonNull Banner newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Banner oldItem, @NonNull Banner newItem) {
                return oldItem.getId() == newItem.getId();
            }
        });
    }

    public BannerAdaper(AsyncDifferConfig<Banner> config){
        super(config);
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewHolderBannerBinding binding = ViewHolderBannerBinding.inflate(inflater);
        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Banner banner = getItem(position);
        holder.bingBanner(banner);
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private final ViewHolderBannerBinding binding;
        public BannerViewHolder(ViewHolderBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bingBanner(Banner banner){
            Picasso.get().load(banner.getBook_image()).into(binding.bannerImage);
            this.binding.txtTitle.setText(banner.getTitle());
        }
    }
}
