package ml.signpost.signpost.Fragments;

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

    public interface OnRowClickListener {
        void onRowClick(Post post);
    }


    public PostRecyclerViewAdapter(List<Post> listCall, OnRowClickListener listener) {
        mPosts = new ArrayList<>(listCall);
        mListener = listener;
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


        holder.lng.setText(post.getLng()+"");
        holder.lat.setText(post.getLat()+"");
        holder.title.setText(post.getTitle());

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

        @Bind(R.id.row_post_lat_num_text)
        TextView lat;

        @Bind(R.id.row_post_lng_num_text)
        TextView lng;

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


}
