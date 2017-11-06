package cn.com.zlct.baseokhttpdemo.model;

import java.util.List;

/**
 * 行政区域 实体类
 */
public class AreaListEntity {

    /**
     * Code : 200
     * Message : 获取成功
     * Data :
     */

    private int Code;
    private String Message;
    /**
     * AreaId : 110000
     * AreaName : 北京
     * ParentId : 0
     * Layer : 1
     * ChirldData :
     */

    private List<DataEntity> Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public List<DataEntity> getData() {
        return Data;
    }

    public void setData(List<DataEntity> Data) {
        this.Data = Data;
    }

    public static class DataEntity {
        private String AreaId;
        private String AreaName;
        private String ParentId;
        private int Layer;
        /**
         * AreaId : 110100
         * AreaName : 北京市
         * ParentId : 110000
         * Layer : 2
         * ChirldData :
         */

        private List<DataEntity> ChirldData;

        public String getAreaId() {
            return AreaId;
        }

        public void setAreaId(String AreaId) {
            this.AreaId = AreaId;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String AreaName) {
            this.AreaName = AreaName;
        }

        public String getParentId() {
            return ParentId;
        }

        public void setParentId(String ParentId) {
            this.ParentId = ParentId;
        }

        public int getLayer() {
            return Layer;
        }

        public void setLayer(int Layer) {
            this.Layer = Layer;
        }

        public List<DataEntity> getChirldData() {
            return ChirldData;
        }

        public void setChirldData(List<DataEntity> chirldData) {
            ChirldData = chirldData;
        }
    }
}
