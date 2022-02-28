package com.asl.service;

import com.asl.entity.Acdef;

public interface AcdefService {

	public long save(Acdef acdef);

	public long update(Acdef acdef);

	public Acdef find();
}
