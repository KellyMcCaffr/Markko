package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class swipeProductInfo extends Activity {

    private String enteredUsername;
    private String productName;
    private String productIdentifier;
    String productNumber;
    private busProductsInformationDatabaseManager prodInfoDB;
    //Not sure if need business name but set as an attribute
    private String businessName;

    //Lets the user know rather or not a product can be bought or tried
    private boolean whitePurchaseBoxText=false;
    private boolean whiteTrialBoxText=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_product_info);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        productName=paramsPassed.getString("prodName");
        businessName=paramsPassed.getString("compName");
        productNumber=paramsPassed.getString("prodNumber");
        prodInfoDB=new busProductsInformationDatabaseManager(this,null,null,1);
        productIdentifier=prodInfoDB.getProductIdentifierByName(businessName,productName);
        Log.d("At load desc:","fbvj");
        loadInDescription();


        //Lets the user know rather they can purchase or try the product
        if(whitePurchaseBoxText){
            setPurchaseTextToWhite();
        }
        if(whiteTrialBoxText){
            setTrialTextToWhite();
        }
    }

    private void setPurchaseTextToWhite(){
        TextView purchaseView=(TextView)findViewById(R.id.prodPurchaseBox1);
        purchaseView.setTextColor(Color.WHITE);

    }
    private void setTrialTextToWhite(){
        TextView trialView=(TextView)findViewById(R.id.trialBox1);
        trialView.setTextColor(Color.WHITE);

    }

    public void loadInDescription(){
        TextView descripBox=(TextView)findViewById(R.id.mPSDescBox);
        //Apparently this is needed for multi-line typing
        descripBox.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        String productDescription=prodInfoDB.getProductDescriptionByIdentifier(businessName,productIdentifier);
        Log.d("Prod Desc spi:",productDescription);
        descripBox.setText(productDescription);
    }

    public void goToMoreImagesPage(View view){
        Intent i = new Intent(this, swipeProductMoreImages.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("productIdentifier",productName);
        i.putExtra("productNumber",productNumber);
        i.putExtra("businessName",businessName);
        startActivity(i);
    }
    //Needs to go back to previous product open in adapter
    public void goBack(View v){
        onBackPressed();
    }

}
