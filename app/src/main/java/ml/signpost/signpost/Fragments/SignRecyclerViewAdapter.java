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
import ml.signpost.signpost.Models.Sign;
import ml.signpost.signpost.R;

/**
 * Created by student on 4/14/16.
 */
public class SignRecyclerViewAdapter extends RecyclerView.Adapter<SignRecyclerViewAdapter.SignViewHolder> {

        private OnRowClickListener mListener;
        private ArrayList<Sign> mSigns;

public interface OnRowClickListener {
    void onRowClick(Sign sign);
}


    public SignRecyclerViewAdapter(List<Sign> listCall, OnRowClickListener listener) {
        mSigns = new ArrayList<>(listCall);
        mListener = listener;
    }

    @Override
    public SignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.row_sign, parent, false);

        return new SignViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final SignRecyclerViewAdapter.SignViewHolder holder, int position) {
        Sign sign = mSigns.get(position);

            //TODO make the view holder.
//        holder.lng.setText(post.getLng()+"");
//        holder.lat.setText(post.getLat()+"");
//        holder.title.setText(post.getTitle());

        holder.fullView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRowClick(mSigns.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    public Sign getItem(int position) {
        return mSigns == null ? null : mSigns.get(position);
    }

    @Override
    public int getItemCount() {
        return mSigns.size();
    }



static class SignViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.row_post_title_text)
    TextView title;

    @Bind(R.id.row_post_lat_num_text)
    TextView lat;

    @Bind(R.id.row_post_lng_num_text)
    TextView lng;

    View fullView;

    public SignViewHolder(View itemView) {
        super(itemView);
        fullView = itemView;
        ButterKnife.bind(this, itemView);
    }
}

    public void addItems(ArrayList<Sign> list) {
        mSigns.addAll(list);
        notifyDataSetChanged();
    }
}
