package cxf_test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

@Path("cxf/rest/v1")
@Produces("text/xml")
public class MachineReport {
    @POST
    @Path("/machine-report/files")
    @Consumes("multipart/form-data")
    @Produces("text/xml")
    public Response files(
            @Multipart("reportId") String reportId,
            @Multipart("file") InputStream inputStream
    ) {
        try (FileOutputStream outputStream = new FileOutputStream(new File(reportId + ".pdf"), false)) {
            IOUtils.copy(inputStream, outputStream);
        }
        catch (Exception e) {
            System.out.println(e);
            return Response.serverError().build();
        }

        return Response.ok("file accepted").build();
    }

    public static void main(String[] args) {
        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceProvider(new SingletonResourceProvider(new MachineReport()));
        factoryBean.setAddress("http://localhost:8080/");

        factoryBean.create();
    }
}
