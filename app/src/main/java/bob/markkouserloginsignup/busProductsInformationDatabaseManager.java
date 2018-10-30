package bob.markkouserloginsignup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Random;

public class busProductsInformationDatabaseManager extends SQLiteOpenHelper {

    PreparedStatement query;
    private static final int DARABASE_VERSION=1;
    private static final String DATABASE_NAME="productsinfo.db";
    private static final String TABLE_PRODUCTS="productsinfo";

    //Names of columns in this database:
    private static final String COLUMN_ID="_id";
    private static final String PRODUCTNAME="productname";
    private static final String BUSINESSIDENTIFIER="businessidentifier";
    private static final String PRODUCTIDENTIFIER="productidentifier";
    private static final String PRODUCTDESCRIPTION="productdescription";

    private static final String CONSUMERDEMOGRAPHICSSTRING="consumerdemographicsstring";
    private static final String CONSUMEREDUCATIONSTRING="consumereducationstring";
    private static final String CONSUMERACESTRING="consumerracesstring";
    private static final String CONSUMERGENDERSTRING="consumergendersstring";
    private static final String CONSUMERRELIGIONSTRING="consumerreligionstring";
    private static final String CONSUMERAGESTRING="consumeragestring";
    private static final String LIKESNUMBER="likesnumber";
    private static final String DISLIKESNUMBER="dislikesnumber";
    private static final String TOTALSWIPES="totalswipes";

    //Both made up of user ids
    private static final String LIKEUSERSSTRING="likeusersstring";
    private static final String DISLIKEUSERSSTRING="dislikeusersstring";

    private static final String PRICE="price";
    private static final String ALLOWTRIALS="allowtrials";
    private static final String ALLOWBUYING="allowbuying";
    protected ArrayList<String> allBusinessNames=new ArrayList();

