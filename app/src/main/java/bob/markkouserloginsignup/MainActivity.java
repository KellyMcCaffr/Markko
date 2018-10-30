package bob.markkouserloginsignup;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;


public class MainActivity extends Activity {

    userAccountDatabaseManager accountDatabaseSearcher;

    businessAccountsDatabaseManager businessDatabaseSearcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountDatabaseSearcher=new userAccountDatabaseManager(this,null,null,1);
        businessDatabaseSearcher=new businessAccountsDatabaseManager(this,null,null,1);
        setContentView(R.layout.activity_main);
        //goToSwipeTest();
    }

    public void goToSwipeTest(){
        Intent i = new Intent(this,userSwipePage.class);
        i.putExtra("enteredUsername","ee");
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void goToMainDBS(View view){
        Intent i = new Intent(this,viewDBs.class);
        startActivity(i);
    }

    //Moves app to the signup screen when sign up text is clicked.
    public void goToSignup(View view){
        Intent i = new Intent(this, signUpChoice.class);
        startActivity(i);
    }

    public void goToMainBusinessPage(String enteredUsername){
        Intent i = new Intent(this, mainBusinessPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void goToMainUserPage(String enteredUsername){
        Intent i = new Intent(this, mainUserPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    //Triggered when the user presses enter.
    //Make some of these protected?
    public void attemptLogin(View view){

        TextView errorMessageBox= (TextView)findViewById(R.id.userLoginProblems);
        EditText enteredUsernameTextField=(EditText)findViewById(R.id.accountUsernameEntered);
        EditText enteredPasswordTextField=(EditText)findViewById(R.id.accountPasswordEntered);
        String enteredUsername=enteredUsernameTextField.getText().toString();
        String enteredPassword=enteredPasswordTextField.getText().toString();
        if(enteredUsername.equals("")) {
            errorMessageBox.setText("Username is missing");
        }
        else if(enteredPassword.equals("")){
            errorMessageBox.setText("Password is missing");
        }
        else {
            if (accountDatabaseSearcher.checkLogin(enteredUsername, enteredPassword)) {
                goToMainUserPage(enteredUsername);
            }
            else if(businessDatabaseSearcher.checkLogin(enteredUsername, enteredPassword)){
                goToMainBusinessPage(enteredUsername);
            }

            else {
                errorMessageBox.setText("Your username or password is incorrect");
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
