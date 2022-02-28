package com.asl.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.asl.entity.POSPermissions;

@Mapper
public interface POSPermissionsMapper {
	
	public long savePOSPermissions(POSPermissions posPermissions);

	public long updatePOSPermissions(POSPermissions posPermissions);

	public List<POSPermissions> getAllPOSPermissions(String zid);

	public POSPermissions findPOSPermissionsByXaccAndXrole(String xacc, String xrole, String zid);
}
