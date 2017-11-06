package cn.com.zlct.baseokhttpdemo.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 搜索记录 实体类
 */
@Table(name = "searchRecords")
public class SearchRecords extends Model{

    @Column
    private String searchName;

    public String getSearchName() {
        return searchName;
    }

    public SearchRecords setSearchName(String searchName) {
        this.searchName = searchName;
        return this;
    }
}
