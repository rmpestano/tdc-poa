package com.tdc.poa.util;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.tdc.poa.model.Conference;
import com.tdc.poa.model.Talk;
import com.tdc.poa.service.ConferenceService;

/**
 * utility rest endpoint to initialize cucumber scenarios
 * just deploy this class within test deployment
 * 
 * @author RAFAEL-PESTANO
 *
 */
@Stateless
@Path("/scenarios")
public class ScenarioEndpoint {
  
  @Inject
  ConferenceService conferenceService;
  
  
  @GET
  @Path("/conference/search")
  public Response initConferenceSearchScenario(){
    this.clear();
    Conference tdcPoa = new Conference();
    tdcPoa.setName("TDC Poa");
    Talk talk = new Talk();
    talk.setTitle("Arquillian Talk");
    talk.setSlots(10);
    Conference javaOne = new Conference();
    javaOne.setName("JavaOne");
    Conference jaxLondon = new Conference();
    jaxLondon.setName("jaxLondon");
    Conference tdcSaoPaulo = new Conference();
    tdcSaoPaulo.setName("TDC São Paulo");
    conferenceService.store(tdcPoa);
    conferenceService.store(tdcSaoPaulo);
    conferenceService.store(jaxLondon);
    conferenceService.store(javaOne);
    return Response.ok().build();
  }
  
  @GET
  @Path("/clear")
  public Response clear(){
    conferenceService.clearConferences();
    return Response.ok().build();
  }

}
