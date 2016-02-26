package com.hsc.textandris.ui.log;

import java.io.Serializable;


public class LogsItem implements Serializable{
	
private int id;
private String name;
private String msgText;
private String date;
private String repeat;
private boolean isActive;




public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getMessageText() {
    return msgText;
}

public void setMessageText(String msgText) {
    this.msgText = msgText;
}

public String getDate() {
    return date;
}

public void setDate(String date) {
    this.date = date;
}
public String getRepeat() {
    return repeat;
}

public void setRepeat(String repeat) {
    this.repeat = repeat;
}


@Override
public String toString() {
    return "[ Name=" + name + ", Message Text=" +
    		msgText + " , date=" + date + " Id=" +
    		id + " ]";
}

public void setActive(boolean isActive) {
	this.isActive = isActive;
}

public boolean isActive() {
	return isActive;
}} 