package bob.markkouserloginsignup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

public class viewAccountsDatabase extends AppCompatActivity {

    TextView accountDatabaseText;
    userAccountDatabaseManager accountsDBManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountsDBManager=new userAccountDatabaseManager(this,null,null,1);
        showDatabase();
    }
    //Moves to the next page for viewing the users database
    public void nextViewDBPage(View view){
        Intent i = new Intent(this, viewAccountsDatabase2.class);
        startActivity(i);
    }
    public void showDatabase(){
        TextView accountsNameViewer = (TextView) findViewById(R.id.nameViewBox);
        TextView accountsPasswordViewer = (TextView) findViewById(R.id.passwordViewBox);
        TextView accountsEmailViewer = (TextView) findViewById(R.id.emailViewBox);
        TextView accountsPointsViewer = (TextView) findViewById(R.id.pointsViewBox);

        String accountsDBString=accountsDBManager.databaseToString();
        accountsPointsViewer.setText(accountsDBString);
        String namesString="";
        String emailsString="";
        String passwordsString="";
        String pointsString="";
        boolean onOtherPages=false;

        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar=='>'){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='%' && currentChar!='^' && currentChar !='*' && currentChar !='~' && currentChar!='$' && currentChar!='#' && currentChar!='>'&& currentChar!=',' ) {
                word += Character.toString(currentChar);
            }
            //We are not adding ids.
            else if (currentChar=='^') {
                namesString += word;
                namesString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='*') {
                passwordsString += word;
                passwordsString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='~') {
                emailsString += word;
                emailsString += "\n\n\n";
                word = "";
            }
            else if(currentChar=='%'){
                pointsString += word;
                pointsString += "\n\n\n\n";
                word = "";
                onOtherPages=true;
            }
        }
        accountsNameViewer.setText(namesString);
        accountsPasswordViewer.setText(passwordsString);
        accountsEmailViewer.setText(emailsString);
        accountsPointsViewer.setText(pointsString);

    }
}