package cn.com.zlct.baseokhttpdemo.model;

/**
 * Created by Administrator on 2017/4/25 0025.
 */
public class GetSecretKeyEntity {

    /**
     * Code : 200
     * Message : 操作成功
     * Data : {"ConsumptionId":"a2bcb27b-e24b-4d26-95df-5cfacb946409","UserId":"d6f607f2-c6ec-470f-8d04-a2c0c6f6cbfa","SerialNumber":"2017042511562811700000015","OperateValue":1,"PayType":1,"PrivateKey":"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMqax66KY0Inob9idzhd8Elcu8OfaSNecTIpGgheGn21tZDKNI18Arb+no2r3c/x4tc+guP641oy32URL1jTqP++K8VLGUkMZzrcNJgnRHgdTh+AMcmmcxm/hrtazmLPa//3C8DoastDdG6XxeK3d2RmLitH0cvXnABc5mmGWhzXAgMBAAECgYBWLWfqEesOZJUkNtnHHA3s5ojnOJMb/DvhviHYlU5nUjcckyWvWKQ++iau0//RR23ZaDl8h2bVIvZqotiky8MB4z3POjlY3zkz5Q17MUnwc/YdlJOMdjjWZqru08W2fq2JW2AB+LrhvQICcyHtQ3w5SwMiL3QaZWxO1d0VE1lxoQJBAOXP9QH7Rsd7hOekZPlF2vNY2ugPl5XFsrRAVlOo0i2LtRQ1KjU15cKpe0kfmij9TN2t8U158XL71KNRI03reIUCQQDhsR/QjPRErmjdz+5sud5QEIKv2nsmVqrlgARIPA/plLGDR8EMNJ3fYqrSAdugf+ztvHWcPwD8keD84Xh3/uyrAkBb97wSHe/2Vt0aInTyON1lc1KvecXs/yAD+JdThYUPCxDdVGVexAH9w/t9iPMVokDHmhuuKLXSkStIbGkXfrtNAkBF1Byo1QO6wE+32V7GixeKpCEbMbkKmqQTj/FDPDocJiJqIOhM03bJJ+j8QxDl7s6qm7Wz2xZ+DtCSzVwNnHUzAkB6BUWX8Vf1GqTf5i1/OpTPbPX+fxwdQAFG7YhMk/9lzpWlh6o97OR40iLjWFNDPMBRi3TkZ1oIQsGYGE3FpVZC","NotifyUrl":"http://139.129.243.229/ZhongLiManage/Pay/AliPayNotify"}
     */

    private int Code;
    private String Message;
    private DataEntity Data;

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

    public DataEntity getData() {
        return Data;
    }

    public void setData(DataEntity Data) {
        this.Data = Data;
    }

    public static class DataEntity {


        /**
         * ConsumptionId : a2bcb27b-e24b-4d26-95df-5cfacb946409
         * UserId : d6f607f2-c6ec-470f-8d04-a2c0c6f6cbfa
         * SerialNumber : 2017042511562811700000015
         * OperateValue : 1.0
         * PayType : 1
         * PrivateKey : MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMqax66KY0Inob9idzhd8Elcu8OfaSNecTIpGgheGn21tZDKNI18Arb+no2r3c/x4tc+guP641oy32URL1jTqP++K8VLGUkMZzrcNJgnRHgdTh+AMcmmcxm/hrtazmLPa//3C8DoastDdG6XxeK3d2RmLitH0cvXnABc5mmGWhzXAgMBAAECgYBWLWfqEesOZJUkNtnHHA3s5ojnOJMb/DvhviHYlU5nUjcckyWvWKQ++iau0//RR23ZaDl8h2bVIvZqotiky8MB4z3POjlY3zkz5Q17MUnwc/YdlJOMdjjWZqru08W2fq2JW2AB+LrhvQICcyHtQ3w5SwMiL3QaZWxO1d0VE1lxoQJBAOXP9QH7Rsd7hOekZPlF2vNY2ugPl5XFsrRAVlOo0i2LtRQ1KjU15cKpe0kfmij9TN2t8U158XL71KNRI03reIUCQQDhsR/QjPRErmjdz+5sud5QEIKv2nsmVqrlgARIPA/plLGDR8EMNJ3fYqrSAdugf+ztvHWcPwD8keD84Xh3/uyrAkBb97wSHe/2Vt0aInTyON1lc1KvecXs/yAD+JdThYUPCxDdVGVexAH9w/t9iPMVokDHmhuuKLXSkStIbGkXfrtNAkBF1Byo1QO6wE+32V7GixeKpCEbMbkKmqQTj/FDPDocJiJqIOhM03bJJ+j8QxDl7s6qm7Wz2xZ+DtCSzVwNnHUzAkB6BUWX8Vf1GqTf5i1/OpTPbPX+fxwdQAFG7YhMk/9lzpWlh6o97OR40iLjWFNDPMBRi3TkZ1oIQsGYGE3FpVZC
         * NotifyUrl : http://139.129.243.229/ZhongLiManage/Pay/AliPayNotify
         */

        private String ConsumptionId;
        private String UserId;
        private String SerialNumber;
        private double OperateValue;
        private int PayType;
        private String PrivateKey;
        private String NotifyUrl;

        public String getConsumptionId() {
            return ConsumptionId;
        }

        public void setConsumptionId(String ConsumptionId) {
            this.ConsumptionId = ConsumptionId;
        }

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String UserId) {
            this.UserId = UserId;
        }

        public String getSerialNumber() {
            return SerialNumber;
        }

        public void setSerialNumber(String SerialNumber) {
            this.SerialNumber = SerialNumber;
        }

        public double getOperateValue() {
            return OperateValue;
        }

        public void setOperateValue(double OperateValue) {
            this.OperateValue = OperateValue;
        }

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int PayType) {
            this.PayType = PayType;
        }

        public String getPrivateKey() {
            return PrivateKey;
        }

        public void setPrivateKey(String PrivateKey) {
            this.PrivateKey = PrivateKey;
        }

        public String getNotifyUrl() {
            return NotifyUrl;
        }

        public void setNotifyUrl(String NotifyUrl) {
            this.NotifyUrl = NotifyUrl;
        }
    }
}
