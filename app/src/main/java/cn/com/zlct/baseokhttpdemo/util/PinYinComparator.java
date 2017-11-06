package cn.com.zlct.baseokhttpdemo.util;


import java.util.Comparator;

import cn.com.zlct.baseokhttpdemo.model.AreaListCopyEntity;


/**
 *
 * @author xiaanming
 *
 */
public class PinYinComparator implements Comparator<AreaListCopyEntity> {

    public int compare(AreaListCopyEntity o1, AreaListCopyEntity o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getFirstChar().equals("#")) {
            return -1;
        } else if (o1.getFirstChar().equals("#")) {
            return 1;
        } else {
            return o1.getFirstChar().compareTo(o2.getFirstChar());
        }

    }

}

