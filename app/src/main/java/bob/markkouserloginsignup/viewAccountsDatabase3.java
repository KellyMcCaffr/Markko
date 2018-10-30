package bob.markkouserloginsignup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewAccountsDatabase3 extends AppCompatActivity {
    userAccountDatabaseManager accountsDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts_database3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountsDBManager=new userAccountDatabaseManager(this,null,null,1);
        showDatabase();
    }

    public void showDatabase(){
        TextView accountsEducationViewer = (TextView) findViewById(R.id.educationViewBox);
        TextView accountsIndustriesViewer = (TextView) findViewById(R.id.industryViewBox);

        String accountsDBString=accountsDBManager.databaseToString();

        String educationString="";
        String industriesString="";

        boolean onOtherPages=true;
        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar=='$'){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='%' && currentChar!='^' && currentChar !='*' && currentChar !='~' && currentChar!='$' && currentChar!='#' && currentChar!='>' && currentChar!=',' ) {
                word += Character.toString(currentChar);
                //We
            }
            //We are not adding ids.
            else if (currentChar=='#') {
                educationString += word;
                educationString += "\n\n\n\n";
                word = "";
            }

            else if(currentChar=='>') {
                industriesString += word;
                industriesString += "\n\n\n\n";
                word = "";
                onOtherPages = true;
            }
        }
        accountsEducationViewer.setText(educationString);
        accountsIndustriesViewer.setText(industriesString);


    }

}
