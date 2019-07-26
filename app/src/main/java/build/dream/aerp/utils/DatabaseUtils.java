package build.dream.aerp.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import build.dream.aerp.constants.Constants;
import build.dream.aerp.database.DatabaseHelper;

public class DatabaseUtils {
    public static SQLiteDatabase getWritableDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(ApplicationHandler.application);
        return databaseHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase() {
        DatabaseHelper databaseHelper = new DatabaseHelper(ApplicationHandler.application);
        return databaseHelper.getReadableDatabase();
    }

    public static void closeSQLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.close();
    }

    public static ContentValues buildContentValues(Object domain) {
        return buildContentValues(domain, Constants.DEFAULT_DATE_PATTERN);
    }

    public static ContentValues buildContentValues(Object domain, String datePattern) {
        Class<?> domainClass = domain.getClass();
        List<Field> fields = ReflectionUtils.obtainAllFields(domainClass);

        ContentValues contentValues = new ContentValues();
        for (Field field : fields) {
            Class<?> type = field.getType();
            String columnName = NamingStrategyUtils.camelCaseToUnderscore(field.getName());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
            if (type == byte.class || type == Byte.class) {
                Byte value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == short.class || type == Short.class) {
                Short value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == int.class || type == Integer.class) {
                Integer value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == long.class || type == Long.class) {
                Long value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == float.class || type == Float.class) {
                Float value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == double.class || type == Double.class) {
                Double value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == char.class || type == Character.class) {
                Character value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value.toString());
            } else if (type == boolean.class || type == Boolean.class) {
                Boolean value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == String.class) {
                String value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, value);
            } else if (type == Date.class) {
                Date value = ReflectionUtils.getField(field, domain);
                contentValues.put(columnName, simpleDateFormat.format(value));
            }
        }
        return contentValues;
    }

    public static String obtainTableName(Object domain) {
        String simpleName = domain.getClass().getSimpleName();
        return NamingStrategyUtils.camelCaseToUnderscore(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1));
    }

    public static long insert(Object domain) {
        return getWritableDatabase().insert(obtainTableName(domain), null, buildContentValues(domain));
    }

    public static long update(Object domain) {
        return getWritableDatabase().update(obtainTableName(domain), buildContentValues(domain), "id = ?", new String[]{"100"});
    }
}
