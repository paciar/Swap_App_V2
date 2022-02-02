package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentUser;
import static com.example.android.swapappv2.HomeActivity.sSelectedUser;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sUsers;
import static com.example.android.swapappv2.PostActivity.sCurrentItem;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

    private ImageView mItemImage;
    private TextView mItemName, mUsername, mItemDescription, mItemID;
    private Button mTrade;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance(String param1, String param2) {
        ItemFragment fragment = new ItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        Log.i("Current item", "Current item: " + sCurrentItem);

        sUsers = new UserDBHelper(getContext());
        sItems = new ItemDBHelper(getContext());

        mItemImage = view.findViewById(R.id.itemImage);
        mItemName = view.findViewById(R.id.itemName);
        mUsername = view.findViewById(R.id.username);
        mItemDescription = view.findViewById(R.id.itemDescription);
        mItemID = view.findViewById(R.id.itemID);
        mTrade = view.findViewById(R.id.trade);
        // If soldStatus = 1 --> put "sold" filter over image

        String ownerID = sItems.getOwnerID(sCurrentItem);
        String itemName = sItems.getItemName(sCurrentItem);
        String owner = sUsers.getName(ownerID);
        String itemDescription = sItems.getItemDescription(sCurrentItem);
        String itemID = sCurrentItem;
        String itemImageString = sItems.getImage(sCurrentItem);
        Uri itemImageURI = Uri.parse(itemImageString);

        // if the current user isn't the owner of the item being viewed, show the trade button
        if (!sCurrentUser.equals(owner))
            mTrade.setVisibility(View.VISIBLE);

        mTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSelectedUser = owner;
                Intent i = new Intent(getActivity(), SendTradeActivity.class);
                startActivity(i);
            }
        });

        mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSelectedUser = owner;
                Fragment selectedFragment = ProfileFragment.newInstance("","");
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, selectedFragment);
                transaction.commit();
            }
        });

        mItemName.setText(itemName);
        mUsername.setText(owner);
        mItemDescription.setText(itemDescription);
        mItemID.setText("itemID: " + itemID);
        Picasso.with(getContext()).load(itemImageURI).into(mItemImage);

        return view;
    }
}