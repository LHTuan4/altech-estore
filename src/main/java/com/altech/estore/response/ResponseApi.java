package com.altech.estore.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseApi {
    private int error;
    private String errorMessage;
    private Object data;

    public ResponseApi(int error) {
        this.error = error;
        this.errorMessage = ResponseError.ToErrorMessage(error);
        data = null;
    }

    public ResponseApi(int error, Object data) {
        this.error = error;
        this.errorMessage = ResponseError.ToErrorMessage(error);
        this.data = data;
    }
}
