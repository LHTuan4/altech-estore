package com.altech.estore.response;

public class ResponseError {
    public static final int Error_Success = 0;
    public static final int Error_NotFound = -1;
    public static final int Error_BadRequest = -2;
    public static final int Error_Unauthorized = -3;
    public static final int Error_Exception = -4;

    public static String ToErrorMessage(int error) {
        return switch (error) {
            case Error_Success -> "Success";
            case Error_NotFound -> "Not Found";
            case Error_BadRequest -> "Bad Request";
            case Error_Unauthorized -> "Unauthorized";
            case Error_Exception -> "Exception";
            default -> "Error";
        };
    }
}
