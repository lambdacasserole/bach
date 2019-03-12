package com.sauljohnson.bach;

/**
 * Represents a connection capacity.
 *
 * @author Saul Johnson
 */
public class ConnectionCapacity {

    /**
     * The connection capacity (i.e. the maximum number of simultaneous connection).
     */
    private int count;

    /**
     * The connection type.
     */
    private Class type;

    /**
     * Initialises a new instance of a connection capacity.
     *
     * @param count the number number of simultaneous connections
     * @param type  the connection type
     */
    public ConnectionCapacity(int count, Class type) {
        this.count = count;
        this.type = type;
    }

    /**
     * Gets the connection capacity (i.e. the maximum number of simultaneous connection).
     *
     * @return  the connection capacity (i.e. the maximum number of simultaneous connection)
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets the connection type.
     *
     * @return  the connection type
     */
    public Class getType() {
        return type;
    }
}
