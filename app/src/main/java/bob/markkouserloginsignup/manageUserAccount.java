package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;


public class manageUserAccount extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner userMenu;
    String enteredUsername;

    TextView usernameBox;
    TextView passwordBox;
    TextView emailBox;

    userAccountDatabaseManager usersDB;
    businessAccountsDatabaseManager busDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        busDB=new businessAccountsDatabaseManager(this,null,null,1);
        setContentView(R.layout.activity_manage_user_account);
        manageSpinner();
        declareTextViews();
        addPrevAccountInfoToViews();
    }

    public void deletePressed(View view){
        Intent i = new Intent(this,checkUserDelete.class);
        //Simply returns to the account info page
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }

    public void checkmarkClicked(View view){
        String newUsername=((TextView)findViewById(R.id.muaUsername)).getText().toString();

        String newPassword=((TextView)findViewById(R.id.muaPassword)).getText().toString();
        String newEmail=((TextView)findViewById(R.id.muaEmail)).getText().toString();
        if(busDB.nameExists(newUsername) || usersDB.nameExists(newUsername) && !(newUsername.equals(enteredUsername))) {
            goToErrorPage("Name already exists");
        }
        else{
            usersDB.updateUsername(enteredUsername, newUsername);
            enteredUsername = newUsername;
            usersDB.updatePassword(enteredUsername, newPassword);
            usersDB.updateEmail(enteredUsername, newEmail);
            goToMainUserPage();
        }
    }
    public void declareTextViews(){
        usernameBox=(TextView)findViewById(R.id.muaUsername);
        passwordBox=(TextView)findViewById(R.id.muaPassword);
        emailBox=(TextView)findViewById(R.id.muaEmail);
    }

    //Adds the previous payment information into the textViews after retrieving it from
    //the businessFinanceDatabase.
    public void addPrevAccountInfoToViews(){
        //Sets variables for the views
        //Why did I need to do all of this again in checkmark clicked?!

        String passwordText=usersDB.getPassword(enteredUsername);
        String emailText=usersDB.getEmail(enteredUsername);

        usernameBox.setText(enteredUsername);
        passwordBox.setText(passwordText);
        emailBox.setText(emailText);
    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        userMenu= (Spinner) findViewById(R.id.userMenuX);
        //Allows us to respond to user selections

        userMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.user_menu_items_accountInfo,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userMenu.setAdapter(adapter);
        userMenu.setBackgroundResource(R.drawable.settingsbuttonwhite);
    }
    //Called when an item in the spinner menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int pos,long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        userMenu.setSelection(0);
        switch(pos){
            case 1:
                goToMainUserPage();
                break;

            case 2:
                goToSwipePage();
                break;
            case 3:
                goToProductControlPage();
                break;
            case 4:
                goToStore();
                break;
            case 5:
                goToPaymentInfoPage();
                break;
            case 6:
                signOut();
                break;
        }
    }

    //I do not understand what this method does or when I can use it
    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }
    public void goToErrorPage(String message){
        Intent i = new Intent(this, generalErrorPage.class);
        i.putExtra("errorMessage",message);
        startActivity(i);
    }
    public void goToPaymentInfoPage(){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("isUser","yes");
        startActivity(i);
    }

    public void signOut(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
    public void goToProductControlPage(){
        Intent i = new Intent(this, userProductControl.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void goToMainUserPage(){
        Intent i = new Intent(this, mainUserPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void goToStore(){
        Intent i = new Intent(this, userStore.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void goToSwipePage(){
        Intent i = new Intent(this, userSwipePage.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }


}
