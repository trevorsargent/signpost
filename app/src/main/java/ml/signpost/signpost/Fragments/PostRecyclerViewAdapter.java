package ml.signpost.signpost.Fragments;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ml.signpost.signpost.Models.Post;
import ml.signpost.signpost.R;

/**
 * Created by Trevor on 4/10/16.
 */
public class PostRecyclerViewAdapter extends RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder> {

    private OnRowClickListener mListener;
    private ArrayList<Post> mPosts;
    private Location mLastLocation;

    public interface OnRowClickListener {
        void onRowClick(Post post);
    }


    public PostRecyclerViewAdapter(List<Post> listCall, OnRowClickListener listener, Location location) {
        mPosts = new ArrayList<>(listCall);
        mListener = listener;
        mLastLocation = location;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.row_post, parent, false);

        return new PostViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final PostRecyclerViewAdapter.PostViewHolder holder, int position) {
        Post post = mPosts.get(position);

        holder.dist.setText("Distance away: " + getDistanceTo(post) + " mi");
        holder.lastUpdated.setText("Last updated: 24 min ago");
        holder.signCount.setText("10 signs");
        holder.title.setText(post.getTitle().substring(0,1).toUpperCase()+ post.getTitle().substring(1).toLowerCase());

        holder.fullView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRowClick(mPosts.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public Post getItem(int position) {
        return mPosts == null ? null : mPosts.get(position);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }



    static class PostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.row_post_title_text)
        TextView title;

        @Bind(R.id.row_post_distance)
        TextView dist;

        @Bind(R.id.row_post_last_updated)
        TextView lastUpdated;

        @Bind(R.id.row_post_sign_count)
        TextView signCount;

        View fullView;

        public PostViewHolder(View itemView) {
            super(itemView);
            fullView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItems(ArrayList<Post> list) {
        mPosts.addAll(list);
        notifyDataSetChanged();
    }

    public String getDistanceTo(Post post){
        float[] res = new float[1];
        double dist;
        Location.distanceBetween(mLastLocation.getLatitude(), mLastLocation.getLongitude(), post.getLat(), post.getLng(), res);
        dist = res[0];
        //conversion to miles
        dist = dist*0.000621371;

        StringBuilder sb = new StringBuilder();
        return sb.append(dist).substring(0,3);
    }

}
