package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class newBusProductAttemptSecondPage extends Activity {
    String enteredUsername;
    String planName;
    String productName;
    String productNumber;
    String price="N/A";
    boolean allowTrials=false;
    boolean allowBuying=false;

    TextView descripView;
    busProductsInformationDatabaseManager prodInfoDatabase;
    productImagesDatabaseManager prodImDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bus_product_attempt_second_page);
        //Sets to multiline:
        descripView=(TextView)findViewById(R.id.descrip1View);
        descripView.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        productName=paramsPassed.getString("productName");
        productNumber=paramsPassed.getString("productNumber");
        prodImDB=new productImagesDatabaseManager(this,null,null,1);
        prodInfoDatabase=new busProductsInformationDatabaseManager(this,null,null,1);
    }
    public void goBack(View view){
        Intent i = new Intent(this,newBusProductAttempt.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //We must also delete images if go back
        prodImDB.deleteProduct(enteredUsername,productNumber);
        super.onBackPressed();
    }
    public void cancelProduct(View view){
        //Makes sure to delete the images from the DB
        prodImDB.deleteProduct(enteredUsername,productNumber);
        Intent i = new Intent(this,busProfileProductsOverview.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);

    }

    public void goToErrorPage(String errorMessage){
        Intent i = new Intent(this,generalErrorPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        i.putExtra("errorMessage",errorMessage);
        startActivity(i);
    }
    public void createProduct(View view){
        CheckBox purchaseViewBox=(CheckBox)findViewById(R.id.purchaseAllowBox);
        CheckBox trialViewBox=(CheckBox)findViewById(R.id.trialAllowBox);
        if(purchaseViewBox.isChecked()){
            allowBuying=true;
        }
        if(trialViewBox.isChecked()){
            allowTrials=true;
        }
        if(allowBuying) {
            TextView priceView = (TextView) findViewById(R.id.prodPriceBox);
            price = priceView.getText().toString();
            if(price.equals("") || price.equals(" ")) {
                goToErrorPage("You must enter a price for your product!");
            }
            else if(Integer.parseInt(price)<=0) {
                goToErrorPage("Price must be greater then 0!");
             }

        }

        //Store the product name, product description,identifier, and blank strings
        //for consumer info in the prod info database.
        String businessIdentifier=enteredUsername;
        String productDescription=(descripView).getText().toString();
        prodInfoDatabase.addProductInfoSet(productName,businessIdentifier,productNumber,productDescription,"","","","","","",price,allowTrials,allowBuying);

        //Return to the main page
        Intent i = new Intent(this,mainBusinessPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

}
