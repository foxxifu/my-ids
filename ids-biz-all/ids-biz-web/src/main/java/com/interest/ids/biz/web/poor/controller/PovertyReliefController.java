package com.interest.ids.biz.web.poor.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.interest.ids.biz.web.poor.DTO.PovertyReliefDto;
import com.interest.ids.common.project.bean.sm.Page;
import com.interest.ids.common.project.bean.sm.PovertyReliefObjectT;
import com.interest.ids.common.project.bean.sm.QueryPovertyRelief;
import com.interest.ids.common.project.bean.sm.UserInfo;
import com.interest.ids.common.project.constant.Response;
import com.interest.ids.common.project.constant.ResponseConstants;
import com.interest.ids.commoninterface.service.poor.PovertyReliefService;

@Controller
@RequestMapping("/povertyRelief")
public class PovertyReliefController {
	
	@Resource
	private PovertyReliefService povertyReliefService;

	private static final Logger log = LoggerFactory.getLogger(PovertyReliefController.class);

	/**
	 * 添加扶贫用户
	 * 
	 * @param povertyRelief
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/insertPovertyRelief", method = RequestMethod.POST)
	public Response<PovertyReliefDto> insertPovertyRelief(
			@RequestBody PovertyReliefDto povertyReliefDto, HttpSession session) {
		Response<PovertyReliefDto> response = new Response<PovertyReliefDto>();
		Object obj = session.getAttribute("user");
		if (null != povertyReliefDto && null != obj) {
			UserInfo user = (UserInfo) obj;
			PovertyReliefObjectT povertyRelief = new PovertyReliefObjectT();
			BeanUtils.copyProperties(povertyReliefDto, povertyRelief);
			povertyRelief.setCreateTime(System.currentTimeMillis());
			povertyRelief.setCreateUserId(user.getId());
			povertyRelief.setCounty("中国");
			int result = povertyReliefService.insertPovertyRelief(povertyRelief);
			if (result == 1) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("insert povertyRelief success, povertyRelief name is "
						+ povertyReliefDto.getUserName());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("insert povertyRelief fail, povertyRelief name is "
						+ povertyReliefDto.getUserName());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("insert povertyRelief fail, povertyRelief is null");
		}
		return response;
	}

	/**
	 * 根据ID查询扶贫用户
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/selectPovertyReliefById", method = RequestMethod.POST)
	@ResponseBody
	public Response<PovertyReliefDto> selectPovertyReliefById(
			@RequestBody PovertyReliefDto povertyReliefDto) {
		Response<PovertyReliefDto> response = new Response<PovertyReliefDto>();
		if (null != povertyReliefDto && null != povertyReliefDto.getId()) {
			PovertyReliefObjectT aid = povertyReliefService
					.selectPovertyReliefById(povertyReliefDto.getId());
			if (null != aid) {
				BeanUtils.copyProperties(aid, povertyReliefDto);
				response.setResults(povertyReliefDto);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("get povertyRelief by id  success, povertyRelief id is "
						+ povertyReliefDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("get povertyRelief by id  fail, povertyRelief id is "
						+ povertyReliefDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("get povertyRelief by id  fail, povertyRelief id is null");
		}

		return response;
	}

	/**
	 * 根据ID删除扶贫用户
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePovertyReliefById", method = RequestMethod.POST)
	public Response<PovertyReliefDto> deletePovertyReliefById(
			@RequestBody PovertyReliefDto povertyReliefDto) {
		Response<PovertyReliefDto> response = new Response<PovertyReliefDto>();
		if (null != povertyReliefDto && null != povertyReliefDto.getId()) {
			boolean result = povertyReliefService
					.deletePovertyReliefById(povertyReliefDto.getId());
			if (result) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("delete povertyRelief by id  success, povertyRelief id is "
						+ povertyReliefDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("delete povertyRelief by id  fail, povertyRelief id is "
						+ povertyReliefDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("delete povertyRelief by id  fail, povertyRelief id is null");
		}

		return response;
	}

	/**
	 * 根据id批量删除扶贫用户
	 * 
	 * @param ids
	 *            ids为所有要删除的扶贫用户id,id与id之间使用,分割
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePovertyReliefByIds", method = RequestMethod.POST)
	public Response<PovertyReliefDto> deletePovertyReliefByIds(
			@RequestBody PovertyReliefDto povertyReliefDto) {
		Response<PovertyReliefDto> response = new Response<PovertyReliefDto>();
		if (null != povertyReliefDto
				&& StringUtils.isNotEmpty(povertyReliefDto.getIds())) {
			String[] temp = povertyReliefDto.getIds().split(",");
			Long[] delete_ids = new Long[temp.length];
			for (int i = 0; i < temp.length; i++) {
				delete_ids[i] = Long.parseLong(temp[i]);
			}
			Integer result = povertyReliefService
					.deletePovertyReliefByIds(delete_ids);
			if (null != result && result > 0) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("delete povertyRelief by ids  success, povertyRelief ids is "
						+ povertyReliefDto.getIds());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("delete povertyRelief by ids fail, povertyRelief ids is "
						+ povertyReliefDto.getIds());
			}

		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("delete povertyRelief by ids  fail, povertyRelief id iss null");
		}
		return response;
	}

	/**
	 * 根据id跟新付扶贫用户
	 * 
	 * @param aid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePovertyReliefById", method = RequestMethod.POST)
	public Response<PovertyReliefDto> updatePovertyReliefById(
			@RequestBody PovertyReliefDto povertyReliefDto, HttpSession session) {
		Response<PovertyReliefDto> response = new Response<PovertyReliefDto>();
		Object obj = session.getAttribute("user");
		if (null != povertyReliefDto && povertyReliefDto.getId() != null
				&& null != obj) {
			UserInfo user = (UserInfo) obj;
			PovertyReliefObjectT aid = new PovertyReliefObjectT();
			BeanUtils.copyProperties(povertyReliefDto, aid);
			aid.setModifyTime(System.currentTimeMillis());
			aid.setModifyUserId(user.getId());
			Integer result = povertyReliefService.updatePovertyReliefById(aid);
			if (null != result && result.equals(1)) {
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("update povertyRelief by id  success, povertyRelief id is "
						+ povertyReliefDto.getId());
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("update povertyRelief by id  fail, povertyRelief id is "
						+ povertyReliefDto.getId());
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("update povertyRelief by id  fail, povertyRelief id is null");
		}

		return response;
	}

	/**
	 * 批量导出扶贫用户(支持导出选中扶贫用户和导出所有扶贫用户,导出所有ids传-1) 最终导出成excel******
	 */
	@RequestMapping(value = "/exportPovertyRelief", method = RequestMethod.GET)
	public void exportPovertyRelief(HttpServletRequest request,
			HttpServletResponse response) {
		String ids = request.getParameter("ids");
		response.setContentType("application/binary;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		List<PovertyReliefObjectT> result = null;

		if (StringUtils.isNotEmpty(ids) && !ids.equals("-1")) {
			String[] temp = ids.split(",");
			Long[] exportIds = new Long[temp.length];
			for (int i = 0; i < temp.length; i++) {
				exportIds[i] = Long.parseLong(temp[i]);
			}
			result = povertyReliefService.exportPovertyRelief(exportIds);
		} else if (null != ids && ids.equals("-1")) {
			result = povertyReliefService.exportPovertyRelief();
		}

		if (null != result && result.size() > 0) {
			try {
				ServletOutputStream out = response.getOutputStream();
				String fileName = new String(new SimpleDateFormat("yyyy-MM-dd")
						.format(new Date()).getBytes(), "UTF-8");
				response.setHeader("Content-disposition",
						"attachment; filename=" + fileName + ".xls");
				String[] titles = { "扶贫对象", "性别", "地址", "详细地址", "联系方式", "对应电站",
						"扶贫完成情况", "创建时间" };
				export(result, out, titles);
			} catch (IOException e) {
				log.error("IOException , msg : " + e);
			}
		}
	}

	// 导出成excel文档
	private void export(List<PovertyReliefObjectT> result,
			ServletOutputStream out, String[] titles) {
		// 1. 创建一个excel文件
		HSSFWorkbook book = new HSSFWorkbook();
		// 2. 创建一个页签
		HSSFSheet sheet = book.createSheet("扶贫用户");
		// 3. 创建表头所在的行
		HSSFRow tiltle = sheet.createRow(0);
		// 4. 设置段远哥表头的风格
		HSSFCellStyle cellStyle = book.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFFont font = book.createFont();
		font.setBold(true);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);
		cellStyle.setFont(font);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		// 5. 设置表头信息
		HSSFCell cell = null;
		for (int i = 0; i < titles.length; i++) {
			cell = tiltle.createCell(i);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(cellStyle);
		}

		// 设置数据
		HSSFRow data = null;
		PovertyReliefObjectT aid = null;
		for (int i = 0; i < result.size(); i++) {
			data = sheet.createRow(i + 1);
			aid = result.get(i);
			for (int j = 0; j < titles.length; j++) {
				cell = data.createCell(j);// "扶贫对象", "性别", "地址",
											// "详细地址","联系方式","对应电站","扶贫完成情况","创建时间"
				if (j == 0) {
					cell.setCellValue(aid.getUserName());
				} else if (j == 1) {
					cell.setCellValue(aid.getGender() == 0 ? "男" : "女");
				} else if (j == 2) {
					String[] temp = aid.getPovertyAddrCode() != null ? aid
							.getPovertyAddrCode().split(",") : null; // 500000:重庆,500101:万州
					StringBuffer temp_address = new StringBuffer();
					if (null != temp && temp.length > 0) {
						for (String str : temp) {
							String[] strs = str.split(":");// 500000:重庆
							if (null != strs && strs.length == 2) {
								temp_address.append(strs[1]).append(" ");
							}
						}
					}
					cell.setCellValue(temp_address.toString());
				} else if (j == 3) {
					cell.setCellValue(aid.getDetailAddr());
				} else if (j == 4) {
					cell.setCellValue(aid.getContactPhone());
				} else if (j == 5) {
					cell.setCellValue(aid.getStationName());
				} else if (j == 6) {
					cell.setCellValue(aid.getPovertyStatus() == 0 ? "未完成" : "已完成");
				} else if (j == 7) {
					cell.setCellValue(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(new Date(aid
							.getCreateTime())));
				}
			}
		}
		try {
			book.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("IOException , msg : " + e);
		} finally {
			try {
				book.close();
			} catch (IOException e) {
				log.error("IOException , msg : " + e);
			}
		}
	}

