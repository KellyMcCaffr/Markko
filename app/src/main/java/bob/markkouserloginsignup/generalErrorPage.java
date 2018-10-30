package bob.markkouserloginsignup;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class generalErrorPage extends Activity {

    private String error="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Actually in gep","True");
        setContentView(R.layout.activity_general_error_page);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        error = paramsPassed.getString("errorMessage");
        displayError();
    }
    public void displayError(){
        TextView errorView=(TextView)findViewById(R.id.errorMainTView);
        errorView.setText(error);
    }

}
