package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class paidBusPlanCheck extends AppCompatActivity {

    private String planName;
    private String enteredUsername;
    private String planCost;
    private String industryString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_bus_plan_check);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        planName=paramsPassed.getString("paidPlanType");
        enteredUsername = paramsPassed.getString("enteredUsername");
        industryString=paramsPassed.getString("industryString");
        if(planName.equals("Premium")){
            planCost="$69.99/ month";
        }
        else if(planName.equals("Professional")){
            planCost="$199.99/ month";
        }
        TextView mainText=(TextView)findViewById(R.id.paidPlanCheckText);
        mainText.setText("Are you sure you want to purchase the "+planName+" plan for "+planCost+" ?");
    }
    public void cancelPClicked(View view){
        Intent i = new Intent(this, businessSignupPlan.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("industryString",industryString);
        startActivity(i);
    }
    public void enterPayInfoClicked(View view){
        Intent i = new Intent(this, tryPaidPlan.class);;
        i.putExtra("paidPlanType",planName);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("industryString",industryString);
        startActivity(i);
    }

}
