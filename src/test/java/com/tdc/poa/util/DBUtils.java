package com.tdc.poa.util;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tdc.poa.model.Attendee;
import com.tdc.poa.model.Conference;
import com.tdc.poa.model.Talk;


@Singleton
@Startup
public class DBUtils {
	
	@PersistenceContext
	EntityManager entityManager;
	
	 
	@PostConstruct
	public void intBD(){
		createDataset();
	}

	/**
	 * cria dataset ao subir a app com:
	 * 4 conferencias:
	 * TDC Poa
	 * TDC Sao Paulo
	 * Java One
	 * Jax London
	 */
	private void createDataset() {
		Conference conference = new Conference();
		conference.setName("TDC Poa");
		Talk talk = new Talk();
		talk.setTitle("Arquillian Talk");
		talk.setSlots(10);
		talk.setAttendees(initAttendees());
		Set<Talk> talks = new HashSet<Talk>();
		talks.add(talk);
		conference.setTalks(talks);
		entityManager.persist(conference);
		Conference javaOne = new Conference();
		javaOne.setName("JavaOne");
		Conference jaxLondon = new Conference();
		jaxLondon.setName("jaxLondon");
		Conference tdcSaoPaulo = new Conference();
		tdcSaoPaulo.setName("TDC São Paulo");
		entityManager.persist(tdcSaoPaulo);
		entityManager.persist(javaOne);
		entityManager.persist(jaxLondon);
		entityManager.flush();
	}

	private Set<Attendee> initAttendees() {
		Set<Attendee> attendees = new HashSet<Attendee>();
		for (int i = 0; i < 5; i++) {
			Attendee attendee = new Attendee();
			attendee.setName("attendee" + i);
			attendee.setAge(i);
			attendees.add(attendee);
		}
		return attendees;
	}

}
