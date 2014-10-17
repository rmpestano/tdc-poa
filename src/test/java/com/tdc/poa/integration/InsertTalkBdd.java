package com.tdc.poa.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.model.Attendee;
import com.tdc.poa.model.Talk;
import com.tdc.poa.service.AttendeeService;
import com.tdc.poa.service.TalkService;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.arquillian.ArquillianCucumber;
import cucumber.runtime.arquillian.api.Features;

/**
 * mais exemplos em: https://github.com/cukespace/cukespace e aqui:
 * https://github.com/cukespace/cukespace/tree/master/examples
 * 
 * @author RAFAEL-PESTANO
 * 
 */
@RunWith(ArquillianCucumber.class)
@Features("features/talk/insert-talk.feature")
@Transactional(TransactionMode.DISABLED)
public class InsertTalkBdd {

	@Inject
	TalkService talkService;

	@Inject
	AttendeeService attendeeService;

	Talk talk;
	
	int numTalks;

	@Deployment
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap
				.create(WebArchive.class, "test.war")
				.addPackage("com.tdc.poa.service")
				.addClass(Crud.class)
				.addPackages(true, Talk.class.getPackage())
				// .addAsWebInfResource(new
				// File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		System.out.println(war.toString(true));
		return war;
	}

	@Before
	public void setup() {
		talkService.clearTalks();
		attendeeService.clearAttendees();
	}

	// não podemos usar DBUnit(@UsingDataset) com cucumber
	// ainda:https://github.com/cukespace/cukespace/issues/37
	@Given("^a talk with title \"([^\"]*)\" with (\\d+) slots and (\\d+) attenddes$")
	public void given(String title, int slots, int numAtendees) {
		//talkService.clearTalks();//@Before executed one time for all scenarios
		talk = new Talk();
		talk.setTitle(title);
		talk.setSlots(slots);
		talk.setAttendees(initAttendees(numAtendees));
	}

	@When("^i persist the talk$")
	public void when() {
		try {
			talkService.store(talk);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Then("^i should have (\\d+) talk$")
	public void then(int numTalks) {
		assertEquals(numTalks, talkService.crud().countAll());
	}

	private Set<Attendee> initAttendees(int numAtendees) {
		Set<Attendee> attendees = new HashSet<Attendee>();
		for (int i = 0; i < numAtendees; i++) {
			Attendee attendee = new Attendee();
			attendee.setName("attendee" + i);
			attendee.setAge(i);
			attendees.add(attendee);
		}
		return attendees;
	}

}
