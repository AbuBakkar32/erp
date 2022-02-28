package com.asl.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.asl.entity.Xtrn;
import com.asl.model.ServiceException;

/**
 * @author Zubayer Ahamed
 * @since Feb 24, 2021
 */
@Component
public interface XtrnService {

	public long save(Xtrn xtrn) throws ServiceException;

	public long save(Xtrn xtrn, String zid, String audituser) throws ServiceException;

	public long update(Xtrn xtrn) throws ServiceException;

	public long update(Xtrn xtrn, String zid, String audituser) throws ServiceException;

	public List<Xtrn> getAllXtrn();

	public List<Xtrn> getAllXtrn(Boolean zactive);

	public List<Xtrn> findByXtypetrn(String xtypetrn);

	public List<Xtrn> findByXtypetrn(String xtypetrn, Boolean zactive);

	public List<Xtrn> findByXtrn(String xtrn);

	public List<Xtrn> findByXtrn(String xtrn, Boolean zactive);

	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn);

	public Xtrn findByXtypetrnAndXtrnAndZid(String xtypetrn, String xtrn, String zid);

	public Xtrn findByXtypetrnAndXtrn(String xtypetrn, String xtrn, Boolean zactive);

	public String generateAndGetXtrnNumber(String xtypetrn, String xtrn, int leftpad);

	public long delete(String xtypetrn, String xtrn);

	public List<Map<String, Object>> getExportDataByChunk(long limit, long offset, String zid);
}
