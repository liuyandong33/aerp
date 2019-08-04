package build.dream.aerp.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String SQL_SCRIPT_FILE_NAME = "sql/aerp-db-sql.xml";
    private static final String DATABASE_NAME = "aerp-db";
    private static final int DATABASE_VERSION = 4;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DATABASE_VERSION);
        this.context = context;
    }

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, DATABASE_VERSION, errorHandler);
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
                NodeList nodeList = onCreateSqlNodeList.item(0).getChildNodes();
                int length = nodeList.getLength();
                if (length > 0) {
                    for (int index = 0; index < length; index++) {
                        Node node = nodeList.item(index);
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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try {
            AssetManager assetManager = context.getAssets();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(assetManager.open(SQL_SCRIPT_FILE_NAME));
            Element rootElement = document.getDocumentElement();

            NodeList onUpgradeSqlNodeList = rootElement.getElementsByTagName("on.upgrade.sql");
            if (onUpgradeSqlNodeList.getLength() <= 0) {
                return;
            }

            Map<String, List<String>> sqlMap = new LinkedHashMap<String, List<String>>();
            NodeList nodeList = onUpgradeSqlNodeList.item(0).getChildNodes();
            for (int index = 0; index < nodeList.getLength(); index++) {
                Node node = nodeList.item(index);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                NodeList childNodes = node.getChildNodes();
                List<String> sqlList = new ArrayList<String>();
                for (int childNodesIndex = 0; childNodesIndex < childNodes.getLength(); childNodesIndex++) {
                    Node childNode = childNodes.item(childNodesIndex);
                    if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    sqlList.add(childNode.getTextContent().trim());
                }
                sqlMap.put(node.getNodeName(), sqlList);
            }
            for (int version = oldVersion + 1; version <= newVersion; version++) {
                List<String> sqlList = sqlMap.get("version." + version);
                if (CollectionUtils.isEmpty(sqlList)) {
                    continue;
                }
                for (String sql : sqlList) {
                    sqLiteDatabase.execSQL(sql);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化数据库失败！");
        }
    }
}
