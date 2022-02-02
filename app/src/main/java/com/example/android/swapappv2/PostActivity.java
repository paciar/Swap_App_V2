package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.mBottomNavigationView;
import static com.example.android.swapappv2.HomeActivity.sCurrentUser;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    private Button mUploadPhoto, mPost;
    private EditText mItemName, mItemDescription;
    private String mItemID = UUID.randomUUID().toString();
    private ImageView mItemImage;

    static String sCurrentItem;
    static Uri sCurrentItemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Log.i("PostActivityUserCheck", "sCurrentUser = " + sCurrentUser);

        sUsers = new UserDBHelper(this);
        sItems = new ItemDBHelper(this);

        mUploadPhoto = findViewById(R.id.uploadPhoto);
        mPost = findViewById(R.id.post);
        mItemName = findViewById(R.id.name);
        mItemDescription = findViewById(R.id.description);
        mItemImage = findViewById(R.id.image);

        mUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = mItemName.getText().toString();
                String itemDescription = mItemDescription.getText().toString();
                String userID = sUsers.getUserID(sCurrentUser); // returns null because of sCurrentUser = null
                int soldStatus = 0; // soldStatus is set to "unsold" or "0" by default

                if (itemName.equals("") || itemDescription.equals(""))
                    Toast.makeText(PostActivity.this, "Fill in all of the fields.", Toast.LENGTH_SHORT).show();
                else {
                    String currentItemImageURI = sCurrentItemImage.toString();
                    boolean postResult = sItems.insertData(itemName, itemDescription, mItemID, userID, soldStatus, currentItemImageURI);

                    if (postResult == true) {
                        Toast.makeText(PostActivity.this, "Item successfully posted!", Toast.LENGTH_SHORT).show();
                        sCurrentItem = mItemID;
                        Fragment selectedFragment = ItemFragment.newInstance("","");
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, selectedFragment);
                        transaction.commit();
                    }
                    else
                        Toast.makeText(PostActivity.this, "Post failed! Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImage() {
        CropImage.activity().start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                sCurrentItemImage = resultUri;
                mItemImage.setBackground(null); // clears default image
                Picasso.with(this).load(resultUri).into(mItemImage); // loads selected image into mItemImage ImageView
                Log.i("GalleryUserCheck", "sCurrentUser = " + sCurrentUser); // after image is loaded, sCurrentUser is set to null
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String itemName = mItemName.getText().toString();
        String itemDescription = mItemDescription.getText().toString();
        outState.putString("sCurrentUser", sCurrentUser);
        // if any of the EditText fields aren't null, they will be saved
        if (!itemName.equals(""))
            outState.putString("itemName", itemName);
        if (!itemDescription.equals(""))
            outState.putString("itemDescription", itemDescription);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String itemName = mItemName.getText().toString();
        String itemDescription = mItemDescription.getText().toString();
        if (sCurrentUser == null)
            sCurrentUser = savedInstanceState.getString("sCurrentUser");
        if (itemName == null)
            mItemName.setText(savedInstanceState.getString("itemName"));
        if (itemDescription == null)
            mItemDescription.setText(savedInstanceState.getString("itemDescription"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBottomNavigationView.setSelectedItemId(R.id.navigation_explore);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBottomNavigationView.setSelectedItemId(R.id.navigation_explore);
    }
}