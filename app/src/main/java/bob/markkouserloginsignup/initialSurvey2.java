package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.graphics.Color;

public class initialSurvey2 extends Activity {
    userAccountDatabaseManager accountDatabaseChanger;
    List<TextView> selectableIndustryViews;
    List<String> selectedIndustriesOfInterest;
    String enteredUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        selectedIndustriesOfInterest=new ArrayList<String>();
        accountDatabaseChanger=new userAccountDatabaseManager(this,null,null,1);
        setContentView(R.layout.activity_initial_survey2);
        TextView Ind1=(TextView) findViewById(R.id.Ind1);
        TextView Ind2=(TextView) findViewById(R.id.Ind2);
        TextView Ind3=(TextView) findViewById(R.id.Ind3);
        TextView Ind4=(TextView) findViewById(R.id.Ind4);
        TextView Ind5=(TextView) findViewById(R.id.Ind5);
        TextView Ind6=(TextView) findViewById(R.id.Ind6);
        TextView Ind7=(TextView) findViewById(R.id.Ind7);
        TextView Ind8=(TextView) findViewById(R.id.Ind8);
        TextView Ind9=(TextView) findViewById(R.id.Ind9);
        TextView Ind10=(TextView) findViewById(R.id.Ind10);
        TextView Ind11=(TextView) findViewById(R.id.Ind11);
        TextView Ind12=(TextView) findViewById(R.id.Ind12);

        TextView Ind13=(TextView) findViewById(R.id.Ind13);
        TextView Ind14=(TextView) findViewById(R.id.Ind14);
        TextView Ind15=(TextView) findViewById(R.id.Ind15);
        TextView Ind16=(TextView) findViewById(R.id.Ind16);
        TextView Ind17=(TextView) findViewById(R.id.Ind17);
        TextView Ind18=(TextView) findViewById(R.id.Ind18);
        TextView Ind19=(TextView) findViewById(R.id.Ind19);
        TextView Ind20=(TextView) findViewById(R.id.Ind20);
        TextView Ind21=(TextView) findViewById(R.id.Ind21);
        TextView Ind22=(TextView) findViewById(R.id.Ind22);
        TextView Ind23=(TextView) findViewById(R.id.Ind23);
        TextView Ind24=(TextView) findViewById(R.id.Ind24);

        selectableIndustryViews=Arrays.asList(Ind1,Ind2,Ind3,Ind4,Ind5,Ind6,Ind7,Ind8,Ind9,Ind10,
                Ind11,Ind12,Ind13,Ind14,Ind15,Ind16,Ind17,Ind18,Ind19,Ind20,Ind21,Ind22,Ind23,Ind24);

        for(final TextView t:selectableIndustryViews){
            t.setOnClickListener(new OnClickListener(){
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
    }

    private String getStringRepresentationOfSelectedIndustries(){
        String stringRepresentation="";
        for(String i:selectedIndustriesOfInterest){
            stringRepresentation+=i;
            stringRepresentation+="|";
        }
        return stringRepresentation;
    }

    public void goToMainUserPage(){
        Intent i = new Intent(this,mainUserPage.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("justCreated",true);
        startActivity(i);
    }
    public void goBackToSignupPage(){
        Intent i = new Intent(this,signUpChoice.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        startActivity(i);
    }
    //Adds all of the new accounts data to the database.
    //Will also take new user to main account page.
    //Does this need to be public for button click to work?
    public void continueClicked(View view){
        //This seems a little bit hacky, hopefully static variables will be changed
        //as expected.
        userAccountDatabaseProducts newAccountObject=new userAccountDatabaseProducts();
        accountDatabaseChanger.addAccount(newAccountObject,getStringRepresentationOfSelectedIndustries());
        goToMainUserPage();
    }
    //Prevent user from entering  info again:
    @Override
    public void onBackPressed(){
      goBackToSignupPage();
    }

}
