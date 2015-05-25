package com.micen.buyers.manager;

import java.util.ArrayList;

import com.micen.buyers.constant.Constants;
import com.micen.buyers.db.DBDataHelper;
import com.micen.buyers.db.DBHelper;
import com.micen.buyers.module.db.CategoryHistory;
import com.micen.buyers.module.db.ListRecord;
import com.micen.buyers.module.db.Module;
import com.micen.buyers.module.db.SearchRecord;
import com.micen.buyers.util.Util;

/**********************************************************
 * @文件名称：DataManager.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年3月13日 上午10:50:25
 * @文件描述：数据帮助类，只负责从数据库中查数据
 * @修改历史：2015年3月13日创建初始版本
 **********************************************************/
public class DataManager
{
	private static DataManager dbManager;

	public static DataManager getInstance()
	{
		if (dbManager == null)
		{
			synchronized (DataManager.class)
			{
				if (dbManager == null)
				{
					dbManager = new DataManager();
				}
			}
		}
		return dbManager;
	}

	/**
	 * 数据库中插入搜索关键词
	 * @param keyWords
	 */
	public void insertKeyWords(String keyWords, String searchType)
	{
		SearchRecord recentSearch = new SearchRecord();
		ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.RECENT_SEARCH_KEYWORDS, null,
				DBHelper.RECENT_KEYWORDS, "'" + keyWords + "'", null, SearchRecord.class);
		if (modules != null && modules.size() != 0)
		{
			DBDataHelper.getInstance().delete(DBHelper.RECENT_SEARCH_KEYWORDS,
					DBHelper.RECENT_KEYWORDS + "=" + "'" + keyWords + "'", null);
		}
		recentSearch.recentKeywords = keyWords;
		recentSearch.visitTime = String.valueOf(System.currentTimeMillis());
		recentSearch.searchType = searchType;
		DBDataHelper.getInstance().insert(DBHelper.RECENT_SEARCH_KEYWORDS, recentSearch);
	}

	/**
	 * 刷新最近搜索关键词列表
	 */
	public ArrayList<SearchRecord> refreshRecentKeywordsList(String searchType)
	{

		ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.RECENT_SEARCH_KEYWORDS, null,
				DBHelper.SEARCH_TYPE, "'" + searchType + "'", "visitTime DESC", SearchRecord.class);
		int maxSize = 10;
		ArrayList<Module> deleteModules = new ArrayList<Module>();
		if (modules.size() > maxSize)
		{
			for (int i = maxSize; i < modules.size(); i++)
			{
				DBDataHelper.getInstance().delete(DBHelper.RECENT_SEARCH_KEYWORDS, modules.get(i));
				deleteModules.add(modules.get(i));
			}
		}
		while (deleteModules.size() > 0)
		{
			modules.remove(deleteModules.get(0));
			deleteModules.remove(deleteModules.get(0));

		}

		ArrayList<SearchRecord> recentSearchDataList = new ArrayList<SearchRecord>();
		for (int i = 0; i < modules.size(); i++)
		{
			SearchRecord moduleBean = (SearchRecord) modules.get(i);
			recentSearchDataList.add(moduleBean);
		}
		return recentSearchDataList;
	}

	public ArrayList<CategoryHistory> initRecentCategory()
	{
		ArrayList<CategoryHistory> categoriesBrowseHisBean = new ArrayList<CategoryHistory>();
		// 目录浏览历史数据
		ArrayList<Module> categroiesModules = DBDataHelper.getInstance().select(DBHelper.CATEGORIES_BROWSE_HISTORY_,
				null, null, null, "visitTime DESC", CategoryHistory.class);
		int number = categroiesModules.size() >= Constants.MAX_RECENT_CATEGORY_NUM ? Constants.MAX_RECENT_CATEGORY_NUM
				: categroiesModules.size();

		for (int i = 0; i < number; i++)
		{
			CategoryHistory moduleBean = (CategoryHistory) categroiesModules.get(i);
			categoriesBrowseHisBean.add(moduleBean);
		}
		return categoriesBrowseHisBean;
	}

	public void deleteRecentKeyWord(String recentKeyword, String searchType)
	{
		DBDataHelper.getInstance().delete(
				DBHelper.RECENT_SEARCH_KEYWORDS,
				DBHelper.RECENT_KEYWORDS + "=" + "'" + recentKeyword + "'" + "and " + DBHelper.MY_WORDS_SEARCH_TYPE
						+ "=" + "'" + searchType + "'", null);
	}

	public void deleteAllRecentKeyWord()
	{
		DBDataHelper.getInstance().delete(DBHelper.RECENT_SEARCH_KEYWORDS, null, null);
	}

	/**
	 * 查出已浏览有效数据并转换为相应类型
	 * @return
	 */
	public ArrayList<String> changeToSearchListRecoder(String type)
	{
		ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.SEARCH_LIST_TYPE_TABLE, null,
				DBHelper.SEARCH_LIST_TYPE, "'" + type + "'", null, ListRecord.class);
		ArrayList<String> productIDs = new ArrayList<String>();
		for (int i = 0; i < modules.size(); i++)
		{
			productIDs.add(((ListRecord) modules.get(i)).searchListID);
		}
		return productIDs;
	}

	/**
	 * 将浏览过的数据加入到表中
	 * @param product
	 */
	public void insertInToSearchListTable(String id, String type)
	{
		// TODO Auto-generated method stub
		ArrayList<Module> modules = DBDataHelper.getInstance().select(DBHelper.SEARCH_LIST_TYPE_TABLE, null,
				DBHelper.SEARCH_LIST_ID, "'" + id + "'", null, ListRecord.class);
		ListRecord searchRecoder = null;
		if (modules != null && modules.size() != 0)
		{
			searchRecoder = ((ListRecord) modules.get(0));
			searchRecoder.time = String.valueOf(System.currentTimeMillis());
			DBDataHelper.getInstance().update(DBHelper.SEARCH_LIST_TYPE_TABLE, searchRecoder);
		}
		else
		{
			// 是一条新记录．．
			searchRecoder = new ListRecord();
			searchRecoder.searchListID = id;
			searchRecoder.time = String.valueOf(System.currentTimeMillis());
			searchRecoder.searchListType = type;
			DBDataHelper.getInstance().insert(DBHelper.SEARCH_LIST_TYPE_TABLE, searchRecoder);
		}
	}

	/**
	 * 将浏览过的无效数据清除
	 * 
	 */
	public void deleteTheUnuseRecord()
	{
		DBDataHelper.getInstance().delete(DBHelper.SEARCH_LIST_TYPE_TABLE, DBHelper.TIME + "<" + Util.calculateTime(),
				null);
	}

}
