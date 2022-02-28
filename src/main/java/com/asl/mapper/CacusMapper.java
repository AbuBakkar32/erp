package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Cacus;

/**
 * @author Zubayer Ahamed
 * @since Mar 1, 2021
 */
@Mapper
public interface CacusMapper {

	public long save(Cacus cacus);

	public long update(Cacus cacus);

	public Cacus findByXcus(String xcus, String zid);

	public Cacus findByXphone(String xphone, String zid);

	public List<Cacus> findByXtypetrn(String xtypetrn, String zid);

	public List<Cacus> getAllCacus(String xtype, String zid);

	public List<Cacus> searchCacus(String xtypetrn, String xcus, String zid);

	public List<Cacus> searchCus(String xtrn, String xcus, String zid);

	public List<Cacus> searchXorg(String xorg, String zid);

	public List<Cacus> searchXgcus(String xgcus, String zid);

	public Cacus findCacusByXcuszid(String xcuszid, String zid);

	public Cacus findCacusByXorg(String xorg, String xcuszid, String zid);

	public long deleteCacus(String xcus, String zid);

	public List<Cacus> searchCustomer(String xtypetrn, String xcus, String zid);

	public List<Cacus> searchParty(String xcus, String zid);

	public List<Cacus> getAllLandMembers(String zid);

	public List<Cacus> searchMember(String xcus, String zid);

	public List<Cacus> searchMemberDir(String xcus, String zid);

	public List<Cacus> getAllBranchCustomer(String zid);

	public Cacus findByXperson(String xperson, String zid);

	public Cacus findDirectSupplier(String zid);


}
