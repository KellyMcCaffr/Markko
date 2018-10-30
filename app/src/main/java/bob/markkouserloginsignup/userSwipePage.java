package bob.markkouserloginsignup;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class userSwipePage extends Activity implements OnItemSelectedListener  {

    String enteredUsername;
    Spinner menu;
    private float x1,y1,x2,y2;
    private int totNumProds;
    //Exterior mainframes:
    private userAccountDatabaseManager usersDB;
    private busProductsInformationDatabaseManager prodInfoDB;
    private productImagesDatabaseManager prodImagDB;
    private businessAccountsDatabaseManager busAccountsDB;

    //Deals with ImageViews specifically:
    private Drawable justLikedImage;               //The image we set our views to for our like threads
    private Drawable justDislikedImage;            //The image we set our views to for our dislike threads
    //Deal with like and dislike data:
    private ArrayList<String> loadedBusNames;      //The full list of filtered unused business names
    private ArrayList<String> loadedProdNames;     //The full list of filtered unused product names
    private ArrayList<ArrayList> finishedProds;    //Nested list of [[busName, prodName],...] which helps filter.
    private ArrayList<Integer> imViewIdList;       //The ids of imageViews 1-10
    private ArrayList<Integer> plusViewIdList;      //The ids of more info views 1-10
    private ArrayList<Integer> likeArrowViewIdList;//The ids of likeArrowViews 1-10
    private ArrayList<Integer> dislikeArrowViewIdList; //The ids of dislikeArrowViews 1-10
    private ArrayList<ArrayList> usedProdInfo;     //Nested list of [[busName, prodName, imageArrayList],...] for prods in each box
    private ArrayList<ArrayList> allNestedViewIds; //Nested list of [[topTextViewID, imViewID, descripViewID, dislViewID, likeViewID]] for each box
    private ArrayList<Boolean> likeCoverBoolList;  //Holds booleans of each box for rather the just liked/disliked image is curr shown. Prevents clicking again before other prod has loaded.
    private ArrayList<Integer> imageCountList;     //Holds the counts of each image currently displayed for all boxes 0-5(?)
    private ArrayList<Timer>   timerList;          //Holds one timer for each box so that we can have a max of 10 different threads
    private int totNumBusinesses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle paramsPassed = new Bundle();
        paramsPassed = getIntent().getExtras();
        enteredUsername = paramsPassed.getString("enteredUsername");
        setContentView(R.layout.activity_user_swipe_page);
        manageSpinner();
        Log.d("SWIPECREATE", "1");
        initializeClassVariables();
        Log.d("SWIPECREATE", "2");
        getNewProducts();
        Log.d("SWIPECREATE", "3");
        //Loads info into boxes:
        for(int i=0;i<10;i++){
            loadProd(i);
        }
        Log.d("SWIPECREATE", "4");
        fitImageViewsToScreen();
        //Sets on swipe listeners for each of the imageViews:
    }

    private void fitImageViewsToScreen(){
        //Fits all product views to the screen so there is no blank space after
        //each image.
        ImageView cView;
        for(int id:imViewIdList){
            cView=(ImageView)findViewById(id);
            cView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
    private void initializeClassVariables(){
        usersDB = new userAccountDatabaseManager(this, null, null, 1);
        busAccountsDB = new businessAccountsDatabaseManager(this, null, null, 1);
        prodImagDB = new productImagesDatabaseManager(this, null, null, 1);
        prodInfoDB = new busProductsInformationDatabaseManager(this, null, null, 1);
        loadedBusNames=new ArrayList<>();
        loadedProdNames=new ArrayList<>();
        //Add likes and dislikes from previous user sessions to finished prods:
        finishedProds=usersDB.getPreviousUPIAsArrayList(enteredUsername, false);
        finishedProds.addAll(usersDB.getPreviousUPIAsArrayList(enteredUsername,true));
        Log.d("INITIALIZE", "Above prev likes output");
        Log.d("INITIALIZE", "Output of prev session likes: "+finishedProds);
        imViewIdList=new ArrayList<>(
                Arrays.asList(R.id.showResultView,R.id.showResultView2,R.id.showResultView3,R.id.showResultView4
                ,R.id.showResultView5,R.id.showResultView6,R.id.showResultView7,R.id.showResultView8,
                        R.id.showResultView9,R.id.showResultView10));
        likeArrowViewIdList=new ArrayList<Integer>(Arrays.asList(R.id.uLike1,R.id.uLike2,R.id.uLike3,R.id.uLike4,R.id.uLike5,R.id.uLike6,R.id.uLike7,
                R.id.uLike8,R.id.uLike9,R.id.uLike10));
        dislikeArrowViewIdList=new ArrayList<Integer>(Arrays.asList(R.id.dLike1,R.id.dLike2,R.id.dLike3,R.id.dLike4,
                R.id.dLike5,R.id.dLike6,R.id.dLike7,R.id.dLike8,R.id.dLike9,R.id.dLike10));
        plusViewIdList=new ArrayList<Integer>(Arrays.asList(R.id.moreInfBox1,R.id.moreInfBox2,R.id.moreInfBox3,
                    R.id.moreInfBox4,R.id.moreInfBox5,R.id.moreInfBox6, R.id.moreInfBox7,R.id.moreInfBox8,R.id.moreInfBox9,
                    R.id.moreInfBox10));
        usedProdInfo=new ArrayList<>();
        allNestedViewIds=new ArrayList<>();
        timerList=new ArrayList<Timer>();
        imageCountList=new ArrayList<Integer>();
        likeCoverBoolList=new ArrayList<Boolean>();
        //Nested list of [[topTextViewID, imViewID, descripViewID, dislViewID, likeViewID]]
        allNestedViewIds=new ArrayList<ArrayList>(Arrays.asList(
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox1,R.id.showResultView,R.id.prodDescBox1,R.id.dLike1,R.id.uLike1)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox2,R.id.showResultView2,R.id.prodDescBox2,R.id.dLike2,R.id.uLike2)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox3,R.id.showResultView3,R.id.prodDescBox3,R.id.dLike3,R.id.uLike3)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox4,R.id.showResultView4,R.id.prodDescBox4,R.id.dLike4,R.id.uLike4)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox5,R.id.showResultView5,R.id.prodDescBox5,R.id.dLike5,R.id.uLike5)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox6,R.id.showResultView6,R.id.prodDescBox6,R.id.dLike6,R.id.uLike6)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox7,R.id.showResultView7,R.id.prodDescBox7,R.id.dLike7,R.id.uLike7)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox8,R.id.showResultView8,R.id.prodDescBox8,R.id.dLike8,R.id.uLike8)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox9,R.id.showResultView9,R.id.prodDescBox9,R.id.dLike9,R.id.uLike9)),
                new ArrayList<Object>(Arrays.asList(R.id.prodExpBox10,R.id.showResultView10,R.id.prodDescBox10,R.id.dLike10,R.id.uLike10))
        ));
        //To do:
        //make usedProdInfo with full image lists,
        //Should copies to the necessary lists:
        for(int i=0;i<10;i++) {
            timerList.add(new Timer());
            imageCountList.add(0);
            likeCoverBoolList.add(false);
            //To prevent from being null later on:
            usedProdInfo.add(new ArrayList(Arrays.asList(null,null,null)));
        }

        justLikedImage=getDrawable(R.drawable.liked_im1);
        justDislikedImage=getDrawable(R.drawable.disliked_im1);
        totNumBusinesses=prodInfoDB.getNumBus();
        totNumProds=prodInfoDB.getTotalNumProds();
        //Set swipe listeners for each of the image views:
        View cV;
        for(int viewID: imViewIdList){
            cV=findViewById(viewID);
            cV.setOnTouchListener(onThumbTouch);
        }
    }
    //Adds the view cover:
    private void setLikeThread(int boxIndex,Drawable initialImage){
        //Sets the appropriate image view to our like image:
        ImageView v=findViewById(imViewIdList.get(boxIndex));
        v.setImageDrawable(initialImage);
        //Sets the appropriate show boolean to true:
        likeCoverBoolList.set(boxIndex, true);
        //Start the thread with an appropriate time:
        TimerTask updateProd = new imageCoverThread(boxIndex);
        Timer threadTimer=timerList.get(boxIndex);
        threadTimer.schedule(updateProd, 500);
    }
    //Removes the view cover:
    private void endLikeThread(int boxIndex){
        //Replace like cover with appropriate image for appropriate view:
        ImageView v=findViewById(imViewIdList.get(boxIndex));
        ArrayList<Drawable>boxImageList=(ArrayList)usedProdInfo.get(boxIndex).get(2);
        Drawable fImage=boxImageList.get(0);
        v.setImageDrawable(fImage);
        //Set the appropriate likeCover boolean to false:
        likeCoverBoolList.set(boxIndex, false);
        //End the thread:
        Timer threadTimer=timerList.get(boxIndex);
        threadTimer.cancel();
        threadTimer.purge();
        timerList.set(boxIndex, new Timer());
    }
    //Restocks our lists of loaded products:
    private void getNewProducts(){
        //Log.d("GETNEWPRODUCTS", "Attempting get fifty");
        //Loads unused product names from 50 different businesses:
        ArrayList<ArrayList> newProdBusPairs=prodInfoDB.getFiftyNewUniqueProds(finishedProds);
        //Log.d("GETNEWPRODUCTS", "Fifty prod: "+newProdBusPairs);
        //Marks the loaded products as finished:
        finishedProds.addAll(newProdBusPairs);
        //Sets loadedBusNames and loadedProdNames:
        loadedBusNames=new ArrayList<String>();
        loadedProdNames=new ArrayList<String>();
        for(ArrayList<String> set:newProdBusPairs){
            loadedBusNames.add(set.get(0));
            loadedProdNames.add(set.get(1));
        }
    }
    //Loads the next product info and images into an available box
    private void loadProd(int boxIndex){
        //Gets new products if necessary:
        if(loadedBusNames.size()==0){
            getNewProducts();
        }
        //If we have products in DB:
        if(loadedBusNames.size()>0) {
            //Pops from loadedBusNames and loadedProdNames:
            String newBusName = loadedBusNames.get(0);
            loadedBusNames.remove(0);
            String newProdName = loadedProdNames.get(0);
            loadedProdNames.remove(0);
            //Retrieves and assigns all views of the appropriate box:
            ArrayList boxViews = allNestedViewIds.get(boxIndex);
            TextView topTextView = (TextView) findViewById((int)boxViews.get(0));
            ImageView imView = (ImageView) findViewById((int)(boxViews.get(1)));
            TextView descripView = (TextView) findViewById((int)(boxViews.get(2)));
            //Collects and assigns appropriate images as byte[]:
            ArrayList<byte[]> pBytes = prodImagDB.getAllDBImages(newBusName, prodInfoDB.getProductIdentifierByName(newBusName,newProdName));
            ArrayList<Drawable> pDrawables = new ArrayList<>();
            //Converts images from byte[] to drawables:
            for (byte[] b : pBytes) {
                pDrawables.add(byteToDrawable(b));
            }
            Log.d("LOADPROD", "Num drawables for index "+boxIndex+": "+pDrawables.size());
            Log.d("LOADPROD", "B name attempted: "+newBusName+"P name attempted: "+newProdName);
            //Adds all information to the appropriate views:
            topTextView.setText(newBusName+": "+newProdName);
            if(pDrawables.size()>0) {
                imView.setImageDrawable(pDrawables.get(0));
            }
            String productDescription=prodInfoDB.getProductDescriptionByIdentifier(newBusName,prodInfoDB.getProductIdentifierByName(newBusName,newProdName));
             if(productDescription.length()>0) {
                 descripView.setText("\n" + productDescription + "\n");
             }
            //Set the imageCount to 0 for the appropriate box:
            imageCountList.set(boxIndex,0);
            //Set bus, prod images pair in usedProdInfo:
            ArrayList currBoxSet=usedProdInfo.get(boxIndex);
            currBoxSet.set(0, newBusName);
            currBoxSet.set(1, newProdName);
            currBoxSet.set(2, pDrawables);
            //Update list of usedProductInfo:
            //Nested list of [[busName, prodName, imageArrayList],...] for prods in each box
            ArrayList newInfo=new ArrayList(Arrays.asList(newBusName,newProdName,pDrawables));
            usedProdInfo.set(boxIndex,newInfo);
        }
        //Clear the box if there are no new products available:
        else{
            //Nested list of [[topTextViewID, imViewID, descripViewID, dislViewID, likeViewID]]
            ArrayList stuff=allNestedViewIds.get(boxIndex);
            TextView topText=findViewById((int)stuff.get(0));
            ImageView imView=findViewById((int)stuff.get(1));
            TextView descrip=findViewById((int)stuff.get(2));
            topText.setText("No items available!");
            topText.setTextColor(Color.RED);
            imView.setVisibility(INVISIBLE);
            descrip.setText("");
        }
    }
    //Responds to a press of the like arrow at some product box:
    public void onLike(View v){
        //Retrieves appropriate boxIndex:
        int boxIndex=0;
        View testV;
        for(int id: likeArrowViewIdList){
            testV=findViewById(id);
            if(testV==v){
                break;
            }
            boxIndex+=1;
        }
        //Only execute if no likes image is shown:
        boolean lImageShown=likeCoverBoolList.get(boxIndex);
        if(!lImageShown) {
            //Retrieves and stores information from the user's account:currBoxSet.set(1, newProdName);
            ArrayList prodInfoSet = usedProdInfo.get(boxIndex);
            String shownBusName = (String) prodInfoSet.get(0);
            String shownProdName = (String) prodInfoSet.get(1);
            //Add the product to the array of finished prods for this session:
            ArrayList usedPair=new ArrayList();
            usedPair.add(shownBusName);
            usedPair.add(shownProdName);
            finishedProds.add(usedPair);
            setLikeData(shownBusName, shownProdName);
            //Loads another product into box:
            loadProd(boxIndex);
            //Increments user points:
            incUserPoints();
            //Set the appropriate likeCover boolean to true:
            likeCoverBoolList.set(boxIndex, true);
            //Get im for the newest product in box(next one):
            //prodInfoSet = usedProdInfo.get(boxIndex);
            //Start the like thread:
            //Drawable shownImage=(Drawable)prodInfoSet.get(2);
            setLikeThread(boxIndex, justLikedImage);
            finishedProds.add(usedPair);
        }
    }
    public void onDislike(View v){
        //Retrieves appropriate boxIndex:
        int boxIndex=0;
        View testV;
        for(int id: dislikeArrowViewIdList){
            testV=findViewById(id);
            if(testV==v){
                break;
            }
            boxIndex+=1;
        }
        //Only execute if no likes image is shown:
        boolean lImageShown=likeCoverBoolList.get(boxIndex);
        if(!lImageShown) {
            //Adds dislike to prodINFODB:
            ArrayList prodInfoSet = usedProdInfo.get(boxIndex);
            String shownBusName = (String) prodInfoSet.get(0);
            String shownProdName = (String) prodInfoSet.get(1);
            //Add the product to the array of finished prods for this session:
            ArrayList usedPair=new ArrayList();
            usedPair.add(shownBusName);
            usedPair.add(shownProdName);
            finishedProds.add(usedPair);
            //Also mark the interaction in users DB:
            prodInfoDB.addDislike(shownBusName, shownProdName);
            //Loads another product into box:
            loadProd(boxIndex);
            //Increments user points:
            incUserPoints();
            //Set the appropriate likeCover boolean to true:
            likeCoverBoolList.set(boxIndex, true);
            //Get im for the newest product in box(next one):
            //prodInfoSet = usedProdInfo.get(boxIndex);
            //Start the like thread with new image:
            //Drawable shownImage=(Drawable)prodInfoSet.get(2);
            setLikeThread(boxIndex, justDislikedImage); //Index, initial image, final image
            //Record the interaction in usersDB:
            usersDB.addDislike(enteredUsername,shownProdName,shownBusName);

        }
    }
    //Changes image shown when user swipes left:
    protected void onLeftSwipe(View v){
        //Get the correct box index:
        int boxIndex=0;
        View testV;
        for(int id: imViewIdList){
            testV=findViewById(id);
            if(testV==v){
                break;
            }
            boxIndex+=1;
        }
        //If showing like cover, don't respond to swipe:
        boolean onThread=likeCoverBoolList.get(boxIndex);
        if(onThread){
            Log.d("ONLEFTSWIPE", "Stopped on thread");
            return;
        }
        //Get the list of images:
        ArrayList<Drawable> mImageList=(ArrayList<Drawable>)usedProdInfo.get(boxIndex).get(2);
        //Gets the imageCount:
        int currImageCount=imageCountList.get(boxIndex);
        //Checks if the image count is greater than 0:
        if(currImageCount<=0 ){
            Log.d("ONLEFTSWIPE", "Imag count condition");
            return;
        }
        //Decrements the imageCount:
        currImageCount--;
        imageCountList.set(boxIndex,currImageCount);
        //Set the appropriate image:
        ImageView prodImView=(ImageView)findViewById(imViewIdList.get(boxIndex));
        prodImView.setImageDrawable(mImageList.get(currImageCount));

    }
    protected void onRightSwipe(View v){
        //Get the correct box index:
        int boxIndex=0;
        View testV;
        for(int id: imViewIdList){
            testV=findViewById(id);
            if(testV==v){
                break;
            }
            boxIndex+=1;
        }
        //If showing like cover, don't respond to swipe:
        boolean onThread=likeCoverBoolList.get(boxIndex);
        if(onThread){
            Log.d("RIGHTSWIPE", "Return at on thread");
            return;
        }
        //Get the list of images:
        ArrayList<Drawable> mImageList=(ArrayList<Drawable>)usedProdInfo.get(boxIndex).get(2);
        //Gets the imageCount:
        int currImageCount=imageCountList.get(boxIndex);
        //Checks if the last im has been reached:
        if(currImageCount>=(mImageList.size()-1) ){
            Log.d("RIGHTSWIPE", "Return at right max");
            Log.d("RIGHTSWIPE", "Curr image count: "+currImageCount);
            Log.d("RIGHTSWIPE", "Curr image list size: "+mImageList.size());
            return;
        }
        //Increments the imageCount:
        currImageCount++;
        Log.d("RIGHTSWIPE", "Curr image count: "+currImageCount);
        imageCountList.set(boxIndex,currImageCount);
        //Set the appropriate image:
        ImageView prodImView=(ImageView)findViewById(imViewIdList.get(boxIndex));
        prodImView.setImageDrawable(mImageList.get(currImageCount));
    }

    class imageCoverThread extends TimerTask{
        int mBoxIndex;
        imageCoverThread(int boxIndex){
            mBoxIndex=boxIndex;
        }
        //Removes like or dislike im for a product, then loads new info to that view.
        public void run(){
            runOnUiThread(new Runnable() {
                public void run () {
                    endLikeThread(mBoxIndex);
                }
            });
        }
    }

    private void setLikeData(String busName, String prodName){
        usersDB.addLike(enteredUsername, prodName, busName);
        String userRace = usersDB.getRace(enteredUsername);
        String userReligion = usersDB.getReligion(enteredUsername);
        String userAge = usersDB.getAge(enteredUsername);
        String userEducation = usersDB.getEducation(enteredUsername);
        String userGender = usersDB.getGender(enteredUsername);
        prodInfoDB.addEthnicity(busName, prodName, userRace);
        prodInfoDB.addReligion(busName, prodName, userReligion);
        prodInfoDB.addAge(busName, prodName, userAge);
        prodInfoDB.addEducation(busName, prodName, userEducation);
        prodInfoDB.addGender(busName, prodName, userGender);
        prodInfoDB.addLike(busName, prodName);
    }

    //Sets the attributes needed to take the user to a more detailed view of a product
    //Need a global variable with current product name to access???
    public void moreProdInfo(View v){
        //Get the box index:
        int boxIndex=0;
        View testV;
        for(int id: plusViewIdList){
            testV=findViewById(id);
            if(testV==v){
                break;
            }
            boxIndex+=1;
        }

        //Get company and product names:
        String currentCompanyName;
        String currentProductName;
        //Nested list of [[busName, prodName, imageArrayList],...]
        currentCompanyName = (String)usedProdInfo.get(boxIndex).get(0);
        currentProductName = (String)usedProdInfo.get(boxIndex).get(1);

        //Execute the intent:
        Intent i = new Intent(this, swipeProductInfo.class);
        i.putExtra("enteredUsername",enteredUsername);
        i.putExtra("compName",currentCompanyName);
        i.putExtra("prodName",currentProductName);
        i.putExtra("prodNumber",prodInfoDB.getProductIdentifierByName(currentCompanyName,currentProductName));
        startActivity(i);
    }
    private Drawable byteToDrawable(byte[] imAsBytes){
        if (imAsBytes.length != 0) {
            Bitmap im1AsBitmap = BitmapFactory.decodeByteArray(imAsBytes, 0, imAsBytes.length);
            Drawable im1AsDrawable = new BitmapDrawable(getResources(), im1AsBitmap);
            return im1AsDrawable;
        }
        return null;
    }
    private ArrayList<String> filterBusForProds(ArrayList<String> busNameList){
        ArrayList<String> newList=new ArrayList<>();
        for(String name:busNameList){
            if(prodInfoDB.getAllBusPNames(name).size()!=0){
                newList.add(name);
            }
        }
        return newList;
    }

    /**Creates a 20 percent chance of a one point increase for every
     * 5 swipes
     **/
    private void incUserPoints(){
        Log.d("INC USER POINTS", "Point increment called");
        Log.d("INC USER POINTS", "Username: "+enteredUsername);
        usersDB.incrementPoints(enteredUsername,1);

    }

    public void manageSpinner() {
        //Manages the spinner(menu) for this activity
        menu= (Spinner) findViewById(R.id.swipePageUserMenu);
        //Allows us to respond to user selections
        menu.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this, R.array.user_menu_swipe,
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
    public void goToManageAccountPage(){
        Intent i = new Intent(this, manageUserAccount.class);
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

    View.OnTouchListener onThumbTouch = new View.OnTouchListener() {
        float previouspoint = 0 ;
        float startPoint=0;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            startPoint=event.getX();
                            System.out.println("Action down,..."+event.getX());
                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            previouspoint=event.getX();
                            if(previouspoint > startPoint){
                                Log.d("TOUCHLISTENER", "Right swipe");
                                onRightSwipe(v);
                            }else{
                                Log.d("TOUCHLISTENER", "Left swipe");
                                onLeftSwipe(v);
                            }
            }
            return true;
        }
    };
}
