package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessesDatabase extends AppCompatActivity {

    businessAccountsDatabaseManager businessesDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_businesses_database);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        businessesDBManager=new businessAccountsDatabaseManager(this,null,null,1);
        showDatabase();
    }

    public void nextViewBusDBPage(View view){
        Intent i = new Intent(this, viewBusinessesDatabase2.class);
        startActivity(i);
    }


    public void showDatabase(){
        TextView businessNameViewer = (TextView) findViewById(R.id.companyNameViewBox);
        TextView businessPasswordViewer = (TextView) findViewById(R.id.busPasswordViewBox);
        TextView businessEmailViewer = (TextView) findViewById(R.id.busEmailViewBox);


        String accountsDBString=businessesDBManager.databaseToString();

        String businessNameString="";
        String businessEmailString="";
        String businessPasswordString="";
        boolean onOtherPages=false;

        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar=='$'){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='^' && currentChar!='*' && currentChar !='~' && currentChar !='%' && currentChar!=',' && currentChar!='.' && currentChar!='$') {
                word += Character.toString(currentChar);
            }
            //We are not adding ids.
            else if (currentChar=='^') {
                businessNameString += word;
                businessNameString += "\n\n\n";
                word = "";
            } else if (currentChar=='*') {
                businessEmailString += word;
                businessEmailString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='~') {
                businessPasswordString += word;
                businessPasswordString += "\n\n\n";
                word = "";
                onOtherPages=true;
            }

        }
        businessNameViewer.setText(businessNameString);
        businessEmailViewer.setText(businessEmailString);
        businessPasswordViewer.setText(businessPasswordString);
    }

}
