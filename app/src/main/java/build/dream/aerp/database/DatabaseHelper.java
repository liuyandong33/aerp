package build.dream.aerp.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_SCRIPT_FILE_NAME = "sql/aerp-db-sql.xml";
    private static final String DATABASE_NAME = "aerp-db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            AssetManager assetManager = context.getAssets();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(assetManager.open(SQL_SCRIPT_FILE_NAME));
            Element rootElement = document.getDocumentElement();

            NodeList onCreateSqlNodeList = rootElement.getElementsByTagName("on.create.sql");
            if (onCreateSqlNodeList.getLength() > 0) {
                NodeList childNodes = onCreateSqlNodeList.item(0).getChildNodes();
                int length = childNodes.getLength();
                if (length > 0) {
                    for (int index = 0; index < length; index++) {
                        Node node = childNodes.item(index);
                        if (node.getNodeType() != Node.ELEMENT_NODE) {
                            continue;
                        }
                        sqLiteDatabase.execSQL(node.getTextContent().trim());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化数据库失败！");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
