package com.sauljohnson.bach;

public class ConnectionCapacity {
    private int count;

    private Class type;

    public ConnectionCapacity(int count, Class type) {
        this.count = count;
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public Class getType() {
        return type;
    }
}
