package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class businessSignup extends Activity {
    businessAccountsDatabaseManager businessDatabaseManager;
    userAccountDatabaseManager userDatabaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);

        businessDatabaseManager=new businessAccountsDatabaseManager(this,null,null,1);
        userDatabaseManager=new userAccountDatabaseManager(this,null,null,1);
    }
    //Returns true if a word contains forbidden characters and false otherwise
    private boolean containsForbiddenCharacters(String word){
        char[] forbiddenCharacters={'#',',','$','^','*','~','%'};

        for(char i:word.toCharArray()){
            for(char j:forbiddenCharacters){
                if(i==j){
                    return true;
                }
            }
        }
        return false;
    }
    //Moves to the next signup page only if several constraints are met.
    public void arrowClicked(View view){
        TextView nameBox=(TextView)findViewById(R.id.nameViewBox);
        String nameText=nameBox.getText().toString().trim();
        TextView emailBox=(TextView)findViewById(R.id.emailViewBox);
        String emailText=emailBox.getText().toString().trim();
        TextView companyBox=(TextView)findViewById(R.id.companyViewBox);
        String companyText=companyBox.getText().toString().trim();
        TextView urlBox=(TextView)findViewById(R.id.urlViewBox);
        String urlText=urlBox.getText().toString().trim();
        TextView passwordBox=(TextView)findViewById(R.id.passwordViewBox);
        String passwordText=passwordBox.getText().toString().trim();
        TextView passwordReBox=(TextView)findViewById(R.id.password2ViewBox);
        String passwordReText=passwordReBox.getText().toString().trim();
        TextView errorViewBox=(TextView)findViewById(R.id.errorViewBoxP);


        if(nameText.equals("")){
            errorViewBox.setText("Name is missing");
        }
        else if(emailText.equals("")){
            errorViewBox.setText("Email is missing");
        }
        else if(companyText.equals("")){
            errorViewBox.setText("Company is missing");
        }
        else if(passwordText.equals("")){
            errorViewBox.setText("Password is missing");
        }
        else if(passwordReText.equals("")){
            errorViewBox.setText("Please re-enter your password");
        }
        else if(emailText.indexOf('@')==-1){
            errorViewBox.setText("Invalid email");
        }
        else if(!passwordText.equals(passwordReText)){
            errorViewBox.setText("Passwords do not match");
        }
        else if(businessDatabaseManager.nameExists(nameText) || userDatabaseManager.nameExists(nameText)){
            errorViewBox.setText("Name already exists");
        }
        else if(containsForbiddenCharacters(nameText)||containsForbiddenCharacters(emailText)||containsForbiddenCharacters(companyText)||containsForbiddenCharacters(passwordText)||containsForbiddenCharacters(urlText)){
            errorViewBox.setText("Characters '*',',','$','^','*','~','%' are forbidden");
        }
        else{
            businessAccountDatabaseProducts newBusinessAccountObject=new businessAccountDatabaseProducts(nameText,emailText,passwordText,companyText,urlText, "Insert Industries Here");
            goToBusinessSignup2(nameText);
        }
    }
    public void goToBusinessSignup2(String nameText){

        Intent i = new Intent(this, businessSignupIndustries.class);
        i.putExtra("enteredUsername",nameText);
        startActivity(i);
    }

}
