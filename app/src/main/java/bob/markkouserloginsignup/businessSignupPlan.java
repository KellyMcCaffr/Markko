package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class businessSignupPlan extends Activity {

    businessAccountsDatabaseManager businessDatabaseChanger;
    String enteredUsername;
    String industryString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup2);

        businessDatabaseChanger=new businessAccountsDatabaseManager(this,null,null,1);

        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        industryString = paramsPassed.getString("industryString");

    }
    public void goToMainBusinessPage(){
        Intent i = new Intent(this, mainBusinessPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("paidPlanType","Premium");
        i.putExtra("justCreated",true);
        startActivity(i);
    }
    public void tryBasic(View view){
        //Already has variables from last page because they are static
        businessAccountDatabaseProducts newBusinessAccountObject=new businessAccountDatabaseProducts();
        newBusinessAccountObject.set_plan("A");
        businessDatabaseChanger.addAccount(newBusinessAccountObject,industryString);
        goToMainBusinessPage();
    }
    public void tryPremium(View view){
        Intent i = new Intent(this, paidBusPlanCheck.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("paidPlanType","Premium");
        i.putExtra("industryString",industryString);
        startActivity(i);
    }
    public void tryProfessional(View view){
        Intent i = new Intent(this,paidBusPlanCheck.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("paidPlanType","Professional");
        i.putExtra("industryString",industryString);
        startActivity(i);
    }

}
