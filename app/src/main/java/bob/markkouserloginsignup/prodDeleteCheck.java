package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class prodDeleteCheck extends Activity {
    String enteredUsername;
    String productNumber;
    String planName;
    String productName;
    productImagesDatabaseManager imagesDatabaseManager;
    busProductsInformationDatabaseManager productsInfoManager;
    userAccountDatabaseManager usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_delete_check);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        productNumber=paramsPassed.getString("productNumber");
        planName = paramsPassed.getString("planName");
        imagesDatabaseManager=new productImagesDatabaseManager(this,null,null,1);
        productsInfoManager=new busProductsInformationDatabaseManager(this,null,null,1);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        productName=productsInfoManager.getProductNameByIdentifier(enteredUsername,productNumber);
    }
    public void cancelClicked(View view){
        Intent i = new Intent(this,productOverview.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        i.putExtra("productNumber",productNumber);
        startActivity(i);
    }
    public void deleteClicked(View view){
        Log.d("PRODDELCHECK", "Delete clicked");
        usersDB.delPFromLL(enteredUsername,productName);
        productsInfoManager.deleteProduct(enteredUsername,productNumber);
        imagesDatabaseManager.deleteProduct(enteredUsername,productNumber);
        Intent i = new Intent(this,busProfileProductsOverview.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

}
