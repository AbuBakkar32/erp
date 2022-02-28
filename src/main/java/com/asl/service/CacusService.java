package com.asl.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.asl.entity.Cacus;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Component
public interface CacusService {

	public long save(Cacus cacus);

	public long update(Cacus cacus);

	public Cacus findByXcus(String xcus);

	public List<Cacus> findByXtypetrn(String xtypetrn);

	public List<Cacus> searchCacus(String xtype, String xcus);

	public List<Cacus> searchCus(String xtrn, String xcus);

	public Cacus findByXphone(String xphone);

	public List<Cacus> searchXorg(String xorg);

	public List<Cacus> searchXgcus(String xgcus);

	public Cacus findCacusByXcuszid(String xcuszid);

	public Cacus findCacusByXorg(String xorg, String xcuszid);

	public long deleteCacus(String xcus);

	public List<Cacus> searchCustomer(String xtype, String xcus);

	public List<Cacus> searchParty(String xcus);

	public List<Cacus> getAllLandMembers();

	public List<Cacus> searchMember(String xcus);

	public List<Cacus> searchMemberDir(String xcus);

	public List<Cacus> getAllBranchCustomer();

	public Cacus findByXperson(String xperson);

	public Cacus findDirectSupplier();

	public List<Cacus> getAllCacus(String xtype);


}
