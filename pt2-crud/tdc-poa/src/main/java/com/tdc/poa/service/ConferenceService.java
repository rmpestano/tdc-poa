package com.tdc.poa.service;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.model.Conference;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class ConferenceService {

	@Inject
	protected Crud<Conference> conferenceCrud;

	public Conference store(Conference entity) {
		conferenceCrud.saveOrUpdate(entity);
		return entity;
	}

	public void remove(Conference entity) {
		conferenceCrud.delete(entity);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Conference find(Conference entity) {
		return conferenceCrud.example(entity).find();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Conference> list(Conference entity) {
		return conferenceCrud.example(entity).list();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Crud crud() {
		return conferenceCrud;
	}
}
