package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class busChangePlanPage extends Activity implements AdapterView.OnItemSelectedListener{
    businessAccountsDatabaseManager newBusAccountsDB;
    businessFinancesDatabaseManager newFinanceInfoDB;
    Spinner businessMenu;
    String enteredUsername;
    String planName;
    TextView errorViewer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bus_change_plan_page);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        manageSpinner();
        displayTextForOtherPlans();
        newBusAccountsDB=new businessAccountsDatabaseManager(this,null,null,1);
        newFinanceInfoDB=new businessFinancesDatabaseManager(this,null,null,1);
    }

    public void backToPlan(View view){
        Intent i = new Intent(this, currentBusPlanView.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    //When either of the plan views is clicked, change user plan or make user enter payment info
    //then if correct and continue clicked continue(add plan as bundle to finance page,
    //parameterize its functions appropriately
    public void newPlanViewOneClicked(View view){
        errorViewer=(TextView)findViewById(R.id.errorViewBoxP);
        if(planName.equals("A")){
            if(newFinanceInfoDB.getAccountInfoByUsername(enteredUsername).equals("Error: no business plan!")){
                errorViewer.setText("Please enter valid payment information before upgrading plan");
            }
            else {
                newBusAccountsDB.updatePlan(enteredUsername, "B");
                planName = "B";
                returnToMainBusPage(view);
            }

        }
        else if(planName.equals("B")){
            newBusAccountsDB.updatePlan(enteredUsername, "A");
            planName = "A";
            returnToMainBusPage(view);
        }
        else if(planName.equals("C")){
            newBusAccountsDB.updatePlan(enteredUsername, "A");
            planName = "A";
            returnToMainBusPage(view);
        }
    }

    public void newPlanViewTwoClicked(View view){
        errorViewer=(TextView)findViewById(R.id.errorViewBoxP);
        if(planName.equals("A")){
            if(newFinanceInfoDB.getAccountInfoByUsername(enteredUsername).equals("Error: no business plan!")){
                errorViewer.setText("Please enter valid payment information before upgrading plan");
            }
            else {
                newBusAccountsDB.updatePlan(enteredUsername, "C");
                planName = "C";
                returnToMainBusPage(view);
            }
        }
        else if(planName.equals("B")){
            if(newFinanceInfoDB.getAccountInfoByUsername(enteredUsername).equals("Error: no business plan!")){
                errorViewer.setText("Please enter valid payment information before upgrading plan");
            }
            else{
                newBusAccountsDB.updatePlan(enteredUsername, "C");
                planName = "C";
                returnToMainBusPage(view);
            }
        }
        else if(planName.equals("C")){
            newBusAccountsDB.updatePlan(enteredUsername, "B");
            planName = "B";
            returnToMainBusPage(view);
        }
    }


    //Shows text in the boxes which do not represent the current plan
    public void displayTextForOtherPlans(){
        TextView otherPlan1Body=(TextView)findViewById(R.id.planOneBodyViewBox);
        TextView otherPlan1Title=(TextView)findViewById(R.id.planOneTitleViewBox);

        TextView otherPlan2Body=(TextView)findViewById(R.id.planTwoBodyViewBox);
        TextView otherPlan2Title=(TextView)findViewById(R.id.planTwoTitleViewBox);

        String planOneTitleText="Basic                               Free";
        String planOneBodyText="\n-1 profile where you can print up to 6 pictures\n-Receive number of likes and dislikes";

        String planTwoTitleText="Premium                $69.99/month";
        String planTwoBodyText="\n-Up to 15 product profiles where you can post\nup to 6 photos each\n-Receive comprehensive market info\n(ie.likes/dislikes,demographics etc.)\n-Option to allow users to buy/pre-order/donate";

        String planThreeTitleText="Professional     $199.99/month";
        String planThreeBodyText="\n-Up to 50 product profiles where you can post up to\n6 photos each\n-Receive comprehensive market info(ie.likes/dislikes, demographics etc)\nOption to allow users to buy/pre-order/donate\n-Preferential status(products appear more often)\n-Receive more in-depth market answers\n(i.e. explanations of why consumers dislike a product)";

        if(planName.equals("A")){
            otherPlan1Title.setText(planTwoTitleText);
            otherPlan1Body.setText(planTwoBodyText);
            otherPlan2Title.setText(planThreeTitleText);
            otherPlan2Body.setText(planThreeBodyText);
        }
        else if(planName.equals("B")){
            otherPlan1Title.setText(planOneTitleText);
            otherPlan1Body.setText(planOneBodyText);
            otherPlan2Title.setText(planThreeTitleText);
            otherPlan2Body.setText(planThreeBodyText);
        }
        else if(planName.equals("C")){
            otherPlan1Title.setText(planOneTitleText);
            otherPlan1Body.setText(planOneBodyText);
            otherPlan2Title.setText(planTwoTitleText);
            otherPlan2Body.setText(planTwoBodyText);
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
        startActivity(i);
    }
    public void goToBusAccountManagementPage(View view){
        Intent i = new Intent(this, businessAccountInfoPage.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
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
        startActivity(i);
    }
    public void goToChangePlanPage(View view){
        Intent i = new Intent(this, busChangePlanPage.class);
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
                goToChangePlanPage(view);
                break;

            case 6:
                goToBusAccountManagementPage(view);
                break;
            case 7:
                signOut(view);
                break;
        }
    }

    //I do not understand what this method does or when I can use it
    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

}
