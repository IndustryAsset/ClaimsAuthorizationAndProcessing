package com.acc.service;


import java.util.List;

import com.acc.dto.CsvFile;
import com.acc.dto.ExcelFile;

public interface PrepareTrainDataService {
	
	public void saveExcelFile(ExcelFile excelFile);
	
	public void saveCsvFile(CsvFile csvFile);
	
	public List<ExcelFile> listAllExcels();
	
	public List<CsvFile> listAllCsvs();
	
	public ExcelFile getExcelFileById(Integer fileId);
	
	public CsvFile getCsvFileById(Integer csvId);
	
	public void deleteCsv(CsvFile csvFile);
	

}
