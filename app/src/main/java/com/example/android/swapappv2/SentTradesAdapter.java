package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentNotification;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sNotifications;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SentTradesAdapter extends RecyclerView.Adapter<SentTradesAdapter.ViewHolder> {

    private List<String> mSentTradesIDs;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    SentTradesAdapter(Context context,List<String> sentTradesIDs) {
        this.mInflater = LayoutInflater.from(context);
        this.mSentTradesIDs = sentTradesIDs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sItems = new ItemDBHelper(mInflater.getContext());
        sNotifications = new NotificationsDBHelper(mInflater.getContext());

        String givingItemID = sNotifications.getGivingItemID(mSentTradesIDs.get(position));
        Uri itemImage = Uri.parse(sItems.getImage(givingItemID));
        holder.itemImage.setImageURI(itemImage);

        String givingItemName = sItems.getItemName(givingItemID);
        holder.itemName.setText(givingItemName);
        /*
        String tradeStatus = "Awaiting response from owner.";
        if (sNotifications.getTradeStatus(mSentTradesIDs.get(position)) == 1)
            tradeStatus = "Trade accepted!";
        if (sNotifications.getTradeStatus(mSentTradesIDs.get(position)) == -1)
            tradeStatus = "Trade declined!";
        Log.i("TradeStatus", tradeStatus);
        holder.itemTradeStatus.setText(tradeStatus);
        */
    }

    @Override
    public int getItemCount() {
        return mSentTradesIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView itemImage;
        public TextView itemName;
        // public TextView itemTradeStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.receivingItemImage);
            itemName = itemView.findViewById(R.id.receivingItemName);
            // itemTradeStatus = itemView.findViewById(R.id.tradeStatusText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Toast.makeText(activity, "This feature hasn't been implemented yet.", Toast.LENGTH_SHORT).show();
            /*
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                sCurrentNotification = getItem(getAdapterPosition());
                Log.i("CurrentNotification", "The notification currently being viewed is " + sCurrentNotification);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment selectedFragment = NotificationItemFragment.newInstance("","");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).addToBackStack(null).commit();
            }
            */
        }
    }

    public String getItem(int i) { return mSentTradesIDs.get(i); }

    public void setClickListener(SentTradesAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void onItemClick(View view, int position) {
        Log.i("NotificationClicked", "You clicked notification number " + getItem(position).toString() + ", which is at cell position " + position);
    }

    public interface  ItemClickListener {
        void onItemClick(View view, int position);
    }

}
