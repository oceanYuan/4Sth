package ocean_user.entity;

import lombok.Data;

@Data
public class Result {
 private boolean flag;
 private Integer code;
 private String messsage;
 private Object data;

    public Result() {
    }

    public Result(boolean flag, Integer code, String messsage) {
        this.flag = flag;
        this.code = code;
        this.messsage = messsage;
    }

    public Result(boolean flag, Integer code, String messsage, Object data) {
        this.flag = flag;
        this.code = code;
        this.messsage = messsage;
        this.data = data;
    }
}
