package mcstorage.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import mcstorage.util.HashGen;

@Service
public class PictureService {
	
	private String rootDir;
	
	private final StorageRepository storageRepository;
	
	public PictureService(StorageRepository storageRepository) {
		this.storageRepository = storageRepository;
		if(System.getProperty("os.name") == "Linux") {
			rootDir = "/var/lib/MarchCat/Storage/";
		} else {
			rootDir = "C:\\MarchCat Storage\\";
		}
	}

	@Transactional
	public boolean store(MultipartFile file, Picture picture) throws ServiceException {
		InputStream is = null;;
		try {
			file.getInputStream();
		} catch (IOException e) {
			throw new ServiceException("Failed to get the inputStream: ", e);
		}
		String origName = file.getOriginalFilename();
		String ext = picture.getExt();
		String hash = HashGen.generateISHash(is);
		String sep = File.separator;
		
		String subDirs = hash.substring(0,1) + sep + hash.substring(2,3);
		
		String dir = rootDir + sep + subDirs;
		Path copyPath = Paths.get(rootDir + sep + hash.substring(4) + "." + ext);
		
		try {
			Files.copy(is, copyPath);
		} catch (IOException e) {
			throw new ServiceException("Failed to copy the file " + origName, e);
		}
		
		storageRepository.insertFile(picture.getId(), dir);
		
		return true;
	}
}
