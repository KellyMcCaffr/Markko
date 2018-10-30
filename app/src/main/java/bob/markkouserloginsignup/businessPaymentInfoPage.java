package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Switch;
import java.util.ArrayList;

public class businessPaymentInfoPage extends AppCompatActivity implements OnItemSelectedListener {
    String enteredUsername="Not Initialized";
    String planName;

    Spinner businessMenu;

    CheckBox cardButtonView1;
    CheckBox cardButtonView2;
    TextView cardNameView;
    TextView cardNumberView;
    TextView expirationMonthView;
    TextView expirationYearView;
    TextView securityCodeView;
    TextView billingAddressView;
    TextView bACityView;
    TextView bAStateView;
    TextView bAZipView;
    TextView shippingAddressView;
    TextView sACityView;
    TextView sAStateView;
    TextView sAZipView;

    String user;
    Boolean isUser;

    businessFinancesDatabaseManager financeDB;
    businessAccountsDatabaseManager businessAccountsDB;

    userFinancesDatabaseManager userFinDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_payment_info_page);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        planName = paramsPassed.getString("planName");
        user=paramsPassed.getString("isUser");
        //We use this page for both user and business payments
        if(user!=null){
            isUser=true;
        }
        else{
            isUser=false;
        }
        financeDB=new businessFinancesDatabaseManager(this,null,null,1);
        businessAccountsDB=new businessAccountsDatabaseManager(this,null,null,1);
        userFinDB=new userFinancesDatabaseManager(this,null,null,1);


        declareTextViews();
        addPreviousPaymentInfoToViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manageSpinner();
    }
    public void declareTextViews(){
        cardButtonView1=(CheckBox)findViewById(R.id.cardButton1P);
        cardButtonView2=(CheckBox)findViewById(R.id.cardButton2p);
        cardNameView=(TextView)findViewById(R.id.cardName);
        cardNumberView=(TextView)findViewById(R.id.cardNumberP);
        securityCodeView=(TextView)findViewById(R.id.cardSecurityCodeP);
        expirationMonthView=(TextView)findViewById(R.id.cardEDateMonthP);
        expirationYearView=(TextView)findViewById(R.id.cardEDateYearP);
        billingAddressView=(TextView)findViewById(R.id.cardBillingAddressP);
        bACityView=(TextView)findViewById(R.id.cardBACityP);
        bAStateView=(TextView)findViewById(R.id.cardBAStateP);
        bAZipView=(TextView)findViewById(R.id.cardBAZipP);
        shippingAddressView=(TextView)findViewById(R.id.cardShippingAddressP);
        sACityView=(TextView)findViewById(R.id.cardSACityP);
        sAStateView=(TextView)findViewById(R.id.cardSAStateP);
        sAZipView=(TextView)findViewById(R.id.cardSAZipP);
    }

    public void manageSpinner(){
        //Manages the spinner(menu) for this activity
        businessMenu= (Spinner) findViewById(R.id.businessMenu2);
        //Allows us to respond to user selections
        businessMenu.setOnItemSelectedListener(this);
        if(!isUser) {

            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.bus_menu_items_pay_info,
                    android.R.layout.simple_spinner_item);
            businessMenu.setBackgroundResource(R.drawable.menuimage);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            businessMenu.setAdapter(adapter);
        }
        else{
            ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.user_menu_items_payInfo,
                    android.R.layout.simple_spinner_item);
            businessMenu.setBackgroundResource(R.drawable.settingsbuttonwhite);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            businessMenu.setAdapter(adapter);
        }
    }

    public void onCheckmarkClicked(View view){
        boolean numberConditionOne=true;
        boolean numberConditionTwo=true;
        int cardSecurityCode=-1;
        int cardNumber=-1;

        String cardButtonResult="";
        Switch sameAddressesSwitch=(Switch)findViewById(R.id.sameAddressSwitchP);
        if(cardButtonView1.isChecked()&&!cardButtonView2.isChecked()){
            cardButtonResult="NP";
        }
        else if(cardButtonView2.isChecked()&&!cardButtonView1.isChecked()){
            cardButtonResult="P";
        }
        String cardName=(cardNameView).getText().toString();
        try{
            cardNumber=Integer.parseInt((cardNumberView).getText().toString());
        }
        //If the number cannot be parsed to an integer(error box?), only add if can be
        catch (NumberFormatException e){
            numberConditionOne=false;
        }
        String cardEDateMonth=(expirationMonthView).getText().toString();
        String cardEDateYear=(expirationYearView).getText().toString();
        String expirationDate=cardEDateMonth+"|"+cardEDateYear;
        try {
            cardSecurityCode=Integer.parseInt((securityCodeView).getText().toString());
        }
        catch(NumberFormatException e){
            numberConditionTwo=false;
        }

        String cardBillingAddress=(billingAddressView).getText().toString();
        String cardBACity=(bACityView).getText().toString();
        String cardBAState=(bAStateView).getText().toString();
        String cardBAZip=(bAZipView).getText().toString();
        String cardShippingAddress=(shippingAddressView).getText().toString();
        String cardSACity=(sACityView).getText().toString();
        String cardSAState=(sAStateView).getText().toString();
        String cardSAZip=(sAZipView).getText().toString();

        String fullBillingAddress=cardBillingAddress+"|"+cardBACity+"|"+cardBAState+"|"+cardBAZip;
        String fullShippingAddress=cardShippingAddress+"|"+cardSACity+"|"+cardSAState+"|"+cardSAZip;

        TextView errorViewBox=(TextView)findViewById(R.id.errorViewBoxP);
        if(cardButtonResult.equals("")){
            errorViewBox.setText("Please select a card type");
        }
        else if(cardName.equals("")){
            errorViewBox.setText("Card name is missing");
        }
        else if(cardNumber==-1){
            errorViewBox.setText("Card number is missing");
        }
        else if(expirationDate.equals("")){
            errorViewBox.setText("Expiration date is missing");
        }
        else if(cardSecurityCode==-1){
            errorViewBox.setText("Security code is missing");
        }
        else if(cardBillingAddress.equals("")||cardBACity.equals("")||cardBAState.equals("")||cardBAZip.equals("")){
            errorViewBox.setText("Full billing address is missing");
        }
        else if( ! sameAddressesSwitch.isChecked() &&(cardShippingAddress.equals("")||cardSACity.equals("")||cardSAState.equals("")||cardSAZip.equals(""))){
            errorViewBox.setText("Full shipping address is missing");
        }
        else if(numberConditionOne==false){
            errorViewBox.setText("Card number must be an integer!");
        }
        else if(numberConditionTwo==false){
            errorViewBox.setText("Security code must be an integer!");
        }
        //We should do more research on these numbers to determine good minimum lengths.

        else{
            if(sameAddressesSwitch.isChecked()){
                fullShippingAddress=fullBillingAddress;
            }
            //Removes any previous financial information of this account
            //so it can be replaced.
            if(!isUser) {
                financeDB.deleteAccount(enteredUsername);
                financeDB.addAccount(enteredUsername, cardButtonResult, cardName, cardNumber, expirationDate, cardSecurityCode
                        , fullBillingAddress, fullShippingAddress);
            }
            else{
                userFinDB.deleteAccount(enteredUsername);
                userFinDB.addAccount(enteredUsername, cardButtonResult, cardName, cardNumber, expirationDate, cardSecurityCode
                        , fullBillingAddress, fullShippingAddress);
            }
            returnToMainBusPage(view);
        }
    }

    //Adds the previous payment information into the textViews after retrieving it from
    //the businessFinanceDatabase.
    public void addPreviousPaymentInfoToViews(){
        //Sets variables for the views
        //Why did I need to do all of this again in checkmark clicked?!

        ArrayList viewsList=new ArrayList<TextView>(){};

        viewsList.add(cardNameView);
        viewsList.add(cardNumberView);
        viewsList.add(expirationMonthView);
        viewsList.add(expirationYearView);
        viewsList.add(securityCodeView);
        viewsList.add(billingAddressView);
        viewsList.add(bACityView);
        viewsList.add(bAStateView);
        viewsList.add(bAZipView);
        viewsList.add(shippingAddressView);
        viewsList.add(sACityView);
        viewsList.add(sAStateView);
        viewsList.add(sAZipView);

        String oldDatabaseInfo="";
        if(!isUser) {
            if (financeDB.getAccountInfoByUsername(enteredUsername) != null) {
                oldDatabaseInfo = financeDB.getAccountInfoByUsername(enteredUsername);
            }
        }
        else{
            if(userFinDB.getAccountInfoByUsername(enteredUsername) != null) {
                oldDatabaseInfo = userFinDB.getAccountInfoByUsername(enteredUsername);
            }
        }
        //Blits the text from the database to the TextViews.
        TextView currentView;
        int viewCount=-1;
        String word="";
        for(char c:oldDatabaseInfo.toCharArray()) {
            if (c == '|') {
                if (viewCount == -1) {
                    if (word.equals("NP")) {
                        cardButtonView1.setChecked(true);
                    } else if (word.equals("P")) {
                        cardButtonView2.setChecked(true);
                    }
                    viewCount += 1;
                    word="";
                }
                else {
                    currentView = (TextView) viewsList.get(viewCount);
                    currentView.setText(word);
                    word = "";
                    viewCount+=1;
                }

            } else {
                word += c;
            }
        }

    }

    public void returnToMainBusPage(View view){

        if(!isUser) {
            Intent i = new Intent(this, mainBusinessPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, mainUserPage.class);
            //We need to pass the enteredUsername back so that our main page remembers it
            //We will need to do this every time.
            i.putExtra("enteredUsername", enteredUsername);
            startActivity(i);
        }
    }



    public void signOut(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void goToBusMarketResultsPage(View view){
        Intent i = new Intent(this, marketResultsMain.class);
        //We must keep passing this around in here so it is not lost
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

    public void goToBusAccountManagementPage(View view){
        if(!isUser) {
            Intent i = new Intent(this, businessAccountInfoPage.class);
            //We must keep passing this around in here so it is not lost
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, manageUserAccount.class);
            //We must keep passing this around in here so it is not lost
            i.putExtra("enteredUsername", enteredUsername);
            startActivity(i);
        }
    }
    public void goToPlanViewPage(View view){
        if(!isUser) {
            Intent i = new Intent(this, currentBusPlanView.class);
            //We must keep passing this around in here so it is not lost
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, userStore.class);
            //We must keep passing this around in here so it is not lost
            i.putExtra("enteredUsername", enteredUsername);
            startActivity(i);
        }
    }

    public void goToBusProductsPage(View view){
        if(!isUser) {
            Intent i = new Intent(this, busProfileProductsOverview.class);
            //We must keep passing this around in here so it is not lost
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("planName", planName);
            startActivity(i);
        }
        else{
            Intent i = new Intent(this, userSwipePage.class);
            i.putExtra("enteredUsername", enteredUsername);
            startActivity(i);
        }
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
                goToBusProductsPage(view);
                break;
            case 3:
                goToBusMarketResultsPage(view);
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
