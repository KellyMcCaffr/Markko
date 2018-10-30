package bob.markkouserloginsignup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class userProductControl extends Activity implements AdapterView.OnItemSelectedListener {
    float x1,x2;
    float y1, y2;
    String enteredUsername;
    Spinner menu;
    String prevLikesString;
    ArrayList prevCompNamesList;
    ArrayList currentLikesPNumList;
    //This is -1 because of indexing
    int currentOlderLikeNumber=3;
    int prevLikesSize;
    int prevNumLikes;
    private userAccountDatabaseManager usersDB;
    private busProductsInformationDatabaseManager busInfoDB;
    private productImagesDatabaseManager prodimDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        setContentView(R.layout.activity_user_product_control);
        manageSpinner();
        usersDB=new userAccountDatabaseManager(this,null,null,1);
        busInfoDB=new busProductsInformationDatabaseManager(this,null,null,1);
        prodimDB=new productImagesDatabaseManager(this,null,null,1);
        prevLikesString=usersDB.getPreviousLikes(enteredUsername);
        prevLikesSize=prevLikesString.length();
        String[] splt=prevLikesString.split("#");
        prevNumLikes=splt.length;
        loadInNewLikes();
        loadOldLike();
    }

    public void moreInfo(View view){
        ImageView nLImView1=(ImageView)findViewById(R.id.NL1);
        ImageView nLImView2=(ImageView)findViewById(R.id.NL2);
        String productNumber;
        String companyName;
        if(view==nLImView1){
            productNumber=(String)currentLikesPNumList.get(0);
            companyName=(String)prevCompNamesList.get(0);
        }
        else if(view==nLImView2){
            productNumber=(String)currentLikesPNumList.get(1);
            companyName=(String)prevCompNamesList.get(1);
        }
        else{
            productNumber=(String)currentLikesPNumList.get(2);
            companyName=(String)prevCompNamesList.get(2);
        }
        String productName=busInfoDB.getProductNameByIdentifier(companyName,productNumber);
        Intent i = new Intent(this, swipeProductInfo.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("prodNumber",productNumber);
        i.putExtra("prodName",productName);
        i.putExtra("compName",companyName);
        startActivity(i);
    }


    //What happens when an arrow is clicked(change, uses reflexion!)
    public void arrowClickedUPC(View v){
        ImageView upArrView=(ImageView)findViewById(R.id.upArr);
        ImageView downArrView=(ImageView)findViewById(R.id.downArr);
        if(v==upArrView && currentOlderLikeNumber>1){
            currentOlderLikeNumber-=1;
        }
        else if(v==downArrView && currentOlderLikeNumber < (prevNumLikes-1)){
            currentOlderLikeNumber+=1;
        }
        loadOldLike();
    }


    private ArrayList getLikeFromPrevList(int index){
        int prodCount=0;
        char currentChar;
        int charCount=0;
        String finalProduct="";
        String finalBusiness="";
        boolean onBusiness=false;
        while(prodCount<=index && charCount<prevLikesSize){
            currentChar=prevLikesString.charAt(charCount);
            if(currentChar=='#'){
                onBusiness=true;
            }
            else if(currentChar=='%'){
                prodCount++;
                onBusiness=false;
            }
            else if(prodCount==index){
                if(!onBusiness){
                    finalProduct+=currentChar;
                }
                else{
                    finalBusiness+=currentChar;
                }
            }

            charCount++;

        }
        ArrayList<String> finals= new ArrayList<>();
        finals.add(finalProduct);
        finals.add(finalBusiness);
        return finals;
    }

    private void loadOldLike(){
        ArrayList oldLike=getLikeFromPrevList(currentOlderLikeNumber);
        String oldProdName= (String)oldLike.get(0);
        String oldBusName= (String)oldLike.get(1);
        String oldProdNum=busInfoDB.getProductIdentifierByName(oldBusName,oldProdName);
        TextView oldLikeNameViewBox=(TextView)findViewById(R.id.PN4);

        ImageView oldImViewBox=(ImageView)findViewById(R.id.OL);
        oldLikeNameViewBox.setText(oldProdName);
        byte[] oldIm=prodimDB.getImage(oldBusName,oldProdNum,1);
        if (oldIm.length != 0) {
            Bitmap im1AsBitmap = BitmapFactory.decodeByteArray(oldIm, 0, oldIm.length);
            Drawable oldImAsDrawable = new BitmapDrawable(getResources(), im1AsBitmap);
            oldImViewBox.setImageDrawable(oldImAsDrawable);
        }
    }

    private void loadInNewLikes(){
        //Assumes that three likes will be represented as new max

        ImageView nLImView1=(ImageView)findViewById(R.id.NL1);
        ImageView nLImView2=(ImageView)findViewById(R.id.NL2);
        ImageView nLImView3=(ImageView)findViewById(R.id.NL3);

        TextView prevNameBox1=(TextView)findViewById(R.id.PN1);
        TextView prevNameBox2=(TextView)findViewById(R.id.PN2);
        TextView prevNameBox3=(TextView)findViewById(R.id.PN3);
        ArrayList prevLike1=getLikeFromPrevList(1);
        ArrayList prevLike2=getLikeFromPrevList(2);
        ArrayList prevLike3=getLikeFromPrevList(3);


        String prevProdName1= (String)prevLike1.get(0);
        String prevBusName1= (String)prevLike1.get(1);
        String prevProdNum1=busInfoDB.getProductIdentifierByName(prevBusName1,prevProdName1);

        String prevProdName2= (String)prevLike2.get(0);
        String prevBusName2= (String)prevLike2.get(1);
        String prevProdNum2=busInfoDB.getProductIdentifierByName(prevBusName2,prevProdName2);

        String prevProdName3= (String)prevLike3.get(0);
        String prevBusName3= (String)prevLike3.get(1);
        String prevProdNum3=busInfoDB.getProductIdentifierByName(prevBusName3,prevProdName3);

        prevNameBox1.setText(prevProdName1);
        prevNameBox2.setText(prevProdName2);
        prevNameBox3.setText(prevProdName3);

        prevCompNamesList=new ArrayList<String>();
        prevCompNamesList.add(prevBusName1);
        prevCompNamesList.add(prevBusName2);
        prevCompNamesList.add(prevBusName3);


        currentLikesPNumList =new <String>ArrayList();
        currentLikesPNumList.add(prevProdNum1);
        currentLikesPNumList.add(prevProdNum2);
        currentLikesPNumList.add(prevProdNum3);

        byte[] im1=prodimDB.getImage(prevBusName1,prevProdNum1,1);
        byte[] im2=prodimDB.getImage(prevBusName2,prevProdNum2,1);
        byte[] im3=prodimDB.getImage(prevBusName3,prevProdNum3,1);

        if (im1.length != 0) {
            Bitmap im1AsBitmap = BitmapFactory.decodeByteArray(im1, 0, im1.length);
            Drawable im1AsDrawable = new BitmapDrawable(getResources(), im1AsBitmap);
            nLImView1.setImageDrawable(im1AsDrawable);
        }

        if (im2.length != 0) {
            Bitmap im2AsBitmap = BitmapFactory.decodeByteArray(im2, 0, im2.length);
            Drawable im2AsDrawable = new BitmapDrawable(getResources(), im2AsBitmap);
            nLImView2.setImageDrawable(im2AsDrawable);
        }
        if (im3.length != 0) {
            Bitmap im3AsBitmap = BitmapFactory.decodeByteArray(im3, 0, im3.length);
            Drawable im3AsDrawable = new BitmapDrawable(getResources(), im3AsBitmap);
            nLImView3.setImageDrawable(im3AsDrawable);
        }

    }
    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        menu= (Spinner) findViewById(R.id.prodControlPageUserMenu);
        //Allows us to respond to user selections
        menu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.user_menu_prod_control,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        menu.setAdapter(adapter);
        menu.setBackgroundResource(R.drawable.swipesettingsbutton);
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
    public void goToPaymentInfoPage(){
        Intent i = new Intent(this, businessPaymentInfoPage.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("isUser","yes");
        startActivity(i);
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

                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    goToMainUserPage();
                }

                break;
            }
        }
        return false;
    }
}
