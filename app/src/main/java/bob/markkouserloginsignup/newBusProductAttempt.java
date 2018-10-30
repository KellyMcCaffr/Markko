package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;


public class newBusProductAttempt extends Activity {

    productImagesDatabaseManager imagesDatabaseManager;

    static final int imageIntentIdentifier=1;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;

    View currentImageClicked;

    String enteredUsername;
    String planName;
    String productNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus_product_attempt);

        imageView1=(ImageView)findViewById(R.id.imageView1);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        imageView3=(ImageView)findViewById(R.id.imageView3);
        imageView4=(ImageView)findViewById(R.id.imageView4);
        imageView5=(ImageView)findViewById(R.id.imageView5);
        imageView6=(ImageView)findViewById(R.id.imageView6);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        productNumber=paramsPassed.getString("productNumber");

        imagesDatabaseManager=new productImagesDatabaseManager(this,null,null,1);
    }
    //Uploads an image when an image box is clicked to the appropriate view box
    //from the user's phone.
    public void uploadImage(View view){
        currentImageClicked=view;
        //launches camera, takes image
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Passes result on
        startActivityForResult(i,imageIntentIdentifier);
    }

    public void backToProductOverview(View view){

        Intent i = new Intent(this,busProfileProductsOverview.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    public byte[] getByteArrayFromImageView(ImageView myImageView){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if(!(myImageView.getDrawable()==null) && !(myImageView.getDrawable() instanceof ColorDrawable)) {
            BitmapDrawable drawable = (BitmapDrawable) myImageView.getDrawable();
            Bitmap image1Bit = drawable.getBitmap();
            image1Bit.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            return bArray;
        }
        else{
            //Returns a blank byte array if the image is null, hence no image in that slot
            return new byte[]{};
        }
    }
    //Adds the images into the products database
    public void addImagesToDatabase(){
        //Later, as users can add more products assign stringed number equiv to this prod and pass on
        //to product details db.
        imagesDatabaseManager.addToImagesDatabase(enteredUsername,productNumber,getByteArrayFromImageView(imageView1),
                getByteArrayFromImageView(imageView2),getByteArrayFromImageView(imageView3),
                getByteArrayFromImageView(imageView4),getByteArrayFromImageView(imageView5),
                getByteArrayFromImageView(imageView6));
    }

    //Executes this when the next button is clicked by the user
    public void goToFinishProductUpload(View view){
        String productName = ((TextView) findViewById(R.id.productNameView)).getText().toString();
        if(!(productName.trim().isEmpty()) && !(productName.trim().equals(""))) {
            Intent i = new Intent(this, newBusProductAttemptSecondPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);

            i.putExtra("productName", productName);
            i.putExtra("productNumber", productNumber);
            addImagesToDatabase();
            startActivity(i);
        }
        //We require businesses to name their products.
        else{
            Intent i = new Intent(this, generalErrorPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("errorMessage", "You must enter a name for your product");
            startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Ensures that we took an image and there were no errors
        if(requestCode==imageIntentIdentifier && resultCode== RESULT_OK){
            //Get image data:
            Bundle extras=data.getExtras();
            //Acquires photo as bitmap stores it in the variable
            Bitmap photo=(Bitmap) extras.get("data");
            if(currentImageClicked.getId()==imageView1.getId()) {
                imageView1.setImageBitmap(photo);
            }
            else if(currentImageClicked.getId()==imageView2.getId()){
                imageView2.setImageBitmap(photo);
            }
            else if(currentImageClicked.getId()==imageView3.getId()){
                imageView3.setImageBitmap(photo);
            }
            else if(currentImageClicked.getId()==imageView4.getId()){
                imageView4.setImageBitmap(photo);
            }
            else if(currentImageClicked.getId()==imageView5.getId()){
                imageView5.setImageBitmap(photo);
            }
            else if(currentImageClicked.getId()==imageView6.getId()){
                imageView6.setImageBitmap(photo);
            }
        }
    }
}
