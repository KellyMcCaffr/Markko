package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class userStore extends Activity implements OnItemSelectedListener{
    String enteredUsername;
    Spinner menu;
    private int pointsNeeded;
    private int userPoints;
    private userAccountDatabaseManager usersDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        setContentView(R.layout.activity_user_store);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        setPointsView();
        manageSpinner();
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        userPoints=usersDB.getPoints(enteredUsername);

    }

    private boolean sufficientFunds(){
        return userPoints>=pointsNeeded;
    }

    private void applyPointCost(){
        usersDB.incrementPoints(enteredUsername,-1*pointsNeeded);
    }
    private void goToErrorPage(String message){
        Intent i = new Intent(this, generalErrorPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("errorMessage",message);
        startActivity(i);
    }
    private void attemptBuyingProcess(){
        if(sufficientFunds()){
            applyPointCost();
        }
        else{
            goToErrorPage("You do not have enough points to buy this item");
        }
    }
    public void buy1Clicked(View v){
        pointsNeeded=10000;
        attemptBuyingProcess();
    }
    public void buy2Clicked(View v){
        pointsNeeded=15000;
        attemptBuyingProcess();
    }
    public void buy3Clicked(View v){
        pointsNeeded=10000;
        attemptBuyingProcess();
    }
    public void buy4Clicked(View v){
        pointsNeeded=50000;
        attemptBuyingProcess();
    }
    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        menu= (Spinner) findViewById(R.id.storeUserMenu);
        //Allows us to respond to user selections
        menu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.user_menu_store,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);
        menu.setBackgroundResource(R.drawable.settingsbuttonwhite);
    }
    //Called when an item in the menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        menu.setSelection(0);
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
                goToManageAccountPage();
                break;
            case 5:
                goToPaymentInfoPage();
                break;
            case 6:
                signOut();
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    public void setPointsView(){
        TextView pointsView=(TextView)findViewById(R.id.storePointsView);
        String currentPoints=Integer.toString(usersDB.getPoints(enteredUsername));
        pointsView.setText("Your total points: "+currentPoints);

    }
    public void signOut(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void goToPaymentInfoPage(){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("isUser","yes");
        startActivity(i);
    }
    public void goToManageAccountPage(){
        Intent i = new Intent(this, manageUserAccount.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public void goToSwipePage(){
        Intent i = new Intent(this, userSwipePage.class);
        i.putExtra("enteredUsername",enteredUsername);
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

}
