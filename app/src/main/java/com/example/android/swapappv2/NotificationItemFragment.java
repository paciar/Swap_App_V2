package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentNotification;
import static com.example.android.swapappv2.MainActivity.sUsers;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sNotifications;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationItemFragment extends Fragment {

    private ImageView mReceivingImage, mGivingImage;
    private TextView mReceivingItem, mReceivingUser, mReceivingUserEmail, mReceivingUserPhone;
    private TextView mGivingItem, mGivingUser, mGivingUserEmail, mGivingUserPhone;
    private Button mAccept, mDecline;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationItemFragment newInstance(String param1, String param2) {
        NotificationItemFragment fragment = new NotificationItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_item, container, false);

        sUsers = new UserDBHelper(getContext());
        sItems = new ItemDBHelper(getContext());
        sNotifications = new NotificationsDBHelper(getContext());

        String receivingItemID = sNotifications.getReceivingItemID(sCurrentNotification);
        String receivingItemImageString = sItems.getImage(receivingItemID);
        Uri receivingItemImage = Uri.parse(receivingItemImageString);
        String receivingItem = sItems.getItemName(receivingItemID);
        String receivingUserID = sItems.getOwnerID(receivingItemID);
        String receivingUser = sUsers.getName(receivingUserID);
        String receivingUserEmail = sUsers.getUserEmail(receivingUserID);
        String receivingUserPhone = sUsers.getUserPhone(receivingUserID);

        mReceivingImage = view.findViewById(R.id.givingImage);
        mReceivingImage.setBackground(null);
        mReceivingImage.setImageURI(receivingItemImage);
        mReceivingItem = view.findViewById(R.id.givingItem);
        mReceivingItem.setText(receivingItem);
        mReceivingUser = view.findViewById(R.id.givingUser);
        mReceivingUser.setText(receivingUser);
        mReceivingUserEmail = view.findViewById(R.id.givingEmail);
        mReceivingUserEmail.setText(receivingUserEmail);
        mReceivingUserPhone = view.findViewById(R.id.givingPhone);
        mReceivingUserPhone.setText(receivingUserPhone);

        /******************************************************************************************/

        String givingItemID = sNotifications.getGivingItemID(sCurrentNotification);
        String givingItemImageString = sItems.getImage(givingItemID);
        Uri givingItemImage = Uri.parse(givingItemImageString);
        String givingItem = sItems.getItemName(givingItemID);
        String givingUserID = sItems.getOwnerID(givingItemID);
        String givingUser = sUsers.getName(givingUserID);
        String givingUserEmail = sUsers.getUserEmail(givingUserID);
        String givingUserPhone = sUsers.getUserPhone(givingUserID);

        mGivingImage = view.findViewById(R.id.receivingImage);
        mGivingImage.setBackground(null);
        mGivingImage.setImageURI(givingItemImage);
        mGivingItem = view.findViewById(R.id.receivingItem);
        mGivingItem.setText(givingItem);
        mGivingUser = view.findViewById(R.id.receivingUser);
        mGivingUser.setText(givingUser);
        mGivingUserEmail = view.findViewById(R.id.receivingEmail);
        mGivingUserEmail.setText(givingUserEmail);
        mGivingUserPhone = view.findViewById(R.id.receivingPhone);
        mGivingUserPhone.setText(givingUserPhone);

        mAccept = view.findViewById(R.id.acceptTrade);
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNotifications.setTradeStatus(sCurrentNotification, 1);
                sItems.setSoldStatus(receivingItemID, 1);
                sItems.setSoldStatus(givingItemID, 1);
                getActivity().getSupportFragmentManager().popBackStack();
                Toast.makeText(getContext(), "Trade accepted!", Toast.LENGTH_SHORT).show();
                // tradeStatus = 1 & soldStatus for both items = 1
                // finish fragment
                // show toast that says trade accepted
            }
        });

        mDecline = view.findViewById(R.id.declineTrade);
        mDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sNotifications.setTradeStatus(sCurrentNotification, -1);
                getActivity().getSupportFragmentManager().popBackStack();
                Toast.makeText(getContext(), "Trade declined!", Toast.LENGTH_SHORT).show();
                // tradeStatus= -1
                // finish fragment
                // show toast that says trade declined
            }
        });

        return view;
    }
}