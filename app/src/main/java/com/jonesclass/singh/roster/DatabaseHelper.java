package com.jonesclass.singh.roster;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String MEMBER_TABLE = "MEMBER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_MEMBER_FIRST_NAME = "MEMBER_FIRST_NAME";
    public static final String COLUMN_MEMBER_LAST_NAME = "MEMBER_LAST_NAME";
    public static final String COLUMN_ACTIVE_MEMBER = "ACTIVE_MEMBER";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "members.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + MEMBER_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MEMBER_FIRST_NAME + " TEXT, " + COLUMN_MEMBER_LAST_NAME +
                " TEXT, " + COLUMN_ACTIVE_MEMBER + " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersionNumber, int newVersionNumber) {

    }

    public boolean addMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MEMBER_FIRST_NAME, member.getFirstName());
        cv.put(COLUMN_MEMBER_LAST_NAME, member.getLastName());
        cv.put(COLUMN_ACTIVE_MEMBER, member.isActive());

        long success = db.insert(MEMBER_TABLE, null, cv);
        db.close();

        if (success == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean removeMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + MEMBER_TABLE + " WHERE " + COLUMN_ID + " = " + member.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }

    }

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + MEMBER_TABLE);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + MEMBER_TABLE + "'");
        db.execSQL("VACUUM");
        db.close();
    }

    public List<Member> getMembers() {
        List<Member> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + MEMBER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                int memberID = cursor.getInt(0);
                String memberFirstName = cursor.getString(1);
                String memberLastName = cursor.getString(2);
                boolean memberActive = cursor.getInt(3) == 1 ? true : false;
                Member member = new Member(memberID, memberFirstName, memberLastName, memberActive);
                returnList.add(member);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;

    }
}
