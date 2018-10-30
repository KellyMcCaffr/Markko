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
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;



public class businessAccountInfoPage extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner businessMenu;
    String enteredUsername;
    String planName;

    String planType;

    businessAccountsDatabaseManager businessAccountsDB;
    busProductsInformationDatabaseManager businessProductsDB;
    productImagesDatabaseManager productsImManager;
    userAccountDatabaseManager usersDB;

    TextView accountUsernameView;
    TextView accountPasswordView;
    TextView accountEmailView;
    TextView accountBusNameView;
    TextView accountUrlView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_business_account_info_page);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        businessAccountsDB=new businessAccountsDatabaseManager(this,null,null,1);
        businessProductsDB=new busProductsInformationDatabaseManager(this,null,null,1);
        productsImManager=new productImagesDatabaseManager(this,null,null,1);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        manageSpinner();
        declareTextViews();
        addPreviousPaymentInfoToViews(enteredUsername);
    }


    public void deleteBusButtonClicked(View view){
        Intent i = new Intent(this, checkBusinessDelete.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }

    public void onCheckmarkClicked(View view){

        String accountName=(accountUsernameView).getText().toString();

        String accountPassword=(accountPasswordView).getText().toString();
        String accountEmail=(accountEmailView).getText().toString();
        String companyName=(accountBusNameView).getText().toString();
        String url=(accountUrlView).getText().toString();

        TextView errorViewBox=(TextView)findViewById(R.id.errorViewBoxP);
        if(accountName.equals("")){
            errorViewBox.setText("Name cannot be blank!");
        }
        else if(accountPassword.equals("")){
            errorViewBox.setText("Password cannot be blank");
        }
        else if(accountEmail.equals("")){
            errorViewBox.setText("Email cannot be blank");
        }
        else if(companyName.equals("")){
            errorViewBox.setText("Company name cannot be blank");
        }
        else if(accountEmail.indexOf('@')==-1){
            errorViewBox.setText("New email is invalid");
        }
        //Check to see if the main name is already in the DB:
        else if(businessAccountsDB.nameExists(accountName) && !(accountName.equals(enteredUsername))){
            goToErrorPage("The username already exists!");
        }
        else{
            //Remove any previous financial information of this accoun so it can be replaced.
            businessAccountsDB.deleteAccount(enteredUsername);

            //Add the new, modified account back to Business DB
            businessAccountsDB.addAccount(new businessAccountDatabaseProducts(accountName,accountEmail,accountPassword,companyName,url,planType),"");
            //Update accountname in the products database for that name
            businessProductsDB.updateProductBusName(enteredUsername, accountName);
            Log.d("Final acc name in bInf", accountName);
            //Also update username in the images DB
            productsImManager.updateProductBusName(enteredUsername,accountName);

            usersDB.updateLikesBusNames(enteredUsername,accountName);
            //Update the username sent back to the business page as an intent
            enteredUsername=accountName;

            returnToMainBusPage(view);
        }
    }
    public void declareTextViews(){
        accountUsernameView=(TextView)findViewById(R.id.accountUsernameView);
        accountPasswordView=(TextView)findViewById(R.id.accountPasswordView);
        accountEmailView=(TextView)findViewById(R.id.accountEmailView);
        accountBusNameView=(TextView)findViewById(R.id.accountBusNameView);
        accountUrlView=(TextView)findViewById(R.id.accountUrlView);
    }
    //Adds the previous payment information into the textViews after retrieving it from
    //the businessFinanceDatabase.
    public void addPreviousPaymentInfoToViews(String enteredUsername){
        //Sets variables for the views
        //Why did I need to do all of this again in checkmark clicked?!

        ArrayList<TextView> viewsList=new ArrayList<TextView>(){};

        viewsList.add(accountUsernameView);
        viewsList.add(accountPasswordView);
        viewsList.add(accountEmailView);
        viewsList.add(accountBusNameView);
        viewsList.add(accountUrlView);

        String oldDatabaseInfo="";
        if(businessAccountsDB.getAccountInfoByUsername(enteredUsername)!=null){
            oldDatabaseInfo= businessAccountsDB.getAccountInfoByUsername(enteredUsername);
        }
        //Blits the text from the database to the TextViews.
        TextView currentView;
        int viewCount=0;
        String word="";
        for(char c:oldDatabaseInfo.toCharArray()) {
            if (c == '|') {

                currentView = (TextView) viewsList.get(viewCount);
                currentView.setText(word);
                word = "";
                viewCount+=1;

            }
            //We will need two forbidden characters here for this,
            //replaces needing to add it to bundle.
            else if(c=='~'){
                planType=word;
                word="";
                viewCount+=1;
            }
            else {
                word += c;
            }
        }

    }

    private void goToErrorPage(String message){
        Intent i = new Intent(this, generalErrorPage.class);
        i.putExtra("errorMessage",message);
        startActivity(i);
    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items_manage_account,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessMenu.setAdapter(adapter);
        businessMenu.setBackgroundResource(R.drawable.menuimage);
    }

    public void goToIndustryOverview(View view){
        Intent i = new Intent(this, busIndustryOverview.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }

    public void goToBusPayInfoPage(View view){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
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
    public void goToPlanViewPage(View view){
        Intent i = new Intent(this, currentBusPlanView.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    public void goToBusProductsPage(View view){
        Intent i = new Intent(this, busProfileProductsOverview.class);
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
    //Called when an item in the spinner menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        businessMenu.setSelection(0);
        switch(pos){
            case 1:
                returnToMainBusPage(view);
                break;
            case 2:
                goToBusProductsPage(view);
                break;
            case 3:
                goToBusMarketResultsPage(view);
                break;
            case 4:
                goToBusPayInfoPage(view);
                break;
            case 5:
                goToPlanViewPage(view);
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
