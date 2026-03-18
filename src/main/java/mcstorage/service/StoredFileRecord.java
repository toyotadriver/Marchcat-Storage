package mcstorage.service;

import java.sql.Timestamp;

/**
 * Record of a stored file. !!! MUST BE MOVED TO A SEPARATE COMMON MODULE
 */
public record StoredFileRecord(
		String uploadId,
		String originalName,
		String hashName,
		String extension,
		long size,
		Timestamp storedAt) {

}
