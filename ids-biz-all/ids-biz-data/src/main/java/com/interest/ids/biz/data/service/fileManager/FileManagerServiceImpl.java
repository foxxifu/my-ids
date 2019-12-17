package com.interest.ids.biz.data.service.fileManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interest.ids.common.project.bean.sm.FileManagerM;
import com.interest.ids.commoninterface.dao.filemanager.FileManagerDaoMapper;
import com.interest.ids.commoninterface.service.filemanager.IFileManagerService;

@Service
public class FileManagerServiceImpl implements IFileManagerService {

	@Autowired
	FileManagerDaoMapper fileManagerDaoMapper;

	@Override
	public void insertFileManager(FileManagerM fileManagerM) {
		this.fileManagerDaoMapper.insertFileManager(fileManagerM);
	}

	@Override
	public FileManagerM selectFileManagerById(String fileId) {
		return this.fileManagerDaoMapper.selectFileManagerById(fileId);
	}

	@Override
	public void updateFileManager(FileManagerM fileM) {
		this.fileManagerDaoMapper.updateFileManager(fileM);
	}

	@Override
	public void deleteFileById(String fileId) {
		this.fileManagerDaoMapper.deleteFileById(fileId);
	}

	@Override
	public List<FileManagerM> getFileInfo(String[] fileIds) {
		return this.fileManagerDaoMapper.getFileInfo(fileIds);
	}

}
