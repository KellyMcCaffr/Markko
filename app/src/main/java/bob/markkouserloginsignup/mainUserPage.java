package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;


public class mainUserPage extends Activity implements OnItemSelectedListener {
    float x1,x2;
    float y1, y2;
    String enteredUsername;
    Spinner userMenu;
    userAccountDatabaseManager usersDB;
    boolean justCreated=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        //If account just created, don't allow back to prev page
        if(paramsPassed.getBoolean("justCreated")==true){
            justCreated=true;
        }
        setContentView(R.layout.activity_main_user_page);
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        manageSpinner();
        setLikesView();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        //Update user points display on back press:
        setLikesView();

    }
    public void setLikesView(){
        TextView pointsView=(TextView)findViewById(R.id.userPointsView);
        String currentPoints=Integer.toString(usersDB.getPoints(enteredUsername));
        pointsView.setText(currentPoints+" points");
    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        userMenu= (Spinner) findViewById(R.id.mainPageUserMenu);
        //Allows us to respond to user selections
        userMenu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.user_menu_items1,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userMenu.setAdapter(adapter);
        userMenu.setBackgroundResource(R.drawable.usersettingsbuttorange);
    }
    //Called when an item in the menu is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
        //Prevents the spinner from filling itself with text from selected item.
        //Works because I made the 0th item in the spinner an empty string("").
        userMenu.setSelection(0);
        switch(pos){
            case 1:
                goToSwipePage();
                break;

            case 2:
                goToProductControlPage();
                break;
            case 3:
                goToStore();
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

    public void signOut(){
        Intent i = new Intent(this, MainActivity.class);
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
    public void goToPaymentInfoPage(){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("isUser","yes");
        startActivity(i);
    }
    public void goToStore(){
        Intent i = new Intent(this, userStore.class);
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                //if left to right sweep event on screen
                if (x1 < x2)
                {
                    goToProductControlPage();
                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    goToSwipePage();
                }

                break;
            }
        }
        return false;
    }
    @Override
    public void onBackPressed(){
        if(! justCreated){
            super.onBackPressed();
        }
    }


}
