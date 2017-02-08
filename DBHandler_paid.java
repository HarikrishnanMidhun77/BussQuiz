package com.quizapp.hp.quiz;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler_paid extends SQLiteOpenHelper {
    public static final String KEY_QID ="QId";
    public static final String KEY_Q_CAT ="Q_cat";
    public static final String KEY_Q_SUB ="Q_sub";
    public static final String KEY_Q_TYPE ="Q_type";
    public static final String KEY_DIFFICULTY="Difficulty";
    public static final String KEY_QUESTION="Question";
    public static final String KEY_ANS1="ans1";
    public static final String KEY_ANS2="ans2";
    public static final String KEY_ANS3="ans3";
    public static final String KEY_ANS4="ans4";
    public static final String KEY_CORRECTANS="Correct_ans";
    public static final String KEY_CORRECTANS2="Correct_ans2";
    public static final String KEY_CORRECTANS3="Correct_ans3";
    public static final String KEY_CORRECTANS4="Correct_ans4";
    public static final String KEY_C_ANS_NO="C_ans_no";
    public static final String KEY_PIC_NAME="Pic_name";
    public static final String KEY_CUR_DATE="Curdate";
    public static final String KEY_IS_SHOWN="IS_SHOWN";





    /*public static final String KEY_ITEMPRICE="ItemPrice";
    public static final String KEY_ITEMPLACE="ItemPlace";
    public static final String KEY_ITEMDESCRIP="ItemDescrip";*/

    private static final String DATABASE_NAME = "DB_Questions_paid";//"DB_PaidQuestions";//old DB_Random
    private static final String DATABASE_TABLE = "Table_Q_paid";//"Table_Random_Questions"
    private static final String TABLE_QUESTIONS = "Table_Questions";
    private static final int DATABASE_VERSION = 18;

    private SQLiteDatabase odb;
  private static final   String qstn1=" Income level of the customers acquired and % of customers in each of these levels are as follow.\n" +
            "Income Level\t% Customers\n" +
            "Low\t10%\n" +
            "Medium\t50%\n" +
            "High\t35%\n" +
            "Very High\t5%\n" +
            "A customer is selected randomly. What is probability that the customer will be from HIGH and VERY HIGH income group.";
    private static final String USER_MASTER_CREATE =
        "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE+ "("
            + KEY_QID + " INTEGER, "+KEY_Q_CAT+ " VARCHAR(5) ,"+KEY_Q_SUB+ " VARCHAR(45) ,"+KEY_DIFFICULTY+ " INT ,"+ KEY_Q_TYPE  + " INT , " +
                KEY_QUESTION + " VARCHAR(1000) , " + KEY_ANS1 + " VARCHAR(300) , " + KEY_ANS2 + " VARCHAR(300) , " + KEY_ANS3 + " VARCHAR(300) , " + KEY_ANS4 + " VARCHAR(300), " + KEY_CORRECTANS + " VARCHAR(300), "+ KEY_CORRECTANS2 + " VARCHAR(300), "+ KEY_CORRECTANS3 + " VARCHAR(300),"+ KEY_CORRECTANS4 + " VARCHAR(300),"+KEY_C_ANS_NO+ " INT,"+KEY_PIC_NAME+ " VARCHAR(100), "+ KEY_CUR_DATE+" DATE DEFAULT (date('now')), "+KEY_IS_SHOWN+" INT DEFAULT 0)";
 private static final String PRIMARY_ENTRY="INSERT INTO "+ DATABASE_TABLE + "(" + KEY_QID+" , "+ KEY_Q_CAT+" , "+KEY_Q_SUB+ " , "+ KEY_DIFFICULTY +" , "+ KEY_Q_TYPE  + " , " + KEY_QUESTION + " , " + KEY_ANS1 + "  , " + KEY_ANS2 + "  , " + KEY_ANS3 + "  , " + KEY_ANS4 + "  , " + KEY_CORRECTANS + " , " + KEY_CORRECTANS2 + " , " + KEY_CORRECTANS3 + ","+ KEY_CORRECTANS4 + ","+ KEY_C_ANS_NO + ","+KEY_PIC_NAME+","+KEY_IS_SHOWN+" ) VALUES (1,'SST','SAS',1,0"+",'"+ qstn1 +"',"+"'35%','30%','90%','40%',null,null,null,'40%',1,'pic1',0)";


    private static final String CREATE_TABLE_QUESTIONS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTIONS+ "("
                   +KEY_QID+ " INTEGER,"+KEY_Q_CAT+ " VARCHAR(5) ,"+KEY_Q_SUB+ " VARCHAR(45) ,"+KEY_DIFFICULTY+ " INT ,"+ KEY_Q_TYPE  + " INT , " +
                    KEY_QUESTION + " VARCHAR(1000) , " + KEY_ANS1 + " VARCHAR(300) , " + KEY_ANS2 + " VARCHAR(300) , " + KEY_ANS3 + " VARCHAR(300) , " + KEY_ANS4 + " VARCHAR(300), " + KEY_CORRECTANS + " VARCHAR(300), "+ KEY_CORRECTANS2 + " VARCHAR(300), "+ KEY_CORRECTANS3 + " VARCHAR(300),"+ KEY_CORRECTANS4 + " VARCHAR(300),"+KEY_C_ANS_NO+ " INT,"+KEY_PIC_NAME+ " VARCHAR(100), "+ KEY_CUR_DATE+" DATE DEFAULT (date('now')),"+KEY_IS_SHOWN+" INT)";

   // SQLiteDatabase odb = this.getReadableDatabase();


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(USER_MASTER_CREATE);
            db.execSQL(PRIMARY_ENTRY);
           // db.execSQL(CREATE_TABLE_QUESTIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL("DROP TABLE "+ DATABASE_TABLE);
            onCreate(db);
        }

    public DBHandler_paid(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    public long insertmaster(String col1, String col2, String col3, String col4, String col5, String col6,String col7,String col8,String col9,String col10,String col11, String col12,String col13,String col14,String col15,String col16 ) throws SQLException {
        Log.d("", col1);
        Log.d("", col2);
        Log.d("", col3);
        Log.d("", col4);
        Log.d("", col5);
        Log.d("", col6);
        Log.d("", col7);
        Log.d("", col8);
        Log.d("", col9);
        Log.d("", col10);
        Log.d("", col11);
        Log.d("", col12);
        Log.d("", col13);
        Log.d("", col14);
        Log.d("", col15);
        Log.d("", col16);
        odb = this.getReadableDatabase();

        ContentValues IV = new ContentValues();
        IV.put(KEY_QID, col1);
        IV.put(KEY_Q_CAT, col2);
        IV.put(KEY_Q_SUB, col3);
        IV.put(KEY_Q_TYPE, col4);
        IV.put(KEY_DIFFICULTY, col5);
        IV.put(KEY_QUESTION, col6);
        IV.put(KEY_ANS1, col7);
        IV.put(KEY_ANS2, col8);
        IV.put(KEY_ANS3, col9);
        IV.put(KEY_ANS4, col10);
        IV.put(KEY_CORRECTANS, col11);
        IV.put(KEY_CORRECTANS2, col12);
        IV.put(KEY_CORRECTANS3, col13);
        IV.put(KEY_CORRECTANS3, col14);
        IV.put(KEY_C_ANS_NO, col15);
        IV.put(KEY_PIC_NAME, col16);


        long insert= odb.insert(DATABASE_TABLE, null, IV);
        return insert;
        // returns a number >0 if inserting data is successful
    }
    public Cursor getRandom(){
        odb = this.getReadableDatabase();
        Cursor cursor =odb.rawQuery("select * from " + DATABASE_TABLE + " ORDER BY RANDOM() LIMIT 1", null);
        return cursor;
    }
    public Cursor getUsed(){
        odb = this.getReadableDatabase();
        Cursor cursor =odb.rawQuery("select * from " + DATABASE_TABLE + " WHERE "+ KEY_IS_SHOWN +" = 1", null);
        return cursor;
    }
    public Cursor getByLevel(int lev)
    {
        odb = this.getReadableDatabase();
        Cursor cursor =odb.rawQuery("select * from " + DATABASE_TABLE + " WHERE "+KEY_DIFFICULTY +" = "+ lev +" ORDER BY RANDOM() LIMIT 1", null);
        return cursor;
    }
    public void updateRow(long rowID, String col1, String col2, String col3, String col4, String col5, String col6,String col7,String col8,String col9,String col10,String col11, String col12,String col13,String col14,String col15,String col16  ) {
        odb = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_QID, col1);
        values.put(KEY_Q_CAT, col2);
        values.put(KEY_Q_SUB, col3);
        values.put(KEY_Q_TYPE, col4);
        values.put(KEY_DIFFICULTY, col5);
        values.put(KEY_QUESTION, col6);
        values.put(KEY_ANS1, col7);
        values.put(KEY_ANS2, col8);
        values.put(KEY_ANS3, col9);
        values.put(KEY_ANS4, col10);
        values.put(KEY_CORRECTANS, col11);
        values.put(KEY_CORRECTANS2, col12);
        values.put(KEY_CORRECTANS3, col13);
        values.put(KEY_CORRECTANS3, col14);
        values.put(KEY_C_ANS_NO, col15);
        values.put(KEY_PIC_NAME, col16);
        try {
            odb.update(DATABASE_TABLE, values, KEY_QID + "=" + rowID, null);
        } catch (Exception e) {
        }
           // odb.close();

    }
    public int getRowCount(String TableName) {
        odb = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TableName;
        Cursor cursor = odb.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
       // odb.close();
        cursor.close();

        // return row count
        return rowCount;
    }


    public int getallrowcount() {
        // return row count
        return getRowCount(DATABASE_TABLE);
    }
    public int getLevelRowCount(String TableName,int lev) {
        odb = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TableName +" WHERE "+KEY_DIFFICULTY +" = "+ lev;
        Cursor cursor = odb.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
       // odb.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    public int getLrowcount(int lev) {
        // return row count
        return getLevelRowCount(DATABASE_TABLE,lev);
    }
    public void resetrows()
    {
        odb = this.getReadableDatabase();
        odb.execSQL("DELETE FROM " + DATABASE_TABLE);
       // odb.close();
    }

  /*  public Cursor getRandom(){
        //odb = this.getReadableDatabase();
        return odb.rawQuery("select "+KEY_QID+","+KEY_QUESTION+" from "+DATABASE_TABLE+" ORDER BY RANDOM() LIMIT 1",null);
    }*/
    public boolean delete() {
        odb = this.getReadableDatabase();
        return odb.delete(DATABASE_TABLE, null, null) > 0;
    }

    public Cursor getAllTitles() {
        // using simple SQL query
        odb = this.getReadableDatabase();
        return odb.rawQuery("select * from " + DATABASE_TABLE, null);
    }
    public Cursor getAllIds() {
        // using simple SQL query
        odb = this.getReadableDatabase();
        return odb.rawQuery("select "+KEY_QID+" from " + DATABASE_TABLE, null);
    }


    public void setUsed(int qid)
    {
        String qry="UPDATE "+ DATABASE_TABLE+" SET "+KEY_IS_SHOWN+" = 1"+" WHERE "+ KEY_QID +" = "+ qid;
         odb.execSQL(qry);
       // odb.close();
    }
    public boolean isUsed(int qid){
        String qry="SELECT * FROM "+ DATABASE_TABLE+" WHERE "+KEY_IS_SHOWN+" = 1"+" AND "+ KEY_QID +" = "+ qid;
        Cursor c=odb.rawQuery(qry, null);
        if(c.getCount()>0)
            return true;
        else
            return false;
      /* c.moveToFirst();
        if(c.isNull(0))
            return false;
        else
            return true;*/


    }
/* public Cursor getSum(String Type){
	 return odb.rawQuery("select total( KEY_ITEMPRICE ) from "+DATABASE_TABLE+" where "+" KEY_ITEMTYPE  " +" = "+ "'"+Type+"'",null);
 }
 public Cursor getSumOfPrice(){
	 return odb.rawQuery("select total( KEY_ITEMPRICE ) from "+DATABASE_TABLE,null);
 }

public Cursor getDates(){
	return odb.rawQuery("select distinct "+KEY_USERDATE+" from "+DATABASE_TABLE,null);
}*/
    public Cursor getallCols(String id) throws SQLException {
        Cursor mCursor = odb.query(DATABASE_TABLE, new String[] {  KEY_QID, KEY_Q_CAT ,KEY_Q_SUB ,KEY_Q_TYPE, KEY_DIFFICULTY , KEY_QUESTION ,  KEY_ANS1 , KEY_ANS2 , KEY_ANS3 , KEY_ANS4 , KEY_CORRECTANS ,  KEY_CORRECTANS2 ,  KEY_CORRECTANS3 , KEY_CORRECTANS4 + ","+ KEY_C_ANS_NO + ","+KEY_PIC_NAME }, null, null, null, null, null);
        Log.e("getallcols zmv", "opening successfull");
        return mCursor;
    }

    public Cursor getColsById(long id) throws SQLException {
        odb = this.getReadableDatabase();
        Cursor mCursor = odb.query(DATABASE_TABLE, new String[] { KEY_QID, KEY_Q_CAT ,KEY_Q_SUB ,KEY_Q_TYPE, KEY_DIFFICULTY , KEY_QUESTION ,  KEY_ANS1 , KEY_ANS2 , KEY_ANS3 , KEY_ANS4 , KEY_CORRECTANS ,  KEY_CORRECTANS2 ,  KEY_CORRECTANS3 , KEY_CORRECTANS4 + ","+ KEY_C_ANS_NO + ","+KEY_PIC_NAME }, KEY_QID + " = " + id, null, null, null, null);
        Log.e("getallcols zmv", "opening successfull");
       // odb.close();
        return mCursor;
    }
    public boolean deleteRow(Long rowId)
    {
        odb = this.getReadableDatabase();
    	return odb.delete(DATABASE_TABLE, KEY_QID+ "=" +rowId, null)>0;
    }
}