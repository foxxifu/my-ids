package com.interest.ids.biz.web.utils;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.deepoove.poi.NiceXWPFDocument;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.policy.RenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import com.interest.ids.common.project.bean.device.DeviceInfoDto;
import com.interest.ids.common.project.constant.DevTypeConstant;

public class DeviceDetailTable implements RenderPolicy
{

	private Map<Integer,List<DeviceInfoDto>> map;
	private Integer number; //表格的行数
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Map<Integer, List<DeviceInfoDto>> getMap() {
		return map;
	}

	public void setMap(Map<Integer, List<DeviceInfoDto>> map) {
		this.map = map;
	}

	@Override
	public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template)
	{
		if(number > 0  && null != map && map.size() > 0)
		{
			NiceXWPFDocument doc = template.getXWPFDocument();
			RunTemplate runTemplate = (RunTemplate) eleTemplate;
			XWPFRun run = runTemplate.getRun();
			XWPFTable table = doc.insertNewTable(run, number+1, 5);
			
			CTTbl ttbl = table.getCTTbl();
			CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();    
			CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();    
			tblWidth.setW(new BigInteger("13000"));    
			tblWidth.setType(STTblWidth.DXA);  
			//doc.mergeCellsHorizonal(table, 0, 0, 1);
			setCellText(doc, getCellHight(table, 0, 0), "序列号"); 
			setCellText(doc, getCellHight(table, 0, 1), "设备名称"); 
			setCellText(doc, getCellHight(table, 0, 2), "厂家"); 
			setCellText(doc, getCellHight(table, 0, 3), "型号"); 
			setCellText(doc, getCellHight(table, 0, 4), "数量"); 
			
			Set<Integer> set = map.keySet();
			Iterator<Integer> iterator = set.iterator();
			Integer key = null;
			List<DeviceInfoDto> value = null;
			DeviceInfoDto dto = null;
			for (int i = 1; i < number;) 
			{
				boolean flag = false;
				key = iterator.next();
				value = map.get(key);
				if(null != value && value.size() > 0)
				{
					for (int j = 0; j < value.size(); j++) {
						dto = value.get(j);
						if(value.size() > 1 && !flag)
						{
							doc.mergeCellsVertically(table, 0, i, i+value.size()-1);
							doc.mergeCellsVertically(table, 1, i, i+value.size()-1);
							flag = true;
						}
						setCellText(doc, getCellHight(table, i, 0), i+""); 
						setCellText(doc, getCellHight(table, i, 1), DevTypeConstant.DEV_TYPE_I18N_ID.get(dto.getDevTypeId()));
						setCellText(doc, getCellHight(table, i, 2), dto.getVenderName()); 
						setCellText(doc, getCellHight(table, i, 3), dto.getSignalVersion()); 
						setCellText(doc, getCellHight(table, i, 4), dto.getNum()+""); 
						i++;
					}
				}
			}
		}
	    
	    
	}
	
	 /**  
     *   
     * @param xDocument  
     * @param cell  
     * @param text  
     * @param bgcolor  
     * @param width  
     */    
    private static void setCellText(XWPFDocument xDocument, XWPFTableCell cell,    
            String text) {  
            CTP ctp = CTP.Factory.newInstance();    
            XWPFParagraph p = new XWPFParagraph(ctp, cell);    
            p.setAlignment(ParagraphAlignment.CENTER);    
            XWPFRun run = p.createRun();    
            run.setColor("000000");    
            run.setFontSize(10);    
            run.setText(text);    
            CTRPr rpr = run.getCTR().isSetRPr() ? run.getCTR().getRPr() : run.getCTR().addNewRPr();    
            CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();    
            fonts.setAscii("微软雅黑");    
            fonts.setEastAsia("微软雅黑");    
            fonts.setHAnsi("微软雅黑");   
            cell.setParagraph(p);    
            cell.setVerticalAlignment(XWPFVertAlign.CENTER);
    }  
    
    //设置表格高度    
    private static XWPFTableCell getCellHight(XWPFTable xTable,int rowNomber,int cellNumber){    
        XWPFTableRow row = null;    
        row = xTable.getRow(rowNomber);    
        row.setHeight(100);    
        XWPFTableCell cell = null;    
        cell = row.getCell(cellNumber);    
        return cell;    
    }  
}