	/**
	 * 根据条件查询扶贫用户
	 */
	@RequestMapping(value = "/selectPovertyReliefByCondition", method = RequestMethod.POST)
	@ResponseBody
	public Response<Page<PovertyReliefDto>> selectPovertyReliefByCondition(
			@RequestBody PovertyReliefDto povertyReliefDto, HttpSession session) {
		Response<Page<PovertyReliefDto>> response = new Response<>();
		Page<PovertyReliefDto> page = new Page<>();
		Object obj = session.getAttribute("user");
		if (null != povertyReliefDto && null != obj) {
			UserInfo user = (UserInfo) obj;
			PovertyReliefObjectT povertyRelief = new PovertyReliefObjectT();
			BeanUtils.copyProperties(povertyReliefDto, povertyRelief);
			QueryPovertyRelief queryPovertyRelief = new QueryPovertyRelief();
			queryPovertyRelief.setUserId(user.getId());
			queryPovertyRelief.setType_(user.getType_());
			queryPovertyRelief.setPovertyRelief(povertyRelief);
			Integer result = povertyReliefService
					.selectAllCount(queryPovertyRelief);// 查询总记录数
			page.setIndex(povertyReliefDto.getIndex() != null ? povertyReliefDto
					.getIndex() : 0);
			page.setPageSize(povertyReliefDto.getPageSize() != null ? povertyReliefDto
					.getPageSize() : 0);
			if (null == page.getIndex()
					|| (null != page.getIndex() && page.getIndex() < 1)) {
				page.setIndex(1);
			}
			if (null == page.getPageSize()
					|| (null != page.getPageSize() && 0 == page.getPageSize())) {
				page.setPageSize(15);
			}

			// 计算总分页数
			int allSize = result % page.getPageSize() == 0 ? result
					/ page.getPageSize() : result / page.getPageSize() + 1;
			if (page.getIndex() > allSize) {
				page.setPageSize(allSize);
			}

			page.setAllSize(allSize);
			page.setCount(result);
			page.setStart((page.getIndex() - 1) * page.getPageSize());// 计算起始位置
			page.setPageSize(povertyReliefDto.getPageSize());
			queryPovertyRelief.setPage(page);

			List<PovertyReliefObjectT> list = povertyReliefService
					.selectPovertyReliefByCondition(queryPovertyRelief);
			if (null != list) {
				List<PovertyReliefDto> l = new ArrayList<>();
				PovertyReliefDto dto = null;
				String[] temp = null;
				String[] strs = null;
				StringBuffer temp_address = new StringBuffer();
				StringBuffer temp_code = new StringBuffer();
				for (PovertyReliefObjectT aid : list) {
					temp_address.delete(0, temp_address.length());
					temp_code.delete(0, temp_code.length());
					dto = new PovertyReliefDto();
					temp = aid.getPovertyAddrCode() != null ? aid.getPovertyAddrCode()
							.split(",") : null; // 500000:重庆,500101:万州
					if (null != temp && temp.length > 0) {
						for (String str : temp) {
							strs = str.split(":");// 500000:重庆
							if (null != strs && strs.length == 2) {
								temp_address.append(strs[1]).append(" ");
								temp_code.append(strs[0]).append("@");
							}
						}
					}
					BeanUtils.copyProperties(aid, dto);
					dto.setAidAddr(temp_address.toString());
					dto.setPovertyAddrCode(temp_code.toString().substring(0,
							temp_code.toString().length() - 1));
					l.add(dto);
				}
				page.setList(l);
				response.setCode(ResponseConstants.CODE_SUCCESS);
				response.setMessage(ResponseConstants.CODE_SUCCESS_VALUE);
				log.info("query povertyRelief by condition success");
			} else {
				response.setCode(ResponseConstants.CODE_FAILED);
				response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
				log.error("query povertyRelief by condition fail");
			}
		} else {
			response.setCode(ResponseConstants.CODE_FAILED);
			response.setMessage(ResponseConstants.CODE_FAILED_VALUE);
			log.error("query povertyRelief by condition fail, query condition is null or no user login");
		}
		response.setResults(page);
		return response;
	}
}
