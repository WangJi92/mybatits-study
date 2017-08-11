package com.common.utils;


public class ActionResult {

    private boolean success;

    private String message;

    private Object data;


    public ActionResult(){
    }

    public ActionResult(boolean success){
        this(success, null, null);
    }

    public ActionResult(boolean success, String message){
        this(success, message, null);
    }

    public ActionResult(boolean success, String message, Object data){
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 获取success
     * @return success success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置success
     * @param success success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取message
     * @return message message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取data
     * @return data data
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置data
     * @param data data
     */
    public void setData(Object data) {
        this.data = data;
    }
}