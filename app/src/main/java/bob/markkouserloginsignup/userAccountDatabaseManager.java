package bob.markkouserloginsignup;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
/*
Works directly with the database for non-business users account
information.
 */

public class userAccountDatabaseManager extends SQLiteOpenHelper {

    private static final int DARABASE_VERSION=1;
    private static final String DATABASE_NAME="accounts.db";
    public static final String TABLE_PRODUCTS="accounts";

    //Names of columns in this database:
    public static final String COLUMN_ID="_id";
    public static final String USERNAME="username";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String POINTS="points";

    public static final String GENDER="gender";
    public static final String AGE="age";
    public static final String RACE="race";
    public static final String RELIGION="religion";
    public static final String EDUCATIONLEVEL="educationLevel";

    public static final String INDUSTRIES="industries";

    //Currently, this is in the form of previous product names separated by
    //% followed by # and business name. This allows easier access between DBs.
    public static final String PREVIOUSLIKES="previouslikes";
    //% followed by # and business name. This allows easier access between DBs.
    public static final String PREVIOUSDISLIKES="previousdislikes";//Needed to remember which prods users have interacted with

    public userAccountDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DARABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the table
        String newQuery="CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT ," +
                EMAIL + " TEXT ," +
                PASSWORD + " TEXT ," +
                POINTS + " INTEGER ," +
                GENDER + " TEXT ," +
                AGE + " TEXT ," +
                RACE + " TEXT ," +
                RELIGION + " TEXT ," +
                EDUCATIONLEVEL + " TEXT ," +
                INDUSTRIES + " TEXT ," +
                PREVIOUSLIKES + " TEXT ," +
                PREVIOUSDISLIKES +
                ");";
        //Executes the query to create the table
        sqLiteDatabase.execSQL(newQuery);
    }

    //When structure(?) is changed, delete old table and create a new one
    //with that structure.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }


    public void addAccount(userAccountDatabaseProducts account,String selectedIndustriesOfInterest){
        ContentValues values=new ContentValues();
        //values.put(COLUMN_ID,account.get_id());
        values.put(USERNAME,account.get_name());
        values.put(EMAIL,account.get_email());
        values.put(PASSWORD,account.get_password());
        values.put(POINTS,0);

        values.put(GENDER,account.get_gender());
        values.put(AGE,account.get_age());
        values.put(RACE,account.get_race());
        values.put(RELIGION,account.get_religion());
        values.put(EDUCATIONLEVEL,account.get_educationLevel());

        values.put(INDUSTRIES,selectedIndustriesOfInterest);
        //Made up of unique product ids
        values.put(PREVIOUSLIKES,"");

        //Get/s the database that we are using
        SQLiteDatabase db=getWritableDatabase();
        //Inserts the new column into the database
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }

    public void deleteAccount(String userName){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "username = ?" , new String[]{userName});
    }
    //Builds the disliked prods dataset for tracking user interactions:
    public void addDislike(String enteredUsername,String prodName,String busName){
        String oldDislikes=getPreviousDislikes(enteredUsername);
        //oldLikes+="%"+prodName+"#" + busName;
        //Add the new like at the beginning of the likes string
        oldDislikes="%"+prodName+"#" + busName+oldDislikes;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET PREVIOUSLIKES = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(PREVIOUSDISLIKES, oldDislikes);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{enteredUsername});
    }
    public void addLike(String enteredUsername, String prodName, String busName){

        String oldLikes=getPreviousLikes(enteredUsername);
        //oldLikes+="%"+prodName+"#" + busName;
        //Add the new like at the beginning of the likes string
        oldLikes="%"+prodName+"#" + busName+oldLikes;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET PREVIOUSLIKES = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(PREVIOUSLIKES, oldLikes);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{enteredUsername});
    }

    protected void incrementPoints(String enteredUsername,int numIncrease){
        int currentPoints=getPoints(enteredUsername);
        currentPoints+=numIncrease;

        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET POINTS = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(POINTS, currentPoints);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{enteredUsername});

    }
    public int getPoints(String enteredUsername){

        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("points"))!=null) {
                return c.getInt(c.getColumnIndex("points"));
            }
        }
        return -1;
    }
    public String getPreviousLikes(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("previouslikes"))!=null) {
                return c.getString(c.getColumnIndex("previouslikes"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getPreviousDislikes(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE  USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("previousdislikes"))!=null) {
                return c.getString(c.getColumnIndex("previousdislikes"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public ArrayList<ArrayList> getPreviousUPIAsArrayList(String enteredUsername, boolean getDislikes){
        ArrayList finalLikes=new ArrayList<ArrayList>();
        String prevLikesAsString;
        if(getDislikes){
            prevLikesAsString=this.getPreviousDislikes(enteredUsername);
        }
        else {
            prevLikesAsString=this.getPreviousLikes(enteredUsername);
        }
        Log.d("GETPREVLASLIST", "Prev l as string: "+prevLikesAsString);
        String currPName="";
        String currBName="";
        boolean onProd=false;
        boolean onName=false;
        boolean onFirst=true;
        ArrayList currPair=new ArrayList();
        int totLength=prevLikesAsString.length();
        int count=0;
        for(char c:prevLikesAsString.toCharArray()){
            //New prod name:
            if(c=='%'){
                onProd=true;
                onName=false;
                if(onFirst){
                    onFirst=false;
                }
                //Move onto the next pair
                else {
                    currPair.add(currBName);
                    currBName="";
                    currPair.add(currPName);
                    currPName="";
                    finalLikes.add(currPair);
                    currPair=new ArrayList();
                }
            }
            else if(count==(totLength-1)){
                currBName+=c;
                currPair.add(currBName);
                currPair.add(currPName);
                finalLikes.add(currPair);
                currPair=new ArrayList();
            }
            //New bus name:
            else if(c=='#'){
                onName=true;
                onProd=false;
            }
            else if(onName){
                currBName+=c;
            }
            else if(onProd){
                currPName+=c;
            }
            count++;
        }
        Log.d("GETPREVUPI", "Final likes: "+finalLikes);
        return finalLikes;
        //Prev likes rep:
        //oldLikes="%"+prodName+"#" + busName+oldLikes;
        //Finished prods in user swipe:
        //Nested list of [[busName, prodName],...] which helps filter.
    }
    private ArrayList<ArrayList> getAllPreviousLikes(){
        ArrayList namesAndLLists=new ArrayList();
        ArrayList holder=new ArrayList();
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS;
        Cursor c=db.rawQuery(newQuery,new String[]{});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("previouslikes"))!=null) {
                //Create a nested array with name, then prev likes string
                holder.add(c.getString(c.getColumnIndex("username")));
                holder.add(c.getString(c.getColumnIndex("previouslikes")));
                namesAndLLists.add(holder);
            }
            c.moveToNext();
        }
        c.close();
        return namesAndLLists;

    }

    public String getRace(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("race"))!=null) {
                return c.getString(c.getColumnIndex("race"));
            }
        }
        return "";
    }
    public String getReligion(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("religion"))!=null) {
                return c.getString(c.getColumnIndex("religion"));
            }
        }
        return "";
    }
    public String getAge(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("age"))!=null) {
                return c.getString(c.getColumnIndex("age"));
            }
        }
        return "";
    }
    public String getEducation(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("educationLevel"))!=null) {
                return c.getString(c.getColumnIndex("educationLevel"));
            }
        }
        return "";
    }
    public String getGender(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("gender"))!=null) {
                return c.getString(c.getColumnIndex("gender"));
            }
        }
        return "";
    }
    public String getPassword(String enteredUsername){

        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("password"))!=null) {
                return c.getString(c.getColumnIndex("password"));
            }
        }
        return "";
    }
    public String getEmail(String enteredUsername){

        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("email"))!=null) {
                return c.getString(c.getColumnIndex("email"));
            }
        }
        return "";
    }
    //Delete the business identifier from the likes String, done when a bus account is deleted
    private String deleteBusIdentifierPInLString(String oldLikes, String oldName){
        Log.d("Old likes:", oldLikes);
        //Make the replacement:
        oldLikes.replace('#'+oldName, "$"+"$");
        char[] likesAsChar=oldLikes.toCharArray();
        //Remove all with $$ pairs
        String finalLikes="";
        String likedProd="";
        boolean onFirst=true;
        char previousChar='#';
        int count=1;
        for(char c: likesAsChar){
            if(onFirst){
                previousChar=c;
                likedProd+=c;
                onFirst=false;
            }
            else if(count>=likesAsChar.length) {
                finalLikes+=likedProd+c;
            }
            else if(c=='$' && previousChar=='$'){
                likedProd="";
            }
            else if(c=='%' && !onFirst){
                finalLikes+=likedProd;
                likedProd="%";
            }
            else{
                likedProd+=c;
            }
            count++;
        }
        Log.d("USERACCOUNTDBDELETELS", "Final likes length: "+finalLikes.length());
        return finalLikes;
    }
    private String deleteBusProductX(String oldLikes, String oldName, String busIdentifier){
        Log.d("USERACCOUNTDB", "Del product x");
        Log.d("In dele bus prod f l:", "true");
        Log.d("Old Likes:", oldLikes);
        Log.d("New Likes aft rep:", oldLikes.replace('%'+oldName+'#'+busIdentifier,""));
        return oldLikes.replace('%'+oldName+'#'+busIdentifier,"");
    }
    //Replaces the old product with a new product.
    private String alterProdNameInLString(String oldLikes, String oldName, String newName, String busIdentifier){
        Log.d("Old likes:", oldLikes);
        Log.d("New likes:", oldLikes.replace('%'+oldName+'#'+busIdentifier, '%'+newName+'#'+busIdentifier));
        return oldLikes.replace('%'+oldName+'#'+busIdentifier, '%'+newName+'#'+busIdentifier);
    }
    //Update all products with 1 business name to another
    private String alterBusIdentifierInLString(String oldLikes, String oldName, String newName){
        Log.d("Old likes:", oldLikes);
        Log.d("New likes:", oldLikes.replace(oldName, newName));
        return oldLikes.replace(oldName, newName);
    }
    //False for change name, true for delete product string rep
    private ArrayList<ArrayList> updateStringsToNewName(ArrayList<ArrayList> oldLikesArray,String oldBusName,String newBusName, boolean typeBoolean) {
        ArrayList<ArrayList> newLikesSet=new ArrayList<>();
        ArrayList<String> currNestedInfo=new ArrayList<>();
        String currName;
        String currLikesList;
        for(ArrayList<String> n:oldLikesArray){
            currName=n.get(0);
            currLikesList=n.get(1);
            if(! typeBoolean) {
                currLikesList = alterBusIdentifierInLString(currLikesList, oldBusName, newBusName);
            }
            else{
                currLikesList=deleteBusIdentifierPInLString(currLikesList, oldBusName);
            }

            currNestedInfo.add(currName);
            currNestedInfo.add(currLikesList);

            newLikesSet.add(currNestedInfo);

            currNestedInfo=new ArrayList<>();
            Log.d("Curr name in iter:",""+newLikesSet);

        }
        return newLikesSet;
    }

    //False for change name, true for delete product string rep
    private ArrayList<ArrayList> updateProductsToNewNameInLList(ArrayList<ArrayList> oldLikesArray,String oldProdName,String newProdName, String busIdentifier) {
        ArrayList<ArrayList> newLikesSet=new ArrayList<>();
        ArrayList<String> currNestedInfo=new ArrayList<>();
        String currName;
        String currLikesList;
        for(ArrayList<String> n:oldLikesArray){
            currName=n.get(0);
            currLikesList=n.get(1);

            //Change the current likes list(this is a nested array list)
            currLikesList = alterProdNameInLString(currLikesList, oldProdName, newProdName, busIdentifier);

            currNestedInfo.add(currName);
            currNestedInfo.add(currLikesList);

            newLikesSet.add(currNestedInfo);

            currNestedInfo=new ArrayList<>();
            Log.d("Curr name in iter:",""+newLikesSet);

        }
        return newLikesSet;
    }


    private String likeArrayToString(ArrayList<ArrayList> newLikesArray){
        String newString="";
        for(ArrayList<String> n:newLikesArray){
            String currUserName=n.get(0);
            String currLikesString=n.get(1);
            newString+=currLikesString;
        }
        return newString;
    }

    public void updateLikesBusNames(String oldBusName,String newBusName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        ArrayList<ArrayList> oldLikesArray=this.getAllPreviousLikes();
        ArrayList<ArrayList> newLikesArray=updateStringsToNewName(oldLikesArray,oldBusName,newBusName, false);
        String translatedLikesString=likeArrayToString(newLikesArray);
        values.put(PREVIOUSLIKES, translatedLikesString);
        db.update(TABLE_PRODUCTS, values, "", new String[]{});
    }
    public void updateLikesProdNames(String busIdentifier,String oldProdName, String newProdName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        ArrayList<ArrayList> oldLikesArray=this.getAllPreviousLikes();
        ArrayList<ArrayList> newLikesArray=updateProductsToNewNameInLList(oldLikesArray,oldProdName,newProdName, busIdentifier);
        String translatedLikesString=likeArrayToString(newLikesArray);
        values.put(PREVIOUSLIKES, translatedLikesString);
        db.update(TABLE_PRODUCTS, values, "", new String[]{});
    }

    public void delPFromLL(String busIdentifier, String prodName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String newLikesString=deleteBusProductX(likeArrayToString(this.getAllPreviousLikes()), prodName, busIdentifier);
        values.put(PREVIOUSLIKES, newLikesString);
        db.update(TABLE_PRODUCTS, values, "", new String[]{});
    }

    public void updateUsername(String oldUsername,String newUsername) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET USERNAME = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(USERNAME, newUsername);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{oldUsername});
    }
    public void updatePassword(String enteredUsername,String newPassword){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET PASSWORD = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(PASSWORD, newPassword);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{enteredUsername});
    }
    public void updateEmail(String enteredUsername,String newEmail){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE accounts SET EMAIL = ? WHERE USERNAME = ?;";
        ContentValues values = new ContentValues();
        values.put(EMAIL, newEmail);
        db.update(TABLE_PRODUCTS, values, " USERNAME = ? ", new String[]{enteredUsername});
    }
    //Returns true if the name exists in the accounts database
    public boolean nameExists(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result="";
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ?";

        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("password"))!=null) {
                return true;
            }
        }
        return false;
    }

    //Returns true if the username password combination is in the database
    //and false otherwise.
    public boolean checkLogin(String enteredUsername,String enteredPassword){
        SQLiteDatabase db=getWritableDatabase();
        String result="";
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE USERNAME = ? AND PASSWORD = ?";

        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername,enteredPassword});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("password"))!=null) {
                return true;
            }
        }
        return false;
    }


    //Prints a string representation of database for viewing
    public String databaseToString(){
        String dbString="";
        SQLiteDatabase db=getWritableDatabase();
        //This selects from all items in my database
        String query=" SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c=db.rawQuery(query,null);
        //Moves pointer to position 0
        c.moveToFirst();
        //% symbolizes separation
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("username"))!=null){
                //Does this actually treat id as String?
                //dbString+=c.getString(c.getColumnIndex("_id"));

                dbString+=c.getString(c.getColumnIndex("username"));
                dbString+="^";
                dbString+=c.getString(c.getColumnIndex("password"));
                dbString+="*";
                dbString+=c.getString(c.getColumnIndex("email"));
                dbString+="~";
                dbString+=c.getString(c.getColumnIndex("points"));
                dbString+="%";

                dbString+=c.getString(c.getColumnIndex("gender"));
                dbString+=",";
                dbString+=c.getString(c.getColumnIndex("age"));
                dbString+="^";
                dbString+=c.getString(c.getColumnIndex("race"));
                dbString+="~";
                dbString+=c.getString(c.getColumnIndex("religion"));
                dbString+="$";

                dbString+=c.getString(c.getColumnIndex("educationLevel"));
                dbString+="#";
                dbString+=c.getString(c.getColumnIndex("industries"));
                dbString+=">";

                c.moveToNext();
            }
        }
        c.close();
        return dbString;
    }

}
