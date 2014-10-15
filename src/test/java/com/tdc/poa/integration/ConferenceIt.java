package com.tdc.poa.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.model.Conference;
import com.tdc.poa.service.ConferenceService;

@RunWith(Arquillian.class)
public class ConferenceIt {

	@Inject
	private ConferenceService conferenceService;

	@Deployment
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addClass(ConferenceService.class)
				.addClass(Crud.class)
				.addPackages(true, Conference.class.getPackage())
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(war.toString(true));
		return war;
	}

	@Test
	@InSequence(1)
	public void shouldInsertConference() {
		Conference conference = new Conference();
		conference.setName("TDC");
		Conference conferenceInserted = conferenceService.store(conference);
		assertNotNull(conferenceInserted);
		assertNotNull(conferenceInserted.getId());
	}
	
	@Test
	@InSequence(2)
	public void shouldRemoveConference() {
		int numConveferences = conferenceService.crud().countAll();
		Conference conferenceSearch = new Conference();
		conferenceSearch.setName("TDC");
		Conference conferenceToRemove = conferenceService.find(conferenceSearch);
		assertNotNull(conferenceToRemove);
		conferenceService.remove(conferenceToRemove);
		assertEquals(numConveferences-1,conferenceService.crud().countAll());
	}
	
	@Test
	@UsingDataSet("conference.yml")
	@Cleanup(phase=TestExecutionPhase.BEFORE)
	@Transactional(value=TransactionMode.DISABLED)
	public void shouldFailToInsertConferenceWithDuplicateName(){
	    Conference conference = new Conference();
	    conference.setName("TDC Porto Alegre");
	    try{
	       conferenceService.store(conference);
	    }catch(RuntimeException e){
	      assertEquals("Conference already exists",e.getCause().getMessage());
	    }
	}
}
