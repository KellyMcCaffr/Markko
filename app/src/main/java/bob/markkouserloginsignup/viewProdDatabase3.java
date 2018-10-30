package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewProdDatabase3 extends AppCompatActivity {
    busProductsInformationDatabaseManager busPDManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prod_database3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busPDManager=new busProductsInformationDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void nextClicked(View view){
        Intent i = new Intent(this, viewProdDatabase1.class);
        //We must keep passing this around in here so it is not lost
        startActivity(i);
    }
    public void showDatabase() {
        TextView consReligionViewer = (TextView) findViewById(R.id.consReligionView);
        TextView consLikesViewer = (TextView) findViewById(R.id.consLikesView);

        String busProdString = busPDManager.databaseToString();

        String consReligionString ="";
        String consLikesString= "";
        String word = "";
        boolean onOtherPages=true;
        for (char currentChar : busProdString.toCharArray()) {
            if (!onOtherPages) {
                if (currentChar != '~' && currentChar != '$') {
                    word += currentChar;
                } else if (currentChar == '~') {
                    consReligionString += word;
                    consReligionString += "\n\n\n\n";
                    word = "";
                }
                //We are not adding ids.
                else if (currentChar == '$') {
                    consLikesString += word;
                    consLikesString += "\n\n\n\n";
                    word = "";
                    onOtherPages=true;
                }
            }
            else if(currentChar=='>'){
                onOtherPages=false;
            }

        }

        consReligionViewer.setText(consReligionString);
        consLikesViewer.setText(consLikesString);
    }

}
