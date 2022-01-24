package com.raj.cloud.service.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.raj.cloud.service.model.FileDocument;
import com.raj.cloud.service.model.FileUploadResponse;
import com.raj.cloud.service.repo.FilesStoreRepo;

@Service
public class StoreFilesService {

	@Autowired
	FilesStoreRepo filesRepo;
	
	Path fileStoragePath;
	String fileStorageLocation;
	public StoreFilesService(@Value("${file.storage.location:temp}") String fileStorageLocation) throws IOException {
		this.fileStorageLocation=fileStorageLocation;
		fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
		Files.createDirectories(fileStoragePath);
	}
	
	public FileUploadResponse storeFile(MultipartFile file) throws IOException
	{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path filePath =	Paths.get(fileStoragePath+"\\"+fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		FileUploadResponse response = new FileUploadResponse(fileName,file.getContentType(),"","");
		return response;
	}

	public Resource downloadFile(String fileName) throws MalformedURLException {
		Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(fileName);		
		Resource resource = new UrlResource(path.toUri());
		
		if(resource.exists() && resource.isReadable())
		{
			return resource;
		}
		else {
			throw new RuntimeException("Failed to download file");
		}
	}
	
	public FileUploadResponse saveFile(MultipartFile file) throws IOException
	{
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDocument fileDocument = new FileDocument();
		fileDocument.setFileName(fileName);
		fileDocument.setDocumentFile(file.getBytes());
		fileDocument.setFileType(file.getContentType());
		filesRepo.save(fileDocument);
		FileUploadResponse response = new FileUploadResponse(fileName,file.getContentType(),"","");
		
		return response;
	}
	
	@Cacheable(cacheNames = "files", key = "#fileName")
	public FileDocument fetchFile(String fileName)
	{
		System.out.println("got from database");
		FileDocument file = filesRepo.findByFileName(fileName);
		return file;
	}
}
