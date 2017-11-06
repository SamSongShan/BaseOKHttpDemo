package cn.com.zlct.baseokhttpdemo.model;

/**
 * 传递的消息实体
 */
public class MsgEntity {

    private String type;
    private int position;
    private int num;

    public MsgEntity(String type) {
        this.type = type;
    }

    public MsgEntity(String type, int position) {
        this.type = type;
        this.position = position;
    }

    public MsgEntity(String type, int position, int num) {
        this.type = type;
        this.position = position;
        this.num = num;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
