package com.example.android.swapappv2;

import static com.example.android.swapappv2.HomeActivity.sCurrentUser;
import static com.example.android.swapappv2.HomeActivity.sSelectedUser;
import static com.example.android.swapappv2.MainActivity.sItems;
import static com.example.android.swapappv2.MainActivity.sUsers;
import static com.example.android.swapappv2.MainActivity.sNotifications;
import static com.example.android.swapappv2.PostActivity.sCurrentItem;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class SendTradeActivity extends AppCompatActivity {

    private ImageView mReceivingImage, mGivingImage;
    private TextView mReceivingItem, mReceivingUser, mReceivingEmail, mReceivingPhone;
    private Spinner mGivingItem;
    private TextView mGivingUser, mGivingEmail, mGivingPhone;
    private Button mSendTrade;
    private String mGivingItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_trade);

        sUsers = new UserDBHelper(this);
        sItems = new ItemDBHelper(this);
        String ownerID = sItems.getOwnerID(sCurrentItem);
        String currentUserID = sUsers.getUserID(sCurrentUser);
        /************** ************** RECEIVING START ************** **************/
        mReceivingImage = findViewById(R.id.receivingImage);
        String itemImageString = sItems.getImage(sCurrentItem);
        Uri itemImageURI = Uri.parse(itemImageString);
        mReceivingImage.setBackground(null);
        mReceivingImage.setImageURI(itemImageURI);

        mReceivingItem = findViewById(R.id.receivingItem);
        String itemName = sItems.getItemName(sCurrentItem);
        mReceivingItem.setText(itemName);

        mReceivingUser = findViewById(R.id.receivingUser);
        mReceivingUser.setText(sSelectedUser);

        mReceivingEmail = findViewById(R.id.receivingEmail);
        String receivingEmail = sUsers.getUserEmail(ownerID);
        mReceivingEmail.setText(receivingEmail);

        mReceivingPhone = findViewById(R.id.receivingPhone);
        String receivingPhone = sUsers.getUserPhone(ownerID);
        mReceivingPhone.setText(receivingPhone);
        /************** ************** RECEIVING END ************** **************/

        /************** ************** GIVING START ************** **************/

        // select item from spinner and then it loads item image --> in the spinner adapter, get all the itemIDs for the user's posts
        mGivingItem = findViewById(R.id.givingItem); // spinner

        List<String> userItemIDs = sItems.getUserItemIDs(sUsers.getUserID(sCurrentUser));
        List<String> userItems = sItems.getUserItems(sUsers.getUserID(sCurrentUser));
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, userItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGivingItem.setAdapter(spinnerAdapter);

        mGivingImage = findViewById(R.id.givingImage);
        // after item is selected, load the itemImage
        mGivingItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPosition = mGivingItem.getSelectedItemPosition();
                mGivingItemID = userItemIDs.get(spinnerPosition);
                mGivingImage.setBackground(null);
                mGivingImage.setImageURI(Uri.parse(sItems.getImage(mGivingItemID)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mGivingUser = findViewById(R.id.givingUser);
        mGivingUser.setText(sCurrentUser);

        mGivingEmail = findViewById(R.id.givingEmail);
        String givingEmail = sUsers.getUserEmail(currentUserID);
        mGivingEmail.setText(givingEmail);

        mGivingPhone = findViewById(R.id.givingPhone);
        String givingPhone = sUsers.getUserPhone(currentUserID);
        mGivingPhone.setText(givingPhone);
        /************** ************** GIVING END ************** **************/

        sNotifications = new NotificationsDBHelper(this);
        mSendTrade = findViewById(R.id.sendTrade);

        mSendTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String notificationID = UUID.randomUUID().toString();
                String receivingItemID = sCurrentItem;
                String receivingUserID = sUsers.getUserID(sSelectedUser);
                String givingItemID = mGivingItemID;
                String givingUserID = sUsers.getUserID(sCurrentUser);
                int tradeStatus = 0;
                boolean sendTradeResult = sNotifications.insertData(notificationID, receivingItemID, receivingUserID, givingItemID, givingUserID, tradeStatus);
                if (sendTradeResult == true) {
                    Toast.makeText(SendTradeActivity.this, "Trade successfully sent!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(SendTradeActivity.this, "Trade could not be sent.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}