    public busProductsInformationDatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DARABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create the table
        String newQuery="CREATE TABLE " + TABLE_PRODUCTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRODUCTNAME + " TEXT ," +
                BUSINESSIDENTIFIER + " TEXT ," +
                PRODUCTIDENTIFIER + " TEXT ," +
                PRODUCTDESCRIPTION + " TEXT ," +
                CONSUMERDEMOGRAPHICSSTRING + " TEXT ," +
                CONSUMEREDUCATIONSTRING + " TEXT ," +
                CONSUMERACESTRING + " TEXT ," +
                CONSUMERGENDERSTRING + " TEXT ," +
                CONSUMERRELIGIONSTRING + " TEXT ," +
                CONSUMERAGESTRING + " TEXT ," +
                LIKESNUMBER + " INTEGER ," +
                TOTALSWIPES + " INTEGER ," +
                LIKEUSERSSTRING + " Text ," +
                DISLIKEUSERSSTRING + " Text ," +
                PRICE  + " Text ," +
                ALLOWTRIALS + " Text ," +
                ALLOWBUYING + " Text ," +
                DISLIKESNUMBER + " Text " +
                ");";
        //Executes the query to create the table
        sqLiteDatabase.execSQL(newQuery);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP_TABLE_IF_EXISTS" + TABLE_PRODUCTS);
        onCreate(sqLiteDatabase);
    }
    public void deleteProduct(String businessIdentifier,String productIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        Log.d("BUSPRODINFODB", "In delete product");
        db.delete(TABLE_PRODUCTS, "businessidentifier = ? AND productidentifier = ?" , new String[]{businessIdentifier,productIdentifier});
        db.close();
    }
    public void deleteAllProductsFromBusiness(String businessIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_PRODUCTS, "businessidentifier = ?" , new String[]{businessIdentifier});
        db.close();
    }
    public void addProductInfoSet(String productName,String busIdentifier,String prodIdentifier,String prodDescription,String consumerDemString,String consumerEdString,
                                  String consumerRaceString,String consumerGenString,String consumerAgeString,String consumerReligionString,String price,boolean allowTrials,boolean allowBuying){
        ContentValues values=new ContentValues();
        //values.put(COLUMN_ID,account.get_id());
        values.put(PRODUCTNAME,productName);
        values.put(BUSINESSIDENTIFIER,busIdentifier);
        values.put(PRODUCTIDENTIFIER,prodIdentifier);
        values.put(PRODUCTDESCRIPTION,prodDescription);
        values.put(CONSUMERDEMOGRAPHICSSTRING,consumerDemString);
        values.put(CONSUMEREDUCATIONSTRING,consumerEdString);
        values.put(CONSUMERACESTRING,consumerRaceString);
        values.put(CONSUMERGENDERSTRING,consumerGenString);
        values.put(CONSUMERRELIGIONSTRING,consumerReligionString);
        values.put(CONSUMERAGESTRING,consumerAgeString);
        values.put(CONSUMEREDUCATIONSTRING,"");
        values.put(LIKESNUMBER,0);
        values.put(TOTALSWIPES,0);
        values.put(LIKEUSERSSTRING,"");
        values.put(DISLIKEUSERSSTRING,"");
        values.put(PRICE,price);
        if(allowTrials){
            values.put(ALLOWTRIALS,"TRUE");
        }
        else{
            values.put(ALLOWTRIALS,"FALSE");
        }
        if(allowBuying){
            values.put(ALLOWBUYING,"TRUE");
        }
        else{
            values.put(ALLOWBUYING,"FALSE");
        }
        //Get/s the database that we are using
        SQLiteDatabase db=getWritableDatabase();
        //Inserts the new column into the database
        db.insert(TABLE_PRODUCTS,null,values);
        db.close();
    }
    public String getProductNameByIdentifier(String businessIdentifier,String productIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{businessIdentifier,productIdentifier});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("productname"))!=null) {
                return c.getString(c.getColumnIndex("productname"));
            }
        }
        c.close();
        return "";
    }
    public String getPrice(String busName,String prodNumber){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodNumber});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("price"))!=null) {
                return c.getString(c.getColumnIndex("price"));
            }
        }
        c.close();
        return "";
    }
    public String getTotalLikes(String busName,String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("likesnumber"))!=null) {
                return c.getString(c.getColumnIndex("likesnumber"));
            }
        }
        c.close();
        return "";
    }
    public String getTotalDislikes(String busName,String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("dislikesnumber"))!=null) {
                return c.getString(c.getColumnIndex("dislikesnumber"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    private boolean hasProducts(String busIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busIdentifier});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("productidentifier"))!=null) {
                return true;
            }
            c.moveToNext();
        }
        c.close();
        return false;
    }
    private void makeBusinessNamesArrayList(){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS;
        Cursor c=db.rawQuery(newQuery,new String[]{});
        c.moveToFirst();
        String busName;
        while(!c.isAfterLast()){
            busName=(c.getString(c.getColumnIndex("businessidentifier")));
            if(busName!=null && busName!="" && this.hasProducts(busName)&&!(allBusinessNames.contains(busName))) {
                allBusinessNames.add(busName);
            }
            c.moveToNext();
        }
    }
    protected int getNumBus(){
        return allBusinessNames.size();
    }
    //Returns [busName, prodName] for a new product
    private String getOneNewBName(ArrayList prevNames){
        Random randomGenerator=new Random();
        ArrayList busProd=new ArrayList();
        if(allBusinessNames.size()==0) {
            makeBusinessNamesArrayList();
        }
        //What to do if no bus in db
        if(allBusinessNames.size()==0 || prevNames.size()>=allBusinessNames.size()) {
            return "";
        }
        int namesArraySize=allBusinessNames.size();
        int index = randomGenerator.nextInt(namesArraySize);
        //Needed to avoid having multiple copies of the same business
        String newName = (String) allBusinessNames.get(index);
        while(prevNames.contains(newName)) {
            index = randomGenerator.nextInt(namesArraySize);
            newName = (String) allBusinessNames.get(index);
        }
        return newName;
    }
    //Must be written myself because java contains is broken for arraylists(?)
    private boolean arrayListContainsArrayList(ArrayList<ArrayList> list, ArrayList item){
        for(ArrayList l:list){
            if(item.equals(l)){
                return true;
            }
            else{
               //Log.d("NEW CONTAINS: ", "List :"+l+" does not equal list: "+item);
            }
        }
        return false;
    }
    protected int getTotalNumProds(){
        int prodCount=0;
        for(String busName:allBusinessNames){
            prodCount+=this.getAllBusPNames(busName).size();
        }
        return prodCount;
    }
    //Returns 50 random filtered products in the form of [[busName,prodName],...]
    public ArrayList<ArrayList> getFiftyNewUniqueProds(ArrayList<ArrayList> previousProdsInfo){
        //Initialize the variables:
        ArrayList<ArrayList> allProdInfo=new ArrayList<>();
        String currBName;
        String currPName;
        ArrayList busNamePSet;
        //If first use in session,build the full array list:
        if(allBusinessNames.size()==0) {
            makeBusinessNamesArrayList();
        }
        //The number of prods that have been added:
        int pCount=0;
        int totalBus=allBusinessNames.size();
        int totalUsedProds=previousProdsInfo.size();
        int currIndex;
        Random R=new Random();
        //The pair list we will add each time:
        ArrayList<String>pairAdd=new ArrayList();
        //While not reached either 50 prods or max number of unique prods for all businesses:
        int totalNumProds=this.getTotalNumProds();
        Log.d("GETFIFTYPROD", "Total num prods: "+totalNumProds);
        Log.d("GETFIFTYPROD", "Total used prods: "+totalUsedProds);
        Log.d("GETFIFTYPROD","All business names: "+allBusinessNames);
        while(pCount<(totalNumProds-totalUsedProds)&&(pCount<50)){
            //Pick a random business name:
            currIndex=R.nextInt(totalBus); //Next int automatically factors in indexing so no -1
            currBName=(String)allBusinessNames.get(currIndex);
            //Pick a random product name from that business:
            busNamePSet=getAllBusPNames(currBName);
            currIndex=R.nextInt(busNamePSet.size());
            currPName=(String)busNamePSet.get(currIndex);
            Log.d("GET FIFTY PROD", "Curr B Name: "+currBName);
            Log.d("GET FIFTY PROD", "Curr P Name: "+currPName);
            Log.d("GET FIFTY PROD", "Prev prods: "+previousProdsInfo);
            Log.d("GET FIFTY PROD", "All prod info: "+allProdInfo);
            //Add info to possible new set before filtering:
            pairAdd.add(currBName);
            pairAdd.add(currPName);
            //Check if product/bus pair is in previous products or has already been added:
            //Log.d("GETFIFTYPROD"," Previous prods info: "+previousProdsInfo);
            //Log.d("GETFIFTYPROD"," All prod info: "+allProdInfo);
            if(!(arrayListContainsArrayList(previousProdsInfo,pairAdd))&&!(arrayListContainsArrayList(allProdInfo,pairAdd))){
                //Add the new pair to the list of bus, prod pairs:
                allProdInfo.add(pairAdd);
                //Increment total product count:
                pCount++;
            }
            Log.d("GETFIFTYPROD", "New pCount: "+pCount);
            //Clear the pair holder:
            pairAdd=new ArrayList();
        }
        Log.d("GETFIFTYPROD", "Finished");
        return allProdInfo;
    }

    public String getEthnicityString(String busName, String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("consumerracesstring"))!=null) {
                return c.getString(c.getColumnIndex("consumerracesstring"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getReligionString(String busName, String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("consumerreligionstring"))!=null) {
                return c.getString(c.getColumnIndex("consumerreligionstring"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getAgeString(String busName, String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("consumeragestring"))!=null) {
                return c.getString(c.getColumnIndex("consumeragestring"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getEducationString(String busName, String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("consumereducationstring"))!=null) {
                return c.getString(c.getColumnIndex("consumereducationstring"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getGenderString(String busName, String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("consumergendersstring"))!=null) {
                return c.getString(c.getColumnIndex("consumergendersstring"));
            }
            c.moveToNext();
        }
        c.close();
        return "";
    }
    public String getAllowBuying(String busName,String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("allowbuying"))!=null) {
                return c.getString(c.getColumnIndex("allowbuying"));
            }
        }
        c.close();
        return "";
    }
    public String getAllowTrials(String busName,String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("allowtrials"))!=null) {
                return c.getString(c.getColumnIndex("allowtrials"));
            }
        }
        c.close();
        return " ";
    }

    public String getProductIdentifierByName(String busName,String prodName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{busName,prodName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                return c.getString(c.getColumnIndex("productidentifier"));
            }
        }
        c.close();
        return "";
    }
    public ArrayList<String> getAllBusPNames(String busName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ?";
        ArrayList<String> allPNames=new ArrayList<String>();
        Cursor c=db.rawQuery(newQuery,new String[]{busName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                allPNames.add(c.getString(c.getColumnIndex("productname")));
            }
            c.moveToNext();
        }
        c.close();
        return allPNames;
    }
    public String getProductDescriptionByIdentifier(String businessIdentifier,String productIdentifier){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE BUSINESSIDENTIFIER = ? AND PRODUCTIDENTIFIER = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{businessIdentifier,productIdentifier});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                return c.getString(c.getColumnIndex("productdescription"));
            }
        }
        c.close();
        return "";
    }
    public String getBusinessNameByPName(String productName){
        SQLiteDatabase db=getWritableDatabase();
        String newQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE PRODUCTNAME = ?";
        Cursor c=db.rawQuery(newQuery,new String[]{productName});
        c.moveToFirst();
        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex("businessidentifier"))!=null) {
                return c.getString(c.getColumnIndex("businessidentifier"));
            }
        }
        c.close();
        return "";
    }
    public  void addLike(String busName,String prodName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevLikes=this.getTotalLikes(busName,prodName);
        int newLikes=1;
        if(prevLikes.length()>0) {
            newLikes=Integer.parseInt(prevLikes)+1;
        }
        values.put(LIKESNUMBER, newLikes);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});

    }
    public void addEthnicity(String busName, String prodName, String newEthnicity){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevEthnicity=this.getEthnicityString(busName,prodName);
        if(prevEthnicity.length()>0) {
            newEthnicity=prevEthnicity+"%"+newEthnicity;
        }

        values.put(CONSUMERACESTRING, newEthnicity);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }
    public void addReligion(String busName, String prodName, String newReligion){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevReligion=this.getReligionString(busName,prodName);
        if(prevReligion.length()>0) {
            newReligion=prevReligion+"%"+newReligion;
        }

        values.put(CONSUMERRELIGIONSTRING, newReligion);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }
    public void addAge(String busName, String prodName, String newAge){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevAgeLikes=this.getAgeString(busName,prodName);
        if(prevAgeLikes.length()>0) {
            newAge=prevAgeLikes+"%"+newAge;
        }
        values.put(CONSUMERAGESTRING, newAge);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }
    public void addEducation(String busName, String prodName, String newEducation){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevEducationLikes=this.getEducationString(busName,prodName);
        if(prevEducationLikes.length()>0) {
            newEducation=prevEducationLikes+"%"+newEducation;
        }
        values.put(CONSUMEREDUCATIONSTRING, newEducation);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }
    public void addGender(String busName, String prodName, String newGender){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevGenderLikes=this.getGenderString(busName,prodName);
        if(prevGenderLikes.length()>0) {
            newGender=prevGenderLikes+"%"+newGender;
        }
        values.put(CONSUMERGENDERSTRING, newGender);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }
    public  void addDislike(String busName,String prodName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String prevDislikes=this.getTotalDislikes(busName,prodName);
        int newDislikes=1;
        if(prevDislikes.length()>0) {
            newDislikes=Integer.parseInt(prevDislikes)+1;
        }
        values.put(DISLIKESNUMBER, newDislikes);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ? AND PRODUCTNAME = ?", new String[]{busName,prodName});
    }

    //Changes all products with an owner of a certain name to those of a different name
    public void updateProductBusName(String oldUsername,String newUsername) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUSINESSIDENTIFIER, newUsername);
        db.update(TABLE_PRODUCTS, values, " BUSINESSIDENTIFIER= ?", new String[]{oldUsername});
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
            if(c.getString(c.getColumnIndex("productname"))!=null){
                dbString+=c.getString(c.getColumnIndex("productname"));
                dbString+="^";
                dbString+=c.getString(c.getColumnIndex("businessidentifier"));
                dbString+="*";
                //For now, no product identifier, we just assume it is there.
                dbString+=c.getString(c.getColumnIndex("consumerdemographicsstring"));
                dbString+="~";
                dbString+=c.getString(c.getColumnIndex("consumereducationstring"));
                dbString+="%";
                dbString+=c.getString(c.getColumnIndex("consumerracesstring"));
                dbString+=",";
                dbString+=c.getString(c.getColumnIndex("consumergendersstring"));
                dbString+=">";
                //dbString+=c.getString(c.getColumnIndex("consumerreligionstring"));
                c.getString(c.getColumnIndex("productidentifier"));
                dbString+="~";
                //dbString+=c.getString(c.getColumnIndex("likesnumber"));
                c.getString(c.getColumnIndex("productidentifier"));
                dbString+="$";
                c.moveToNext();
            }
        }
        c.close();
        if(dbString==null || dbString.equals("")){
            dbString="Null?";
        }
        return dbString;
    }

}
