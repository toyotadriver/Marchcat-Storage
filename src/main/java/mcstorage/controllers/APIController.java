package mcstorage.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import mcstorage.service.PictureService;

@RestController
public class APIController {
	
	private final PictureService pictureService;
	
	public APIController(PictureService pictureService) {
		this.pictureService = pictureService;
	}
	
	public ResponseEntity<Void> uploadFile(@PathVariable int pictureId, @RequestPart("file") MultipartFile file){
		
		
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
}
