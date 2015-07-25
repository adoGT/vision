package sk.mksk.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;

import sk.mksk.model.Check;

@Path("/checks")
@Stateless
public class CheckService extends BaseEntityService<Check> {

	@Inject
	private EntityManager em;
	
	public CheckService() {
        super(Check.class);
    }
	
	@DELETE
    public Response deleteAllChecks() {
    	List<Check> checks = getAll(new MultivaluedHashMap<String, String>());
    	for (Check check : checks) {
    		deleteCheck(check.getId());
    	}
        return Response.noContent().build();
	}
	
	 /**
     * <p>
     * Delete a check by id
     * </p>
     * @param id
     * @return
     */
	@DELETE
    @Path("/{id:[0-9][0-9]*}")
    public Response deleteCheck(@PathParam("id") Long id) {
        Check check = getEntityManager().find(Check.class, id);
        if (check == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        getEntityManager().remove(check);
        
        return Response.noContent().build();
    }
	
	/**
    * <p>
    *   Create a check.
    * </p>
    * @param checkRequest
    * @return
    */
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response createCheck(CheckRequest checkRequest) {
       try {
           Check check = new Check();
           check.setName(checkRequest.getName());
           check.setDescription(checkRequest.getDescription());
           getEntityManager().persist(check);
           return Response.ok().entity(check).type(MediaType.APPLICATION_JSON_TYPE).build();
           
       } catch (ConstraintViolationException e) {
           // If validation of the data failed using Bean Validation, then send an error
           Map<String, Object> errors = new HashMap<String, Object>();
           List<String> errorMessages = new ArrayList<String>();
           for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
               errorMessages.add(constraintViolation.getMessage());
           }
           errors.put("errors", errorMessages);
           // A WebApplicationException can wrap a response
           // Throwing the exception causes an automatic rollback
           throw new RestServiceException(Response.status(Response.Status.BAD_REQUEST).entity(errors).build());
       } catch (Exception e) {
           // Finally, handle unexpected exceptions
           Map<String, Object> errors = new HashMap<String, Object>();
           errors.put("errors", Collections.singletonList(e.getMessage()));
           // A WebApplicationException can wrap a response
           // Throwing the exception causes an automatic rollback
           throw new RestServiceException(Response.status(Response.Status.BAD_REQUEST).entity(errors).build());
       }
   }
}
