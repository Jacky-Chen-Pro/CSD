package com.android.incongress.cd.conference.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.beans.ActivityBean;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SpeakerBean;
import com.android.incongress.cd.conference.utils.MyLogger;
import com.android.incongress.cd.conference.utils.StringUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Pair;

/**
 * 数据库字段的建立，以及数据库的删除和重构
 */
public class DbAdapter {
    private static DbAdapter instance = null;

    public static int DB_VERSION = 2;
    private Context mContext;
    private SQLiteDatabase mDb = null;
    private DbHelper mDbHelper = null;

    public static DbAdapter getInstance() {
        if (instance == null) {
            instance = new DbAdapter(AppApplication.instance().getApplicationContext());
        }
        return instance;
    }

    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, context.getFilesDir() + File.separator + Constants.DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTablesIfNotExist(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_AD);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_ALERT);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_CLASS);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_CONFERENCES);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_CONFIELD);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_EXHIBITOR);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_EXHIBITORACTYIVITY);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_MAP);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_MEETING);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_NEWS);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_Note);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_SESSION);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_SPEAKER);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_TIPS);
                db.execSQL("DROP TABLE IF EXISTS " + ConferenceTables.TABLE_INCONGRESS_ROLE);
                onCreate(db);

                SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(mContext);
                Editor edit = preference.edit();
                edit.putInt(Constants.PREFERENCE_DB_VERSION, 0);
                edit.commit();
            }
        }

        public void dropTables(SQLiteDatabase db, String tableName) {
            db.execSQL("DROP TABLE IF EXISTS " + tableName);
        }

        public void createTablesIfNotExist(SQLiteDatabase db) {
            //create table CONFERENCES
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_CONFERENCES + " ("
                    + ConferenceTableField.CONFERENCES_CONFERENCESID + " INT PRIMARY KEY,"
                    + ConferenceTableField.CONFERENCES_ABBREVIATION + " TEXT,"
                    + ConferenceTableField.CONFERENCES_ADMINUSERID + " INT,"
                    + ConferenceTableField.CONFERENCES_CODE + " TEXT,"
                    + ConferenceTableField.CONFERENCES_CONFERENCESADDRESS + " TEXT,"
                    + ConferenceTableField.CONFERENCES_CONFERENCESSTARTTIME + " TEXT,"
                    + ConferenceTableField.CONFERENCES_CONFERENCESENDTIME + " TEXT,"
                    + ConferenceTableField.CONFERENCES_CONFERENCESNAME + " TEXT,"
                    + ConferenceTableField.CONFERENCES_CONFERENCESSTATE + " INT,"
                    + ConferenceTableField.CONFERENCES_CREATETIME + " TEXT,"
                    + ConferenceTableField.CONFERENCES_ENLINK + " TEXT,"
                    + ConferenceTableField.CONFERENCES_POSTERSHOWSTATE + " INT,"
                    + ConferenceTableField.CONFERENCES_POSTERSTATE + " INT,"
                    + ConferenceTableField.CONFERENCES_VIEWSTATE + " INT,"
                    + ConferenceTableField.CONFERENCES_ZHLINK + " TEXT" + ")");

            //create table SESSION
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_SESSION + " ("
                    + ConferenceTableField.SESSION_SESSIONGROUPID + " INT PRIMARY KEY,"
                    + ConferenceTableField.SESSION_SESSIONNAME + " TEXT,"
                    + ConferenceTableField.SESSION_CLASSESID + " INT,"
                    + ConferenceTableField.SESSION_SESSIONDAY + " TEXT,"
                    + ConferenceTableField.SESSION_STARTTIME + " TEXT,"
                    + ConferenceTableField.SESSION_ENDTIME + " TEXT,"
                    + ConferenceTableField.SESSION_CONFIELDID + " INT,"
                    + ConferenceTableField.SESSION_REMARK + " TEXT,"
                    + ConferenceTableField.SESSION_ATTENTION + " INT,"
                    + ConferenceTableField.SESSION_SESSIONNAME_EN + " TEXT,"
                    + ConferenceTableField.SESSION_FACULTYID + " TEXT,"
                    + ConferenceTableField.SESSION_ROLEID + " TEXT"
                    + ")");

            //create table MEETING
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_MEETING + " ("
                    + ConferenceTableField.MEETING_MEETINGID + " INT PRIMARY KEY,"
                    + ConferenceTableField.MEETING_TOPIC + " TEXT,"
                    + ConferenceTableField.MEETING_SUMMARY + " TEXT,"
                    + ConferenceTableField.MEETING_CLASSESID + " INT,"
                    + ConferenceTableField.MEETING_MEETINGDAY + " TEXT,"
                    + ConferenceTableField.MEETING_STARTTIME + " TEXT,"
                    + ConferenceTableField.MEETING_ENDTIME + " TEXT,"
                    + ConferenceTableField.MEETING_CONFIELDID + " INT,"
                    + ConferenceTableField.MEETING_SESSIONGROUPID + " INT,"
                    + ConferenceTableField.MEETING_ATTENTION + " INT,"
                    + ConferenceTableField.MEETING_TOPIC_EN + " TEXT,"
                    + ConferenceTableField.MEETING_SUMMARY_EN + " TEXT,"
                    + ConferenceTableField.MEETING_FACULTYID + " TEXT,"
                    + ConferenceTableField.MEETING_ROLEID + " TEXT"
                    + ")");

            //create table CONFIELD
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_CONFIELD + " ("
                    + ConferenceTableField.CONFIELD_CONFIELDID + " INT PRIMARY KEY,"
                    + ConferenceTableField.CONFIELD_CONFIELDNAME + " TEXT" + ")");

            //create table CLASS
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_CLASS + " ("
                    + ConferenceTableField.CLASS_CLASSESID + " INT PRIMARY KEY,"
                    + ConferenceTableField.CLASS_CONFERENCESID + " INT,"
                    + ConferenceTableField.CLASS_CLASSESCAPACITY + " INT,"
                    + ConferenceTableField.CLASS_CLASSESCODE + " TEXT,"
                    + ConferenceTableField.CLASS_CLASSESLOCATION + " TEXT,"
                    + ConferenceTableField.CLASS_MAPNAME + " TEXT,"
                    + ConferenceTableField.CLASS_CLASSLEVEL + " INT,"
                    + ConferenceTableField.CLASS_CLASSESCODEPINGYIN + " TEXT" + ")");

            //create table SPEAKER
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_SPEAKER + " ("
                    + ConferenceTableField.SPEAKER_SPEAKERID + " INT PRIMARY KEY,"
                    + ConferenceTableField.SPEAKER_CONFERENCESID + " INT,"
                    + ConferenceTableField.SPEAKER_REMARK + " TEXT,"
                    + ConferenceTableField.SPEAKER_SPEAKERFROM + " TEXT,"
                    + ConferenceTableField.SPEAKER_SPEAKERNAME + " TEXT,"
                    + ConferenceTableField.SPEAKER_TYPE + " INT,"
                    + ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN + " TEXT,"
                    + ConferenceTableField.SPEAKER_ATTENTION + " INT default 0,"
                    + ConferenceTableField.SPEAKER_ENNAME + " TEXT,"
                    + ConferenceTableField.SPEAKER_ENTITYID + " INT,"
                    + ConferenceTableField.SPEAKER_PYKEY + " TEXT,"
                    + ConferenceTableField.SPEAKER_USERID + " INT"
                    + ")");

            //create table EXHIBITOR
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_EXHIBITOR + " ("
                    + ConferenceTableField.EXHIBITOR_EXHIBITORSID + " INT PRIMARY KEY,"
                    + ConferenceTableField.EXHIBITOR_ADDRESS + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_TITLE + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_INFO + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_PHONE + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_FAX + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_NET + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_LOCATION + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_LOGO + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_ATTENTION + " INT,"
                    + ConferenceTableField.EXHIBITOR_STORELOGO + " INT,"
                    + ConferenceTableField.EXHIBITOR_ADDRESS_EN + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_TITLE_EN + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_INFO_EN + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_EXHIBITOR_LOCATION + " TEXT,"
                    + ConferenceTableField.EXHIBITOR_MAP_NAME + " TEXT"
                    + ")");

            //create table EXHIBITORACTYIVITY
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_EXHIBITORACTYIVITY + " ("
                    + ConferenceTableField.EXHIBITORACTYIVITY_ACTIVITYID + " INT PRIMARY KEY,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_NAME + " TEXT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_VERSION + " INT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_HOT + " INT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_URL + " TEXT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_LOGO + " TEXT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_STORELOGO + " INT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_STOREURL + " INT,"
                    + ConferenceTableField.EXHIBITORACTYIVITY_ATTENTION + " INT" + ")");

            //create table Notes
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_Note + " ("
                    + ConferenceTableField.NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ConferenceTableField.NOTES_CONTENTS + " TEXT,"
                    + ConferenceTableField.NOTES_START + " varchar,"
                    + ConferenceTableField.NOTES_END + " varchar,"
                    + ConferenceTableField.NOTES_DATE + " varchar,"
                    + ConferenceTableField.NOTES_ROOM + " varchar,"
                    + ConferenceTableField.NOTES_CREATETIME + " varchar,"
                    + ConferenceTableField.NOTES_UPDATETIME + " varchar,"
                    + ConferenceTableField.NOTES_SESSIONID + " varchar,"
                    + ConferenceTableField.NOTES_CLASSID + " varchar,"
                    + ConferenceTableField.NOTES_MEETINGID + " varchar,"
                    + ConferenceTableField.NOTES_TITLE + " varchar" + ")");
            //create table TIPS
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_TIPS + " ("
                    + ConferenceTableField.TIPS_TIPSID + " INT PRIMARY KEY,"
                    + ConferenceTableField.TIPS_CONFERENCESID + " INT,"
                    + ConferenceTableField.TIPS_TIPSCONTENT + " TEXT,"
                    + ConferenceTableField.TIPS_TIPSTIME + " TEXT,"
                    + ConferenceTableField.TIPS_TIPSTITLE + " TEXT,"
                    + ConferenceTableField.TIPS_TIPSTITLE_EN + " TEXT,"
                    + ConferenceTableField.TIPS_TIPSCONTENT_EN + " TEXT" + ")");

            //create table NEWS
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_NEWS + " ("
                    + ConferenceTableField.NEWS_NEWSID + " INT PRIMARY KEY ,"
                    + ConferenceTableField.NEWS_NEWSTITLE + " TEXT,"
                    + ConferenceTableField.NEWS_NEWSSUMMARY + " TEXT,"
                    + ConferenceTableField.NEWS_NEWSIMAGEURL + " TEXT,"
                    + ConferenceTableField.NEWS_NEWSSOURCE + " TEXT,"
                    + ConferenceTableField.NEWS_NEWSDATE + " TEXT,"
                    + ConferenceTableField.NEWS_NEWSSTOREITEM + " INT,"
                    + ConferenceTableField.NEWS_NEWSSTOREDETAIL + " INT,"
                    + ConferenceTableField.NEWS_NEWSCONTENT + " TEXT" + ")");

            //create table ALERT
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_ALERT + " ("
                    + ConferenceTableField.ALERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + ConferenceTableField.ALERT_DATE + " varchar,"
                    + ConferenceTableField.ALERT_ENABLE + " INTEGER,"
                    + ConferenceTableField.ALERT_RELATIVEID + " varchar,"
                    + ConferenceTableField.ALERT_REPEATDISTANCE + " INTEGER,"
                    + ConferenceTableField.ALERT_REPEATTIMES + " INTEGER,"
                    + ConferenceTableField.ALERT_TITLE + " varchar,"
                    + ConferenceTableField.ALERT_START + " varchar,"
                    + ConferenceTableField.ALERT_END + " varchar,"
                    + ConferenceTableField.ALERT_ROOM + " varchar,"
                    + ConferenceTableField.ALERT_TYPE + " INTEGER" + ")");

            //create table AD
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_AD + " ("
                    + ConferenceTableField.AD_ADID + " INT PRIMARY KEY,"
                    + ConferenceTableField.AD_CONFERENCESID + " INT,"
                    + ConferenceTableField.AD_ADIMAGE + " TEXT,"
                    + ConferenceTableField.AD_ADLEVEL + " INT,"
                    + ConferenceTableField.AD_ADLINK + " TEXT,"
                    + ConferenceTableField.AD_IMGURL + " TEXT,"
                    + ConferenceTableField.AD_VERSION + " INT,"
                    + ConferenceTableField.AD_VIEWLEVEL + " INT,"
                    + ConferenceTableField.AD_STOPTIME + " INT" + ")");

            //create table MAP
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_MAP + " ("
                    + ConferenceTableField.MAP_CONFERENCESMAPID + " INT PRIMARY KEY,"
                    + ConferenceTableField.MAP_CONFERENCESID + " INT,"
                    + ConferenceTableField.MAP_MAPREMARK + " TEXT,"
                    + ConferenceTableField.MAP_MAPURL + " TEXT" + ")");

            //create table ROLE
            db.execSQL("CREATE TABLE IF NOT EXISTS "
                    + ConferenceTables.TABLE_INCONGRESS_ROLE + " ("
                    + ConferenceTableField.ROLE_ID + " INT PRIMARY KEY,"
                    + ConferenceTableField.ROLE_NAME + " TEXT,"
                    + ConferenceTableField.ROLE_ENNAME + " TEXT" + ")");
        }
    }

    private DbAdapter(Context context) {
        mContext = context;
        open();
        mDbHelper.createTablesIfNotExist(mDb);
        close();
    }

    public void open() throws SQLException {
        mDbHelper = new DbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public long insert(String tableName, DbBean bean) {
        ContentValues values = new ContentValues();
        List<Pair<String, String>> container = bean.getContainer();

        for (Pair<String, String> pair : container) {
            try {
                values.put(pair.first, pair.second);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (0 != values.size()) {
            return mDb.insert(tableName, null, values);
        }

        // error
        return -1;
    }

    public long insertNotes(String tableName, DbBean bean) {
        ContentValues values = new ContentValues();
        List<Pair<String, String>> container = bean.getContainer();

        for (Pair<String, String> pair : container) {
            try {
                if (pair.first.equals("id")) continue;
                values.put(pair.first, pair.second);
            } catch (Exception e) {
            }
        }

        if (0 != values.size()) {
            return mDb.insert(tableName, ConferenceTableField.NOTES_ID, values);
        }

        // error
        return -1;
    }

    public long deleteNotes(String id) {

        return mDb.delete(ConferenceTables.TABLE_INCONGRESS_Note, " id = ?", new String[]{id});

    }

    public long deleteItems(String table, String whereArgs, String[] args) {

        return mDb.delete(table, whereArgs, args);

    }

    public long insertOnConflict(String tableName, DbBean bean, int conflictAlgorithm) {
        ContentValues values = new ContentValues();
        List<Pair<String, String>> container = bean.getContainer();

        for (Pair<String, String> pair : container) {
            try {
                values.put(pair.first, pair.second);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (0 != values.size()) {
            return mDb.insertWithOnConflict(tableName, null, values, conflictAlgorithm);
        }

        // error
        return -1;
    }

    public long update(String tableName, DbBean bean, String whereClause, String[] whereArgs) {
        ContentValues values = new ContentValues();
        List<Pair<String, String>> container = bean.getContainer();

        for (Pair<String, String> pair : container) {
            try {
                values.put(pair.first, pair.second);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        if (0 != values.size()) {
            return mDb.update(tableName, values, whereClause, whereArgs);
        }
        return -1;
    }

    private static final String FETCH_LIST_SQL = "SELECT %s FROM %s";
    private static final String FETCH_GROUP_LIST_SQL = "SELECT %s FROM %s GROUP BY %s";
    private static final String FETCH_LIST_SQL_WITH_SORT = "SELECT %s FROM %s ORDER BY PINYIN";
    private static final String FETCH_LIST_BY_SQL = "SELECT %s FROM %s WHERE %s=%s ORDER BY PINYIN";
    private static final String QUERY_DICTIONARY = "SELECT %s FROM %s WHERE %s='%s'";

    public String queryDictionary(String tableName, String keyColumn,
                                  String keyValue, String valueColumn) {
        Cursor cursor = null;
        String result = null;

        String sqlString = String.format(QUERY_DICTIONARY, valueColumn,
                tableName, keyColumn, keyValue);
        cursor = mDb.rawQuery(sqlString, null);

        if (null != cursor && cursor.moveToFirst()) {
            result = cursor.getString(0);
        }

        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public void deleteTable(String tableName) {
        mDb.execSQL("DROP TABLE " + tableName);
    }

    public String queryDictionaryBySql(String sqlString) {
        Cursor cursor = null;
        String result = null;

        cursor = mDb.rawQuery(sqlString, null);

        if (null != cursor && cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        if (cursor != null) {
            cursor.close();
        }
        return result;
    }

    public void execSql(String sql) {
        mDb.execSQL(sql);
    }

    // fetch list of one table
    public List<String> fetchSortedList(String tableName, String columnName) {
        Cursor cursor = null;
        List<String> list = null;

        String sqlString = String.format(FETCH_LIST_SQL_WITH_SORT, columnName,
                tableName);
        cursor = mDb.rawQuery(sqlString, null);
        if (null != cursor && cursor.moveToFirst()) {
            list = new ArrayList<String>();
            do {
                String data = cursor.getString(0);
                list.add(data);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public List<String> fetchRawList(String tableName, String columnName) {
        Cursor cursor = null;
        List<String> list = null;

        String sqlString = String.format(FETCH_LIST_SQL, columnName, tableName);
        cursor = mDb.rawQuery(sqlString, null);
        if (null != cursor && cursor.moveToFirst()) {
            list = new ArrayList<String>();
            do {
                String data = cursor.getString(0);
                list.add(data);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    public List<String> fetchRawGroupList(String tableName, String columnName, String groupcolumnName) {
        Cursor cursor = null;
        List<String> list = null;

        String sqlString = String.format(FETCH_GROUP_LIST_SQL, columnName, tableName, groupcolumnName);
        cursor = mDb.rawQuery(sqlString, null);
        if (null != cursor && cursor.moveToFirst()) {
            list = new ArrayList<String>();
            do {
                String data = cursor.getString(0);
                list.add(data);
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * fetch list of one table group by one columnName
     */


    /**
     * fetch list of one table
     *
     * @param sqlString
     * @return
     */
    public List<List<String>> fetchListBySql(String sqlString) {
        Cursor cursor = null;
        List<List<String>> list = null;
        System.out.println("fetchListBySql====" + sqlString);
        cursor = mDb.rawQuery(sqlString, null);
        if (null != cursor && cursor.moveToFirst()) {
            list = new ArrayList<List<String>>();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                List<String> subList = new ArrayList<String>();
                list.add(subList);
            }
            do {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String data = cursor.getString(i);
                    list.get(i).add(data);
                }
            } while (cursor.moveToNext());

        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 判断某张表是否存在
     * @param tabName 表名
     * @return
     */
    public boolean tabIsExist(String tabName) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                    + tabName.trim() + "' ";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public void beginTransaction() {
        mDb.beginTransaction();
    }

    public void endTransaction() {
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }

    public void endFailedTransaction() {
        mDb.endTransaction();
    }


    public void dropTables(String tableName) {
        mDbHelper.dropTables(mDb, tableName);
    }

    // [2012-7-1 Terry]: 
    public void resetTable(String tableName) {
        try {
            int ret = mDb.delete(tableName, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询出某张表的数据总量
    public int getTableItemCount(String tablename) {
        String sql = "select * from " + tablename;
        Cursor cursor = mDb.rawQuery(sql, null);
        return cursor.getCount();
    }

    public int getTableItemCountSql(String sql) {
        Cursor cursor = mDb.rawQuery(sql, null);
        return cursor.getCount();
    }

    /**
     * 获取 讲者/主持人 信息
     **/
    public List<SpeakerBean> getSpeakerInfo(String speaker_name) {
        boolean isHas = false;
        List<SpeakerBean> speakers = new ArrayList<SpeakerBean>();
        Cursor cursor = null;
        try {
            cursor = mDb.query(ConferenceTables.TABLE_INCONGRESS_SPEAKER, null, "speakerName = ? COLLATE NOCASE", new String[]{speaker_name}, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            while (cursor.moveToNext()) {
                isHas = true;
                SpeakerBean speaker = new SpeakerBean();
                speaker.setSpeakerId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERID)));
                speaker.setConferencesId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_CONFERENCESID)));
                speaker.setRemark(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_REMARK)));
                speaker.setSpeakerFrom(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERFROM)));
                speaker.setEnName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ENNAME)));
                speaker.setSpeakerName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAME)));
                speaker.setType(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_TYPE)));
                speaker.setSpeakerNamePingyin(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN)));
                speaker.setAttention(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ATTENTION)));
                speakers.add(speaker);
            }
        }

        if (isHas)
            return speakers;

        /**
         * 说明speakerName字段下没有信息，去speakerNamePinyin下搜索
         */
        if (!isHas) {
            try {
                cursor = mDb.query(ConferenceTables.TABLE_INCONGRESS_SPEAKER, null, "speakerNamePingyin = ? COLLATE NOCASE", new String[]{speaker_name}, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    isHas = true;
                    SpeakerBean speaker = new SpeakerBean();
                    speaker.setSpeakerId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERID)));
                    speaker.setConferencesId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_CONFERENCESID)));
                    speaker.setRemark(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_REMARK)));
                    speaker.setSpeakerFrom(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERFROM)));
                    speaker.setEnName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ENNAME)));
                    speaker.setSpeakerName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAME)));
                    speaker.setType(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_TYPE)));
                    speaker.setSpeakerNamePingyin(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN)));
                    speaker.setAttention(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ATTENTION)));
                    speakers.add(speaker);
                }
            }
        }

        if (isHas)
            return speakers;

        /**
         * 说明speakerName字段下没有信息，去speakerEnName下搜索
         */
        if (!isHas) {
            try {
                cursor = mDb.query(ConferenceTables.TABLE_INCONGRESS_SPEAKER, null, "enName = ? COLLATE NOCASE", new String[]{speaker_name}, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    SpeakerBean speaker = new SpeakerBean();
                    speaker.setSpeakerId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERID)));
                    speaker.setConferencesId(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_CONFERENCESID)));
                    speaker.setRemark(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_REMARK)));
                    speaker.setSpeakerFrom(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERFROM)));
                    speaker.setEnName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ENNAME)));
                    speaker.setSpeakerName(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAME)));
                    speaker.setType(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_TYPE)));
                    speaker.setSpeakerNamePingyin(cursor.getString(cursor.getColumnIndex(ConferenceTableField.SPEAKER_SPEAKERNAMEPINGYIN)));
                    speaker.setAttention(cursor.getInt(cursor.getColumnIndex(ConferenceTableField.SPEAKER_ATTENTION)));
                    speakers.add(speaker);
                }
            }
        }

        return speakers;
    }

    /**
     * 通过user_id来获取参加的活动 包括主持和发言
     **/
    public List<ActivityBean> getActivitysBean(List<SpeakerBean> mSpeakers) {
        List<ActivityBean> activitys = new ArrayList<ActivityBean>();

        if (mSpeakers.size() == 0) {
            return activitys;
        }

        int size = mSpeakers.size();
        for (int i = 0; i < size; i++) {
            SpeakerBean bean = mSpeakers.get(i);
            activitys.addAll(getActivitysBeanFromSpeakerId(bean.getSpeakerId(), bean.getType()));
        }

        //根据id查演讲数据
        return activitys;
    }

    /**
     * 通过id来获得此人在15中身份中所担任的任务
     *
     * @param speakerId type
     * @return
     */
    private List<ActivityBean> getActivitysBeanFromSpeakerId(int speakerId, int type) {
        List<ActivityBean> beans = new ArrayList<ActivityBean>();
        //首先6个是Session表
        //第一个主持
        if (type == Constants.HYRC_SPEAKER_ZHUCHI) {
            Cursor host =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "moderatorIds = ? or moderatorIds like ? or moderatorIds like ? or moderatorIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location = null;
            if (host != null) {
                while (host.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host.getString(host.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host.getInt(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host.getInt(host.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location.moveToNext()) {
                            String location = host_location.getString(host_location.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //第二个 主席
        else if (type == Constants.HYRC_SPEAKER_ZHUXI) {
            Cursor host =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "chairIds = ? or chairIds like ? or chairIds like ? or chairIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location1 = null;
            if (host != null) {
                while (host.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host.getString(host.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host.getInt(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host.getString(host.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host.getInt(host.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location1 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location1.moveToNext()) {
                            String location = host_location1.getString(host_location1.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //第三个共同主席
        else if (type == Constants.HYRC_SPEAKER_COZHUXI) {
            Cursor host2 =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "coChairIds = ? or coChairIds like ? or coChairIds like ? or coChairIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location2 = null;
            if (host2 != null) {
                while (host2.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host2.getInt(host2.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host2.getString(host2.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host2.getInt(host2.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location2 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location2.moveToNext()) {
                            String location = host_location2.getString(host_location2.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //第四个讨论者
        else if (type == Constants.HYRC_SPEAKER_TAOLUN) {
            Cursor host3 =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "discussantIds = ? or discussantIds like ? or discussantIds like ? or discussantIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location3 = null;
            if (host3 != null) {
                while (host3.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host3.getInt(host3.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host3.getString(host3.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host3.getInt(host3.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location3 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location3.moveToNext()) {
                            String location = host_location3.getString(host_location3.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //第五个评论者
        else if (type == Constants.HYRC_SPEAKER_PINGLUN) {
            Cursor host4 =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "panelistIds = ? or panelistIds like ? or panelistIds like ? or panelistIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location4 = null;
            if (host4 != null) {
                while (host4.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host4.getInt(host4.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host4.getString(host4.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host4.getInt(host4.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location4 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location4.moveToNext()) {
                            String location = host_location4.getString(host_location4.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //第六个评委
        else if (type == Constants.HYRC_SPEAKER_JUDGE) {
            Cursor host5 =
                    mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                            "judgeIds = ? or judgeIds like ? or judgeIds like ? or judgeIds like ? COLLATE NOCASE",
                            new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                            null, null, null);
            Cursor host_location5 = null;
            if (host5 != null) {
                while (host5.moveToNext()) {
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));
                    activity.setType(type);
                    activity.setTime(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                            + " " + host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setMeetingId(host5.getInt(host5.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                    activity.setStart_time(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                    activity.setEnd_time(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                    activity.setDate(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                    activity.setActivityNameEN(host5.getString(host5.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);
                    int classId = host5.getInt(host5.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        host_location5 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (host_location5.moveToNext()) {
                            String location = host_location5.getString(host_location5.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //接着是Meeting表的10个类型
        //发言
        else if (type == Constants.HYRC_SPEAKER_FAYAN) {
            //meeting表中的讲者
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "speakerIds = ? or speakerIds like ? or speakerIds like ? or speakerIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location.moveToNext()) {
                            String location = speaker_location.getString(speaker_location.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //Meeting表中的报告
        else if (type == Constants.HYRC_SPEAKER_BAOGAO) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "presenterIds = ? or presenterIds like ? or presenterIds like ? or presenterIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location1 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location1 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location1.moveToNext()) {
                            String location = speaker_location1.getString(speaker_location1.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //speak
        else if (type == Constants.HYRC_SPEAKER_SPEAK) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "speakIds = ? or speakIds like ? or speakIds like ? or speakIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location2 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location2 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location2.moveToNext()) {
                            String location = speaker_location2.getString(speaker_location2.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }
        //术者
        else if (type == Constants.HYRC_SPEAKER_SHUZHE) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "operatorIds = ? or operatorIds like ? or operatorIds like ? or operatorIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location3 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location3 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location3.moveToNext()) {
                            String location = speaker_location3.getString(speaker_location3.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_JIESHUO) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "IVUSInterpreterIds = ? or IVUSInterpreterIds like ? or IVUSInterpreterIds like ? or IVUSInterpreterIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location4 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location4 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location4.moveToNext()) {
                            String location = speaker_location4.getString(speaker_location4.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_BANJIANG) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "AwardPresenterIds = ? or AwardPresenterIds like ? or AwardPresenterIds like ? or AwardPresenterIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location5 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location5 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location5.moveToNext()) {
                            String location = speaker_location5.getString(speaker_location5.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_LINGJIANG) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "AwardRecepientIds = ? or AwardRecepientIds like ? or AwardRecepientIds like ? or AwardRecepientIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location6 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location6 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location6.moveToNext()) {
                            String location = speaker_location6.getString(speaker_location6.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_FANYI) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "interpreterIds = ? or interpreterIds like ? or interpreterIds like ? or interpreterIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location7 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location7 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location7.moveToNext()) {
                            String location = speaker_location7.getString(speaker_location7.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_LEADDISCUSSANT) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "leadDiscussantIds = ? or leadDiscussantIds like ? or leadDiscussantIds like ? or leadDiscussantIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location8 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location8 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location8.moveToNext()) {
                            String location = speaker_location8.getString(speaker_location8.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        } else if (type == Constants.HYRC_SPEAKER_OTHER) {
            Cursor speaker = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null,
                    "otherIds = ? or otherIds like ? or otherIds like ? or otherIds like ? COLLATE NOCASE",
                    new String[]{speakerId + "", speakerId + ",%", "%," + speakerId + "", "%," + speakerId + "" + ",%"},
                    null, null, null);
            Cursor speaker_location8 = null;

            if (speaker != null) {
                while (speaker.moveToNext()) {
                    //讲者
                    ActivityBean activity = new ActivityBean();
                    activity.setActivityName(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setType(type);
                    activity.setTime(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                            + " " + speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setMeetingId(speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                    activity.setStart_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                    activity.setEnd_time(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                    activity.setDate(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                    activity.setActivityNameEN(speaker.getString(speaker.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                    activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                    int classId = speaker.getInt(speaker.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                    //根据classId获取到会议地点的中文名
                    try {
                        speaker_location8 = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""},
                                null, null, null);
                        if (speaker_location8.moveToNext()) {
                            String location = speaker_location8.getString(speaker_location8.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(location);
                        } else {
                            activity.setLocation("");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(activity);
                }
            }
        }

        return beans;
    }


    /**
     * 根据专家id，查询该专家的session和meeting
     *
     * @param speakerId
     */
    public List<ActivityBean> getSessionAndMeetingBySpeakerId(int speakerId) {
        List<ActivityBean> beans = new ArrayList<ActivityBean>();

        //查询session表
        Cursor cursorSession = mDb.query(ConferenceTables.TABLE_INCONGRESS_SESSION, null,
                "facultyId like ? or facultyId like ? or facultyId like ? COLLATE NOCASE",
                new String[]{speakerId + ",%", "%," + speakerId + ",%", "%," + speakerId}, null, null, null);
        Cursor cursorSessionLocation = null;
        if (cursorSession != null) {
            while (cursorSession.moveToNext()) {
                ActivityBean activity = new ActivityBean();
                activity.setActivityName(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME)));

                String facultyId = cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_FACULTYID));
                String roleId = cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_ROLEID));
                if (StringUtils.isEmpty(facultyId)) {
                    activity.setType(-1);
                } else {
                    String[] facultyIds = facultyId.split(",");
                    String[] roleIds = roleId.split(",");
                    activity.setType(getType(speakerId + "", facultyIds, roleIds));
                }

                activity.setTime(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY))
                        + " " + cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                activity.setMeetingId(cursorSession.getInt(cursorSession.getColumnIndex(ConferenceTableField.SESSION_SESSIONGROUPID)));
                activity.setStart_time(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_STARTTIME)));
                activity.setEnd_time(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_ENDTIME)));
                activity.setDate(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_SESSIONDAY)));
                activity.setActivityNameEN(cursorSession.getString(cursorSession.getColumnIndex(ConferenceTableField.SESSION_SESSIONNAME_EN)));
                activity.setIsSessionOrMeeting(AlertBean.TYPE_SESSTION);

                {
                    //设置身份名称
                    Cursor cursorTypeName = mDb.query(ConferenceTables.TABLE_INCONGRESS_ROLE, null, "roleId=?", new String[]{activity.getType() + ""}, null, null, null);
                    if (cursorTypeName != null) {
                        if (cursorTypeName.moveToFirst()) {
                            activity.setTypeName(cursorTypeName.getString(cursorTypeName.getColumnIndex(ConferenceTableField.ROLE_NAME)));
                        }
                    }
                    cursorTypeName.close();
                }

                int classId = cursorSession.getInt(cursorSession.getColumnIndex(ConferenceTableField.SESSION_CLASSESID));

                //根据classId获取到会议地点的中文名
                try {
                    cursorSessionLocation = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""}, null, null, null);
                    if (cursorSessionLocation.moveToNext()) {
                        if (cursorSessionLocation.moveToNext()) {
                            String location = cursorSessionLocation.getString(cursorSessionLocation.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                            activity.setLocation(getLocationChinesAndEnglish(location)[0]);
                            activity.setLocationEn(getLocationChinesAndEnglish(location)[1]);
                        } else {
                            activity.setLocation("");
                            activity.setLocationEn("");
                        }
                    } else {
                        activity.setLocation("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                beans.add(activity);
            }
        }

        //获取所有的Meeting,因为facultyId可能有一个也可能有多个，所以必须区分对待，不能用和sesion查询一样的方法
        //查询session表
        Cursor cursorMeeting = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null, null, null, null, null, null);
        Cursor cursorMeetingLocation = null;

        if (cursorMeeting != null) {
            while (cursorMeeting.moveToNext()) {
                ActivityBean activity = new ActivityBean();
                activity.setActivityName(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));

                String facultyId = cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_FACULTYID));
                String roleId = cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_ROLEID));

                if (StringUtils.isEmpty(facultyId)) {
                    continue;
                } else {
                    String[] facultyIds = facultyId.split(",");
                    String[] roleIds = roleId.split(",");

                    boolean isNeedMeeting = false;
                    for (int i = 0; i < facultyIds.length; i++) {
                        if (StringUtils.isEquals(speakerId + "", facultyIds[i])) {
                            isNeedMeeting = true;
                            break;
                        }
                    }

                    if (isNeedMeeting) {
                        activity.setType(getType(speakerId + "", facultyIds, roleIds));
                    } else {
                        continue;
                    }
                }

                activity.setTime(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY))
                        + " " + cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                activity.setMeetingId(cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                activity.setStart_time(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                activity.setEnd_time(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                activity.setDate(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                activity.setActivityNameEN(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_TOPIC_EN)));
                activity.setIsSessionOrMeeting(AlertBean.TYPE_MEETING);

                {
                    //设置身份名称
                    Cursor cursorTypeName = mDb.query(ConferenceTables.TABLE_INCONGRESS_ROLE, null, "roleId=?", new String[]{activity.getType() + ""}, null, null, null);
                    if (cursorTypeName != null) {
                        if (cursorTypeName.moveToFirst()) {
                            activity.setTypeName(cursorTypeName.getString(cursorTypeName.getColumnIndex(ConferenceTableField.ROLE_NAME)));
                        }
                    }
                    cursorTypeName.close();
                }

                int classId = cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_CLASSESID));

                //根据classId获取到会议地点的中文名
                try {
                    cursorMeetingLocation = mDb.query(ConferenceTables.TABLE_INCONGRESS_CLASS, null, "classesId = ?", new String[]{classId + ""}, null, null, null);
                    if (cursorMeetingLocation.moveToNext()) {
                        String location = cursorMeetingLocation.getString(cursorMeetingLocation.getColumnIndex(ConferenceTableField.CLASS_CLASSESCODE));
                        activity.setLocation(getLocationChinesAndEnglish(location)[0]);
                        activity.setLocationEn(getLocationChinesAndEnglish(location)[1]);
                    } else {
                        activity.setLocation("");
                        activity.setLocationEn("");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                beans.add(activity);
            }
        }

        return beans;
    }


    /**
     * 根据facultyId得到相应的数组位置
     *
     * @param facultyId
     * @param facultyIds
     * @param roleIds    若返回-1表示没有找到该id
     */
    private int getType(String facultyId, String[] facultyIds, String[] roleIds) {
        int position = -1;
        for (int i = 0; i < facultyIds.length; i++) {
            if (StringUtils.isEquals(facultyId, facultyIds[i])) {
                position = i;
            }
        }

        if (position == -1) {
            return 1;
        } else {
            return Integer.parseInt(roleIds[position]);
        }
    }

    private String[] getLocationChinesAndEnglish(String location) {
        return location.split("#@#");
    }


    /**
     * 根据当前时间获取所有的meeting，首先根据会议的位置，再根据时间进行排序
     *
     * @param meetingDay
     */
    public void getAllMeetingByLocationAndTime(String meetingDay) {
        List<MeetingBean> meetings = new ArrayList<MeetingBean>();

        if (!mDb.isOpen()) {
            mDb = mDbHelper.getWritableDatabase();
        }
        Cursor cursorMeeting = mDb.query(ConferenceTables.TABLE_INCONGRESS_MEETING, null, "meetingDay = ?", new String[]{meetingDay},
                ConferenceTableField.MEETING_CLASSESID, null, ConferenceTableField.MEETING_STARTTIME);
//        String sql = "select * from meeting " + ConferenceTables.TABLE_INCONGRESS_MEETING + " where meetingDay = " +meetingDay + "order by " + ConferenceTableField.MEETING_CLASSESID + "ASC" + "," + ConferenceTableField.MEETING_STARTTIME + " DES";
//        mDb.execSQL();

        if (cursorMeeting != null) {
            while (cursorMeeting.moveToNext()) {
                MeetingBean meetingBean = new MeetingBean();
                meetingBean.setMeetingId(cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_MEETINGID)));
                meetingBean.setTopic(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_TOPIC)));
                meetingBean.setClassesId(cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_CLASSESID)));
                meetingBean.setMeetingDay(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_MEETINGDAY)));
                meetingBean.setStartTime(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_STARTTIME)));
                meetingBean.setEndTime(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_ENDTIME)));
                meetingBean.setSessionGroupId(cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_SESSIONGROUPID)));
                meetingBean.setAttention(cursorMeeting.getInt(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_ATTENTION)));
                meetingBean.setTopic_En(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_TOPIC_EN)));
                meetingBean.setFacultyIds(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_FACULTYID)));
                meetingBean.setRoleIds(cursorMeeting.getString(cursorMeeting.getColumnIndex(ConferenceTableField.MEETING_ROLEID)));

                meetings.add(meetingBean);
            }
        }

        if (meetings != null && meetings.size() > 0) {
            for (int i = 0; i < meetings.size(); i++) {
                MyLogger.jLog().i(meetings.get(i).toString());
            }
        }
    }
}
