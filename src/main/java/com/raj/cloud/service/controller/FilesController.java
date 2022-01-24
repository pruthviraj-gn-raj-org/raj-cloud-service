package com.raj.cloud.service.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.raj.cloud.service.config.rabbitmq.NotificationServiceRabbitMQConfig;
import com.raj.cloud.service.model.CallDetails;
import com.raj.cloud.service.model.FileDocument;
import com.raj.cloud.service.model.FileUploadResponse;
import com.raj.cloud.service.service.StoreFilesService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Controller which is used to upload and download files")
public class FilesController {

	@Autowired
	StoreFilesService fileService;
	
	@Autowired
	RabbitTemplate template;
	
	@PostMapping("single/file/upload")
	@ApiOperation("To Upload Single File")
	public FileUploadResponse uploadSingleFile(@RequestParam("file") MultipartFile file) throws IOException
	{
		FileUploadResponse response = fileService.storeFile(file);
		String url = ServletUriComponentsBuilder.fromCurrentContextPath()
		.path("/download/")
		.path(response.getFileName()).toUriString();
		response.setFileDownloadLocation(url);		
		response.setFileViewLocation(url.replaceAll("\\/download/\\b", "/view/"));
		return response;
	}
	
	@RequestMapping("test/{num}")
	public void test(@PathVariable String num)
	{
		//http://demo.twilio.com/docs/voice.xml
		List<String> lis= new ArrayList<String>();
		lis.add(num);
		template.convertAndSend(NotificationServiceRabbitMQConfig.exchange,NotificationServiceRabbitMQConfig.callroutingKey, new CallDetails(lis,"http://demo.twilio.com/docs/voice.xml"));
		//template.convertAndSend(NotificationServiceRabbitMQConfig.exchange,NotificationServiceRabbitMQConfig.simpleMailroutingKey, "sent mail");
	}
	
	@PostMapping("multiple/file/upload")
	@ApiOperation("To Upload Multiple Files")
	public List<FileUploadResponse> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) throws IOException
	{
		List<FileUploadResponse> responseList = new ArrayList<>();
		Arrays.asList(files).stream().forEach(file ->{
		FileUploadResponse response;
		try {
			response = fileService.storeFile(file);
			String url = ServletUriComponentsBuilder.fromCurrentContextPath()
					.path("/download/")
					.path(response.getFileName()).toUriString();
			response.setFileDownloadLocation(url);		
			response.setFileViewLocation(url.replaceAll("\\/download/\\b", "/view/"));
					responseList.add(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		});
		
		return responseList;
	}
	
	@GetMapping("download/{fileName}")
	@ApiOperation("To Download Single File")
	public ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName,HttpServletRequest request) throws IOException
	{
		Resource resource = fileService.downloadFile(fileName);
		String mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())
				.body(resource);
	}
	
	@GetMapping("view/{fileName}")
	@ApiOperation("To View Single File")
	public ResponseEntity<Resource> viewSingleFile(@PathVariable String fileName,HttpServletRequest request) throws IOException
	{
		Resource resource = fileService.downloadFile(fileName);
		String mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName="+resource.getFilename())
				.body(resource);
	}
	
	@GetMapping("download/zip")
	@ApiOperation("To Download Multiple Files")
	public void downloadZip(@RequestParam("fileName") String files[] , HttpServletResponse response) throws IOException {
		
		try(ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())){
			Arrays.asList(files).stream().forEach(file  -> {
				
				try {
					Resource resource = fileService.downloadFile(file);
					ZipEntry zipEntry = new ZipEntry(resource.getFilename());
					zipEntry.setSize(resource.contentLength());
					zos.putNextEntry(zipEntry);
					StreamUtils.copy(resource.getInputStream(),zos);
					zos.closeEntry();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			});
			
			zos.finish();
		}
		response.setStatus(200);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=zipFile");
	}
	
	@PostMapping("save")
	public FileUploadResponse saveFile(@RequestParam("file") MultipartFile file) throws IOException
	{
		FileUploadResponse response = fileService.saveFile(file);
		String url=ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/fetch/")
				.path(response.getFileName()).toUriString();
		response.setFileDownloadLocation(url);		
		response.setFileViewLocation(url.replaceAll("\\/download/\\b", "/view/"));
		return response;
	}
	
	@GetMapping("fetch/{fileName}")
	public ResponseEntity<byte[]> fetchFile(@PathVariable("fileName") String fileName,HttpServletRequest request)
	{
		FileDocument file = fileService.fetchFile(fileName);
		String mimeType = request.getServletContext().getMimeType(file.getFileName());
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+file.getFileName())
				.body(file.getDocumentFile());
	}
}
