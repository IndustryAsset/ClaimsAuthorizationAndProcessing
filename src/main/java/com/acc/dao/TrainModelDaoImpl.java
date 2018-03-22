package com.acc.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.acc.dto.ArffFile;
import com.acc.dto.CsvFile;
import com.acc.dto.ModelFile;
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
		return (ArffFile)query.uniqueResult();	
	}
	
	public void deleteArff(ArffFile arffFile) {
		Session session = getSession();
		session.delete(arffFile);
		
		
	}

	public void saveModel(ModelFile modelFile) {
		Session session = getSession();
		session.save(modelFile);		
	}
	
	public ModelFile getModelById(Integer fileId) {
        Session session = getSession();
        Query query = session.createQuery("select a from ModelFile a where a.id=:fileId ");
        query.setParameter("fileId", fileId);
        return (ModelFile)query.uniqueResult();
 }

 public List<ModelFile> listAllModelsOfJava() {
        Session session = getSession();
        Query query = session.createQuery("select a from ModelFile a where a.flag=:flag");
        query.setParameter("flag","java");
        return query.list();
 }

public ModelFile getModelFileByArffId(Integer id) {
	Session session = getSession();
    Query query = session.createQuery("select a from ModelFile a where a.arffId=:fileId ");
    query.setParameter("fileId", id);
    return (ModelFile)query.uniqueResult();
}

}
