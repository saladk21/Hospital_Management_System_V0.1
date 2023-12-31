package com.example.hospital_management_system_v01;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "HospitalManagementDB.db";
    public static final int DATABASE_VERSION = 1;

    // Common columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PASSWORD = "pass";

    // Doctor table
    public static final String DOCTOR_TABLE_NAME = "Doctor";
    public static final String DOCTOR_COLUMN_ID = "_id";
    public static final String DOCTOR_COLUMN_SPECIALTY = "specialty";

    // Nurse table
    public static final String NURSE_TABLE_NAME = "Nurse";
    public static final String NURSE_COLUMN_ID = "_id";
    public static final String NURSE_COLUMN_DEPARTMENT = "department";

    // Patient table
    public static final String PATIENT_TABLE_NAME = "Patient";
    public static final String PATIENT_COLUMN_ID = "_id";
    public static final String PATIENT_COLUMN_AGE = "age";
    public static final String PATIENT_COLUMN_GENDER = "gender";
    public static final String PATIENT_COLUMN_ILLNESS = "ILL";
    public static final String PATIENT_COLUMN_STATUS = "SEVERE";

    // Medicine table
    public static final String MEDICINE_TABLE_NAME = "Medicine";
    public static final String MEDICINE_COLUMN_ID = "_id";
    public static final String MEDICINE_COLUMN_STOCK = "stock";
    public static final String MEDICINE_COLUMN_EXPIRY_DATE = "expiry_date";

    // Admin table
    public static final String ADMIN_TABLE_NAME = "Admin";
    public static final String ADMIN_COLUMN_USERNAME = "username";
    public static final String ADMIN_COLUMN_ID = "_id";

    // Audit Trail table
    public static final String AUDIT_TRAIL_TABLE_NAME = "AuditTrail";
    public static final String AUDIT_TRAIL_COLUMN_ID = "_id";
    public static final String AUDIT_TRAIL_COLUMN_USER = "user";
    public static final String AUDIT_TRAIL_COLUMN_ACTION = "action";
    public static final String AUDIT_TRAIL_COLUMN_TIMESTAMP = "timestamp";

    // Appointment table
    public static final String APPOINTMENT_TABLE_NAME = "Appointment";
    public static final String APPOINTMENT_COLUMN_ID = "_id";
    public static final String APPOINTMENT_COLUMN_PATIENT_NAME = "patient_name";
    public static final String APPOINTMENT_COLUMN_DOCTOR_ID = "doctor_id";
    public static final String APPOINTMENT_COLUMN_DOCTOR_NAME = "doctor_name";
    public static final String APPOINTMENT_COLUMN_PATIENT_ID = "patient_id";
    public static final String APPOINTMENT_COLUMN_DATE = "date";
    public static final String APPOINTMENT_COLUMN_TIME = "time";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Doctor table
        createTable(db, DOCTOR_TABLE_NAME,
                DOCTOR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        DOCTOR_COLUMN_SPECIALTY + " TEXT");

        // Create Nurse table
        createTable(db, NURSE_TABLE_NAME,
                NURSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        NURSE_COLUMN_DEPARTMENT + " TEXT");

        // Create Patient table
        createTable(db, PATIENT_TABLE_NAME,
                PATIENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT, " +
                        PATIENT_COLUMN_ILLNESS + " TEXT, " +
                        PATIENT_COLUMN_STATUS + " TEXT, " +
                        PATIENT_COLUMN_AGE + " INTEGER, " +
                        PATIENT_COLUMN_GENDER + " TEXT, " +
                        "assigned_to INTEGER REFERENCES " + DOCTOR_TABLE_NAME + "(" + DOCTOR_COLUMN_ID + ") ON DELETE SET NULL, " + // assigned_to foriegn key
                        "prescribed_with INTEGER REFERENCES " + MEDICINE_TABLE_NAME + "(" + MEDICINE_COLUMN_ID + ") ON DELETE SET NULL");// prescribed_with foriegn key



        // Create Medicine table
        createTable(db, MEDICINE_TABLE_NAME,
                MEDICINE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        MEDICINE_COLUMN_EXPIRY_DATE + " TEXT, " +
                        MEDICINE_COLUMN_STOCK + " INTEGER");

        // Create Admin table
        createTable(db, ADMIN_TABLE_NAME,
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_PASSWORD + " TEXT");


        // Create Audit Trail table
        createTable(db, AUDIT_TRAIL_TABLE_NAME,
                AUDIT_TRAIL_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        AUDIT_TRAIL_COLUMN_USER + " TEXT, " +
                        AUDIT_TRAIL_COLUMN_ACTION + " TEXT, " +
                        AUDIT_TRAIL_COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP");

        // Create Appointment table
        createTable(db, APPOINTMENT_TABLE_NAME,
                APPOINTMENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        APPOINTMENT_COLUMN_DATE + " TEXT, " +
                        APPOINTMENT_COLUMN_TIME + " TEXT, " +
                        APPOINTMENT_COLUMN_DOCTOR_ID + " INTEGER, " +
                        APPOINTMENT_COLUMN_DOCTOR_NAME + " TEXT, " +
                        APPOINTMENT_COLUMN_PATIENT_ID + " INTEGER, " +
                        APPOINTMENT_COLUMN_PATIENT_NAME + " TEXT, " +
                        "FOREIGN KEY(" + APPOINTMENT_COLUMN_DOCTOR_ID + ") REFERENCES " +
                        DOCTOR_TABLE_NAME + "(" + DOCTOR_COLUMN_ID + "), " +
                        "FOREIGN KEY(" + APPOINTMENT_COLUMN_PATIENT_ID + ") REFERENCES " +
                        PATIENT_TABLE_NAME + "(" + PATIENT_COLUMN_ID + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades, if needed
    }

    // Universal method to create a table
    private void createTable(SQLiteDatabase db, String tableName, String columns) {
        String tableQuery = "CREATE TABLE " + tableName + " (" + columns + ");";
        db.execSQL(tableQuery);
    }

    // Universal method to add a record to any table
    public long addRecord(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(tableName, null, values);
    }

    // Universal method to retrieve all records from any table
    public Cursor getAllRecords(String tableName) {
        // Get a readable instance of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // Specify the columns you want to retrieve (null means all columns)
        String[] projection = null;

        // Specify the selection criteria (null means all rows)
        String selection = null;
        String[] selectionArgs = null;

        // Specify the sorting order of the results (null means no sorting)
        String sortOrder = null;

        // Perform the query and return the cursor
        return db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }


    // Universal method to get the count of records in any table
    public int getNumberOfRecords(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Universal method to retrieve the audit trail
    public Cursor getAuditTrail() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(AUDIT_TRAIL_TABLE_NAME, null, null, null, null, null, null);
    }

    // Universal method to retrieve a specific record from any table
    public Cursor getRecordById(String tableName, long recordId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String idColumn = getTableIdColumn(tableName);
        return db.query(tableName, null, idColumn + " = " + recordId, null, null, null, null);
    }

    // Universal method to update a record in any table
    public int updateRecord(String tableName, long itemId, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};
        return db.update(tableName, values, selection, selectionArgs);
    }


    // Universal method to delete a record from any table
    public int deleteRecord(String tableName, long recordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String idColumn = getTableIdColumn(tableName);
        return db.delete(tableName, idColumn + " = " + recordId, null);
    }
