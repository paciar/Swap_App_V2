package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentUser;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sUsers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

public class EditProfile extends AppCompatActivity {

    private ImageButton mEditProfilePicture;
    private EditText mEditUsername, mEditPassword, mEditEmail, mEditPhone;
    private Button mSave;
    private Uri mNewProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (savedInstanceState != null)
            sCurrentUser = savedInstanceState.getString("sCurrentUser");

        Log.i("EditActivityUserCheck", "sCurrentUser = " + sCurrentUser); // sCurrentUser is null

        sUsers = new UserDBHelper(this);
        sItems = new ItemDBHelper(this);

        mEditProfilePicture = findViewById(R.id.editProfilePicture);
        mEditUsername = findViewById(R.id.editUsername);
        mEditPassword = findViewById(R.id.editPassword);
        mEditEmail = findViewById(R.id.editEmail);
        mEditPhone = findViewById(R.id.editPhone);
        mSave = findViewById(R.id.save);

        String userID = sUsers.getUserID(sCurrentUser); // sCurrentUser is null here

        mEditProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUsername = mEditUsername.getText().toString();
                String newPassword = mEditPassword.getText().toString();
                String newEmail = mEditEmail.getText().toString();
                String newPhone = mEditPhone.getText().toString();
                String newProfilePicture = mNewProfilePicture.toString(); // returns null

                if (!newUsername.equals(""))
                    sUsers.setNewUsername(newUsername, userID);
                if (!newPassword.equals(""))
                    sUsers.setNewUsername(newPassword, userID);
                if (!newEmail.equals(""))
                    sUsers.setNewUsername(newEmail, userID);
                if (!newPhone.equals(""))
                    sUsers.setNewUsername(newPhone, userID);
                if (mNewProfilePicture != null)
                    sUsers.setNewProfilePicture(newProfilePicture, userID);

                // if any of the user's information has been updated, the user will be notified that their edits were saved
                if (sUsers.getName(userID).equals(newUsername) || sUsers.getPassword(userID).equals(newPassword)
                || sUsers.getUserEmail(userID).equals(newEmail) || sUsers.getUserPhone(userID).equals(newPhone)
                || sUsers.getUserProfilePicture(userID).equals(newProfilePicture)) {
                    Toast.makeText(EditProfile.this, "Settings saved!", Toast.LENGTH_SHORT).show();
                    sCurrentUser = newUsername;
                    finish();
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
                mNewProfilePicture = resultUri;
                mEditProfilePicture.setBackground(null); // clears default image
                Picasso.with(this).load(resultUri).into(mEditProfilePicture); // loads selected image
                Log.i("GalleryEditActivityUserCheck", "sCurrentUser = " + sCurrentUser); // never called for some reason
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String newUsername = mEditUsername.getText().toString();
        String newPassword = mEditPassword.getText().toString();
        String newEmail = mEditEmail.getText().toString();
        String newPhone = mEditPhone.getText().toString();
        outState.putString("sCurrentUser", sCurrentUser);
        // if any of the EditText fields aren't null, they will be saved
        if (!newUsername.equals(""))
            outState.putString("newUsername", newUsername);
        if (!newPassword.equals(""))
            outState.putString("newPassword", newPassword);
        if (!newEmail.equals(""))
            outState.putString("newEmail", newEmail);
        if (!newPhone.equals(""))
            outState.putString("newPhone", newPhone);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String newUsername = mEditUsername.getText().toString();
        String newPassword = mEditPassword.getText().toString();
        String newEmail = mEditEmail.getText().toString();
        String newPhone = mEditPhone.getText().toString();
        if (sCurrentUser == null)
            sCurrentUser = savedInstanceState.getString("sCurrentUser");
        if (newUsername == null)
            mEditUsername.setText(savedInstanceState.getString("newUsername"));
        if (newPassword == null)
            mEditPassword.setText(savedInstanceState.getString("newPassword"));
        if (newEmail == null)
            mEditEmail.setText(savedInstanceState.getString("newEmail"));
        if (newPhone == null)
            mEditPhone.setText(savedInstanceState.getString("newPhone"));
    }
}