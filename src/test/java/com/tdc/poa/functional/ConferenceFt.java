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

@RunWith(Arquillian.class)
public class ConferenceFt {

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

	@Test
	@InSequence(1)
	public void shouldOpenInitialPage() {
		browser.get(context.toString());
		WebElement h1 = browser.findElement(By.xpath("//div[@id='content']//h1[contains(text(),'Welcome to Forge')]"));
		assertNotNull(h1);
		assertTrue(h1.isDisplayed());
	}
	
	@Test
	@InSequence(2)
	public void shouldNavigateToConference(@InitialPage ConferenceSearchPage page){
		WebElement h1 = browser.findElement(By.xpath("//div[@id='content']//h1[contains(text(),'Conference')]"));
		assertNotNull(h1);
		assertTrue(h1.isDisplayed());
	}
	
	 
	@Test
	@InSequence(3)
	public void shouldNavigateToConference(){
		Graphene.goTo(ConferenceSearchPage.class);
		assertTrue(conferenceSearch.isDisplayed());
	}
	
	@Test
	@InSequence(4)
	public void shouldSearchConferenceByName(@InitialPage ConferenceSearchPage page){
		assertTrue(page.isDisplayed());
		page.searchByName("TDC");
		List<WebElement> rows =  browser.findElements(By.xpath("//*[@id='search:conferenceBeanPageItems']//tr//span"));
		for (WebElement row : rows) {
			assertTrue(row.getText().equals("TDC Poa"));
		}
	}
	
	@Test
	@InSequence(5)
	public void shouldNavigateViaMenu(){
		Graphene.guardHttp(home).click();
		menu.gotoConference();
		assertTrue(conferenceSearch.isDisplayed());
	}
	
	
}
