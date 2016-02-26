package com.hsc.textandris.contacts;

public class Model {

private String name;
private boolean selected;

public Model(String name, boolean isSelected) {
    this.name = name;
    selected = isSelected;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public boolean isSelected() {
    return selected;
}

public void setSelected(boolean selected) {
    this.selected = selected;
}

} 