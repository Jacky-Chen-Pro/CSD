package com.android.incongress.cd.conference.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

public class EasyConDb {
    private static final String CONFERENCES_TXT = "conferences.txt";
    private static final String SESSION_TXT = "session2.0.txt";
    private static final String MEETING_TXT = "meeting2.0.txt";
    private static final String SPEAKER_TXT = "speaker2.0.txt";
    private static final String CLASSES_TXT = "classes2.0.txt";
    private static final String CONFIELD_TXT = "conField.txt";
    private static final String EXHIBITORS_TXT = "exhibitorsNew.txt";
    private static final String TIPS_TXT = "tips.txt";
    private static final String AD_TXT = "ad.txt";
    private static final String CONFERENCE_MAP_TXT = "conferencesMap.txt";
    private static final String ROLE_TXT = "role.txt";

    public static void createDb(String filePath, boolean isRebuild) {

    	SharedPreferences preferences=
    	PreferenceManager.getDefaultSharedPreferences(AppApplication.getContext());
    	Editor editor = preferences.edit();
    	String path = filePath;
        FileInputStream is = null;
        DbAdapter db = DbAdapter.getInstance();
        db.open();
        db.beginTransaction();
        List<DbBean> list = null;
        File file = null;
        
        //conferences.txt 参会易
        try {
            file = new File(path + CONFERENCES_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                DbBean bean = JsonParser.parseConference(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_CONFERENCES);
                db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_CONFERENCES, bean, SQLiteDatabase.CONFLICT_REPLACE);
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //sessionNew.txt 会议
        try {
            file = new File(path + SESSION_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseSession(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_SESSION);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_SESSION, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_HYRC_VISIBLE_NEW, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // meetingNew.txt 会议演讲
        try {
            file = new File(path + MEETING_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseMeeting(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_MEETING);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_MEETING, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_HYRC_VISIBLE_NEW, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // conField.txt 领域
        try {
            file = new File(path + CONFIELD_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseConField(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_CONFIELD);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_CONFIELD, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // classes.txt 会议室
        try {
            file = new File(path + CLASSES_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseClasses(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_CLASS);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_CLASS, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // speaker.txt 演讲者
        try {
            file = new File(path + SPEAKER_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseSpeakers(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_SPEAKER);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_SPEAKER, bean, SQLiteDatabase.CONFLICT_REPLACE);
                	
                }
//                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_JZ_VISIBLE_NEW,
                        true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //exhibitorsNew.txt 参展商
        try {
            file = new File(path + EXHIBITORS_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseExhibitors(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_EXHIBITOR);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_EXHIBITOR, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_CZS_VISIBLE_NEW, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //tips.txt 基本信息
        try {
            file = new File(path + TIPS_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseTipss(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_TIPS);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_TIPS, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_CHZN_VISIBLE_NEW,
                		true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //ad.txt 基本信息
        try {
            file = new File(path + AD_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseAds(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_AD);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_AD, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //conferencesMap.txt 地图
        try {
            file = new File(path + CONFERENCE_MAP_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseMaps(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_MAP);
                for (DbBean bean : list) {
                	db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_MAP, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
                editor.putBoolean(Constants.PREFERENCE_MODULE_CHZN_VISIBLE_NEW,
                        true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO role.txt 身份表
        try {
            file = new File(path + ROLE_TXT);
            if (file.exists()) {
                is = new FileInputStream(file);
                list = JsonParser.parseRole(is);
                is.close();
                db.resetTable(ConferenceTables.TABLE_INCONGRESS_ROLE);
                for (DbBean bean : list) {
                    db.insertOnConflict(ConferenceTables.TABLE_INCONGRESS_ROLE, bean, SQLiteDatabase.CONFLICT_REPLACE);
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        editor.commit();
        db.endTransaction();
        db.close();
    }
}
