package com.tdc.poa.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.MatchMode;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.model.Attendee;

@Stateless
public class AttendeeService {

	@Inject
	protected Crud<Attendee> AttendeeCrud;

	public Attendee store(Attendee entity) {
	  Attendee example = new Attendee();
	  example.setName(entity.getName());
	  if(AttendeeCrud.example(example, MatchMode.EXACT).count() > 0){
	    throw new RuntimeException("Attendee already exists");
	  }
	  AttendeeCrud.saveOrUpdate(entity);
		return entity;
	}

	public void remove(Attendee entity) {
	  AttendeeCrud.delete(entity);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Attendee find(Attendee entity) {
		return AttendeeCrud.example(entity).find();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Attendee> list(Attendee entity) {
		return AttendeeCrud.example(entity).list();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Crud<Attendee> crud() {
		return AttendeeCrud;
	}
	
	public void clearAttendees(){
	  for (Attendee Attendee : AttendeeCrud.listAll()) {
      this.remove(Attendee);
    }
	}
}
