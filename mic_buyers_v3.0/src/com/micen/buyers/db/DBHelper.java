package com.micen.buyers.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.micen.buyers.constant.Constants;

/**********************************************************
 * @文件名称：DBHelper.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014-2-10 下午02:42:02
 * @文件描述：数据库助手类
 * @修改历史：2014-2-10创建初始版本
 **********************************************************/
public final class DBHelper extends SQLiteOpenHelper
{
	private static DBHelper helper = null;
	private static final String DATABASE_NAME = "micen.db";
	public static final String INTEGER_TYPE = " integer";
	private static final String TEXT_TYPE = " TEXT";
	public static final String ID = "id";
	public static final String TIME = "time";

	/**
	 * 产品浏览历史表
	 */
	public static final String PRODUCT_BROWSE_HISTORY_ = "producthistory";
	public static final String CATEGORY_PRODUCT_ID_ = "productId";
	public static final String NAME_ = "productName";
	public static final String COMPANY_NAME_ = "companyName";
	public static final String PRODUCT_IMAGE_ = "productImageUrl";
	public static final String UNIT_PRICE_ = "unitPrice";
	public static final String UNIT_TYPE_ = "unitType";
	public static final String MIN_ORDER_ = "minOrder";
	public static final String TRADE_TERM_ = "tradeTerms";
	public static final String GOLD_MEMBER_FLAG_ = "goldMember";
	public static final String AS_FLAG_ = "as_";
	public static final String FPRODUCT_FLAG_ = "fProduct";
	public static final String CATEGORY_ID_ = "categoryId";
	public static final String VISIT_TIME_ = "vistTime";
	/**
	 * 目录浏览列表
	 */
	public static final String CATEGORIES_BROWSE_HISTORY_ = "categorieshistory";
	public static final String SEARCH_FLAG = "searchFlag";
	public static final String IS_FAVORITES = "isFavorites";
	public static final String SOURCE_SUBJECT = "sourceSubject";
	public static final String CATEGORIES_HISTORY = "categoriesHistory";
	public static final String CATEGORY = "category";
	public static final String VISIT_TIME = "visitTime";
	/**
	 * 搜索历史表
	 */
	public static final String RECENT_SEARCH_KEYWORDS = "recentSearchKeywords";
	public static final String RECENT_KEYWORDS = "recentKeywords";
	public static final String RECENT_KEYWORDS_VISIT_TIME = "visitTime";
	public static final String SEARCH_TYPE = "searchType";
	/**
	 * 我的关键字
	 */
	public static final String MY_KEY_WORDS = "myKeyWords";
	public static final String KEYWORDS = "keyWords";
	public static final String MY_KEY_WORDS_VISIT_TIME = "visitTime";
	public static final String MY_WORDS_SEARCH_TYPE = "searchType";

	/**
	 * 产品，公司浏览记录
	 */
	public static final String SEARCH_LIST_TYPE_TABLE = "searchListTypeTable";
	public static final String SEARCH_LIST_TYPE = "searchListType";
	public static final String SEARCH_LIST_ID = "searchListID";

	private DBHelper()
	{
		super(Constants.currentActivity.getApplicationContext(), DATABASE_NAME, null, 4);
	}

