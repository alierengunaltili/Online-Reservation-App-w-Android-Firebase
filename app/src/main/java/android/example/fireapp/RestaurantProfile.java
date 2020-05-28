package android.example.fireapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Class for the main menu of restaurant owner
 * @date 29.04.2020
 * @author Group_g3C
 */

public class RestaurantProfile extends AppCompatActivity {

    //Properties

    Button logOut, editProfile, help, takeALookAtYourRestaurant, changeMenu, promotions,  myReservations;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView resNameTV;
    private ViewFlipper mViewFlipper;

    //Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_restaurant_profile2);

        //Initialization
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Restaurants");
        user = mAuth.getCurrentUser();

        resNameTV =(TextView)findViewById(R.id.txtNameRestaurantProfile);
        takeALookAtYourRestaurant = (Button)findViewById(R.id.takeALookAtYourRestaurant);
        editProfile =  (Button)findViewById(R.id.resEditProfile);
        help = (Button)findViewById(R.id.resHelp);
        logOut = (Button)findViewById(R.id.logOutResProfile);
        myReservations = (Button)findViewById(R.id.RestaurantReservations);
        promotions = (Button)findViewById(R.id.btnPromotionMenu);
        changeMenu = (Button)findViewById(R.id.btnChangeMenu);
        mViewFlipper = findViewById(R.id.view_flipper);
        int[] images = { R.drawable.food_photo, R.drawable.flipper_img};

        //Call methods
        takeALookAtYourRestaurantAction();
        logOffAction();
        helpAction();
        editTextAction();
        changeMenuAction();
        promotionsActivity();
        myReservationsActivity();

        //Adding the images for the flipper!
        for ( int x = 0; x < images.length; x++)
        {
            flipperImages( images[x]);
        }

        //Get customer name and display
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String userName = dataSnapshot.child(user.getUid()).child("name").getValue(String.class);
                resNameTV.setText(("Welcome, \n" + userName + "!").toUpperCase());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void myReservationsActivity() {
        myReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantProfile.this, RestaurantReservationsActivity.class));
            }
        });
    }

    private void promotionsActivity() {
        promotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(RestaurantProfile.this, PromotionsDisplayActivity.class));
            }
        });
    }

    private void changeMenuAction() {
        changeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantProfile.this, ChangeMenuActivity.class));
            }
        });
    }

    private void editTextAction() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantProfile.this, RestaurantEditProfileActivity.class));
            }
        });
    }

    private void helpAction() {
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantProfile.this, HelpRestaurant.class));
            }
        });
    }

    private void takeALookAtYourRestaurantAction() {
        takeALookAtYourRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(RestaurantProfile.this, RestaurantProfileDisplay.class));
            }
        });
    }

    /*
    Restaurant owners are asked if they want to log off or not.
     */
   private void logOffAction() {
       logOut.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               new AlertDialog.Builder(RestaurantProfile.this)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .setTitle("Are you sure?")
                       .setMessage("Do you want to log out?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               FirebaseAuth.getInstance().signOut();
                               Intent intent = new Intent(RestaurantProfile.this,
                                       LogInActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                               startActivity(intent);


                           }
                       })
                       .setNegativeButton("No", null)
                       .show();
           }
       });

   }

   /*
   This method prevents a bug. If a restaurant owner clicks to back button accidentally, they are
   asked whether they want to log off or not. They can choose yes or no.
    */
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log off?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(RestaurantProfile.this,
                                LogInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void flipperImages (int image){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource( image);

        mViewFlipper.addView( imageView);
        mViewFlipper.setFlipInterval( 3000);
        mViewFlipper.setAutoStart( true);

        //Time to slide!
        mViewFlipper.setInAnimation( this, android.R.anim.slide_in_left);
        mViewFlipper.setOutAnimation( this, android.R.anim.slide_out_right);

    }
}
