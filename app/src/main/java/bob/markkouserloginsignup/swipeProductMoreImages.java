package bob.markkouserloginsignup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class swipeProductMoreImages extends AppCompatActivity {

    private productImagesDatabaseManager prodImDB;
    String enteredUsername;
    String productName;
    String productNumber;
    String businessName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_product_more_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        prodImDB=new productImagesDatabaseManager(this,null,null,1);
        enteredUsername = paramsPassed.getString("enteredUsername");
        productName=paramsPassed.getString("productIdentifier");
        businessName=paramsPassed.getString("businessName");
        productNumber=paramsPassed.getString("productNumber");
        loadInImages();
    }

    public void goBack(View v){
        onBackPressed();
    }
    public void loadInImages(){
        byte[] im1=prodImDB.getImage(businessName,productNumber,1);
        byte[] im2=prodImDB.getImage(businessName,productNumber,2);
        byte[] im3=prodImDB.getImage(businessName,productNumber,3);
        byte[] im4=prodImDB.getImage(businessName,productNumber,4);
        byte[] im5=prodImDB.getImage(businessName,productNumber,5);
        byte[] im6=prodImDB.getImage(businessName,productNumber,6);

        ImageView pImV1=(ImageView)findViewById(R.id.pIm1);
        ImageView pImV2=(ImageView)findViewById(R.id.pIm2);
        ImageView pImV3=(ImageView)findViewById(R.id.pIm3);
        ImageView pImV4=(ImageView)findViewById(R.id.pIm4);
        ImageView pImV5=(ImageView)findViewById(R.id.pIm5);
        ImageView pImV6=(ImageView)findViewById(R.id.pIm6);


        if (im1.length != 0) {
            Bitmap im1AsBitmap = BitmapFactory.decodeByteArray(im1, 0, im1.length);
            Drawable im1AsDrawable = new BitmapDrawable(getResources(), im1AsBitmap);
            pImV1.setImageDrawable(im1AsDrawable);
        }


        if (im2.length != 0) {
            Bitmap im2AsBitmap = BitmapFactory.decodeByteArray(im2, 0, im2.length);
            Drawable im2AsDrawable = new BitmapDrawable(getResources(), im2AsBitmap);
            pImV2.setImageDrawable(im2AsDrawable);
        }
        if (im3.length != 0) {
            Bitmap im3AsBitmap = BitmapFactory.decodeByteArray(im3, 0, im3.length);
            Drawable im3AsDrawable = new BitmapDrawable(getResources(), im3AsBitmap);
            pImV3.setImageDrawable(im3AsDrawable);
        }
        if (im4.length != 0) {
            Bitmap im4AsBitmap = BitmapFactory.decodeByteArray(im4, 0, im4.length);
            Drawable im4AsDrawable = new BitmapDrawable(getResources(), im4AsBitmap);
            pImV4.setImageDrawable(im4AsDrawable);
        }
        if (im5.length != 0) {
            Bitmap im5AsBitmap = BitmapFactory.decodeByteArray(im5, 0, im5.length);
            Drawable im5AsDrawable = new BitmapDrawable(getResources(), im5AsBitmap);
            pImV5.setImageDrawable(im5AsDrawable);
        }
        if (im6.length != 0) {
            Bitmap im6AsBitmap = BitmapFactory.decodeByteArray(im6, 0, im6.length);
            Drawable im6AsDrawable = new BitmapDrawable(getResources(), im6AsBitmap);
            pImV6.setImageDrawable(im6AsDrawable);
        }
    }

}
