package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;


public class productOverview extends Activity {
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;

    productImagesDatabaseManager imagesDatabaseManager;
    busProductsInformationDatabaseManager productsInfoManager;
    //We need this to update likes list if prod name is changed:
    userAccountDatabaseManager usersDB;

    String enteredUsername;
    String planName;
    String productNumber;
    View currentImageClicked;

    static final int imageIntentIdentifier=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_overview);
        imageView1=(ImageView)findViewById(R.id.prodImSecondView1);
        imageView2=(ImageView)findViewById(R.id.prodImSecondView2);
        imageView3=(ImageView)findViewById(R.id.prodImSecondView3);
        imageView4=(ImageView)findViewById(R.id.prodImSecondView4);
        imageView5=(ImageView)findViewById(R.id.prodImSecondView5);
        imageView6=(ImageView)findViewById(R.id.prodImSecondView6);
        //imageView1.setCropToPadding(true);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        productNumber=paramsPassed.getString("productNumber");
        planName = paramsPassed.getString("planName");

        imagesDatabaseManager=new productImagesDatabaseManager(this,null,null,1);
        productsInfoManager=new busProductsInformationDatabaseManager(this,null,null,1);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        loadProductImages();
        Log.d("Data","loaded");

        loadDataIntoInterface();
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
    public void checkmarkClicked(View view){
        //Remove previous data in the database
        CheckBox trialAllowBox=(CheckBox)findViewById(R.id.trialAllowBox2);
        CheckBox purchaseAllowBox=(CheckBox)findViewById(R.id.purchaseAllowBox2);

        //Get the new information from text views
        EditText newNameTextView=(EditText)findViewById(R.id.prodName2View);
        String newName=newNameTextView.getText().toString();
        String oldName;
        EditText newDescripTextView=(EditText)findViewById(R.id.prodDescripView);
        String newDescription=newDescripTextView.getText().toString();

        boolean allowBuying=false;
        boolean allowTrials=false;
        String price="N/A";

        if((purchaseAllowBox.isChecked())){
            allowBuying=true;
        }
        if(trialAllowBox.isChecked()){
            allowTrials=true;
        }

        if(allowBuying) {
            EditText priceView = (EditText) findViewById(R.id.prodPriceBox2);
            price = priceView.getText().toString();

            //Takes $ symbol out of price
            if(price.length()>0 && price.contains("$")){
                price=price.substring(1);
            }
        }

        //We need to see if price string is a valid integer
        boolean stopBcPrice=false;
        if(allowBuying) {
            try {
                Integer.parseInt(price);
            } catch (NumberFormatException e) {
                stopBcPrice=true;
            }
        }
        if(newName.equals("")){
            Intent i = new Intent(this, generalErrorPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("errorMessage", "You must enter a name for your product");
            startActivity(i);
        }
        else if(price.trim().length()<=0 && allowBuying){
            Log.d("Go to error page", "t");
            goToErrorPage("You must enter a price for your product!");
        }
        else if(stopBcPrice){
            goToErrorPage("Your products price must be greater than $0!");
        }
        else {
            oldName=productsInfoManager.getProductNameByIdentifier(enteredUsername,productNumber);
            imagesDatabaseManager.deleteProduct(enteredUsername, productNumber);
            productsInfoManager.deleteProduct(enteredUsername, productNumber);


            //Re-adds the images from previous views
            imagesDatabaseManager.addToImagesDatabase(enteredUsername, productNumber, getByteArrayFromImageView(imageView1), getByteArrayFromImageView(imageView2), getByteArrayFromImageView(imageView3),
                    getByteArrayFromImageView(imageView4), getByteArrayFromImageView(imageView5),
                    getByteArrayFromImageView(imageView6));

            productsInfoManager.addProductInfoSet(newName, enteredUsername, productNumber, newDescription, "", "", "", "", "","", price, allowTrials, allowBuying);


            if(!(newName.equals(oldName))){
                //Update PRODUCT name in user likes for all products
                usersDB.updateLikesProdNames(enteredUsername, oldName, newName);
            }
            //Take user back to the product overview page
            Intent i = new Intent(this, busProfileProductsOverview.class);
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            startActivity(i);
        }
    }
    public void deleteClicked(View view){
        Log.d("PRODUCTOVERVIEW", "Delete clicked");
        Intent i = new Intent(this,prodDeleteCheck.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        i.putExtra("productNumber",productNumber);
        startActivity(i);
    }
    public void backButtonSelected(View view){
        Intent i = new Intent(this,busProfileProductsOverview.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    //Allows users to change images inside of the database
    public void uploadImage(View view){
        currentImageClicked=view;
        //launches camera, takes image
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Passes result on
        startActivityForResult(i,imageIntentIdentifier);
    }
    //Allows us to display the image once uploaded
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

    public void loadDataIntoInterface(){
        CheckBox trialAllowBox2=(CheckBox)findViewById(R.id.trialAllowBox2);
        CheckBox purchaseAllowBox2=(CheckBox)findViewById(R.id.purchaseAllowBox2);
        TextView nameView=(TextView)findViewById(R.id.prodName2View);
        TextView descriptionView=(TextView)findViewById(R.id.prodDescripView);
        TextView prodPriceBox2=(TextView)findViewById(R.id.prodPriceBox2);

        String productNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,productNumber);
        String productDescriptionText=productsInfoManager.getProductDescriptionByIdentifier(enteredUsername,productNumber);
        String allowBuying=productsInfoManager.getAllowBuying(enteredUsername,productNumber);
        String allowTrials=productsInfoManager.getAllowTrials(enteredUsername,productNumber);
        String cost="$"+productsInfoManager.getPrice(enteredUsername,productNumber);

        if(allowTrials.equals("TRUE")){
            Log.d("Allow trials set:","Selected");
            trialAllowBox2.setChecked(true);
        }
        else{
            Log.d("Allow trials is:",allowTrials);
        }
        if(allowBuying.equals("TRUE")){
            Log.d("Allow buying set:","Selected");
            purchaseAllowBox2.setChecked(true);
        }
        prodPriceBox2.setText(cost);
        nameView.setText(productNameText);
        //Apparently this is needed for multi-line typing
        descriptionView.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        descriptionView.setText(productDescriptionText);
    }
    public void loadProductImages(){
        imageView1=(ImageView)findViewById(R.id.prodImSecondView1);
        imageView2=(ImageView)findViewById(R.id.prodImSecondView2);
        imageView3=(ImageView)findViewById(R.id.prodImSecondView3);
        imageView4=(ImageView)findViewById(R.id.prodImSecondView4);
        imageView5=(ImageView)findViewById(R.id.prodImSecondView5);
        imageView6=(ImageView)findViewById(R.id.prodImSecondView6);
        byte[] im1AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,1);
        byte[] im2AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,2);
        byte[] im3AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,3);
        byte[] im4AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,4);
        byte[] im5AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,5);
        byte[] im6AsBytes= imagesDatabaseManager.getImage(enteredUsername,productNumber,6);
        loadIndividualImage(imageView1, im1AsBytes);
        loadIndividualImage(imageView2, im2AsBytes);
        loadIndividualImage(imageView3, im3AsBytes);
        loadIndividualImage(imageView4, im4AsBytes);
        loadIndividualImage(imageView5, im5AsBytes);
        loadIndividualImage(imageView6, im6AsBytes);

    }
    public void loadIndividualImage(ImageView targetView,byte[] imageAsBytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes , 0, imageAsBytes.length);

        //Allows us to see if the bitmap contains an image or not.
        //If we try to setimagebitmap with no image the view is turned black.
        //Without try/catch the checker causes an exception, crashes app.
        try {
            if (bitmap.getWidth() != 0) {
                targetView.setImageBitmap(bitmap);
            }
        }
        catch(java.lang.RuntimeException e){

        }
    }

    public void goToErrorPage(String errorMessage){
        Intent i=new Intent(this,generalErrorPage.class);
        i.putExtra("errorMessage",errorMessage);
        startActivity(i);
    }

}
