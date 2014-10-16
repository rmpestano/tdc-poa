package com.tdc.poa.functional;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.tdc.poa.crud.Crud;
import com.tdc.poa.functional.fragment.Menu;
import com.tdc.poa.functional.page.ConferenceSearchPage;
import com.tdc.poa.util.DBUtils;
import com.tdc.poa.view.ConferenceBean;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import cucumber.runtime.arquillian.ArquillianCucumber;
import cucumber.runtime.arquillian.api.Features;

@RunWith(ArquillianCucumber.class)
@Features("features/conference/search.feature")
public class ConferenceAt {

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL context;
	
	@FindBy(id="navigation")
	Menu menu;
	
	@FindByJQuery("#homeLink")
	WebElement home;
	
	@Page
	ConferenceSearchPage conferenceSearch;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {

		WebArchive war = ShrinkWrap.create(WebArchive.class);
		war.addPackages(true, ConferenceBean.class.getPackage())
				.addPackages(true, "com.tdc.poa.rest")
				.addPackages(true, "com.tdc.poa.model")
				.addPackages(true, "com.tdc.poa.view")
				.addPackages(true, "com.tdc.poa.service")
				.addClass(DBUtils.class)
				.addClass(Crud.class)
				.addAsWebInfResource(
						new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
				.addAsWebInfResource(new File("src/main/webapp/WEB-INF","faces-config.xml"), "faces-config.xml")		
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class), "/", Filters.include(".*\\.(xhtml|html|css|js|png|ico)$"));
		return war;

	}
	
	 @Page
	 ConferenceSearchPage conferenceSearchPage;
	 

	@Given("^i navigate to search conference page$")
	public void navigateToSearch(){
		Graphene.goTo(ConferenceSearchPage.class);
	}
	
	@When("^i search a conference with name \"([^\"]*)\"$")
	public void search(String name){
		conferenceSearchPage.searchByName(name);
	}
	
	@Then("resulting list should contain conferences with \"([^\"]*)\" and total records of (\\d+)$")
	public void result(String name, int records){
		List<WebElement> rows =  browser.findElements(By.xpath("//*[@id='search:conferenceBeanPageItems']//tr//span"));
		assertEquals(records, rows.size());
		for (WebElement row : rows) {
			assertTrue(row.getText().contains(name));
		}
	}
}
