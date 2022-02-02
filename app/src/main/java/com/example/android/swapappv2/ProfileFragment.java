package com.example.android.swapappv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import static com.example.android.swapappv2.HomeActivity.sCurrentUser;
import static com.example.android.swapappv2.HomeActivity.sSelectedUser;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sUsers;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements ProfileAdapter.ItemClickListener {

    private ImageView mProfilePicture;
    private TextView mUsername, mEmail, mPhone;
    private Button mEditProfile;
    private ProfileAdapter mProfileAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sUsers = new UserDBHelper(getContext());
        sItems = new ItemDBHelper(getContext());

        String userID = sUsers.getUserID(sSelectedUser);

        mProfilePicture = view.findViewById(R.id.profilePicture);
        mProfilePicture.setImageURI(Uri.parse(sUsers.getUserProfilePicture(userID)));

        mUsername = view.findViewById(R.id.username);
        mUsername.setText(sSelectedUser);

        mEmail = view.findViewById(R.id.email);
        mEmail.setText(sUsers.getUserEmail(userID));

        mPhone = view.findViewById(R.id.phone);
        mPhone.setText(sUsers.getUserPhone(userID));
        /************************ NOT WORKING *****************************/
        mEditProfile = view.findViewById(R.id.editProfile);
        if (sUsers.getUserID(sCurrentUser).equals(sUsers.getUserID(sSelectedUser)))
            mEditProfile.setVisibility(View.VISIBLE);

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
        /************************ NOT WORKING *****************************/

        String selectedUserID = sUsers.getUserID(sSelectedUser);

        List<String> itemIDs = sItems.getUserItemIDs(selectedUserID);
        Log.i("ItemListLength", Integer.toString(itemIDs.size()));

        RecyclerView recyclerView = view.findViewById(R.id.profile_RecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mProfileAdapter = new ProfileAdapter(getContext(), itemIDs);
        mProfileAdapter.setClickListener(this);
        recyclerView.setAdapter(mProfileAdapter);

        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("Clicked item", "You clicked number " + mProfileAdapter.getItem(position) + ", which is at cell position " + position);
    }
}