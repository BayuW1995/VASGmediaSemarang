package gmedia.net.id.vasgmediasemarang.utils;

public class SimpleObjectModel {
    private String id;
    private String value;

    public SimpleObjectModel(String id, String value){
        this.id = id;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return value;
    }
}
