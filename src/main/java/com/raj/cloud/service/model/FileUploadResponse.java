package com.raj.cloud.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponse {

	String fileName;
	String contentType;
	String fileDownloadLocation;
	String fileViewLocation;
	
}
