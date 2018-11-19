package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.util.Log;

public class firstUserSignupSurvey extends Activity {
    String enteredUsername;
    boolean dataAccepted=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        setContentView(R.layout.activity_first_user_signup_survey);

    }
    private void setSurvey1DataVariables(){

        String gender;
        String age;
        String race;
        String religion;
        String educationLevel;

        CheckBox A1=(CheckBox) findViewById(R.id.A1);
        CheckBox A2=(CheckBox) findViewById(R.id.A2);
        CheckBox A3=(CheckBox) findViewById(R.id.A3);
        if(A1.isChecked()&& ! A2.isChecked() && ! A3.isChecked()){
            gender="male";
        }
        else if(A2.isChecked()&& ! A1.isChecked() && ! A3.isChecked()){
            gender="female";
        }
        else if(A3.isChecked()&& ! A1.isChecked() && ! A2.isChecked()){
            gender="other";
        }
        else{
            Log.d("firstSurvey","1");
            dataAccepted=false;
            gender="";
        }

        CheckBox B1=(CheckBox) findViewById(R.id.B1);
        CheckBox B2=(CheckBox) findViewById(R.id.B2);
        CheckBox B3=(CheckBox) findViewById(R.id.B3);
        CheckBox B4=(CheckBox) findViewById(R.id.B4);
        if(B1.isChecked()&& ! B2.isChecked() && ! B3.isChecked() && ! B4.isChecked()){
            age="<18";
        }
        else if(B2.isChecked()&& ! B1.isChecked() && ! B3.isChecked() && ! B4.isChecked()){
            age="18-25";
        }
        else if(B3.isChecked()&& ! B1.isChecked() && ! B2.isChecked() && ! B4.isChecked()){
            age="25-35";
        }
        else if(B4.isChecked()&& ! B1.isChecked() && ! B2.isChecked() && ! B3.isChecked()){
            age="35+";
        }
        else{
            Log.d("firstSurvey","2");
            dataAccepted=false;
            age="";
        }
        CheckBox C1=(CheckBox) findViewById(R.id.C1);
        CheckBox C2=(CheckBox) findViewById(R.id.C2);
        CheckBox C3=(CheckBox) findViewById(R.id.C3);
        CheckBox C4=(CheckBox) findViewById(R.id.C4);
        if(C1.isChecked()&& ! C2.isChecked() && ! C3.isChecked() && ! C4.isChecked()){
            race="black";
        }
        else if(C2.isChecked()&& ! C1.isChecked() && ! C3.isChecked() && ! C4.isChecked()){
            race="white";
        }
        else if(C3.isChecked()&& ! C1.isChecked() && ! C2.isChecked() && ! C4.isChecked()){
            race="asian";
        }
        else if(C4.isChecked()&& ! C1.isChecked() && ! C2.isChecked() && ! C3.isChecked()){
            race="other";
        }
        else{
            Log.d("firstSurvey","3");
            dataAccepted=false;
            race="";
        }
        CheckBox D1=(CheckBox) findViewById(R.id.D1);
        CheckBox D2=(CheckBox) findViewById(R.id.D2);
        CheckBox D3=(CheckBox) findViewById(R.id.D3);
        CheckBox D4=(CheckBox) findViewById(R.id.D4);
        CheckBox D5=(CheckBox) findViewById(R.id.D5);
        CheckBox D6=(CheckBox) findViewById(R.id.D6);
        if(D1.isChecked()&& ! D2.isChecked() && ! D3.isChecked() && ! D4.isChecked() && ! D5.isChecked() && ! D6.isChecked()){
            religion="Christian";
        }
        else if(D2.isChecked()&& ! D1.isChecked() && ! D3.isChecked() && ! D4.isChecked() && ! D5.isChecked()&& ! D6.isChecked()){
            religion="Muslim";
        }
        else if(D3.isChecked()&& ! D1.isChecked() && ! D2.isChecked() && ! D4.isChecked() && ! D5.isChecked()&& ! D6.isChecked()){
            religion="Buddhist";
        }
        else if(D4.isChecked()&& ! D1.isChecked() && ! D2.isChecked() && ! D3.isChecked() && ! D5.isChecked()&& ! D6.isChecked()){
            religion="Jewish";
        }
        else if(D5.isChecked()&& ! D1.isChecked() && ! D2.isChecked() && ! D3.isChecked() && ! D4.isChecked()&& ! D6.isChecked()){
            religion="Atheist";
        }
        else if(D6.isChecked()&& ! D1.isChecked() && ! D2.isChecked() && ! D3.isChecked() && ! D4.isChecked()&& ! D5.isChecked()){
            religion="other";
        }
        else{
            Log.d("firstSurvey","4");
            dataAccepted=false;
            religion="";
        }
        CheckBox E1=(CheckBox) findViewById(R.id.E1);
        CheckBox E2=(CheckBox) findViewById(R.id.E2);
        CheckBox E3=(CheckBox) findViewById(R.id.E3);
        CheckBox E4=(CheckBox) findViewById(R.id.E4);
        CheckBox E5=(CheckBox) findViewById(R.id.E5);
        if(E1.isChecked()&& ! E2.isChecked() && ! E3.isChecked() && ! E4.isChecked() && ! E5.isChecked()){
            educationLevel="elementary";
        }
        else if(E2.isChecked()&& ! E1.isChecked() && ! E3.isChecked() && ! E4.isChecked() && ! E5.isChecked()){
            educationLevel="middle";
        }
        else if(E3.isChecked()&& ! E1.isChecked() && ! E2.isChecked() && ! E4.isChecked() && ! E5.isChecked()){
            educationLevel="high school";
        }
        else if(E4.isChecked()&& ! E1.isChecked() && ! E2.isChecked() && ! E3.isChecked() && ! E5.isChecked()){
            educationLevel="college";
        }
        else if(E5.isChecked()&& ! E1.isChecked() && ! E2.isChecked() && ! E3.isChecked() && ! E4.isChecked()){
            educationLevel="post-grad";
        }
        else{
            educationLevel="";
            Log.d("firstSurvey","5");
            dataAccepted=false;
        }
        userAccountDatabaseProducts.set_gender(gender);
        userAccountDatabaseProducts.set_age(age);
        userAccountDatabaseProducts.set_race(race);
        userAccountDatabaseProducts.set_religion(religion);
        userAccountDatabaseProducts.set_educationLevel(educationLevel);

    }
    public void continueToSurvey2(View view){
        setSurvey1DataVariables();
        if(dataAccepted) {
            Intent i = new Intent(this, initialSurvey2.class);
            i.putExtra("enteredUsername", enteredUsername);
            startActivity(i);
        }
        else{
            dataAccepted=true;
            goToErrorPage("You must enter a single value per option!");
        }
    }
    public void goBackToSignupPage(){
        Intent i = new Intent(this,signUpChoice.class);
        //We need to pass the enteredUsername back so that our main page remembers it
        //We will need to do this every time.
        startActivity(i);
    }
    //Prevent user from entering same info again:
    @Override
    public void onBackPressed(){
        goBackToSignupPage();
    }
    public void goToErrorPage(String message){
        Intent i = new Intent(this, generalErrorPage.class);
        i.putExtra("errorMessage",message);
        startActivity(i);
    }

}
