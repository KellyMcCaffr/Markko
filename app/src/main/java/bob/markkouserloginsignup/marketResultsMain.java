package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class marketResultsMain extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner businessMenu;
    String enteredUsername;
    String planName;
    List<Integer> pViewIdsList;
    private int totNumProducts;
    List<String> possPIdentifiers;
    busProductsInformationDatabaseManager pDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_results_main);
        pViewIdsList= Arrays.asList(R.id.pr1,R.id.pr2,R.id.pr3,R.id.pr4,R.id.pr5,R.id.pr6);
        Bundle paramsPassed;
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        pDB=new busProductsInformationDatabaseManager(this,null,null,1);
        loadPNames();
        manageSpinner();
    }
    //Loads existing product names into their appropriate views:
    private void loadPNames(){
        possPIdentifiers=Arrays.asList("1","2","3","4","5","6");
        String currPName;
        Button cView;
        int c=0;
        for(String id:possPIdentifiers){
            currPName=pDB.getProductNameByIdentifier(enteredUsername,id);
            if(currPName.equals("")){
                break;
            }
            cView=(Button)findViewById(pViewIdsList.get(c));
            cView.setText(currPName);
            c++;
        }
    }

    public void goToBusDataPieChart(View v){
        Intent i = new Intent(this, busDataPieChart.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    public void goToBusDataGraph(View v){
        Intent i = new Intent(this, busDataBarChart.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu21);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items_marketResults,
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
    public void goToSpecificProdInfoPage(View view){
        Intent i = new Intent(this, prodDatPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);

        String productNumber="-1";
        if(view.getId()==R.id.pr1){
            productNumber="1";
        }
        if(view.getId()==R.id.pr2){
            productNumber="2";
        }
        if(view.getId()==R.id.pr3){
            productNumber="3";
        }
        if(view.getId()==R.id.pr4){
            productNumber="4";
        }
        if(view.getId()==R.id.pr5){
            productNumber="5";
        }
        if(view.getId()==R.id.pr6){
            productNumber="6";
        }

        i.putExtra("productNumber",productNumber);


        startActivity(i);
    }

    public void goToMainBusPage(View view){
        Intent i = new Intent(this, mainBusinessPage.class);
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
                goToMainBusPage(view);
                break;

            case 2:
                goToBusProductsPage(view);
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
