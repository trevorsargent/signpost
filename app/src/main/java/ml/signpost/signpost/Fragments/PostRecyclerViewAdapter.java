package ml.signpost.signpost.Fragments;

import android.content.Context;
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

    public interface OnRowClickListener {
        void onRowClick(Post post);
    }

    private ArrayList<Post> mPosts;

    public PostRecyclerViewAdapter(List<Post> listCall) {
        mPosts = new ArrayList<>(listCall);
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


        holder.title.setText(post.getTitle());

        Context context = holder.title.getContext();

        holder.fullView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRowClick(mPosts.get(holder.getAdapterPosition()));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.row_post_title_text)
        TextView title;

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
