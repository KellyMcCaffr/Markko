package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewAccountsDatabase2 extends AppCompatActivity {

    userAccountDatabaseManager accountsDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts_database2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        accountsDBManager=new userAccountDatabaseManager(this,null,null,1);
        showDatabase();
    }
    //Moves to the next page for viewing the users database
    public void nextViewDBPage(View view){
        Intent i = new Intent(this, viewAccountsDatabase3.class);
        startActivity(i);
    }
    public void showDatabase(){
        TextView accountsGenderViewer = (TextView) findViewById(R.id.genderViewBox);
        TextView accountsAgeViewer = (TextView) findViewById(R.id.ageViewBox);
        TextView accountsRaceViewer = (TextView) findViewById(R.id.raceViewBox);
        TextView accountsReligionViewer = (TextView) findViewById(R.id.religionViewBox);

        String accountsDBString=accountsDBManager.databaseToString();

        String genderString="";
        String ageString="";
        String raceString="";
        String religionString="";
        boolean onOtherPages=true;

        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar=='%'){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='%' && currentChar!='^' && currentChar !='*' && currentChar !='~' && currentChar!='$' && currentChar!='#'  && currentChar!=',' ) {
                word += Character.toString(currentChar);
            }
            //We are not adding ids.
            else if (currentChar==',') {
                genderString += word;
                genderString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='^') {
                ageString += word;
                ageString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='~') {
                raceString += word;
                raceString += "\n\n\n\n";
                word = "";
            }
            else if(currentChar=='$') {
                religionString += word;
                religionString += "\n\n\n\n";
                word = "";
                onOtherPages = true;
            }
        }
        accountsGenderViewer.setText(genderString);
        accountsAgeViewer.setText(ageString);
        accountsRaceViewer.setText(raceString);
        accountsReligionViewer.setText(religionString);


    }

}
