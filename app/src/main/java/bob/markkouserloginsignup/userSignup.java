package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;

public class userSignup extends Activity {
    businessAccountsDatabaseManager businessDatabaseManager;
    userAccountDatabaseManager userDatabaseManager;
    String enteredUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_signup);

        businessDatabaseManager=new businessAccountsDatabaseManager(this,null,null,1);
        userDatabaseManager=new userAccountDatabaseManager(this,null,null,1);
    }

    //Returns true if a word contains forbidden characters and false otherwise
    private boolean containsForbiddenCharacters(String word){
        char[] forbiddenCharacters={'#',',','$','^','*','~','%','>'};

        for(char i:word.toCharArray()){
            for(char j:forbiddenCharacters){
                if(i==j){
                    return true;
                }
            }
        }
        return false;
    }
    public void goToSurvey1Page(View view){
        Intent i = new Intent(this, firstUserSignupSurvey.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    //Checks and adds new account if possible when user clicks enter.
    public void signUp(View view){
        TextView mistakeBox=(TextView)findViewById(R.id.signupFailed);

        EditText nameTextField=(EditText)findViewById(R.id.Name);
        TextView emailTextField=(TextView)findViewById(R.id.Email);
        EditText firstPasswordTextField=(EditText)findViewById(R.id.Password1);
        EditText secondPasswordTextField=(EditText)findViewById(R.id.Password2);

        String nameEntered=nameTextField.getText().toString().trim();
        String emailEntered=emailTextField.getText().toString().trim();
        String firstPasswordEntered=firstPasswordTextField.getText().toString().trim();
        String secondPasswordEntered=secondPasswordTextField.getText().toString().trim();

        if(nameEntered.equals("")){
            mistakeBox.setText("Name is missing");
        }
        else if(emailEntered.equals("")){
            mistakeBox.setText("Email is missing");
        }
        else if(firstPasswordEntered.equals("")){
            mistakeBox.setText("Password is missing");
        }
        else if(secondPasswordEntered.equals("")){
            mistakeBox.setText("Please re-enter your password");
        }
        else if(!firstPasswordEntered.equals(secondPasswordEntered)){
            mistakeBox.setText("Passwords do not match");
        }
        else if(!emailEntered.contains("@")){
            mistakeBox.setText("Invalid Email Entered");
        }
        else if(businessDatabaseManager.nameExists(nameEntered) || userDatabaseManager.nameExists(nameEntered)){
            mistakeBox.setText("Name already exists");
        }
        else if(containsForbiddenCharacters(nameEntered)||containsForbiddenCharacters(emailEntered)||containsForbiddenCharacters(firstPasswordEntered)){
            mistakeBox.setText("Characters '#',',','$','^','*','~','%','>' are forbidden");
        }
        else{
            userAccountDatabaseProducts newAccountObject=new userAccountDatabaseProducts(nameEntered,emailEntered,firstPasswordEntered);
            enteredUsername=nameEntered;
            goToSurvey1Page(view);

        }
    }
}
