package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class mainBusinessPage extends Activity implements OnItemSelectedListener {
    String businessName;
    String planName;
    String enteredUsername;
    businessAccountsDatabaseManager businessDatabaseSearcher;
    Spinner businessMenu;
    boolean justCreated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessDatabaseSearcher=new businessAccountsDatabaseManager(this,null,null,1);
        setContentView(R.layout.activity_main_business_page);

        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");

        //We need to override back press if just created account
        if(paramsPassed.getBoolean("justCreated")){
            justCreated=true;
        }
        manageSpinner();
        TextView busNameView=(TextView)findViewById(R.id.busNameViewBox);
        TextView planNameView=(TextView)findViewById(R.id.planNameViewBox);

        if(enteredUsername!=null) {
            businessName = businessDatabaseSearcher.getBusinessName(enteredUsername);
            planName=businessDatabaseSearcher.getBusinessPlan(enteredUsername);
        }
        else{
            businessName="null";
            planName="null";
        }

        busNameView.setText(businessName);

        if(planName.equals("A")){
            planNameView.setText("Basic");
        }
        else if(planName.equals("B")){
            planNameView.setText("Premium");
        }
        else if(planName.equals("C")){
            planNameView.setText("Professional");
        }
        else{
            planNameView.setText("Error:No plan name!");
        }
    }
    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items1,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessMenu.setAdapter(adapter);
        businessMenu.setBackgroundResource(R.drawable.menuimage);
    }
    public void goToBusPayInfoPage(View view){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }
    public void goToBusAccountManagementPage(View view){
        Intent i = new Intent(this, businessAccountInfoPage.class);
        //We must keep passing this around in here so it is not lost
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
    //Called when an item in the menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        businessMenu.setSelection(0);
        switch(pos){
            case 1:
                goToBusProductsPage(view);
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
    @Override
    public void onBackPressed(){
        if(!justCreated){
            super.onBackPressed();
        }
    }

}
