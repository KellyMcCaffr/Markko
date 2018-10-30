package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class signUpChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_choice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    //Moves app to the signup screen when sign up text is clicked.
    public void goToUserSignup(View view){
        Intent i = new Intent(this, userSignup.class);
        startActivity(i);
    }
    //Moves app to the signup screen when sign up text is clicked.
    public void goToBusinessSignup(View view){
        Intent i = new Intent(this, businessSignup.class);
        startActivity(i);
    }

}
