package com.asl.service;

import java.util.Date;
import java.util.List;

import com.asl.entity.Opreqdetail;
import com.asl.model.BranchesRequisitions;

public interface RequisitionListService {

	public List<BranchesRequisitions> getAllOpenBranchesRequisitions();

	public List<BranchesRequisitions> getAllBranchesRequisitions(Date xdate, String xcus);

	public List<BranchesRequisitions> getAllBranchesRequisitionDetails(Date xdate);

	public List<Opreqdetail> getDetailListByXpornumAndBranchZid(String xpornum);
}
