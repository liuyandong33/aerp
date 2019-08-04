package build.dream.aerp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import build.dream.aerp.annotations.Table;
import build.dream.aerp.constants.Constants;
import build.dream.aerp.database.DatabaseHelper;
import build.dream.aerp.database.SearchCondition;
import build.dream.aerp.database.SearchModel;

public class DatabaseUtils {
    public static SQLiteDatabase getWritableDatabase(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getReadableDatabase();
    }

    public static void closeSQLiteDatabase(SQLiteDatabase sqLiteDatabase) {
        if (ObjectUtils.isNotNull(sqLiteDatabase)) {
            sqLiteDatabase.close();
        }
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
            ReflectionUtils.makeAccessible(field);
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
                if (value == null) {
                    contentValues.put(columnName, (String) null);
                } else {
                    contentValues.put(columnName, simpleDateFormat.format(value));
                }
            }
        }
        return contentValues;
    }

    public static String[] obtainColumns(Class<?> domainClass) {
        List<Field> fields = ReflectionUtils.obtainAllFields(domainClass);

        int size = fields.size();
        String[] columns = new String[size];
        for (int index = 0; index < size; index++) {
            columns[index] = NamingStrategyUtils.camelCaseToUnderscore(fields.get(index).getName());
        }
        return columns;
    }

    public static String obtainTableName(Class<?> domainClass) {
        Table table = domainClass.getAnnotation(Table.class);
        if (ObjectUtils.isNotNull(table)) {
            return table.name();
        }
        String simpleName = domainClass.getSimpleName();
        return NamingStrategyUtils.camelCaseToUnderscore(simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1));
    }

    public static String obtainTableName(Object domain) {
        return obtainTableName(domain.getClass());
    }

    public static long insert(Context context, Object domain) {
        return insert(getWritableDatabase(context), domain);
    }

    public static long insert(SQLiteDatabase sqLiteDatabase, Object domain) {
        long influenceRowNumber = sqLiteDatabase.insert(obtainTableName(domain), null, buildContentValues(domain));
        closeSQLiteDatabase(sqLiteDatabase);
        return influenceRowNumber;
    }

    public static int update(Context context, Object domain) {
        return update(getWritableDatabase(context), domain);
    }

    public static int update(SQLiteDatabase sqLiteDatabase, Object domain) {
        int influenceRowNumber = sqLiteDatabase.update(obtainTableName(domain), buildContentValues(domain), "id = ?", new String[]{"100"});
        closeSQLiteDatabase(sqLiteDatabase);
        return influenceRowNumber;
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static <T> T instantiateObjectFromCursor(Class<T> clazz, Cursor cursor, String datePattern) {
        T object = ObjectUtils.newInstance(clazz);
        List<Field> fields = ReflectionUtils.obtainAllFields(clazz);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isNative(modifiers)) {
                continue;
            }
            Class<?> fieldType = field.getType();
            String columnName = NamingStrategyUtils.camelCaseToUnderscore(field.getName());
            field.setAccessible(true);
            if (fieldType == Byte.class || fieldType == byte.class) {
                int fieldValue = cursor.getInt(cursor.getColumnIndex(columnName));
                ReflectionUtils.setField(field, object, (byte) fieldValue);
            } else if (fieldType == Short.class || fieldType == short.class) {
                ReflectionUtils.setField(field, object, cursor.getShort(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Integer.class || fieldType == int.class) {
                ReflectionUtils.setField(field, object, cursor.getInt(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Float.class || fieldType == float.class) {
                ReflectionUtils.setField(field, object, cursor.getFloat(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Double.class || fieldType == double.class) {
                ReflectionUtils.setField(field, object, cursor.getDouble(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Long.class || fieldType == long.class) {
                ReflectionUtils.setField(field, object, cursor.getLong(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                ReflectionUtils.setField(field, object, cursor.getInt(cursor.getColumnIndex(columnName)) == 0 ? true : false);
            } else if (fieldType == Character.class || fieldType == char.class) {
                String fieldValue = cursor.getString(cursor.getColumnIndex(columnName));
                if (StringUtils.isNotBlank(fieldValue)) {
                    ReflectionUtils.setField(field, object, fieldValue.charAt(0));
                }
            } else if (fieldType == String.class) {
                ReflectionUtils.setField(field, object, cursor.getString(cursor.getColumnIndex(columnName)));
            } else if (fieldType == Date.class) {
                String value = cursor.getString(cursor.getColumnIndex(columnName));
                Date date = null;
                if (StringUtils.isNotBlank(value)) {
                    date = CustomDateUtils.parse(simpleDateFormat, value);
                }
                ReflectionUtils.setField(field, object, date);
            } else if (fieldType == BigInteger.class) {
                ReflectionUtils.setField(field, object, BigInteger.valueOf(cursor.getInt(cursor.getColumnIndex(columnName))));
            } else if (fieldType == BigDecimal.class) {
                ReflectionUtils.setField(field, object, BigDecimal.valueOf(cursor.getDouble(cursor.getColumnIndex(columnName))));
            }
        }
        return object;
    }

    public static <T> T find(Context context, Class<T> domainClass) {
        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .build();
        return find(context, domainClass, searchModel);
    }

    public static <T> T find(Context context, Class<T> domainClass, SearchModel searchModel) {
        return find(getWritableDatabase(context), domainClass, searchModel);
    }

    public static <T> T find(SQLiteDatabase sqLiteDatabase, Class<T> domainClass, SearchModel searchModel) {
        List<SearchCondition> searchConditions = searchModel.getSearchConditions();
        int size = searchConditions.size();
        List<String> pairs = new ArrayList<String>();
        String[] selectionArgs = new String[size];
        for (int index = 0; index < size; index++) {
            SearchCondition searchCondition = searchConditions.get(index);
            pairs.add(searchCondition.getColumnName() + " " + searchCondition.getOperationSymbol() + " ?");
            selectionArgs[index] = searchCondition.getSearchParameter().toString();
        }
        String selection = StringUtils.join(pairs, " AND ");

        Cursor cursor = sqLiteDatabase.query(obtainTableName(domainClass), obtainColumns(domainClass), selection, selectionArgs, null, null, null);
        T domain = null;
        if (cursor.moveToNext()) {
            domain = instantiateObjectFromCursor(domainClass, cursor, Constants.DEFAULT_DATE_PATTERN);
        }
        closeCursor(cursor);
        closeSQLiteDatabase(sqLiteDatabase);
        return domain;
    }

    public static long delete(Context context, String tableName) {
        return delete(getReadableDatabase(context), tableName);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String tableName) {
        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .build();
        return delete(sqLiteDatabase, tableName, searchModel);
    }

    public static long delete(SQLiteDatabase sqLiteDatabase, String tableName, SearchModel searchModel) {
        List<SearchCondition> searchConditions = searchModel.getSearchConditions();
        int size = searchConditions.size();
        List<String> pairs = new ArrayList<String>();
        String[] selectionArgs = new String[size];
        for (int index = 0; index < size; index++) {
            SearchCondition searchCondition = searchConditions.get(index);
            pairs.add(searchCondition.getColumnName() + " " + searchCondition.getOperationSymbol() + " ?");
            selectionArgs[index] = searchCondition.getSearchParameter().toString();
        }
        String selection = StringUtils.join(pairs, " AND ");
        int influenceRowNumber = sqLiteDatabase.delete(tableName, selection, selectionArgs);
        closeSQLiteDatabase(sqLiteDatabase);
        return influenceRowNumber;
    }
}
