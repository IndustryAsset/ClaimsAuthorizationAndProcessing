package com.acc.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.acc.dto.ArffFile;
import com.acc.dto.CsvFile;
import com.acc.dto.ExcelFile;
import com.acc.dto.ModelFile;
import com.acc.entity.FileUpload;
import com.acc.service.PrepareTrainDataService;
import com.acc.service.TestModelService;
import com.acc.service.TrainModelService;
import com.acc.utility.ClaimsUtility;
import com.acc.utility.RowCount;
import com.acc.utility.SupervisedModel;

import weka.classifiers.Evaluation;

@Controller
public class UserTestModelController {

	@Autowired
	PrepareTrainDataService prepareTrainDataService;
	
	@Autowired
	TrainModelService trainModelService;
	
	@Autowired
	TestModelService testModelService;

	@RequestMapping("userTestModel.htm")
	public ModelAndView trainSaveModel(HttpServletRequest request) {
		ModelAndView modelandview = new ModelAndView();
		modelandview.setViewName("userTestModel");
		return modelandview;
	}

	@RequestMapping("evaluateExcelWithModel.htm")
	public ModelAndView evaluateExcelWithModel(HttpServletRequest request, FileUpload uploadItem) throws IOException {
		ModelAndView modelandview = new ModelAndView();
		List<MultipartFile> files = uploadItem.getFile();

		InputStream inputStream = null;

		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			inputStream = file.getInputStream();
			int position = fileName.lastIndexOf(".");
			String fileType = fileName.substring(position);
			byte[] excelData = null;
			excelData = IOUtils.toByteArray(inputStream);
			ExcelFile excelFile = new ExcelFile();
			excelFile.setFileName(fileName);
			excelFile.setFileContent(excelData);
			inputStream = file.getInputStream();
			byte[] csvData = null;
			if (".xlsx".equals(fileType)) {
				csvData = ClaimsUtility.XLSX2CSV(inputStream);
				inputStream = file.getInputStream();
				excelFile.setRowcount(RowCount.xlsxRowCount(inputStream));
			} else if (".xls".equals(fileType)) {
				csvData = ClaimsUtility.XLS2CSV(inputStream);
				inputStream = file.getInputStream();
				excelFile.setRowcount(RowCount.xlsRowCount(inputStream));
			}

			prepareTrainDataService.saveExcelFile(excelFile);
			
			CsvFile csvFile = new CsvFile();
			String csvName = excelFile.getFileName().substring(0, position) + ".csv";
			csvFile.setFileName(csvName);
			csvFile.setFileContent(csvData);
			csvFile.setExcelId(excelFile.getId());
			csvFile.setRowCount(excelFile.getRowcount());
			prepareTrainDataService.saveCsvFile(csvFile);
			
			byte[] arffData = null;
			ArffFile arffFile = new ArffFile();
			String arffName = excelFile.getFileName().substring(0, position) + ".arff";
			arffData = ClaimsUtility.CSV2ARFF(new ByteArrayInputStream(csvFile.getFileContent()));
			arffFile.setFileName(arffName);
			arffFile.setFileContent(arffData);
			arffFile.setRowCount(csvFile.getRowCount());
			arffFile.setCsvId(csvFile.getId());
			arffFile.setExcelId(csvFile.getExcelId());
			trainModelService.saveArffFile(arffFile);	
			List<ModelFile> modelfiles = trainModelService.listAllModels();
			ModelFile model = new ModelFile();
			for(ModelFile file1 : modelfiles)
				model = file1;
			
			List<String> results = new ArrayList<String>();
            try {
                  results = testModelService.testModel(model, arffFile);
            } catch (Exception e) {
                  e.printStackTrace();
            }
            Map<String,Object> evaluationResult = new HashMap<String, Object>();
            ArffFile trainArffFile = trainModelService.getArffFileById(model.getArffId());
            try {
            	evaluationResult = SupervisedModel.evaluateModel(trainArffFile, arffFile);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            modelandview.addObject("results", results);
            modelandview.addObject("evaluationResult", evaluationResult);
            modelandview.addObject("resultpage","yes");
		}
		modelandview.setViewName("userTestModel");
		return modelandview;
	}
}
