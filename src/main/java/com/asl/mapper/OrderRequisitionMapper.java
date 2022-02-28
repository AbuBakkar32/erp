package com.asl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.asl.entity.Opreqdetail;
import com.asl.model.BranchesRequisitions;

@Mapper
public interface OrderRequisitionMapper {

	public List<BranchesRequisitions> getAllOpenBranchesRequisitions(String zid);
	public List<BranchesRequisitions> getAllBranchesRequisitions(String xdate, String xcus, String zid);
	public List<BranchesRequisitions> getAllBranchesRequisitionDetails(String xdate, String zid);
	public List<Opreqdetail> getDetailListByXpornumAndBranchZid(String xpornum, String zid) ;
}
