package com.asodc.camel;

import javax.ws.rs.*;

@Path("/orders/")
@Consumes(value = "application/xml,application/json")
@Produces(value = "application/xml,application/json")
public interface RestOrderService {
    @GET
    @Path("/{id}")
    Order getOrder(@PathParam("id") int id);

    @POST
    Order createOrder(Order order);

    @PUT
    Order updateOrder(Order order);

    @DELETE
    @Path("/{id}")
    void deleteOrder(@PathParam("id") int id);
}
