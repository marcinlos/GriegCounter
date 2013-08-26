package pl.edu.agh.ki.grieg.util.properties;

public interface PrimitiveMap {
    
    void putByte(String name, byte value);

    void putChar(String name, char value);

    void putShort(String name, short value);

    void putInt(String name, int value);

    void putLong(String name, long value);

    void putFloat(String name, float value);

    void putDouble(String name, double value);

    void putString(String name, String value);

    byte getByte(String name);

    char getChar(String name);

    short getShort(String name);

    int getInt(String name);

    long getLong(String name);

    float getFloat(String name);

    double getDouble(String name);

    String getString(String name);

    byte getByte(String name, byte def);

    char getChar(String name, char def);

    short getShort(String name, short def);

    int getInt(String name, int def);

    long getLong(String name, long def);

    float getFloat(String name, float def);

    double getDouble(String name, double def);

    String getString(String name, String def);
}
