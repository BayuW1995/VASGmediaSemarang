package gmedia.net.id.vasgmediasemarang.menu_equipment;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;

public class ModelListEquipment {
	private String romawi;
	private String judul;
	private ArrayList<ModelListEquipmentChild> list = new ArrayList<>();

	public ModelListEquipment(String nomor_romawi, String nama_kategori, ArrayList<ModelListEquipmentChild> list) {
		this.romawi = nomor_romawi;
		this.judul = nama_kategori;
		this.list = list;
	}

	public String getRomawi() {
		return romawi;
	}

	public void setRomawi(String romawi) {
		this.romawi = romawi;
	}

	public String getJudul() {
		return judul;
	}

	public void setJudul(String judul) {
		this.judul = judul;
	}

	public ArrayList<ModelListEquipmentChild> getList() {
		return list;
	}

	public void setList(ModelListEquipmentChild model) {
		list.add(model);
	}


}
