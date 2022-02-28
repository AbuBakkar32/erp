package com.asl.service;

import java.util.List;

import com.asl.entity.POSPermissions;

public interface POSPermissionsService {

	public long save(POSPermissions posPermissions);

	public long update(POSPermissions posPermissions);

	public List<POSPermissions> getAllPOSPermissions();

	public POSPermissions findByPOSPermissionsByXaccAndXrole(String xacc, String xrole);

}
