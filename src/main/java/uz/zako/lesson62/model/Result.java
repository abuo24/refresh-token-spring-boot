package uz.zako.lesson62.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    private boolean success;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static Result ok(Object data) {
        return new Result(true, null, data);
    }

    public static Result error(String msg) {
        return new Result(false, msg, null);
    }


}
