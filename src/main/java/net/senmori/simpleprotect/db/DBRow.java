package net.senmori.simpleprotect.db;

import java.util.HashMap;

public class DBRow extends HashMap<String, Object> {

    public <T> T get(String column) {
        return (T) super.get(column);
    }

    public <T> T get(String column, T def) {
        T res = (T) super.get(column);
        return res == null ? def : res;
    }

    public <T> T remove(String column) {
        return (T) super.remove(column);
    }

    public <T> T remove(String column, T def) {
        T res = (T) super.remove(column);
        return res == null ? def : res;
    }

    public DBRow clone() {
        DBRow row = new DBRow();
        row.putAll(this);
        return row;
    }
}
