package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class checkUserDelete extends Activity {
    String enteredUsername;
    userAccountDatabaseManager usersDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_delete);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        usersDB=new userAccountDatabaseManager(this,null,null,1);

    }
    public void deleteUAccount(View view){
        usersDB.deleteAccount(enteredUsername);
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
    public void cancelUDelete(View view){
        Intent i = new Intent(this,manageUserAccount.class);
        //Simply returns to the account info page
        i.putExtra("enteredUsername",enteredUsername);
        startActivity(i);
    }

}
