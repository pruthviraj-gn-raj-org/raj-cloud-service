package com.raj.cloud.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raj.cloud.service.model.FileDocument;
@Repository
public interface FilesStoreRepo extends JpaRepository<FileDocument, Integer>{

	FileDocument findByFileName(String fileName);
	
}
