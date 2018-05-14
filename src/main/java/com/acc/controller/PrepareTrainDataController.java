package com.acc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.acc.dto.CsvFile;
import com.acc.dto.ExcelFile;
import com.acc.entity.FileUpload;
import com.acc.service.PrepareTrainDataService;
import com.acc.utility.ClaimsUtility;
import com.acc.utility.ColumnCount;
import com.acc.utility.ExcelUtility;
import com.acc.utility.RowCount;



@Controller
public class PrepareTrainDataController {
	
	@Autowired
	PrepareTrainDataService prepareTrainDataService;
	
	 static Logger log = Logger.getLogger(PrepareTrainDataController.class.getName());
	 
	 @RequestMapping("prepareTrainData.htm")
     public ModelAndView trainSaveModel(HttpServletRequest request, HttpServletResponse response )
     {
                     ModelAndView modelandview = new ModelAndView();
                     List<ExcelFile> excelFiles = prepareTrainDataService.listAllExcels();
                     modelandview.addObject("excelFiles", excelFiles);
                     modelandview.setViewName("prepareTrainingData");
                     return modelandview;
     }
	 
	 @RequestMapping("uploadExcel.htm")
	 public ModelAndView uploadExcel(HttpServletRequest request, FileUpload uploadItem) throws IOException {
			ModelAndView modelandview = new ModelAndView();
			List<MultipartFile> files = uploadItem.getFile();
			InputStream inputStream = null;
			InputStream inputStream1 = null;
			InputStream inputStream2 = null;
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename();
				inputStream = file.getInputStream();
				inputStream1 = file.getInputStream();
				inputStream2 = file.getInputStream();
				byte[] unmergedExcel = ExcelUtility.xlsxUnmerge(file.getInputStream());
				InputStream inputStream6 = new ByteArrayInputStream(unmergedExcel);
				ExcelFile excelFile = new ExcelFile();
				int position = fileName.lastIndexOf(".");
				String fileType = fileName.substring(position);
				ExcelFile dupeExcel = prepareTrainDataService.getExcelFileByName(fileName);
				if (dupeExcel != null && dupeExcel.getFileContent() != null) {
					if (".xlsx".equals(fileType)) {
						ExcelFile appendFile = new ExcelFile();
						byte[] appendContent = ExcelUtility.xlsxDataAppend(file.getInputStream(),
								new ByteArrayInputStream(dupeExcel.getFileContent()));
						appendFile.setFileName(fileName);
						appendFile.setFileContent(appendContent);
						appendFile.setRowcount(RowCount.xlsxRowCount(new ByteArrayInputStream(appendContent)));
						appendFile.setColCount(dupeExcel.getColCount());
						prepareTrainDataService.saveExcelFile(appendFile);
					} else if (".xls".equals(fileType)) {
						ExcelFile appendFile = new ExcelFile();
						byte[] appendContent = ExcelUtility.xlsDataAppend(file.getInputStream(),
								new ByteArrayInputStream(dupeExcel.getFileContent()));
						appendFile.setFileName(fileName);
						appendFile.setFileContent(appendContent);
						appendFile.setRowcount(RowCount.xlsRowCount(new ByteArrayInputStream(appendContent)));
						appendFile.setColCount(dupeExcel.getColCount());
						prepareTrainDataService.saveExcelFile(appendFile);
					}
				} else {
					if (".xlsx".equals(fileType)) {
						excelFile.setRowcount(RowCount.xlsxRowCount(file.getInputStream())); 
						excelFile.setColCount(ColumnCount.xlsxColumnCount(file.getInputStream()));

					} else if (".xls".equals(fileType)) {
						excelFile.setRowcount(RowCount.xlsRowCount(file.getInputStream()));
						excelFile.setColCount(ColumnCount.xlsColumnCount(file.getInputStream()));

					}
					byte[] excelfileData = IOUtils.toByteArray(inputStream6/*file.getInputStream()*/);
					excelFile.setFileName(fileName);
					excelFile.setFileContent(excelfileData);
					prepareTrainDataService.saveExcelFile(excelFile);
				}

			}
			List<ExcelFile> excelFiles = prepareTrainDataService.listAllExcels();
			modelandview.addObject("excelFiles", excelFiles);
			modelandview.addObject("message", "successUpload");
			modelandview.setViewName("prepareTrainingData");
			return modelandview;
		}
	 
	 @RequestMapping("downloadExcel.htm")
	 public void downloadExcel(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) throws IOException {
		ExcelFile excelFile = prepareTrainDataService.getExcelFileById(Integer.valueOf(id));

		ByteArrayInputStream in = new ByteArrayInputStream(excelFile.getFileContent());
		OutputStream outStream = response.getOutputStream();
		String fileName = URLEncoder.encode(excelFile.getFileName(), "UTF-8");
		fileName = URLDecoder.decode(fileName, "ISO8859_1");
		response.setContentType("application/x-msdownload");
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;

		while ((bytesRead = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}
	 }

	 @RequestMapping(value={"convertToCsv.htm"},method = RequestMethod.POST)
	 public String convertToCsv(HttpServletRequest request, @RequestParam("excelId") String id,@RequestParam("language") String language) throws IOException {
		ModelAndView modelandview = new ModelAndView();
		ExcelFile excelFile = prepareTrainDataService.getExcelFileById(Integer.valueOf(id));
		int position = excelFile.getFileName().lastIndexOf(".");
		String fileType = excelFile.getFileName().substring(position);
		InputStream inputStream = new ByteArrayInputStream(excelFile.getFileContent());
		byte[] csvData = null;
		if (".xlsx".equals(fileType)) {
			csvData = ClaimsUtility.XLSX2CSV(inputStream,language);
		}
		else if (".xls".equals(fileType)) {
			csvData = ClaimsUtility.XLS2CSV(inputStream,language);
		}
		String csvName = excelFile.getFileName().substring(0, position) + ".csv";
		log.info("csvName : " + csvName);
		CsvFile csvFile = new CsvFile();
		csvFile.setFileName(csvName);
		csvFile.setFileContent(csvData);
		csvFile.setExcelId(excelFile.getId());
		csvFile.setRowCount(excelFile.getRowcount());
		csvFile.setColumnCount(excelFile.getColCount());
		if(language.equals("java"))
			csvFile.setIsJava(true);
		else
			csvFile.setIsJava(false);
		prepareTrainDataService.saveCsvFile(csvFile);
		return "success";
	 }
	 @RequestMapping("deleteExcel.htm")
	 public ModelAndView deleteExcel(HttpServletRequest request, @RequestParam("id") String id) throws IOException {
			ModelAndView modelandview = new ModelAndView();
			CsvFile csvFile = prepareTrainDataService.getCsvFileByExcelId(Integer.valueOf(id));
			ExcelFile excelFile = prepareTrainDataService.getExcelFileById(Integer.valueOf(id));
			if (csvFile.getExcelId() != null){
				modelandview.addObject("message", "CannotDeleteExcel");
			}	
			else{
				prepareTrainDataService.deleteExcel(excelFile);
				modelandview.addObject("message", "deletedSuccessfully");
			}
			List<ExcelFile> excelFiles = prepareTrainDataService.listAllExcels();
			modelandview.addObject("excelFiles", excelFiles);
			modelandview.setViewName("prepareTrainingData");
			return modelandview;
		 }

}







