package gmedia.net.id.vasgmediasemarang.menu_report_survey;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.vasgmediasemarang.utils.SimpleObjectModel;

public class ReportSelectModel extends ReportModel {

    private String api_url;
    private boolean loaded = false;
    private List<SimpleObjectModel> listSelect = new ArrayList<>();

    public ReportSelectModel(String id, String nama, boolean has_unit, String api_url){
        super(id, nama, has_unit);
        this.api_url = api_url;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public String getApi_url() {
        return api_url;
    }

    public List<SimpleObjectModel> getList() {
        return listSelect;
    }

    public void setListSelect(List<SimpleObjectModel> listSelect) {
        this.listSelect = listSelect;
    }
}