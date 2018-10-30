package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class currentBusPlanView extends Activity implements AdapterView.OnItemSelectedListener {
    Spinner businessMenu;
    String enteredUsername;
    String planName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_bus_plan_view);


        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        manageSpinner();
        displayPlanText();
    }

    public void goToChangePlanPage(View view){
        Intent i = new Intent(this, busChangePlanPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    //Adds whichever text makes up the plan to the view box
    public void displayPlanText(){
        TextView planTitleView=(TextView)findViewById(R.id.planTitleViewBox);
        TextView planBodyView=(TextView)findViewById(R.id.planBodyViewBox);

        String planOneTitleText="Basic                               Free";
        String planOneBodyText="\n-1 profile where you can print up to 6 pictures\n-Receive number of likes and dislikes";

        String planTwoTitleText="Premium                $69.99/month";
        String planTwoBodyText="\n-Up to 15 product profiles where you can post\nup to 6 photos each\n-Receive comprehensive market info\n(ie.likes/dislikes,demographics etc.)\n-Option to allow users to buy/pre-order/donate";

        String planThreeTitleText="Professional     $199.99/month";
        String planThreeBodyText="\n-Up to 50 product profiles where you can post up to\n6 photos each\n-Receive comprehensive market info(ie.likes/dislikes, demographics etc)\nOption to allow users to buy/pre-order/donate\n-Preferential status(products appear more often)\n-Receive more in-depth market answers\n(i.e. explanations of why consumers dislike a product)";


        if(planName.equals("A")){
            planTitleView.setText(planOneTitleText);
            planBodyView.setText(planOneBodyText);
        }
        else if(planName.equals("B")){
            planTitleView.setText(planTwoTitleText);
            planBodyView.setText(planTwoBodyText);
        }
        else if(planName.equals("C")){
            planTitleView.setText(planThreeTitleText);
            planBodyView.setText(planThreeBodyText);
        }

    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items_plan_info,
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

    public void returnToMainBusPage(View view){
        Intent i = new Intent(this,mainBusinessPage.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
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
