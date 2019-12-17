package com.interest.ids.biz.web.filemanager.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.interest.ids.common.project.bean.sm.FileManagerM;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.common.project.il8n.ResourceBundleUtil;
import com.interest.ids.common.project.utils.CommonUtil;
import com.interest.ids.commoninterface.service.filemanager.IFileManagerService;

/**
 * 文件上传controller
 * 
 * @author claude
 *
 */
@Controller
@RequestMapping("/fileManager")
public class FileManagerController {

	private static final Logger log = LoggerFactory.getLogger(FileManagerController.class);

	@Autowired
	IFileManagerService fileManagerService;

	/**
	 * 文件上传类
	 * 
	 * @param request
	 * @return fileId
	 */
	@ResponseBody
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public Response<String> fileUpload(HttpServletRequest request) {
		Response<String> response = new Response<String>();
		// 1、设定上传文件应该存储的路劲
		String filePath = "/srv/fileManager/imageManager";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem ())) {
			filePath = "C:/fileManager/imageManager";
		}
		// 2、判定路径是否存在，如果不存在则创建路径
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		String fileId = request.getParameter("fileId");
		// 根据fileId查询图片名称
		String fileName = null;
		boolean isNeedInsertDB = true;// 默认需要查询文件属性数据
		String oldFileExt = "";
		String oldFileName = "";
		if (StringUtils.isEmpty(fileId)) {// 新上传图片，直接分配图片名称
			fileName = UUID.randomUUID().toString().replaceAll("-", "");
			fileId = UUID.randomUUID().toString().replaceAll("-", "");
		} else {// 根据fileId查询已存在文件的名称
			FileManagerM fileManagerM = this.fileManagerService.selectFileManagerById(fileId);
			if (fileManagerM == null || StringUtils.isEmpty(fileManagerM.getFileName())) {
				fileName = UUID.randomUUID().toString().replaceAll("-", "");
				fileId = UUID.randomUUID().toString().replaceAll("-", "");
			} else {
				fileName = fileManagerM.getFileName();
				oldFileExt = fileManagerM.getFileExt();
				oldFileName = fileName + "." + oldFileExt;
				isNeedInsertDB = false;// 当数据库中存在该文件时不需要重复入库
			}
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultiValueMap<String, MultipartFile> allFiles = multipartRequest.getMultiFileMap();
		try {
			String fileMime = null;
			String fileExt = null;
			String originalName = null;
			for (List<MultipartFile> fileList : allFiles.values()) {
				if (fileList != null && fileList.size() > 0) {
					MultipartFile mfile = fileList.get(0);
					if(mfile.getSize() > 3145728){
						response.setCode(ResponseConstants.CODE_FAILED);
						response.setMessage(ResourceBundleUtil.getBizKey(request.getHeader(ResponseConstants.REQ_LANG), "fileManagerController.filter.big"));//"文件太大，超出3M!"
						return response;
					}
					String originalFilename = mfile.getOriginalFilename();
					if (!StringUtils.isEmpty(originalFilename)) {
						String[] filenames = originalFilename.split("[.]");
						originalName = filenames[0];
						fileExt = filenames.length > 1 ? filenames[filenames.length - 1] : "";
						fileMime = mfile.getContentType();
						String saveFilePath = StringUtils.isEmpty(fileExt) ? (filePath
								+ "/" + fileName) : (filePath + "/" + fileName + "." + fileExt);
						mfile.transferTo(new File(saveFilePath));
						FileManagerM fileM = null;
						if (isNeedInsertDB) {// 需要新增文件数据
							fileM = new FileManagerM();
							fileM.setFileId(fileId);
							fileM.setFileName(fileName);
							fileM.setFileExt(fileExt);
							fileM.setFileMime(fileMime);
							fileM.setOriginalName(originalName);
							this.fileManagerService.insertFileManager(fileM);
						} else {// 存在fileId，但两次上传文件类型不一致，需要更新信息
							fileM = new FileManagerM();
							fileM.setFileId(fileId);
							fileM.setFileName(fileName);
							fileM.setFileExt(fileExt);
							fileM.setFileMime(fileMime);
							fileM.setOriginalName(originalName);
							this.fileManagerService.updateFileManager(fileM);
						}
						if(!fileExt.equals(oldFileExt)){
							File oldFile = new File(filePath + "/" + oldFileName);
							oldFile.delete();
						}
						response.setCode(ResponseConstants.CODE_SUCCESS);
						response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
						response.setResults(fileId);
					}
				}
			}
		} catch (Exception e) {
			log.error("upload file filed. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}

	@ResponseBody
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request,
			HttpServletResponse response) {
		// 1、设定下载文件的路劲
		String filePath = "/srv/fileManager/imageManager";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem())) {
			filePath = "C:/fileManager/imageManager";
		}

		String fileId = request.getParameter("fileId");
		if (StringUtils.isEmpty(fileId)) {
			log.info("file is not exist ");
			return;
		}
		FileManagerM fileM = this.fileManagerService.selectFileManagerById(fileId);
		if (fileM == null) {
			log.info("file is not exist ");
			return;
		}
		String filePathM = null;
		String originalName = null;
		if (StringUtils.isEmpty(fileM.getFileExt())) {
			filePathM = filePath + "/" + fileM.getFileName();
			originalName = fileM.getFileName();
		} else {
			filePathM = filePath + "/" + fileM.getFileName() + "." + fileM.getFileExt();
			originalName = fileM.getOriginalName() + "." + fileM.getFileExt();
		}
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=\"" + originalName + "\"");
		BufferedOutputStream outStream = null;
		try {
			outStream = new BufferedOutputStream(response.getOutputStream());
			byte[] fileByte = file2Bytes(filePathM);
			outStream.write(fileByte);
		} catch (Exception e) {
			log.error("download file filed.");
		} finally {
			if (outStream != null) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					log.error("close io stream error.");
				}
			}
		}
	}
	/**
	 * 文件删除
	 * 
	 * @param request
	 * @return fileId
	 */
	@ResponseBody
	@RequestMapping(value = "/fileDelete", method = RequestMethod.POST)
	public Response<String> fileDelete(@RequestBody JSONObject params) {
		Response<String> response = new Response<String>();
		// 1、设定上传文件应该存储的路劲
		String filePath = "/srv/fileManager/imageManager";// 默认文件在linux系统上的存储路劲
		if ("win".equals(CommonUtil.whichSystem())) {
			filePath = "C:/fileManager/imageManager";
		}
		try {
			String fileIds = params.getString("fileIds");
			// 根据fileId查询图片名称
			List<FileManagerM> fileManagerM = this.fileManagerService.getFileInfo(fileIds.split(","));
			for(FileManagerM file : fileManagerM){
				this.fileManagerService.deleteFileById(file.getFileId());
				String oldFileName = null;
				if (StringUtils.isEmpty(file.getFileExt())) {
					oldFileName = file.getFileName();
				} else {
					oldFileName = file.getFileName() + "." + file.getFileExt();
				}
				File oldFile = new File(filePath + "/" + oldFileName);
				oldFile.delete();
			}
			response.setCode(ResponseConstants.CODE_SUCCESS);
			response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		} catch (Exception e) {
			log.error("delete file filed. error msg : " + e.getMessage());
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
		}
		return response;
	}
	/**
	 * 根据文件id查询文件信息
	 * 
	 * @param request
	 * 			fileIds(逗号隔开)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getFileInfo", method = RequestMethod.POST)
	public Response<List<FileManagerM>> getFileInfo(@RequestBody JSONObject param) {
		Response<List<FileManagerM>> response = new Response<List<FileManagerM>>();
		List<FileManagerM> fileInfos = null;
		try {
			String[] fileIds = param.getString("fileIds").split(",");
			fileInfos = this.fileManagerService.getFileInfo(fileIds);
		} catch (Exception e) {// 否则参数错误
			log.error("getFileInfo error. error msg : " + e);
			response.setCode(ResponseConstants.CODE_ERROR_PARAM);
			response.setMessage(ResponseConstants.CODE_ERROR_PARAM_VALUE);
			return response;
		}
		response.setCode(ResponseConstants.CODE_SUCCESS);
		response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
		response.setResults(fileInfos);
		return response;
	}

	/**
	 * 文件转换为字节
	 * 
	 * @param fileSrc
	 *            文件路径
	 * @return 字节
	 * @throws Exception
	 */
	public byte[] file2Bytes(String fileSrc) throws Exception {

		FileInputStream fis = new FileInputStream(new File(fileSrc));

		byte[] bytes = new byte[fis.available()];
		// 将文件内容写入字节数组，提供测试的case
		fis.read(bytes);

		fis.close();
		return bytes;
	}

}
