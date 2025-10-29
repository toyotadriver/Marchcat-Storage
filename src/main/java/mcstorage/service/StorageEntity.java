package mcstorage.service;

import org.springframework.data.annotation.Id;

public class StorageEntity {

	@Id
	private int id;
	private String folder;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}

	
}
