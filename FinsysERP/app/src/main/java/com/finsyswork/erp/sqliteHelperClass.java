package com.finsyswork.erp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class sqliteHelperClass extends SQLiteOpenHelper{

    private final static String DB_NAME = "FinsysDB";
    private final static String TBL_NAME = "Tbl_qr_scanner";
    private final static String TBL_MATERIAL_ISSUE_SLIP = "tbl_issue_slip";
    private final static String TBL_AUTO_SAVE_DATA = "tbl_auto_save_data";   // Physical verification frm_bar_scanner
    private final static String TBL_AUTO_SAVE_DATA_FG_STACKING = "tbl_auto_save_data_fg_stacking";   // Physical verification frm_bar_scanner
    private final static int VERSION = 4;

    public sqliteHelperClass(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE if not exists "+TBL_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, col1 varchar(250), col2 varchar(250), col3 varchar(250), col4 varchar(250), col5 varchar(250))");
        db.execSQL("CREATE TABLE if not exists "+TBL_MATERIAL_ISSUE_SLIP+"(id INTEGER PRIMARY KEY AUTOINCREMENT, col1 varchar(250), col2 varchar(250), col3 varchar(250), col4 varchar(250), col5 varchar(250))");

//        Auto Save Data Table
        db.execSQL("CREATE TABLE if not exists "+TBL_AUTO_SAVE_DATA+"(id INTEGER PRIMARY KEY AUTOINCREMENT, col1 varchar(250), col2 varchar(250), col3 varchar(250), col4 varchar(250), col5 varchar(250))");
        db.execSQL("CREATE TABLE if not exists "+TBL_AUTO_SAVE_DATA_FG_STACKING+"(id INTEGER PRIMARY KEY AUTOINCREMENT, col1 varchar(250), col2 varchar(250), col3 varchar(250), col4 varchar(250), col5 varchar(250))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TBL_NAME+"");
        db.execSQL("DROP TABLE IF EXISTS "+TBL_MATERIAL_ISSUE_SLIP+"");

        // Auto Save
        db.execSQL("DROP TABLE IF EXISTS "+TBL_AUTO_SAVE_DATA+"");
        db.execSQL("DROP TABLE IF EXISTS "+TBL_AUTO_SAVE_DATA_FG_STACKING+"");
        onCreate(db);
    }

    public long insert_data(scanner_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getQr_Code());
        values.put("col2", m.getQty());
        values.put("col3", m.getXicode());
        values.put("col4", m.getXiname());
        values.put("col5", m.getXbook_qty());
        long id = db.insert(TBL_NAME, null, values);
        db.close();
        return id;
    }

    public long insert_autosave_data(scanner_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getQr_Code());
        values.put("col2", m.getQty());
        values.put("col3", m.getXicode());
        values.put("col4", m.getXiname());
        values.put("col5", m.getXbook_qty());
        long id = db.insert(TBL_AUTO_SAVE_DATA, null, values);
        db.close();
        return id;
    }

    public long insert_autosave_data_fg_stacking(scanner_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getQr_Code());
        values.put("col2", m.getQty());
        values.put("col3", m.getXicode());
        values.put("col4", m.getXiname());
        values.put("col5", m.getXbook_qty());
        long id = db.insert(TBL_AUTO_SAVE_DATA_FG_STACKING, null, values);
        db.close();
        return id;
    }

    public long commaninsert_data(comman_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getXcol1());
        values.put("col2", m.getXcol2());
        values.put("col3", m.getXcol3());
        values.put("col4", m.getXcol4());
        values.put("col5", m.getXcol5());
        long id = db.insert(TBL_NAME, null, values);
        db.close();
        return id;
    }

    public long issue_slip_insert_data(String xreq_no, String xreq_dt, String xiname, String xdept, String xqty){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", xreq_no);
        values.put("col2", xreq_dt);
        values.put("col3", xiname);
        values.put("col4", xdept);
        values.put("col5", xqty);
        long id = db.insert(TBL_MATERIAL_ISSUE_SLIP, null, values);
        db.close();
        return id;
    }

    public String issue_slip_CheckItemQty(String dbfield, String fieldValue) {
        String item="";
        String qty = "";
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "SELECT * FROM "+ TBL_MATERIAL_ISSUE_SLIP + " WHERE " + dbfield + " = '"+ fieldValue +"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.moveToFirst()) {
            do {
                cursor.getString(cursor.getColumnIndex("id"));
                item = cursor.getString(cursor.getColumnIndex("col3"));
                qty = cursor.getString(cursor.getColumnIndex("col5"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        cursor.close();
        return qty;
    }

    public boolean CheckDuplicacy(String dbfield, String fieldValue) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "SELECT * FROM "+ TBL_NAME + " WHERE " + dbfield + " = '"+ fieldValue +"'";
        Cursor cursor = sqldb.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean CheckItemQtyIsGreaterThanOrNot(String dbfield, String fieldValue, String sum_of_column, String after_loop_add_qty, String compared_with_qty) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        double xqty = 0;
        Cursor res = sqldb.rawQuery( "select TOTAL("+sum_of_column+") as qty from "+TBL_NAME+" WHERE "+dbfield+" = '"+fieldValue+"'", null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            xqty = Double.parseDouble(res.getString(res.getColumnIndex("qty")));
            res.moveToNext();
        }
        xqty = xqty + Double.parseDouble(after_loop_add_qty);
        double xordered_qty = Double.parseDouble(compared_with_qty.replaceAll(",", ""));
        if(xqty > xordered_qty)
        {
            return true;
        }
        return false;
    }

    public boolean CheckJobQtyIsGreaterThanOrNot(String sum_of_column, String after_loop_add_qty, String compared_with_qty) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        double xqty = 0;
        Cursor res = sqldb.rawQuery( "select TOTAL("+sum_of_column+") as qty from "+TBL_NAME+"", null );
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            xqty = Double.parseDouble(res.getString(res.getColumnIndex("qty")));
            res.moveToNext();
        }
        xqty = xqty + Double.parseDouble(after_loop_add_qty);
        double xordered_qty = Double.parseDouble(compared_with_qty.replaceAll(",", ""));
        if(xqty > xordered_qty)
        {
            return true;
        }
        return false;
    }



    public int CountTableRows(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TBL_NAME+"",null);
        int count = c.getCount();
        return count;
    }

    public Double TotalQtySum(String sum_of_column) {
        SQLiteDatabase db = this.getWritableDatabase();
        double total = 0;
        Cursor cursor = db.rawQuery("SELECT SUM(" + sum_of_column + ") as Total FROM " + TBL_NAME, null);
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndex("Total"));
            return total;
        }
        return total;
    }

    public long issue_req_insert_data(issue_req_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getItem());
        values.put("col2", m.getQty());
        values.put("col3", "-");
        values.put("col4", "-");
        values.put("col5", "-");
        long id = db.insert(TBL_NAME, null, values);
        db.close();
        return id;
    }

    public long issue_slip_insert_data(issue_req_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getItem());
        values.put("col2", m.getQty());
        values.put("col3", m.getXicode());
        values.put("col4", "-");
        values.put("col5", "-");
        long id = db.insert(TBL_NAME, null, values);
        db.close();
        return id;
    }

    public long record_expense_insert_data(expense_model m){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("col1", m.getExpense());
        values.put("col2", m.getAmount());
        values.put("col3", m.getRemarks());
        values.put("col4", "-");
        values.put("col5", "-");
        long id = db.insert(TBL_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<scanner_model> get_data(){
        ArrayList<scanner_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new scanner_model(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3")),
                        cursor.getString(cursor.getColumnIndex("col4")),
                        cursor.getString(cursor.getColumnIndex("col5"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }

    public ArrayList<scanner_model> get_auto_save_data(){
        ArrayList<scanner_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_AUTO_SAVE_DATA + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new scanner_model(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3")),
                        cursor.getString(cursor.getColumnIndex("col4")),
                        cursor.getString(cursor.getColumnIndex("col5"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }

    public ArrayList<scanner_model> get_auto_save_data_fg_stacking(){
        ArrayList<scanner_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_AUTO_SAVE_DATA_FG_STACKING + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new scanner_model(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3")),
                        cursor.getString(cursor.getColumnIndex("col4")),
                        cursor.getString(cursor.getColumnIndex("col5"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }

    public ArrayList<comman_model> comman_get_data(){
        ArrayList<comman_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new comman_model(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3")),
                        cursor.getString(cursor.getColumnIndex("col4")),
                        cursor.getString(cursor.getColumnIndex("col5"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }


    public ArrayList<expense_model> record_expense_get_data(){
        ArrayList<expense_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new expense_model(
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }

    public ArrayList<issue_req_model> issue_req_get_data(){
        ArrayList<issue_req_model> List = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME + "", null);
        if(cursor.moveToFirst()) {
            do {
                List.add(new issue_req_model(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("col1")),
                        cursor.getString(cursor.getColumnIndex("col2")),
                        cursor.getString(cursor.getColumnIndex("col3"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return List;
    }

    public long issue_req_deletedata(issue_req_model m){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, "id"+ " = '"+m.row_no+"'", null);
        db.close();
        return id;
    }

    public long issue_slip_deletedata(issue_req_model m){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, "id"+ " = '"+m.row_no+"'", null);
        db.close();
        return id;
    }

    public long record_expense_deletedata(expense_model m){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, "col1"+ " = '"+m.getExpense()+"'", null);
        db.close();
        return id;
    }

    public long deletedata(scanner_model m){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, "id"+ " = '"+m.getRow_no()+"'", null);
        db.close();
        return id;
    }

    public long comman_deletedata(comman_model m){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, "id"+ " = '"+m.xcol1+"'", null);
        db.close();
        return id;
    }

    public  long clear_data(){
        SQLiteDatabase db = getWritableDatabase();
        long id = db.delete(TBL_NAME, null, null);
        id = db.delete(TBL_MATERIAL_ISSUE_SLIP, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TBL_NAME + "'");
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TBL_MATERIAL_ISSUE_SLIP + "'");
        db.close();
        return id;
    }

    public  long clear_auto_save_data(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ TBL_AUTO_SAVE_DATA);
        return 1;
    }

    public  long clear_auto_save_data_fg_stacking(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from "+ TBL_AUTO_SAVE_DATA_FG_STACKING);
        return 1;
    }
}
