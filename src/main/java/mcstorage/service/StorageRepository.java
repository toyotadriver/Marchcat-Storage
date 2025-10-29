package mcstorage.service;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Abstract CRUDRepository interface for storages repositories interfaces
 */
public abstract interface StorageRepository extends CrudRepository<StorageEntity, Integer> {

	//Non-functional
	@Modifying
	@Query("INSERT INTO smthstorage(id, folder) VALUES (:fileId, :folder)")
	public void insertFile(int fileId, String folder);
	
	@Query("SELECT storage_name FROM storages WHERE id = :storageId")
	public String getStorageName(int storageId);
}

