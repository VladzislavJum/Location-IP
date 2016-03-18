package by.jum.locationbyip.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import by.jum.locationbyip.models.LocationInformation;

import java.io.ByteArrayOutputStream;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "location";
    private static final String TABLE_NAME_INFO = "location_info";
    private static final int DATABASE_VERSION = 1;

    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String IP = "ip";
    private static final String ID = "id";
    private static final String FLAG = "flag";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_INFO + "(" +
                ID + " INTEGER PRIMARY KEY, " +
                IP + " TEXT, " +
                COUNTRY + " TEXT, " +
                CITY + " TEXT, " +
                FLAG + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addInformation(LocationInformation information, SQLiteDatabase database) {
        ContentValues content = new ContentValues();
        content.put(IP, information.getIp());
        content.put(COUNTRY, information.getCountry());
        content.put(CITY, information.getCity());
        Bitmap flag = information.getFlag();
        if (flag != null) {
            content.put(FLAG, getBytes(flag));
        }
        database.beginTransaction();
        database.insert(TABLE_NAME_INFO, null, content);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public LocationInformation getInformationByIP(String ip, SQLiteDatabase database) {
        LocationInformation information = null;
        String[] columns = new String[]{IP, COUNTRY, CITY, FLAG};
        Cursor cursor = database.query(TABLE_NAME_INFO, columns, IP + "=?", new String[]{ip}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            information = new LocationInformation();
            information.setIp(cursor.getString(cursor.getColumnIndex(IP)));
            information.setCountry(cursor.getString(cursor.getColumnIndex(COUNTRY)));
            information.setCity(cursor.getString(cursor.getColumnIndex(CITY)));
            byte[] flagDb = cursor.getBlob(cursor.getColumnIndex(FLAG));
            information.setFlag(BitmapFactory.decodeByteArray(flagDb, 0, flagDb.length));
        }
        return information;
    }
}
