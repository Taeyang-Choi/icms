package com.ogp.icms.global.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

@NoArgsConstructor
@Data
public class ResultCode {
    public int code = -99;
    public String message = "예기치 않은 에러입니다.";
    public Object data = null;

    public ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultCode(int code) {
        this.code = code;
        if(code == 0 ) {
            this.message = "성공적으로 처리하였습니다.";
        }
    }

    public ResultCode data(Object data) {
        this.data = data;
        return this;
    }

    public ResultCode(Exception e) {
        code = -89;
        message = e.getLocalizedMessage();
        e.printStackTrace();
    }

    @Override
    public String toString() {
        if(data == null) {
            return String.format("{\"code\":%d, \"result\":\"%s\"}", code, message);
        }
        else {
            return String.format("{\"code\":%d, \"result\":\"%s\", \"data\":%s}", code, message, data);
        }
    }
}
