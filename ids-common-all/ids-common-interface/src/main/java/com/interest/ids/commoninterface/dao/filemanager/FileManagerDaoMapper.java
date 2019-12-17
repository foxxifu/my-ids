package com.interest.ids.commoninterface.dao.filemanager;

import java.util.List;

import com.interest.ids.common.project.bean.sm.FileManagerM;

/**
 * 文件管理DAO
 * 
 * @author claude
 *
 */
public interface FileManagerDaoMapper{

	/**
	 * 新增文件
	 * 
	 * @param fileManagerM
	 */
    void insertFileManager(FileManagerM fileManagerM);

	/**
	 * 根据文件ID查询文件
	 * 
	 * @param fileId
	 *            文件id
	 * @return 文件属性
	 */
    FileManagerM selectFileManagerById(String fileId);

	/**
	 * 更新文件
	 * 
	 * @param fileM
	 */
    void updateFileManager(FileManagerM fileM);

    /**
     * 根据文件id删除文件
     * 
     * @param fileId
     * 				文件id
     */
	void deleteFileById(String fileId);

	/**
	 * 获取文件信息
	 * 
	 * @param fileIds
	 * @return
	 */
	List<FileManagerM> getFileInfo(String[] fileIds);

	List<FileManagerM> selectFilesByIds(List<String> fileIdList);

}