	public static DBHelper getInstance()
	{
		if (helper == null)
		{
			helper = new DBHelper();
		}
		return helper;
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase(SQLiteDatabase db)
	{
		if (db != null && db.isOpen())
		{
			db.close();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.beginTransaction();
		try
		{
			createAllTables(db);
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// dropTable(db, DOWNLOAD);
		dropTable(db, CATEGORIES_BROWSE_HISTORY_);
		// dropTable(db, BOOKMARK);
		createCategoryHistory(db);
		createSearchList(db);

		db.execSQL("alter table " + PRODUCT_BROWSE_HISTORY_ + " add " + UNIT_TYPE_ + " " + TEXT_TYPE);
	}

	private void createAllTables(SQLiteDatabase db)
	{
		String[] cloumns = new String[]
		{ CATEGORY_PRODUCT_ID_ + TEXT_TYPE, NAME_ + TEXT_TYPE, COMPANY_NAME_ + TEXT_TYPE, PRODUCT_IMAGE_ + TEXT_TYPE,
				UNIT_PRICE_ + TEXT_TYPE, UNIT_TYPE_ + TEXT_TYPE, MIN_ORDER_ + TEXT_TYPE, TRADE_TERM_ + TEXT_TYPE,
				GOLD_MEMBER_FLAG_ + TEXT_TYPE, AS_FLAG_ + TEXT_TYPE, FPRODUCT_FLAG_ + TEXT_TYPE,
				CATEGORY_ID_ + TEXT_TYPE, VISIT_TIME_ + TEXT_TYPE };
		createTable(db, PRODUCT_BROWSE_HISTORY_, cloumns);
		cloumns = new String[]
		{ SEARCH_FLAG + TEXT_TYPE, IS_FAVORITES + TEXT_TYPE, SOURCE_SUBJECT + TEXT_TYPE,
				CATEGORIES_HISTORY + TEXT_TYPE, CATEGORY + TEXT_TYPE, VISIT_TIME + TEXT_TYPE };
		createTable(db, CATEGORIES_BROWSE_HISTORY_, cloumns);
		cloumns = new String[]
		{ RECENT_KEYWORDS + TEXT_TYPE, RECENT_KEYWORDS_VISIT_TIME + TEXT_TYPE, SEARCH_TYPE + TEXT_TYPE };
		createTable(db, RECENT_SEARCH_KEYWORDS, cloumns);
		cloumns = new String[]
		{ KEYWORDS + TEXT_TYPE, MY_KEY_WORDS_VISIT_TIME + TEXT_TYPE, MY_WORDS_SEARCH_TYPE + TEXT_TYPE };
		createTable(db, MY_KEY_WORDS, cloumns);

		cloumns = new String[]
		{ SEARCH_LIST_ID + TEXT_TYPE, SEARCH_LIST_TYPE + TEXT_TYPE };
		createTable(db, SEARCH_LIST_TYPE_TABLE, cloumns);
	}

	private void createSearchList(SQLiteDatabase db)
	{

		String[] cloumns = new String[]
		{ SEARCH_LIST_ID + TEXT_TYPE, SEARCH_LIST_TYPE + TEXT_TYPE };
		createTable(db, SEARCH_LIST_TYPE_TABLE, cloumns);
	}

	private void createCategoryHistory(SQLiteDatabase db)
	{
		String[] cloumns = new String[]
		{ SEARCH_FLAG + TEXT_TYPE, IS_FAVORITES + TEXT_TYPE, SOURCE_SUBJECT + TEXT_TYPE,
				CATEGORIES_HISTORY + TEXT_TYPE, CATEGORY + TEXT_TYPE, VISIT_TIME + TEXT_TYPE };
		createTable(db, CATEGORIES_BROWSE_HISTORY_, cloumns);
	}

	/**
	 * @param sqliteDatabase    
	 * @param table 要创建的数据表名
	 * @param columns 列名
	 */
	private void createTable(SQLiteDatabase sqliteDatabase, String table, String[] columns)
	{
		String createTable = "create table if not exists ";
		String primaryKey = " Integer primary key autoincrement";
		String text = " text";
		char leftBracket = '(';
		char rightBracket = ')';
		char comma = ',';
		int stringBufferSize = 170;
		StringBuffer sql = new StringBuffer(stringBufferSize);
		sql.append(createTable).append(table).append(leftBracket).append(ID).append(primaryKey).append(comma);
		for (String column : columns)
		{
			sql.append(column);
			sql.append(comma);
		}
		sql.append(TIME).append(text).append(rightBracket);
		try
		{
			sqliteDatabase.execSQL(sql.toString());
		}
		catch (Exception e)
		{
			e.getMessage();
		}

	}

	/**
	 * drop表
	 * @param table 需要drop的表名
	 */
	public synchronized void dropTable(final SQLiteDatabase db, final String table)
	{
		SQLiteDatabase database = null;
		try
		{
			database = db == null ? getWritableDatabase() : db;
			database.execSQL("drop table if exists " + table);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 插入数据
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	public synchronized long insert(final String table, final String nullColumnHack, final ContentValues values)
	{
		SQLiteDatabase database = null;
		try
		{
			database = getWritableDatabase();
			return database.insert(table, nullColumnHack, values);
		}
		catch (Exception e)
		{
			return -1;
		}
		finally
		{
			closeDatabase(database);
		}
	}

	/**
	 * 删除数据
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(final String table, final String whereClause, final String[] whereArgs)
	{
		SQLiteDatabase database = null;
		try
		{
			database = getWritableDatabase();
			return database.delete(table, whereClause, whereArgs);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			closeDatabase(database);
		}
	}

	/**
	 * 更新数据
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(final String table, final ContentValues values, final String whereClause, final String[] whereArgs)
	{
		SQLiteDatabase database = null;
		try
		{
			database = getWritableDatabase();
			return database.update(table, values, whereClause, whereArgs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
		finally
		{
			closeDatabase(database);
		}
	}
}
