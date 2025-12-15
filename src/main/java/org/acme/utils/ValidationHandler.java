package org.acme.utils;

import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ValidationHandler implements ExceptionMapper<ConstraintViolationException>  {
   @Override
    public Response toResponse(ConstraintViolationException exception) {
        String errors = exception.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new handlerResponse(errors, "01", null))
                .build();
    } 
}
