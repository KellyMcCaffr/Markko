package bob.markkouserloginsignup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class viewDBs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dbs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    //Takes the (tester) to a page showing a visual representation of the database of non-business accounts.
    public void viewAccountsDatabase(View view){
        Intent i = new Intent(this, viewAccountsDatabase.class);
        startActivity(i);
    }
    //Takes the (tester) to a page showing a visual representation of the database of businesses.
    public void viewBusinessDatabase(View view){
        Intent i = new Intent(this, viewBusinessesDatabase.class);
        startActivity(i);
    }
    public void viewBusinessFinancesDatabase(View view){
        Intent i = new Intent(this, viewBusinessFinanceDatabase.class);
        startActivity(i);
    }
    public void viewBusinessProductsDatabase(View view){
        Intent i = new Intent(this, viewProdDatabase1.class);
        startActivity(i);
    }


}
