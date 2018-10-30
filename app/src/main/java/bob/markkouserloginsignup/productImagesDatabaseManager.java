package bob.markkouserloginsignup;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class productImagesDatabaseManager extends SQLiteOpenHelper {
    private static final int DARABASE_VERSION=1;
    private static final String DATABASE_NAME="prodimages.db";
    public static final String TABLE_PRODUCTS="images";

    public static final String COLUMN_ID="_id";

    //The id of the business account which the information belongs to
    public static final String BUSINESSIDENTIFIER="businessidentifier";
    //identifies products owned by businesses from one another
    public static final String PRODUCTIDENTIFIER="productidentifier";

    public static final String IMAGE1="image1";
    public static final String IMAGE2="image2";
    public static final String IMAGE3="image3";
    public static final String IMAGE4="image4";
    public static final String IMAGE5="image5";
    public static final String IMAGE6="image6";

    public productImagesDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DARABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the table
        String newQuery="CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BUSINESSIDENTIFIER + " TEXT ," +
                PRODUCTIDENTIFIER + " TEXT ," +
                IMAGE1 + " BLOB ," +
                IMAGE2 + " BLOB ," +
                IMAGE3 + " BLOB ," +
                IMAGE4 + " BLOB ," +
                IMAGE5 + " BLOB ," +
                IMAGE6 + " BLOB " +
                ");";
        //Executes the query to create the table
        sqLiteDatabase.execSQL(newQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP_TABLE_IF_EXISTS" + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }
    public void addToImagesDatabase(String busIdentifier,String prodIdentifier,byte[] image1,byte[] image2,byte[] image3,
                                    byte[] image4,byte[] image5,byte[] image6){
        ContentValues values=new ContentValues();
        values.put(BUSINESSIDENTIFIER,busIdentifier);
        values.put(PRODUCTIDENTIFIER,prodIdentifier);
        values.put(IMAGE1,image1);
        values.put(IMAGE2,image2);
        values.put(IMAGE3,image3);
        values.put(IMAGE4,image4);
        values.put(IMAGE5,image5);
        values.put(IMAGE6,image6);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }
    public void deleteProduct(String businessIdentifier,String productIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        Log.d("PRODUCTIMDB", "Deleting product");
        db.delete(TABLE_PRODUCTS, "businessidentifier = ? AND productidentifier = ?" , new String[]{businessIdentifier,productIdentifier});
    }
    public void deleteAllProductsFromBusiness(String businessIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "businessidentifier = ?" , new String[]{businessIdentifier});
        db.close();
    }
    //Changes all products with an owner of a certain name to those of a different name
    public void updateProductBusName(String oldUsername,String newUsername) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUSINESSIDENTIFIER, newUsername);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ?", new String[]{oldUsername});
    }
    public ArrayList<byte[]> getAllDBImages(String businessIdentifier,String productID){
        SQLiteDatabase db=getWritableDatabase();
        ArrayList<String> dbColumnKeys=new ArrayList<String>();
        dbColumnKeys.add("image1");
        dbColumnKeys.add("image2");
        dbColumnKeys.add("image3");
        dbColumnKeys.add("image4");
        dbColumnKeys.add("image5");
        dbColumnKeys.add("image6");

        byte[] im1;
        byte[] im2;
        byte[] im3;
        byte[] im4;
        byte[] im5;
        byte[] im6;
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{businessIdentifier,productID});
        c.moveToFirst();
        ArrayList<byte[]> currImages=new ArrayList<>();
        im1=this.getImage(businessIdentifier,productID, 1);
        im2=this.getImage(businessIdentifier,productID, 2);
        im3=this.getImage(businessIdentifier,productID, 3);
        im4=this.getImage(businessIdentifier,productID, 4);
        im5=this.getImage(businessIdentifier,productID, 5);
        im6=this.getImage(businessIdentifier,productID, 6);
        if(im1.length!=0){
            currImages.add(im1);
        }
        if(im2.length!=0){
            currImages.add(im2);
        }
        if(im3.length!=0){
            currImages.add(im3);
        }
        if(im4.length!=0){
            currImages.add(im4);
        }
        if(im5.length!=0){
            currImages.add(im5);
        }
        if(im6.length!=0){
            currImages.add(im6);
        }
        return currImages;
    }

    //Returns a given image from a given set of images(returning an array of blobs would not work)
    //Trusts the user to enter an image number 1-6
    public byte[] getImage(String businessIdentifier,String productIdentifier,int imageNumber){
        byte[] im1;
        SQLiteDatabase db=getWritableDatabase();
        String chosenImage;
        if(imageNumber==1){
            chosenImage="image1";
        }
        else if(imageNumber==2){
            chosenImage="image2";
        }
        else if(imageNumber==3){
            chosenImage="image3";
        }
        else if(imageNumber==4){
            chosenImage="image4";
        }
        else if(imageNumber==5){
            chosenImage="image5";
        }
        else{
            chosenImage="image6";
        }
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{businessIdentifier,productIdentifier});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                im1=c.getBlob(c.getColumnIndex(chosenImage));
                return im1;
            }
        }
        return new byte[]{};
    }

}