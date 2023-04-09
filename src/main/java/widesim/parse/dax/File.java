package widesim.parse.dax;

public class File {
    private final String id;
    private final double size;

    public File(String id, double size) {
        this.id = id;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public double getSize() {
        return size;
    }
}
