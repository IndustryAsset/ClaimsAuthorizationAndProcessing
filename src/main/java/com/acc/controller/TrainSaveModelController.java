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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.acc.dto.ArffFile;
import com.acc.dto.CsvFile;
import com.acc.dto.ModelFile;
import com.acc.entity.FileUpload;
import com.acc.service.PrepareTrainDataService;
import com.acc.service.TrainModelService;
import com.acc.utility.ClaimsUtility;

import weka.core.Instances;
import weka.core.converters.ArffLoader;

@Controller
public class TrainSaveModelController {
	@Autowired
	TrainModelService trainModelService;
	
	@Autowired
	PrepareTrainDataService prepareTrainDataService;
	
	 @RequestMapping("trainSaveModel.htm")
     public ModelAndView trainSaveModel(HttpServletRequest request)
     {
		ModelAndView modelandview = new ModelAndView();
		List<ArffFile> arffFiles = trainModelService.listAllArffs();
		List<CsvFile> csvFiles = prepareTrainDataService.listAllPythonCsv();
		modelandview.addObject("arffFiles", arffFiles);
		modelandview.addObject("csvFiles", csvFiles);
		modelandview.setViewName("trainSaveModel");
		return modelandview;
     }
	 
	 @RequestMapping("uploadArff.htm")
		public ModelAndView uploadArff(HttpServletRequest request, FileUpload uploadItem) throws IOException {
			ModelAndView modelandview = new ModelAndView();
			List<MultipartFile> files = uploadItem.getFile();
			InputStream inputStream = null;		 
			for(MultipartFile file : files)
			{
				String fileName = file.getOriginalFilename();
				inputStream = file.getInputStream();
				ArffLoader loader = new ArffLoader();
				loader.setSource(inputStream);	

				Instances data =loader.getDataSet();
				byte[] arffFileContent = data.toString().getBytes();
				ArffFile arffFile = new ArffFile();
				arffFile.setFileName(fileName);
				arffFile.setFileContent(arffFileContent);
				arffFile.setColCount(data.numAttributes());
				arffFile.setRowCount(data.size());
				trainModelService.saveArffFile(arffFile);				
			}
			List<ArffFile> arffFiles = trainModelService.listAllArffs();
			modelandview.addObject("arffFiles", arffFiles);
			List<CsvFile> csvFiles = prepareTrainDataService.listAllPythonCsv();
			modelandview.addObject("csvFiles", csvFiles);
			modelandview.addObject("message", "successUpload");
			modelandview.setViewName("trainSaveModel");
			return modelandview;

		}

	 @RequestMapping("downloadArff.htm")
		public void downloadArff(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) throws IOException {
			ArffFile arffFile = trainModelService.getArffFileById(Integer.valueOf(id));

			ByteArrayInputStream in = new ByteArrayInputStream(arffFile.getFileContent());
			OutputStream outStream = response.getOutputStream();
			String fileName = URLEncoder.encode(arffFile.getFileName(), "UTF-8");
			fileName = URLDecoder.decode(fileName, "ISO8859_1");
			int fileContentSize=arffFile.getFileContent().length;
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			byte[] buffer = new byte[fileContentSize];
			int bytesRead = -1;

			while ((bytesRead = in.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
		}

		@RequestMapping("deleteArff.htm")
		public ModelAndView deleteArff(HttpServletRequest request)
		{
			ModelAndView modelandview = new ModelAndView();
			Integer id = Integer.valueOf(request.getParameter("id"));
			ArffFile arffFile = trainModelService.getArffFileById(id);
			trainModelService.deleteArff(arffFile);		 
			modelandview.setViewName("trainSaveModel");
			List<ArffFile> arffFiles = trainModelService.listAllArffs();
			modelandview.addObject("arffFiles", arffFiles);
			List<CsvFile> csvFiles = prepareTrainDataService.listAllPythonCsv();
			modelandview.addObject("csvFiles", csvFiles);
			modelandview.addObject("message", "Deleted");
			return modelandview;
		}
	 
	 @RequestMapping("trainModel.htm")
     public ModelAndView trainModel(HttpServletRequest request, @RequestParam("id") String id ) throws Exception
     {
		ModelAndView modelandview = new ModelAndView();
		Integer fileId = Integer.valueOf(id);
		ArffFile arffFile = trainModelService.getArffFileById(fileId);
		int position = arffFile.getFileName().lastIndexOf(".");
		String modelName = arffFile.getFileName().substring(0, position) + ".model";
		InputStream inputStream = new ByteArrayInputStream(arffFile.getFileContent());
		byte[] modelContent = ClaimsUtility.ARFF2Model(inputStream);
		ModelFile modelFile = new ModelFile();
		modelFile.setFileContent(modelContent);
		modelFile.setFileName(modelName);
		modelFile.setColCount(arffFile.getColCount());
		modelFile.setRowcount(arffFile.getRowCount());
		modelFile.setArffId(arffFile.getId());
		trainModelService.saveModel(modelFile);
		List<ArffFile> arffFiles = trainModelService.listAllArffs();
		modelandview.addObject("arffFiles", arffFiles);
		List<CsvFile> csvFiles = prepareTrainDataService.listAllPythonCsv();
		modelandview.addObject("csvFiles", csvFiles);
		modelandview.setViewName("trainSaveModel");
		return modelandview;
     }

}
