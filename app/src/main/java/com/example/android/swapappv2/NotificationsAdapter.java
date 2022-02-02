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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<String> mNotificationIDs;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    NotificationsAdapter(Context context,List<String> notificationIDs) {
        this.mInflater = LayoutInflater.from(context);
        this.mNotificationIDs = notificationIDs;
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

        String receivingItemID = sNotifications.getReceivingItemID(mNotificationIDs.get(position));
        Uri itemImage = Uri.parse(sItems.getImage(receivingItemID));
        holder.itemImage.setImageURI(itemImage);

        String receivingItemName = sItems.getItemName(receivingItemID);
        holder.itemName.setText(receivingItemName);

    }

    @Override
    public int getItemCount() {
        return mNotificationIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView itemImage;
        public TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.receivingItemImage);
            itemName = itemView.findViewById(R.id.receivingItemName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
                sCurrentNotification = getItem(getAdapterPosition());
                Log.i("CurrentNotification", "The notification currently being viewed is " + sCurrentNotification);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment selectedFragment = NotificationItemFragment.newInstance("","");
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, selectedFragment).addToBackStack(null).commit();
            }
        }
    }

    public String getItem(int i) { return mNotificationIDs.get(i); }

    public void setClickListener(NotificationsAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public void onItemClick(View view, int position) {
        Log.i("NotificationClicked", "You clicked notification number " + getItem(position).toString() + ", which is at cell position " + position);
    }

    public interface  ItemClickListener {
        void onItemClick(View view, int position);
    }

}
