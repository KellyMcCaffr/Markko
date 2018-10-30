package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewProdDatabase2 extends AppCompatActivity {
    busProductsInformationDatabaseManager busPDManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prod_database2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busPDManager=new busProductsInformationDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void nextClicked(View view){
        Intent i = new Intent(this, viewProdDatabase3.class);
        //We must keep passing this around in here so it is not lost
        startActivity(i);
    }
    public void showDatabase() {
        TextView consEducationViewer = (TextView) findViewById(R.id.consEdView);
        TextView consRaceViewer = (TextView) findViewById(R.id.consRaceView);
        TextView consGenderViewer = (TextView) findViewById(R.id.consGenderView);

        String busProdString = busPDManager.databaseToString();

        String consEducationString ="";
        String consRaceString = "";
        String consGenderString = "";
        String word = "";
        boolean onOtherPages=true;
        for (char currentChar : busProdString.toCharArray()) {
            if(!onOtherPages){
                if (currentChar != '%' && currentChar != ',' && currentChar != '>') {
                    word += currentChar;
                } else if (currentChar == '%') {
                    consEducationString += word;
                    consEducationString += "\n\n\n\n";
                    word = "";
                }
                //We are not adding ids.
                else if (currentChar == ',') {
                    consRaceString += word;
                    consRaceString += "\n\n\n\n";
                    word = "";
                } else if (currentChar=='>') {
                    consGenderString += word;
                    consGenderString += "\n\n\n\n";
                    word = "";
                    onOtherPages=true;
                }
            }
            else if(currentChar=='~'){
                onOtherPages=false;
            }
        }
        consEducationViewer.setText(consEducationString);
        consRaceViewer.setText(consRaceString);
        consGenderViewer.setText(consGenderString);
    }

}
