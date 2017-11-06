package cn.com.zlct.baseokhttpdemo.model;

import java.util.List;

/**
 * 行政区域 实体类
 */
public class AreaListCopyEntity {

    private String AreaId;
    private String AreaName;
    private String ParentId;
    private int Layer;
    private String firstChar;
    private List<AreaListEntity.DataEntity> ChirldData;

    public AreaListCopyEntity(String areaId, String areaName, String parentId, int layer, String firstChar, List<AreaListEntity.DataEntity> chirldData) {
        AreaId = areaId;
        AreaName = areaName;
        ParentId = parentId;
        Layer = layer;
        this.firstChar = firstChar;
        ChirldData = chirldData;
    }

    public String getAreaId() {
        return AreaId;
    }

    public void setAreaId(String areaId) {
        AreaId = areaId;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public int getLayer() {
        return Layer;
    }

    public void setLayer(int layer) {
        Layer = layer;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public List<AreaListEntity.DataEntity> getChirldData() {
        return ChirldData;
    }

    public void setChirldData(List<AreaListEntity.DataEntity> chirldData) {
        ChirldData = chirldData;
    }
}
