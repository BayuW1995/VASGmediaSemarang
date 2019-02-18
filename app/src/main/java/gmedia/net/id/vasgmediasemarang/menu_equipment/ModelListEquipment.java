package gmedia.net.id.vasgmediasemarang.menu_equipment;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;

public class ModelListEquipment {
	private String jenis;
	private ArrayList<ModelListEquipmentChild> list = new ArrayList<>();

	public String getJenis() {
		return jenis;
	}

	public void setJenis(String jenis) {
		this.jenis = jenis;
	}

	public ArrayList<ModelListEquipmentChild> getList() {
		return list;
	}

	public void setList(ModelListEquipmentChild model) {
		list.add(model);
	}
}
