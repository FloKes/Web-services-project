package dtuPayApi.service.adapter.rest;

import dtuPayApi.service.dtos.ReportDTO;
import dtuPayApi.service.factories.ReportFactory;
import dtuPayApi.service.services.ReportService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
@Path("/manager")
public class ManagerResource {
    ReportService reportService = new ReportFactory().getService();


    @Path("/reports")
    @GET
    @Produces("application/json")
    public ReportDTO requestManagerReport() {
        var reportDTO = reportService.requestManagerReport();
        return reportDTO;
    }
}
