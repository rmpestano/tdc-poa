package com.tdc.poa.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.MatchMode;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.model.Talk;

@Stateless
public class TalkService {

	@Inject
	protected Crud<Talk> talkCrud;

	public Talk store(Talk entity) {
	  Talk example = new Talk();
	  example.setTitle(entity.getTitle());
	  if(talkCrud.example(example, MatchMode.EXACT).count() > 0){
	    throw new RuntimeException("Talk already exists");
	  }
	  if(entity.getAttendees().size() > entity.getSlots()){
	    throw new RuntimeException("Talk has no more slots");
	  }
	  talkCrud.saveOrUpdate(entity);
		return entity;
	}

	public void remove(Talk entity) {
	  talkCrud.delete(entity);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Talk find(Talk entity) {
		return talkCrud.example(entity).find();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<Talk> list(Talk entity) {
		return talkCrud.example(entity).list();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Crud<Talk> crud() {
		return talkCrud;
	}
	
  public void clearTalks(){
    for (Talk talk : talkCrud.listAll()) {
      this.remove(talk);
    }
  }
}
