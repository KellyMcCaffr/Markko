package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class checkBusinessDelete extends Activity {
    String enteredUsername;
    businessAccountsDatabaseManager busAccountsDB;
    businessFinancesDatabaseManager busFinanceDB;
    busProductsInformationDatabaseManager busPInfoDB;
    productImagesDatabaseManager busPImDB;

    userAccountDatabaseManager usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_business_delete);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        busAccountsDB=new businessAccountsDatabaseManager(this,null,null,1);
        busFinanceDB=new businessFinancesDatabaseManager(this,null,null,1);
        busPInfoDB=new busProductsInformationDatabaseManager(this,null,null,1);
        busPImDB=new productImagesDatabaseManager(this,null,null,1);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
    }

    //Action to take when the cancel delete button is clicked.
    public void cancelDelete(View view){
        Intent i = new Intent(this,businessAccountInfoPage.class);
        //Simply returns to the account info page
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void deleteAccount(View view){

        ArrayList<String> allPNames=busPInfoDB.getAllBusPNames(enteredUsername);
        Log.d("All P Names in cbd N:", ""+allPNames);
        busAccountsDB.deleteAccount(enteredUsername);
        busFinanceDB.deleteAccount(enteredUsername);
        busPInfoDB.deleteAllProductsFromBusiness(enteredUsername);
        busPImDB.deleteAllProductsFromBusiness(enteredUsername);


        //Delete all previous likes from users which rep bus products
        for(String pName:allPNames) {
            usersDB.delPFromLL(enteredUsername, pName);
        }
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }


}
