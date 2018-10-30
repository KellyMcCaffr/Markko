package bob.markkouserloginsignup;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

import java.sql.PreparedStatement;
import java.util.ArrayList;


/*
Works directly with the database for business user account
information.
 */

public class businessAccountsDatabaseManager extends SQLiteOpenHelper {

    protected ArrayList allBusinessNames=new ArrayList();
    PreparedStatement query;
    private static final int DARABASE_VERSION=1;
    private static final String DATABASE_NAME="businesses.db";
    public static final String TABLE_PRODUCTS="businesses";

    //Names of columns in this database:
    public static final String COLUMN_ID="_id";
    public static final String NAME="name";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";

    public static final String COMPANY="company";
    public static final String URL="url";

    public static final String PLAN="plan";

    public static final String PRODUCTS="products";

    public static final String INDUSTRIES="industries";


    public businessAccountsDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DARABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the table

        String newQuery="CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME + " TEXT ," +
                EMAIL + " TEXT ," +
                PASSWORD + " TEXT ," +
                COMPANY + " TEXT ," +
                URL + " TEXT ," +
                PLAN + " TEXT ," +
                PRODUCTS + " TEXT ," +
                INDUSTRIES + " TEXT " +
                ");";
        //Executes the query to create the table
        sqLiteDatabase.execSQL(newQuery);
    }

    //When structure(?) is changed, delete old table and create a new one
    //with that structure.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP_TABLE_IF_EXISTS" + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }


    //Takes the business name and new plan and updates the account
    public void updatePlan(String name,String newPlan){
        SQLiteDatabase db=getWritableDatabase();
        String sql="UPDATE businesses SET PLAN = ? WHERE NAME = ?;";
        ContentValues values=new ContentValues();
        values.put(PLAN,newPlan);
        db.update(TABLE_PRODUCTS,values," NAME = ? ",new String[]{name});
    }

    public void updateIndustries(String name,String newIndustries){
        SQLiteDatabase db=getWritableDatabase();
        String sql="UPDATE businesses SET INDUSTRIES = ? WHERE NAME = ?;";
        ContentValues values=new ContentValues();
        values.put(INDUSTRIES,newIndustries);
        db.update(TABLE_PRODUCTS,values," NAME = ? ",new String[]{name});
    }

    public void addAccount(businessAccountDatabaseProducts account, String industries){
        ContentValues values=new ContentValues();
        //values.put(COLUMN_ID,account.get_id());
        values.put(NAME,account.get_name());
        values.put(EMAIL,account.get_email());
        values.put(PASSWORD,account.get_password());
        values.put(COMPANY,account.get_company());

        values.put(URL,account.get_url());
        values.put(PLAN,account.get_plan());

        values.put(PRODUCTS,"");
        values.put(INDUSTRIES,industries);

        //Get/s the database that we are using
        SQLiteDatabase db=getWritableDatabase();
        //Inserts the new column into the database
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }


    private void makeBusinessNamesArrayList(){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS;
        Cursor c=db.rawQuery(newQuery,new String[]{});
        c.moveToFirst();
        String busName;
        int arraySize;
        while(!c.isAfterLast()){
            busName=(c.getString(c.getColumnIndex("name")));
            if(busName!=null && busName!="") {
                allBusinessNames.add(busName);
            }
            c.moveToNext();
        }
    }

    public ArrayList getFiveRandomBusinessNames(){
        if(allBusinessNames.size()==0) {
            makeBusinessNamesArrayList();
        }
        ArrayList<String> fiveBusinessNames=new ArrayList<>();
        Random randomGenerator=new Random();
        int namesArraySize=allBusinessNames.size();
        Log.d("All BN size in db:",Integer.toString(allBusinessNames.size()));
        ArrayList<String>prevIndexes=new ArrayList<>();
        for(int i=0;i<5;i++){
            if(i<allBusinessNames.size()) {
                int index = randomGenerator.nextInt(namesArraySize);
                //Needed to avoid having multiple copies of the same business
                Log.d("Random chosen bus:",Integer.toString(index));
                String newName = (String) allBusinessNames.get(index);
                while(fiveBusinessNames.contains(newName)) {
                    index = randomGenerator.nextInt(namesArraySize);
                    newName = (String) allBusinessNames.get(index);
                }

                fiveBusinessNames.add(newName);
            }
            else{
                return fiveBusinessNames;
            }
        }
        return fiveBusinessNames;
    }
    public void deleteAccount(String name){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "name = ?" , new String[]{name});
    }

    //Returns true if the name exists in the business database
    public boolean nameExists(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ?";

        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("password"))!=null) {
                return true;
            }
        }
        c.close();
        return false;
    }

    public String getAccountInfoByUsername(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("name"))!=null) {
                return c.getString(c.getColumnIndex("name"))+"|"+
                        c.getString(c.getColumnIndex("password"))+"|"+
                        c.getString(c.getColumnIndex("email"))+"|"+
                        c.getString(c.getColumnIndex("company"))+"|"+
                        c.getString(c.getColumnIndex("url"))+"|"+
                        c.getString(c.getColumnIndex("plan"))+"~";
            }
        }
        return "Error: no business plan!";
    }
    //Returns true if the username password combination is in the database
    //and false otherwise.
    public boolean checkLogin(String enteredUsername,String enteredPassword){
        SQLiteDatabase db=getWritableDatabase();
        String result="";
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ? AND PASSWORD = ?";

        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername,enteredPassword});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("password"))!=null) {
                return true;
            }
        }
        return false;
    }
    public String getBusinessName(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("company"))!=null) {
                return c.getString(c.getColumnIndex("company"));
            }
        }
        return "Error: no business name!";
    }
    public String getBusinessPlan(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("plan"))!=null) {
                return c.getString(c.getColumnIndex("plan"));
            }
        }
        return "Error: no business plan!";
    }
    public String getBusinessIndustries(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE NAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("industries"))!=null) {
                return c.getString(c.getColumnIndex("industries"));
            }
        }
        return "Error: no business plan!";
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
            if(c.getString(c.getColumnIndex("name"))!=null){
                //Does this actually treat id as String?
                //dbString+=c.getString(c.getColumnIndex("_id"));

                dbString+=c.getString(c.getColumnIndex("name"));
                dbString+="^";
                dbString+=c.getString(c.getColumnIndex("email"));
                dbString+="*";
                dbString+=c.getString(c.getColumnIndex("password"));
                dbString+="~";
                dbString+=c.getString(c.getColumnIndex("company"));
                dbString+="%";
                dbString+=c.getString(c.getColumnIndex("url"));
                dbString+=",";
                dbString+=c.getString(c.getColumnIndex("plan"));
                dbString+="*";
                dbString+=c.getString(c.getColumnIndex("products"));
                dbString+="$";

                c.moveToNext();
            }
        }
        c.close();
        return dbString;
    }

}
