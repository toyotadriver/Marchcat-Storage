package mcstorage.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import mcstorage.util.HashGen;
import mcstorage.util.Logging;

@Service
public class StorageService {

	private String rootDir;
	private String sep = File.separator;
	private final RedisTemplate<String, String> redisTemplate;

	private final long MAXSIZE = 3000000L;

	public StorageService(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;

		if (System.getProperty("os.name") == "Linux") {
			rootDir = "/var/lib/MarchCat_Storage";
		} else {
			rootDir = "C:\\MarchCat_Storage";
		}
	}

	/**
	 * Check for token in Redis by uploadId and hashed token, write the file and
	 * return the hash of the file.
	 * 
	 * @param file
	 * @param uploadId
	 * @param token
	 * @return
	 * @throws StorageException
	 */
	@Transactional
	public StoredFileRecord store(MultipartFile file, String uploadId) throws StorageException {

		long size = file.getSize();
		if (size > MAXSIZE) {
			throw new StorageException("Your file is too big for mee >.<!");
		}
		
		InputStream is;

			try {
				is = file.getInputStream();
			} catch (IOException e) {
				throw new StorageException("Failed to get the inputStream: ", e);
			}
			
		String hash = HashGen.generateISHash(is);
		String origName = file.getOriginalFilename();
		Logging.log(origName);
		String[] splitName = origName.split("\\."); // Because name can have dots
		String ext = splitName[splitName.length - 1];

		String[] subDirs = getSubDirs(hash);

		String dirs = rootDir + sep + subDirs[0] + sep + subDirs[1];
		Path dirsPath = Paths.get(dirs);
		Path copyPath = Paths.get(dirs + sep + hash + "." + ext);

		StoredFileRecord storedFileRecord = new StoredFileRecord(uploadId, origName, hash, ext, size, Timestamp.from(Instant.now()));

		// Check if the file already exists and return as if it was stored
		

		try {
			Files.createDirectories(dirsPath);
			
			if (!Files.exists(copyPath)) {
				Files.copy(is, copyPath);
			}
			Collection<Object> hashKeysColl = redisTemplate.opsForHash().keys(uploadId)
					.stream()
					.filter(t -> t instanceof String)
					.collect(Collectors.toList());
			hashKeysColl.forEach(t -> {
				t = (String) t; //Well, I filtered it
			}); 
			
			redisTemplate.opsForHash().persist(uploadId, hashKeysColl);
			
		} catch (IOException e) {
			throw new StorageException("Failed to copy the file: " + origName, e);
		}

		return storedFileRecord;
	}

	/**
	 * Return the byte array of the file
	 * @param hashNameWithExt - hashed name with extension
	 * @return byte[]
	 * @throws StorageException
	 */
	public byte[] getBytesOfPicture(String hashNameWithExt) throws StorageException {
		String[] subDirs = getSubDirs(hashNameWithExt);
		String[] splitName = splitHashNameAndExt(hashNameWithExt);

		byte[] bytes = null;

		try (InputStream is = Files
				.newInputStream(Paths.get(rootDir, subDirs[0], subDirs[1], splitName[0] + '.' + splitName[1]))) {
			bytes = is.readAllBytes();
			is.close();
		} catch (IOException e) {
			throw new StorageException("Failed to read bytes of the file: " + hashNameWithExt);
		}

		return bytes;
	}

	/**
	 * Delete the file 
	 * @param hashNameWithExt - hash name with extension.
	 * @throws StorageException
	 */
	public void delete(String hashNameWithExt) throws StorageException {
		String[] splitName = splitHashNameAndExt(hashNameWithExt);

		String[] subDirs = getSubDirs(hashNameWithExt);

		Path path = Paths.get(rootDir, subDirs[0], subDirs[1], splitName[0] + '.' + splitName[1]);

		try {
			Files.delete(path);
		} catch (IOException e) {
			throw new StorageException("Failed to delete the file " + hashNameWithExt, e);
		}
	}

	/**
	 * Get subdirectories for file in the storage that are two first pairs of
	 * characters.
	 * 
	 * @param hashName
	 * @return String[] {(0, 1) , (2, 3)}
	 */
	private String[] getSubDirs(String hashName) {
		String[] subDirs = { hashName.substring(0, 1), hashName.substring(2, 3) };
		return subDirs;
	}

	/**
	 * Return an array of filename and extension.
	 * 
	 * @param fileHashName
	 * @return String[]
	 */
	private String[] splitHashNameAndExt(String fileHashName) {
		int lastDot = fileHashName.lastIndexOf('.');
		String[] splitName = { fileHashName.substring(0, lastDot),
				fileHashName.substring(lastDot + 1, fileHashName.length() - 1) };
		return splitName;
	}
}
