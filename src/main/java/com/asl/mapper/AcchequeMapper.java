package com.asl.mapper;
import java.util.List;

import com.asl.entity.Acchequedetail;
import com.asl.entity.Acchequeheader;


public interface AcchequeMapper {
	
	// For Header Entity
	public long saveAcchequeheader(Acchequeheader acchequeheader);
	public long updateAcchequeheader(Acchequeheader acchequeheader);
	
	public List<Acchequeheader> getAllAcchequeheader(String zid);
	
	// For Detail Entity
	public long saveAcchequedetail(Acchequedetail acchequedetail);
	public long updateAcchequedetail(Acchequedetail acchequedetail);
	
	public List<Acchequedetail> getAllAcchequedetail(String zid);
}
