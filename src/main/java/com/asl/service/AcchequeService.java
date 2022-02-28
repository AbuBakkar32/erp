package com.asl.service;

import java.util.List;

import com.asl.entity.Acchequedetail;
import com.asl.entity.Acchequeheader;

public interface AcchequeService {
	
	// For Header Entity
	public long save(Acchequeheader acchequeheader);
	public long update(Acchequeheader acchequeheader);
	
	public List<Acchequeheader> getAllAcchequeheader();
	
	// For Detail Entity
	public long save(Acchequedetail acchequedetail);
	public long update(Acchequedetail acchequedetail);
	
	public List<Acchequedetail> getAllAcchequedetail();
}
