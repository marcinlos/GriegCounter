package pl.edu.agh.ki.grieg.data;

public enum Endianess {
    
    /** Big-endian */
    BIG("big-endian"),
    
    /** Little-endian */
    LITTLE("little-endian");
    
    private final String name;
    
    private Endianess(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
