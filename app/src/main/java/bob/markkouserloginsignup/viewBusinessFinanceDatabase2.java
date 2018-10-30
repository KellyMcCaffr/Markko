package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessFinanceDatabase2 extends AppCompatActivity {
    businessFinancesDatabaseManager busFNManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_business_finance_database2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busFNManager=new businessFinancesDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void goToNextViewFinancePage(View view){
        Intent i = new Intent(this, viewBusinessFinanceDatabase3.class);
        startActivity(i);
    }
    public void showDatabase() {
        TextView busNumberViewer = (TextView) findViewById(R.id.cardNumberViewBox);
        TextView expirationDateViewer = (TextView) findViewById(R.id.expirationDateViewBox);
        TextView securityCodeViewer = (TextView) findViewById(R.id.securityCodeViewBox);

        String busFNDBString = busFNManager.databaseToString("cardnumber","expirationdate","securitycode");

        String cardNumbersString ="";
        String expirationDateString = "";
        String securityCodeString = "";

        String word = "";
        for (char currentChar : busFNDBString.toCharArray()) {

            if(currentChar!='^'&&currentChar!='*'&&currentChar!='~'){
                word+=currentChar;
            }
            else if(currentChar=='^'){
                cardNumbersString += word;
                cardNumbersString += "\n\n\n\n";
                word = "";
            }
            //We are not adding ids.
            else if (currentChar == '*') {
                expirationDateString += word;
                expirationDateString += "\n\n\n";
                word = "";
            } else if (currentChar == '~') {
                securityCodeString += word;
                securityCodeString += "\n\n\n\n";
                word = "";
            }
            busNumberViewer.setText(cardNumbersString);
            expirationDateViewer.setText(expirationDateString);
            securityCodeViewer.setText(securityCodeString);
        }
    }


}
