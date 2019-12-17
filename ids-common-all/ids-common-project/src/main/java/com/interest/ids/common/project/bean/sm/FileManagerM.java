package com.interest.ids.common.project.bean.sm;

import javax.persistence.Column;
import javax.persistence.Table;

import com.interest.ids.common.project.bean.BaseBean;

@Table(name = "ids_file_manager_t")
public class FileManagerM extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "file_id")
	private String fileId;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_ext")
	private String fileExt;

	@Column(name = "file_mime")
	private String fileMime;

	@Column(name = "original_name")
	private String originalName;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExt() {
		return fileExt;
	}

	public void setFileExt(String fileExt) {
		if (fileExt == null) {
			this.fileExt = "";
		}
		this.fileExt = fileExt;
	}

	public String getFileMime() {
		return fileMime;
	}

	public void setFileMime(String fileMime) {
		this.fileMime = fileMime;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}