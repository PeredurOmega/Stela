package com.pi.stela.objects;

/**
 * Created by Paul on 24/10/2016.
 */

public class Reminder {
    int Priority;
    String Message;

    public Reminder(int Priority, String Message){
        this.Priority = Priority;
        this.Message = Message;
    }
    public int GetPriority(){
        return this.Priority;
    }
    public String GetMessage(){
        return this.Message;
    }
}
