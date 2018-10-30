package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessFinanceDatabase extends AppCompatActivity {
    businessFinancesDatabaseManager busFNManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_business_finance_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busFNManager=new businessFinancesDatabaseManager(this,null,null,1);
        showDatabase();
    }

    public void goToNextViewFinancePage(View view){
        Intent i = new Intent(this, viewBusinessFinanceDatabase2.class);
        startActivity(i);
    }
    public void showDatabase() {
        TextView busIdentifierViewer = (TextView) findViewById(R.id.identifierViewBox);
        TextView cardTypeViewer = (TextView) findViewById(R.id.cardTypeViewBox);
        TextView cardNameViewer = (TextView) findViewById(R.id.cardNameViewBox);

        String busFNDBString = busFNManager.databaseToString("businessidentifier","cardtype","cardname");

        String busIdentifiersString ="";
        String cardTypesString = "";
        String cardNamesString = "";

        String word = "";
        for (char currentChar : busFNDBString.toCharArray()) {
            if(currentChar!='^'&&currentChar!='*'&&currentChar!='~'){
                word+=currentChar;
            }
            else if(currentChar=='^'){
                busIdentifiersString += word;
                busIdentifiersString += "\n\n\n\n";
                word = "";
            }
            //We are not adding ids.
            else if (currentChar == '*') {
                cardTypesString += word;
                cardTypesString += "\n\n\n\n";
                word = "";
            } else if (currentChar == '~') {
                cardNamesString += word;
                cardNamesString += "\n\n\n\n";
                word = "";
            }
            busIdentifierViewer.setText(busIdentifiersString);
            cardTypeViewer.setText(cardTypesString);
            cardNameViewer.setText(cardNamesString);
        }
    }
}
