package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessesDatabase2 extends AppCompatActivity {

    businessAccountsDatabaseManager businessesDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_businesses_database2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        businessesDBManager=new businessAccountsDatabaseManager(this,null,null,1);
        showDatabase();
    }
    public void showDatabase(){
        TextView companyNameViewer = (TextView) findViewById(R.id.companyNameViewBox);
        TextView businessUrlViewer = (TextView) findViewById(R.id.busUrlViewBox);

        String accountsDBString=businessesDBManager.databaseToString();

        String companyNameString="";
        String urlString="";
        boolean onOtherPages=true;

        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar=='~'){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='^' && currentChar!='*' && currentChar !='~' && currentChar !='%' && currentChar!=',' && currentChar!='.' && currentChar!='$') {
                word += Character.toString(currentChar);
            }
            //We are not adding ids.
            else if (currentChar=='%') {
                companyNameString += word;
                companyNameString += "\n\n\n\n";
                word = "";
            } else if (currentChar==',') {
                urlString += word;
                urlString += "\n\n\n\n";
                onOtherPages=true;
                word = "";
            }
        }
        companyNameViewer.setText(companyNameString);
        businessUrlViewer.setText(urlString);
    }
    //Moves to the next page for viewing this database
    public void nextViewBusDBPage(View view){
        Intent i = new Intent(this, viewBusinessesDatabase3.class);
        startActivity(i);
    }

}
