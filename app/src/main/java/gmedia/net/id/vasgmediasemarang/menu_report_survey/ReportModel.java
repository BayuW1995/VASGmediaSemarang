package gmedia.net.id.vasgmediasemarang.menu_report_survey;

public class ReportModel {
    private String id = "";
    private String nama = "";
    private boolean has_unit;

    private String unit_id = "";
    private String value = "";

    public ReportModel(String id, String nama, boolean has_unit){
        this.id = id;
        this.has_unit = has_unit;
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }

    public boolean is_unit() {
        return has_unit;
    }

    public String getId() {
        return id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getValue() {
        return value;
    }

    public String getUnit_id() {
        return unit_id;
    }
}
