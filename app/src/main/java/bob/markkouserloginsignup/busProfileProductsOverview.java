package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.TextView;

public class busProfileProductsOverview extends Activity implements AdapterView.OnItemSelectedListener {

    String enteredUsername="Not Initialized";
    String planName;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    Spinner businessMenu;
    int numberOfProductsInDB;
    productImagesDatabaseManager imagesDatabaseManager;
    busProductsInformationDatabaseManager productsInfoManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_profile_products_overview);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");


        imagesDatabaseManager=new productImagesDatabaseManager(this,null,null,1);
        productsInfoManager=new busProductsInformationDatabaseManager(this,null,null,1);
        numberOfProductsInDB=getCurrentNumberOfProducts();
        displayProductNames();
        displayProductImages();
        manageSpinner();

    }
    private void displaySomeImageView(ImageView imView,String productIdentifier,int imageNumber){

        //We assume that image 1 is what should be shown
        byte[] mainImageAsBytes= imagesDatabaseManager.getImage(enteredUsername,productIdentifier,imageNumber);
        Bitmap bitmap = BitmapFactory.decodeByteArray(mainImageAsBytes , 0, mainImageAsBytes.length);

        //Allows us to see if the bitmap contains an image or not.
        //If we try to setimagebitmap with no image the view is turned black.
        //Without tru/catch the checker causes an exception, crashes app.
        try {
            if (bitmap.getWidth() != 0) {
                imView.setImageBitmap(bitmap);
                //Set the dimensions of the image to fit the view:
                imView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        catch(java.lang.RuntimeException e){

        }
    }
    public void displayProductImages(){
        imageView1=(ImageView)findViewById(R.id.prodImView1);
        imageView2=(ImageView)findViewById(R.id.prodImView2);
        imageView3=(ImageView)findViewById(R.id.prodImView3);
        imageView4=(ImageView)findViewById(R.id.prodImView4);
        imageView5=(ImageView)findViewById(R.id.prodImView5);
        imageView6=(ImageView)findViewById(R.id.prodImView6);
        displaySomeImageView(imageView1,"1",1);
        displaySomeImageView(imageView2,"2",1);
        displaySomeImageView(imageView3,"3",1);
        displaySomeImageView(imageView4,"4",1);
        displaySomeImageView(imageView5,"5",1);
        displaySomeImageView(imageView6,"6",1);

    }
    public void manageSpinner(){
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items_prod_profiles,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessMenu.setAdapter(adapter);
        businessMenu.setBackgroundResource(R.drawable.menuimage);
    }

    //Takes user to another activity dedicated to adding products
    //and either initiates an add or creates a message preventing it
    //(bad plan in red)
    public void addProdButtonClicked(View view){

        if(numberOfProductsInDB>0 && planName.equals("A")){
            Intent i = new Intent(this, generalErrorPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("errorMessage", "You may only make one product profile with a basic plan");
            startActivity(i);
        }
        else {

            Intent i = new Intent(this, newBusProductAttempt.class);
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            i.putExtra("productNumber",Integer.toString(numberOfProductsInDB+1));
            startActivity(i);
        }
    }
    public int getCurrentNumberOfProducts(){
        String prodOneNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"1");
        String prodTwoNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"2");
        String prodThreeNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"3");
        String prodFourNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"4");
        String prodFiveNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"5");
        String prodSixNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"6");
        if(prodOneNameText.equals("")){
            return 0;
        }
        else if(prodTwoNameText.equals("")){
            return 1;
        }
        else if(prodThreeNameText.equals("")){
            return 2;
        }
        else if(prodFourNameText.equals("")){
            return 3;
        }

        else if(prodFiveNameText.equals("")){
            return 4;
        }
        else if(prodSixNameText.equals("")){
            return 5;
        }
        return 6;

    }

    public void returnToMainBusPage(View view){
        Intent i = new Intent(this,mainBusinessPage.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    public void signOut(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    //Displays the names of each product on the product boxes
    public void displayProductNames(){
        TextView prodNameView1=(TextView)findViewById(R.id.prodOneTextView);
        TextView prodNameView2=(TextView)findViewById(R.id.prodTwoTextView);
        TextView prodNameView3=(TextView)findViewById(R.id.prodThreeTextView);
        TextView prodNameView4=(TextView)findViewById(R.id.prodFourTextView);
        TextView prodNameView5=(TextView)findViewById(R.id.prodFiveTextView);
        TextView prodNameView6=(TextView)findViewById(R.id.prodSixTextView);
        String prodOneNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"1");
        String prodTwoNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"2");
        String prodThreeNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"3");
        String prodFourNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"4");
        String prodFiveNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"5");
        String prodSixNameText=productsInfoManager.getProductNameByIdentifier(enteredUsername,"6");
        prodNameView1.setText(prodOneNameText);
        prodNameView2.setText(prodTwoNameText);
        prodNameView3.setText(prodThreeNameText);
        prodNameView4.setText(prodFourNameText);
        prodNameView5.setText(prodFiveNameText);
        prodNameView6.setText(prodSixNameText);
    }
    private void attemptShowProduct(String productNumber){

        if(!(productsInfoManager.getProductNameByIdentifier(enteredUsername,productNumber)).equals("")){
            Intent i = new Intent(this, productOverview.class);
            //We will need to do this every time.
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            i.putExtra("productNumber",productNumber);
            startActivity(i);
        }
        else{
            Log.d("Get this product:","blank string");
            Log.d("Prod Num:",productNumber);
            Log.d("BusinessName:",enteredUsername);
        }
    }
    public void prodBoxClicked(View view){

        String clickedProdViewNumber;
        if(view==(findViewById(R.id.prodImView1))){
            clickedProdViewNumber="1";
            attemptShowProduct(clickedProdViewNumber);
        }
        else if((view==(findViewById(R.id.prodImView2)))){
            clickedProdViewNumber="2";
            attemptShowProduct(clickedProdViewNumber);
        }
        else if((view==(findViewById(R.id.prodImView3)))){
            clickedProdViewNumber="3";
            attemptShowProduct(clickedProdViewNumber);
        }
        else if((view==(findViewById(R.id.prodImView4)))){
            clickedProdViewNumber="4";
            attemptShowProduct(clickedProdViewNumber);
        }
        else if((view==(findViewById(R.id.prodImView5)))){
            clickedProdViewNumber="5";
            attemptShowProduct(clickedProdViewNumber);
        }
        else if((view==(findViewById(R.id.prodImView6)))){
            clickedProdViewNumber="6";
            attemptShowProduct(clickedProdViewNumber);
        }
    }
    public void goToBusAccountManagementPage(View view){
        Intent i = new Intent(this, businessAccountInfoPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    public void goToPlanViewPage(View view){
        Intent i = new Intent(this, currentBusPlanView.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }


    public void goToBusPayInfoPage(View view){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    public void goToBusMarketResultsPage(View view){
        Intent i = new Intent(this, marketResultsMain.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    //Called when an item in the menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        businessMenu.setSelection(0);
        switch(pos){
            case 1:
                returnToMainBusPage(view);
                break;
            case 2:
                goToBusMarketResultsPage(view);
                break;
            case 3:
                goToBusPayInfoPage(view);
                break;
            case 4:
                goToPlanViewPage(view);
                break;
            case 5:
                goToBusAccountManagementPage(view);
                break;
            case 6:
                signOut(view);
                break;
        }
    }

    //I do not understand what this method does or when I can use it
    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

}
