package ua.km.nashgorodok.adapters;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ua.km.nashgorodok.R;
import ua.km.nashgorodok.database.models.Feed;
import ua.km.nashgorodok.databinding.FeedItemBinding;


public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedItemViewHolder> {

    private List<Feed> feedList;
    private boolean changed = false;

    @NonNull
    @Override
    public FeedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FeedItemBinding feedItemBinding = FeedItemBinding.inflate(inflater, parent, false);
        return new FeedItemViewHolder(feedItemBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull FeedItemViewHolder holder, int position) {
        holder.feedItemBinding.setFeed(feedList.get(position));
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public void setFeedList(List<Feed> feedList){
        this.feedList = feedList;
    }

    public List<Feed> getFeedList() {
        return feedList;
    }

    public boolean isChanged() {
        return changed;
    }

    @BindingAdapter("app:imageUrl")
    public static void bindImageView(ImageView imageView, String url){
        Picasso.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }



    public class FeedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FeedItemBinding feedItemBinding;

        FeedItemViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.item_feed).setOnClickListener(this);
            feedItemBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void onClick(View view) {
            final Feed feed = feedItemBinding.getFeed();
            if (feed.getIsNew()) {
                feed.setIsNew(false);
                notifyItemChanged(getAdapterPosition());
            }
            changed = !feed.getIsNew();
        }
    }

}
