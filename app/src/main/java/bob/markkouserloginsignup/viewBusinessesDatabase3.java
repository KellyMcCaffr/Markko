package bob.markkouserloginsignup;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class viewBusinessesDatabase3 extends AppCompatActivity {
    businessAccountsDatabaseManager businessesDBManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_businesses_database3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        businessesDBManager=new businessAccountsDatabaseManager(this,null,null,1);
        showDatabase();
    }

    public void showDatabase(){

        TextView businessPlanViewer = (TextView) findViewById(R.id.busPlanViewBox);
        TextView businessProductsViewer = (TextView) findViewById(R.id.busProductsViewBox);

        String businessPlanString="";
        String businessProductsString="";
        String accountsDBString=businessesDBManager.databaseToString();
        boolean onOtherPages=true;

        String word="";
        for(char currentChar:accountsDBString.toCharArray()){
            if(onOtherPages){
                if(currentChar==','){
                    onOtherPages=false;
                }
            }
            else if (currentChar!='^' && currentChar!='*' && currentChar !='~' && currentChar !='%' && currentChar!=',' && currentChar!='.' && currentChar!='$') {
                word += Character.toString(currentChar);
            }
            //We are not adding ids.
            else if (currentChar=='*') {
                businessPlanString += word;
                businessPlanString += "\n\n\n\n";
                word = "";
            } else if (currentChar=='$') {
                businessProductsString += word;
                businessProductsString += "\n\n\n\n";
                onOtherPages=true;
                word = "";
            }
        }
        businessPlanViewer.setText(businessPlanString);
        businessProductsViewer.setText(businessProductsString);
    }

}
