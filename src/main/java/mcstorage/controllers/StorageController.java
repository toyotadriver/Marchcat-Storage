package mcstorage.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mcstorage.service.StorageService;
import mcstorage.service.StoredFileRecord;
import mcstorage.util.Logging;
import mcstorage.security.StorageTempTokenManager;
import mcstorage.service.EventPublisher;
import mcstorage.service.StorageException;

import static mcstorage.util.Logging.log;

@RestController
public class StorageController {
	
	private final StorageService pictureService;
	private final EventPublisher eventPublisher;
	private final StorageTempTokenManager storageTempTokenManager;
	
	public StorageController(StorageService pictureService, 
			RabbitTemplate rabbitTemplate, 
			EventPublisher eventPublisher,
			StorageTempTokenManager storageTempTokenManager) {
		this.pictureService = pictureService;
		this.eventPublisher = eventPublisher;
		this.storageTempTokenManager = storageTempTokenManager;
	}
	
	@PostMapping("/picture/{uploadId}")
	public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file,
			@PathVariable("uploadId") String uploadId,
			@RequestParam("token") String uploadToken){
		
		try {
			Logging.log("Hello from MarchCat Storage!");
			if(storageTempTokenManager.verifyRequest(uploadId, uploadToken)) {
				StoredFileRecord storedFileRecord = pictureService.store(file, uploadId);

				//SEND RABBITMQ MESSAGE:
				eventPublisher.publishFileUploaded(storedFileRecord);
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			
		} catch (StorageException e) {
			log(e.getMessage());
			return ResponseEntity.badRequest().build();
		}
		
	}
	
	@RequestMapping(value = "/picture", method = RequestMethod.OPTIONS)
	public ResponseEntity<?> handleOptions() {
    return ResponseEntity.ok().build();
	}
	
	@GetMapping("/ping")
  public String ping() {
      return "pong";
  }
	
	@GetMapping("/link/{picLink}")
	public byte[] getFile(@PathVariable("picLink") String picLink){
		
		//TODO
		return new byte[] {};
	}
}
