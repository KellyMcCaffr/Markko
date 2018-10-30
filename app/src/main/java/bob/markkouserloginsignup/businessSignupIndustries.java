package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class businessSignupIndustries extends Activity {
    userAccountDatabaseManager accountDatabaseChanger;
    List<TextView> selectableIndustryViews;
    List<String> selectedIndustriesOfInterest;
    String enteredUsername;
    private int industryCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedIndustriesOfInterest=new ArrayList<String>();
        accountDatabaseChanger=new userAccountDatabaseManager(this,null,null,1);
        setContentView(R.layout.activity_business_signup3);


        TextView Ind1=(TextView) findViewById(R.id.Ind1b);
        TextView Ind2=(TextView) findViewById(R.id.Ind2b);
        TextView Ind3=(TextView) findViewById(R.id.Ind3b);
        TextView Ind4=(TextView) findViewById(R.id.Ind4b);
        TextView Ind5=(TextView) findViewById(R.id.Ind5b);
        TextView Ind6=(TextView) findViewById(R.id.Ind6b);
        TextView Ind7=(TextView) findViewById(R.id.Ind7b);
        TextView Ind8=(TextView) findViewById(R.id.Ind8b);
        TextView Ind9=(TextView) findViewById(R.id.Ind9b);
        TextView Ind10=(TextView) findViewById(R.id.Ind10b);
        TextView Ind11=(TextView) findViewById(R.id.Ind11b);
        TextView Ind12=(TextView) findViewById(R.id.Ind12b);

        TextView Ind13=(TextView) findViewById(R.id.Ind13b);
        TextView Ind14=(TextView) findViewById(R.id.Ind14b);
        TextView Ind15=(TextView) findViewById(R.id.Ind15b);
        TextView Ind16=(TextView) findViewById(R.id.Ind16b);
        TextView Ind17=(TextView) findViewById(R.id.Ind17b);
        TextView Ind18=(TextView) findViewById(R.id.Ind18b);
        TextView Ind19=(TextView) findViewById(R.id.Ind19b);
        TextView Ind20=(TextView) findViewById(R.id.Ind20b);
        TextView Ind21=(TextView) findViewById(R.id.Ind21b);
        TextView Ind22=(TextView) findViewById(R.id.Ind22b);
        TextView Ind23=(TextView) findViewById(R.id.Ind23b);
        TextView Ind24=(TextView) findViewById(R.id.Ind24b);

        selectableIndustryViews=Arrays.asList(Ind1,Ind2,Ind3,Ind4,Ind5,Ind6,Ind7,Ind8,Ind9,Ind10,
                Ind11,Ind12,Ind13,Ind14,Ind15,Ind16,Ind17,Ind18,Ind19,Ind20,Ind21,Ind22,Ind23,Ind24);

        for(final TextView t:selectableIndustryViews){
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
        }
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
    }

    private String getStringRepresentationOfSelectedIndustries(){
        String stringRepresentation="";
        industryCount=0;
        for(String i:selectedIndustriesOfInterest){
            stringRepresentation+=i;
            stringRepresentation+="|";
            industryCount+=1;
        }

        return stringRepresentation;

    }
    //Adds all of the new accounts data to the database.
    //Will also take new user to main account page.
    //Does this need to be public for button click to work?
    public void continueClicked(View view){
        //This seems a little bit hacky, hopefully static variables will be changed
        //as expected.
        String industriesString=getStringRepresentationOfSelectedIndustries();
        if(industryCount>2){
            Intent i = new Intent(this, generalErrorPage.class);
            i.putExtra("errorMessage","You may enter a maximum of two matching industries");
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, businessSignupPlan.class);
            i.putExtra("enteredUsername", enteredUsername);
            i.putExtra("industryString", industriesString);
            startActivity(i);
        }
    }


}
