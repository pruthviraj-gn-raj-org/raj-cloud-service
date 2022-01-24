package com.raj.cloud.service.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name="FILES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FileDocument implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	//@GenericGenerator(name = "uuid", strategy = "uuid2")
	private int id;
	
	@Column(name="FILE_NAME")
	private String fileName;
	
	@Column(name="DOCUMENT_FILE")
	@Lob
	private byte[] documentFile;
	
	@Column(name="FILE_TYPE")
	private String fileType;
	
	@Column(name="FILE_SIZE")
	private String fileSize;
}