// Update med Information


    public boolean updateMedicine(int medicineId, String newMedicineName, int newMedicineStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, newMedicineName);
        values.put(MEDICINE_COLUMN_STOCK, newMedicineStock);

        String selection = MEDICINE_COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(medicineId)};

        int updatedRows = db.update(MEDICINE_TABLE_NAME, values, selection, selectionArgs);

        return updatedRows > 0;
    }

    //Assign Doctor to a patient in the Nurse Class


    public boolean assignDoctorToPatient(int patientId, int doctorId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("assigned_to", doctorId);

        // Construct the WHERE clause to update the specific patient ID
        String selection = PATIENT_COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(patientId) };

        // Perform the update operation
        int updatedRows = db.update(PATIENT_TABLE_NAME, values, selection, selectionArgs);

        // Check if the update was successful
        return updatedRows > 0;
    }

    public long bookAppointment(int doctorId, int patientId, String date, String time, String patientName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(APPOINTMENT_COLUMN_DOCTOR_ID, doctorId);
        values.put(APPOINTMENT_COLUMN_PATIENT_ID, patientId);
        values.put(APPOINTMENT_COLUMN_DATE, date);
        values.put(APPOINTMENT_COLUMN_TIME, time);
        values.put(APPOINTMENT_COLUMN_PATIENT_NAME, patientName); // Add patientName to the ContentValues

        // Insert the appointment record
        return db.insert(APPOINTMENT_TABLE_NAME, null, values);
    }



    public Cursor getAllRows(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(tableName, null, null, null, null, null, null);
    }

    // Helper method to get the primary key column for a table
    private String getTableIdColumn(String tableName) {
        switch (tableName) {
            case DOCTOR_TABLE_NAME:
                return DOCTOR_COLUMN_ID;
            case NURSE_TABLE_NAME:
                return NURSE_COLUMN_ID;
            case PATIENT_TABLE_NAME:
                return PATIENT_COLUMN_ID;
            case MEDICINE_TABLE_NAME:
                return MEDICINE_COLUMN_ID;
            case ADMIN_TABLE_NAME:
                return ADMIN_COLUMN_ID;
            default:
                return COLUMN_ID;
        }
    }

    public Cursor checkLogin(String tableName, String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        return db.query(tableName, null, selection, selectionArgs, null, null, null);
    }

    //cursor method for readalldata
    @SuppressLint("Recycle")
    Cursor readAllData() {
        String query = "SELECT * FROM " + APPOINTMENT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
    //cursor method for readalldata2
    @SuppressLint("Recycle")
    Cursor readAllData2() {
        String query = "SELECT * FROM " + PATIENT_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    // ... (Existing code)

    // Universal method to prescribe medicine to a patient
    public void prescribeMedicine(long patientId, long medicineId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("prescribed_with", medicineId);

        String selection = " _id = ?";
        String[] selectionArgs = {String.valueOf(patientId)};

        db.update(PATIENT_TABLE_NAME, values, selection, selectionArgs);
    }

    public int getPatientIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int patientId = -1; // Default value if not found

        // Define the query
        String query = "SELECT " + PATIENT_COLUMN_ID +
                " FROM " + PATIENT_TABLE_NAME +
                " WHERE " + COLUMN_NAME + " = ?";

        // Execute the query
        Cursor cursor = db.rawQuery(query, new String[]{username});

        // Check if the cursor has data
        if (cursor.moveToFirst()) {
            int patientIdColumnIndex = cursor.getColumnIndex(PATIENT_COLUMN_ID);
            if (patientIdColumnIndex != -1) {
                patientId = cursor.getInt(patientIdColumnIndex);
            }
        }


        cursor.close();
        db.close();

        return patientId;
    }

    @SuppressLint("Range")
    public int getDoctorIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int doctorId = -1;

        String[] projection = {DOCTOR_COLUMN_ID};
        String selection = COLUMN_NAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(DOCTOR_TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                doctorId = cursor.getInt(cursor.getColumnIndex(DOCTOR_COLUMN_ID));
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return doctorId;
    }

}


