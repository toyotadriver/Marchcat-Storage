package mcstorage.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mcstorage.service.StorageService;
import mcstorage.service.StoredFileRecord;
import mcstorage.service.EventPublisher;
import mcstorage.service.StorageException;

import static mcstorage.util.Logging.log;

@RestController
public class StorageController {
	
	private final StorageService pictureService;
	private final EventPublisher eventPublisher;
	
	public StorageController(StorageService pictureService, RabbitTemplate rabbitTemplate, EventPublisher eventPublisher) {
		this.pictureService = pictureService;
		this.eventPublisher = eventPublisher;
	}
	
	@PostMapping("/picture/{uploadId}")
	public ResponseEntity<Void> uploadFile(@RequestPart("file") MultipartFile file,
			@PathVariable("uploadId") String uploadId,
			@RequestParam("uploadToken") String uploadToken){
		
		try {
			//Send this through Rabbit
			StoredFileRecord storedFileRecord = pictureService.store(file, uploadId, uploadToken);
			
			//SEND RABBITMQ MESSAGE:
			eventPublisher.publishFileUploaded(storedFileRecord);
			return new ResponseEntity<>(HttpStatus.CREATED);
			
		} catch (StorageException e) {
			log(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}
	
	public ResponseEntity<Void> getFile(){
		
		//TODO
		return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
	}
}
