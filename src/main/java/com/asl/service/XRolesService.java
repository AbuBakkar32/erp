package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Xroles;

@Component
public interface XRolesService {

	public long save(Xroles xroles);

	public long update(Xroles xroles);

	public Xroles findByXrole(String xrole);

	public List<Xroles> getAllXroles();

}
