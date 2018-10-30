package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

public class tryPaidPlan extends AppCompatActivity {
    private String planName;
    private String enteredUsername;
    String industryString;
    businessFinancesDatabaseManager financeDB;
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
    businessAccountsDatabaseManager businessDatabaseChanger;
    String planSymbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_paid_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        businessDatabaseChanger=new businessAccountsDatabaseManager(this,null,null,1);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        planName=paramsPassed.getString("paidPlanType");
        industryString=paramsPassed.getString("industryString");
        if (planName.equals("Basic")){
            planSymbol="A";
        }
        else if(planName.equals("Premium")){
            planSymbol="B";
        }
        else{
            planSymbol="C";
        }
        enteredUsername = paramsPassed.getString("enteredUsername");
        financeDB=new businessFinancesDatabaseManager(this,null,null,1);
        declareViews();
    }
    public void declareViews(){
        cardButtonView1=(CheckBox)findViewById(R.id.cardButton1);
        cardButtonView2=(CheckBox)findViewById(R.id.cardButton2);
        cardNameView=(TextView)findViewById(R.id.cardName);
        cardNumberView=(TextView)findViewById(R.id.cardNumber);
        securityCodeView=(TextView)findViewById(R.id.cardSecurityCode);
        expirationMonthView=(TextView)findViewById(R.id.cardEDateMonth);
        expirationYearView=(TextView)findViewById(R.id.cardEDateYear);
        billingAddressView=(TextView)findViewById(R.id.cardBillingAddress);
        bACityView=(TextView)findViewById(R.id.cardBACity);
        bAStateView=(TextView)findViewById(R.id.cardBAState);
        bAZipView=(TextView)findViewById(R.id.cardBAZip);
        shippingAddressView=(TextView)findViewById(R.id.cardShippingAddress);
        sACityView=(TextView)findViewById(R.id.cardSACity);
        sAStateView=(TextView)findViewById(R.id.cardSAState);
        sAZipView=(TextView)findViewById(R.id.cardSAZip);
    }
    public void tryCheckmarkClicked(View view){
        boolean numberConditionOne=true;
        boolean numberConditionTwo=true;
        int cardSecurityCode=-1;
        int cardNumber=-1;

        String cardButtonResult="";
        Switch sameAddressesSwitch=(Switch)findViewById(R.id.sameAddressSwitch);
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

        TextView errorViewBox=(TextView)findViewById(R.id.errorViewBox);
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
            financeDB.deleteAccount(enteredUsername);
            financeDB.addAccount(enteredUsername,cardButtonResult,cardName,cardNumber,expirationDate,cardSecurityCode
                    ,fullBillingAddress,fullShippingAddress);
            businessAccountDatabaseProducts newBusinessAccountObject=new businessAccountDatabaseProducts();
            newBusinessAccountObject.set_plan(planSymbol);
            businessDatabaseChanger.addAccount(newBusinessAccountObject,industryString);
            goToMainBusPage(view);
        }
    }
    public void goToMainBusPage(View view){
        Intent i = new Intent(this,mainBusinessPage.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("planName",planName);
        startActivity(i);
    }

}
