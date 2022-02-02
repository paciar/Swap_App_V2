package com.example.android.swapappv2;

import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.PostActivity.sCurrentItem;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private List<String> mItemIDs;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    ProfileAdapter(Context context, List<String> IDs) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemIDs = IDs;
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        sItems = new ItemDBHelper(mInflater.getContext());
        Uri itemImage = Uri.parse(sItems.getImage(mItemIDs.get(position)));
        holder.itemImage.setImageURI(itemImage);
    }

    @Override
    public int getItemCount() {
        return mItemIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                sCurrentItem = getItem(getAdapterPosition());
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment selectedFragment = ItemFragment.newInstance("","");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).addToBackStack(null).commit();
            }
        }
    }

    public String getItem(int i) {
        return mItemIDs.get(i);
    }

    public void setClickListener(ProfileAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void onItemClick(View view, int position) {
        Log.i("ItemClicked", "You clicked item number " + getItem(position).toString() + ", which is at cell position " + position);
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
