package bob.markkouserloginsignup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class busIndustryOverview extends AppCompatActivity {

    TextView ind1c;
    TextView ind2c;
    TextView ind3c;
    TextView ind4c;
    TextView ind5c;
    TextView ind6c;
    TextView ind7c;
    TextView ind8c;
    TextView ind9c;
    TextView ind10c;
    TextView ind11c;
    TextView ind12c;
    TextView ind13c;
    TextView ind14c;
    TextView ind15c;
    TextView ind16c;
    TextView ind17c;
    TextView ind18c;
    TextView ind19c;
    TextView ind20c;
    TextView ind21c;
    TextView ind22c;
    TextView ind23c;
    TextView ind24c;
    String enteredUsername;
    List<TextView> selectableIndustryViews;
    businessAccountsDatabaseManager busAccountsDB;
    List<String> selectedIndustriesOfInterest;
    private int industryCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedIndustriesOfInterest=new ArrayList<String>();
        setContentView(R.layout.activity_bus_industry_overview);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        declareTextViews();
        selectableIndustryViews= Arrays.asList(ind1c,ind2c,ind3c,ind4c,ind5c,ind6c,ind7c,ind8c,ind9c,ind10c,
                ind11c,ind12c,ind13c,ind14c,ind15c,ind16c,ind17c,ind18c,ind19c,ind20c,ind21c,ind22c,ind23c,ind24c);

        int num=0;
        for(final TextView t:selectableIndustryViews){
            Log.i("View count",Integer.toString(num));
            t.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                    String currentText=t.getText().toString();
                    if(selectedIndustriesOfInterest.indexOf(currentText)==-1) {
                        selectedIndustriesOfInterest.add(currentText);
                        t.setTextColor(Color.WHITE);
                    }
                    else{
                        t.setTextColor(Color.BLACK);
                        //Remove item from the list if already selected;
                        selectedIndustriesOfInterest.remove(currentText);
                    }
                }
            });
            num+=1;
        }
        busAccountsDB=new businessAccountsDatabaseManager(this,null,null,1);
        addPreviousIndustriesToViews();
    }

    public void declareTextViews(){
        ind1c=(TextView)findViewById(R.id.Ind1c);
        ind2c=(TextView)findViewById(R.id.Ind2c);
        ind3c=(TextView)findViewById(R.id.Ind3c);
        ind4c=(TextView)findViewById(R.id.Ind4c);
        ind5c=(TextView)findViewById(R.id.Ind5c);
        ind6c=(TextView)findViewById(R.id.Ind6c);
        ind7c=(TextView)findViewById(R.id.Ind7c);
        ind8c=(TextView)findViewById(R.id.Ind8c);
        ind9c=(TextView)findViewById(R.id.Ind9c);
        ind10c=(TextView)findViewById(R.id.Ind10c);
        ind11c=(TextView)findViewById(R.id.Ind11c);
        ind12c=(TextView)findViewById(R.id.Ind12c);
        ind13c=(TextView)findViewById(R.id.Ind13c);
        ind14c=(TextView)findViewById(R.id.Ind14c);
        ind15c=(TextView)findViewById(R.id.Ind15c);
        ind16c=(TextView)findViewById(R.id.Ind16c);
        ind17c=(TextView)findViewById(R.id.Ind17c);
        ind18c=(TextView)findViewById(R.id.Ind18c);
        ind19c=(TextView)findViewById(R.id.Ind19c);
        ind20c=(TextView)findViewById(R.id.Ind20c);
        ind21c=(TextView)findViewById(R.id.Ind21c);
        ind22c=(TextView)findViewById(R.id.Ind22c);
        ind23c=(TextView)findViewById(R.id.Ind23c);
        ind24c=(TextView)findViewById(R.id.Ind24c);
    }
    public void selectView(TextView TV){
        String currentText=TV.getText().toString();
        TV.setTextColor(Color.WHITE);
        selectedIndustriesOfInterest.add(currentText);

    }
    public void addPreviousIndustriesToViews(){
        String currentIndustriesString=busAccountsDB.getBusinessIndustries(enteredUsername);
        String industryOne="";
        String industryTwo="";
        String currentViewText;
        int count=0;
        //Gets either one or two industries from the Business Account Data
        for(char i:currentIndustriesString.toCharArray()){
            if(!(i=='|') && (count==0)){
                industryOne+=i;
            }
            else if(!(i=='|') && (count==1)){
                industryTwo+=i;
            }
            else if(i=='|'){
                count+=1;
            }
        }
        for(final TextView t:selectableIndustryViews){
            currentViewText=t.getText().toString();
            if(currentViewText.equals(industryOne)||currentViewText.equals(industryTwo)){
                selectView(t);
            }
        }
    }
    public void returnToMainBusinessPage(){
        Intent i = new Intent(this,mainBusinessPage.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);

    }
    public String getNewIndustriesString(){
        String newIndString="";
        String currentViewText;
        industryCount=0;
        for(final TextView t:selectableIndustryViews){
            if(t.getCurrentTextColor()==Color.WHITE){
                currentViewText=t.getText().toString();
                newIndString+=currentViewText;
                newIndString+="|";
                industryCount+=1;
            }
        }
        if(industryCount>2) {
            newIndString = "";
        }
        return newIndString;
    }
    public void cancelClicked(View view){
        Intent i = new Intent(this,businessAccountInfoPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void finishedClicked(View view){
        String industriesString=getNewIndustriesString();
        if(industryCount>2) {
            Intent i = new Intent(this, generalErrorPage.class);
            i.putExtra("errorMessage", "You may enter a maximum of two matching industries");
            startActivity(i);
        }
        else{
            busAccountsDB.updateIndustries(enteredUsername,industriesString);
            returnToMainBusinessPage();
        }
    }

}
