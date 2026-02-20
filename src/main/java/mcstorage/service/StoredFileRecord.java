package mcstorage.service;

import java.sql.Timestamp;

public record StoredFileRecord(
		String originalName,
		String hashName,
		String extension,
		long size,
		Timestamp storedAt) {

}
