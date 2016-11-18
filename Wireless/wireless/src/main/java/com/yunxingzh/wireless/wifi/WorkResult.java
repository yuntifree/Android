package com.yunxingzh.wireless.wifi;

public class WorkResult {
    private int err;

    private String message;

    public WorkResult(int err, String message){
        this.err = err;
        this.message = message;
    }

    public boolean success(){
        return err == 0;
    }

    public String message(){
        return this.message;
    }

    public static WorkResult SUCCESS = new WorkResult(0, null);
}
