package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Caitem;
import com.asl.entity.Caitemdetail;

@Component
public interface CaitemService {

	public long save(Caitem caitem);

	public long update(Caitem caitem);

	public long deleteCaitem(String xitem);

	public List<Caitem> getAllCaitems();

	public List<Caitem> findByXcatitem(String xcatitem);

	public Caitem findByXitem(String xitem);

	public List<Caitem> searchCaitem(String hint);
	
	public List<Caitem> searchAssetCaitem(String hint);
	
	public List<Caitem> searchNonServiceCaitem(String hint);

	public List<Caitem> searchFinishedProductionCaitem(String hint);

	public List<Caitem> searchRawMaterialsCaitem(String hint);

	public List<Caitem> searchCentralCaitem(String hint);

	public List<Caitem> searchCentralCaitemForRequisition(String hint);

	public Caitem findCentralItemByXitem(String xitem);

	public List<Caitem> getWithoutProductionCaitems(String hint);

	public List<Caitem> getFunctionItems(String hint);

	public List<Caitem> getFoodItems(String hint);

	public List<Caitem> searchItemName(String xdesc);

	public List<Caitem> getAllItemsWithoutRawMaterials();

	public List<Caitem> getAllRequisitionItems();

	public long saveCaitemdetail(Caitemdetail caitemDetail);

	public Caitemdetail findCaitemdetailByXitemAndXsubitem(String xitem, String xsubitem);

	public List<Caitemdetail> findCaitemdetailByXitem(String xitem);

	public long deleteCaitemDetail(Caitemdetail caitemdetail);
	
	public List<Caitem> findAllSubCategoryByCategory(String xcatitem);
}
