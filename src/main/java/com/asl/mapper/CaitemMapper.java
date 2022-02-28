package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Caitem;
import com.asl.entity.Caitemdetail;

@Mapper
public interface CaitemMapper {

	public long saveCaitem(Caitem caitem);

	public long updateCaitem(Caitem caitem);
	
	public long deleteCaitem(String xitem, String zid);

	public List<Caitem> getAllCaitems(String zid);

	public List<Caitem> findByXcatitem(String xcatitem, String zid);

	public Caitem findByXitem(String xitem, String zid);

	public List<Caitem> searchCaitem(String xitem, String zid);
	
	public List<Caitem> searchAssetCaitem(String xitem, String zid);

	public List<Caitem> searchNonServiceCaitem(String xitem, String zid);

	public List<Caitem> searchFinishedProductionCaitem(String xitem, String zid);

	public List<Caitem> searchRawMaterialsCaitem(String xitem, String zid);

	public List<Caitem> getWithoutProductionCaitems(String xitem, String zid);

	public List<Caitem> getFunctionItems(String xitem, String zid);

	public List<Caitem> getFoodItems(String xitem, String zid);

	public List<Caitem> searchCentralCaitem(String xitem, String centralzid);

	public List<Caitem> searchCentralCaitemForRequisition(String xitem, String centralzid);

	public Caitem findCentralItemByXitem(String xitem, String centralzid);

	public List<Caitem> searchItemName(String xdesc, String zid);

	public List<Caitem> getAllItemsWithoutRawMaterials(String zid);

	public List<Caitem> getAllRequisitionItems(String zid);

	public long saveCaitemdetail(Caitemdetail caitemDetail);

	public Caitemdetail findCaitemdetailByXitemAndXsubitem(String xitem, String xsubitem, String zid);

	public List<Caitemdetail> findCaitemdetailByXitem(String xitem, String zid);

	public long deleteCaitemDetail(Caitemdetail caitemdetail);
	
	public List<Caitem> findAllSubCategoryByCategory(String xcatitem, String zid);
}
