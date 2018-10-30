package bob.markkouserloginsignup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessFinanceDatabase3 extends AppCompatActivity {
    businessFinancesDatabaseManager busFNManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_business_finance_database3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busFNManager=new businessFinancesDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void showDatabase() {
        TextView billingAdViewer = (TextView) findViewById(R.id.bAViewBox);
        TextView shippingAdViewer = (TextView) findViewById(R.id.sAViewBox);

        String busFNDBString = busFNManager.databaseToString("billingaddress","shippingaddress","X");

        String bAString="";
        String sAString="";

        String word = "";
        for (char currentChar : busFNDBString.toCharArray()) {

            if(currentChar!='^'&&currentChar!='*'){
                word+=currentChar;
            }
            else if(currentChar=='^'){
                bAString += word;
                bAString += "\n\n\n";
                word = "";
            }
            //We are not adding ids.
            else if (currentChar == '*') {
                sAString += word;
                sAString += "\n\n\n";
                word = "";
            }
            billingAdViewer.setText(bAString);
            shippingAdViewer.setText(sAString);
        }
    }

}
