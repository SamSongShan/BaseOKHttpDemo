package cn.com.zlct.baseokhttpdemo.util;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import cn.com.zlct.baseokhttpdemo.model.SearchRecords;


/**
 * 搜索记录 数据库操作工具类
 */
public class SearchDBUtil {

    /**
     * 搜索记录 添加item
     */
    public static void addItem(String key){
        deleteItem(key);
        new SearchRecords().setSearchName(key)
                .save();
    }

    /**
     * 搜索记录 删除item
     */
    public static void deleteItem(String key){
        SearchRecords searchRecords = new Select().from(SearchRecords.class)
                .where("searchName=?", key)
                .executeSingle();
        if (searchRecords != null) {
            searchRecords.delete();
        }
    }

    /**
     * 搜索记录 删除item
     */
    public static void deleteAll(){
        new Delete().from(SearchRecords.class).execute();
    }
}
