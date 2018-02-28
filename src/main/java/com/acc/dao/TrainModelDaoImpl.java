package com.acc.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.acc.dto.ArffFile;
@Repository
public class TrainModelDaoImpl extends AbstractDao implements TrainModelDao {
	
	public void saveArffFile(ArffFile arffFile){
		Session session = getSession();
		session.save(arffFile);		
	}
	
	public List<ArffFile> listAllArffs(){
		Session session = getSession();
		Query query = session.createQuery("from ArffFile");
		return query.list();
	}
	
	public ArffFile getArffFileById(Integer arffId){
		Session session = getSession();
		ArffFile arffFile = new ArffFile();
		Query query = session.createQuery("select a from ArffFile a where a.id=:arffId ");
		query.setParameter("arffId", arffId);
		List<ArffFile> arffList = query.list();
		for (ArffFile file : arffList) {
			arffFile.setId(file.getId());
			arffFile.setFileName(file.getFileName());
			arffFile.setFileContent(file.getFileContent());
		}
		return arffFile;	
	}
	
	public void deleteArff(ArffFile arffFile) {
		Session session = getSession();
		session.delete(arffFile);
		
		
	}
}
