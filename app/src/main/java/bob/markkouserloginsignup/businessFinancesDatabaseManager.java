package bob.markkouserloginsignup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Manages a database of all of the financial information for businesses.
 */

public class businessFinancesDatabaseManager extends SQLiteOpenHelper {
    private static final int DARABASE_VERSION=1;
    private static final String DATABASE_NAME="finances.db";
    public static final String TABLE_PRODUCTS="fnances";



    public static final String COLUMN_ID="_id";

    //The id of the business account which the information belongs to
    public static final String BUSINESSIDENTIFIER="businessidentifier";

    public static final String CARDTYPE="cardtype";
    public static final String CARDNAME="cardname";
    public static final String CARDNUMBER="cardnumber";
    public static final String EXPIRATIONDATE="expirationdate";
    public static final String SECURITYCODE="securitycode";
    public static final String BILLINGADDRESS="billingaddress";
    public static final String SHIPPINGADDRESS="shippingaddress";

    public businessFinancesDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DARABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the table
        String newQuery="CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUSINESSIDENTIFIER + " TEXT ," +
                CARDTYPE + " TEXT ," +
                CARDNAME + " TEXT ," +
                CARDNUMBER + " INTEGER ," +
                EXPIRATIONDATE + " TEXT ," +
                SECURITYCODE + " INTEGER ," +
                BILLINGADDRESS + " TEXT ," +
                SHIPPINGADDRESS + " TEXT " +
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

    public void addAccount(String businessIdentifier,String cardType,
                           String cardName,int cardNumber,String expirationDate,int securityCode,String billingAddress,String shippingAddress){
        ContentValues values=new ContentValues();

        //Prevent null values from being added(used on debugging no longer necessary?)
        values.put(BUSINESSIDENTIFIER,businessIdentifier);
        if(cardType!=null) {
            values.put(CARDTYPE, cardType);
        }
        else{
            values.put(CARDTYPE,"");
        }
        if(cardName!=null) {
            values.put(CARDNAME, cardName);
        }
        else{
            values.put(CARDNAME,"");
        }
        if(cardNumber!=0) {
            values.put(CARDNUMBER, cardNumber);
        }
        else{
            values.put(CARDNUMBER,-1);
        }
        if(expirationDate!=null) {
            values.put(EXPIRATIONDATE, expirationDate);
        }
        else{
            values.put(EXPIRATIONDATE,"");
        }
        if(securityCode!=0) {
            values.put(SECURITYCODE, securityCode);
        }
        else{
            values.put(SECURITYCODE,"");
        }
        if(cardType!=null) {
            values.put(BILLINGADDRESS, billingAddress);
        }
        else{
            values.put(BILLINGADDRESS,"");
        }
        if(shippingAddress!=null) {
            values.put(SHIPPINGADDRESS, shippingAddress);
        }
        else{
            values.put(SHIPPINGADDRESS,"");
        }

        //Gets the database that we are using
        SQLiteDatabase db=getWritableDatabase();
        //Inserts the new column into the database
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }

    public void deleteAccount(String businessIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "BUSINESSIDENTIFIER = ?" , new String[]{businessIdentifier});
    }

    public String getAccountInfoByUsername(String enteredUsername){
        SQLiteDatabase db=getWritableDatabase();
        String result;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{enteredUsername});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                return c.getString(c.getColumnIndex("cardtype"))+"|"+
                        c.getString(c.getColumnIndex("cardname"))+"|"+
                        c.getString(c.getColumnIndex("cardnumber"))+"|"+
                        c.getString(c.getColumnIndex("expirationdate"))+"|"+
                        c.getString(c.getColumnIndex("securitycode"))+"|"+
                        c.getString(c.getColumnIndex("billingaddress"))+"|"+
                        c.getString(c.getColumnIndex("shippingaddress"))+"|";
            }
        }
        return "Error: no business plan!";
    }
    //Prints a string representation of database for viewing
    public String databaseToString(String colIndex1,String colIndex2,String colIndex3) {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        //This selects from all items in my database
        String query = " SELECT * FROM " + TABLE_PRODUCTS + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        //Moves pointer to position 0
        c.moveToFirst();
        //% symbolizes separation
        while (!c.isAfterLast()) {
            if (c.getString(c.getColumnIndex("businessidentifier")) != null) {

                dbString += c.getString(c.getColumnIndex(colIndex1));
                dbString += "^";
                if(! colIndex2.equals("X"))
                    dbString += c.getString(c.getColumnIndex(colIndex2));
                    dbString += "*";
                if(! colIndex3.equals("X")) {
                    dbString += c.getString(c.getColumnIndex(colIndex3));
                    dbString += "~";
                }

                c.moveToNext();
            }
        }
        c.close();
        return dbString;
    }


}
