package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewProdDatabase1 extends AppCompatActivity {
    busProductsInformationDatabaseManager busPDManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prod_database1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        busPDManager=new busProductsInformationDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void nextClicked(View view){
        Intent i = new Intent(this, viewProdDatabase2.class);
        //We must keep passing this around in here so it is not lost
        startActivity(i);
    }
    public void showDatabase() {
        TextView productNameViewer = (TextView) findViewById(R.id.prodNameForViewDBView);
        TextView busNameViewer = (TextView) findViewById(R.id.bussNameView);
        TextView consDemViewer = (TextView) findViewById(R.id.consDemView);

        String busProdString = busPDManager.databaseToString();

        String prodNameString ="";
        String busNameString = "";
        String cardDemString = "";
        String word = "";
        boolean onOtherPages=false;
        for (char currentChar : busProdString.toCharArray()) {
            if(!onOtherPages){
                if (currentChar != '^' && currentChar != '*' && currentChar != '~') {
                    word += currentChar;
                } else if (currentChar == '^') {
                    prodNameString += word;
                    prodNameString += "\n\n\n\n";
                    word = "";
                }
                //We are not adding ids.
                else if (currentChar == '*') {
                    busNameString += word;
                    busNameString += "\n\n\n\n";
                    word = "";
                } else if (currentChar == '~') {
                    cardDemString += word;
                    cardDemString += "\n\n\n\n";
                    word = "";
                    onOtherPages=true;
                }
            }
            else if(currentChar=='$'){
                onOtherPages=false;
            }
        }
        productNameViewer.setText(prodNameString);
        busNameViewer.setText(busNameString);
        consDemViewer.setText(cardDemString);
    }

}
