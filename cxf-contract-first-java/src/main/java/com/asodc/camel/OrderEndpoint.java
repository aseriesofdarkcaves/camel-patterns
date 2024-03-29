package com.asodc.camel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.10
 * Thu Feb 16 17:04:27 CET 2023
 * Generated source version: 2.2.10
 * 
 */
 
@WebService(targetNamespace = "http://camel.asodc.com", name = "OrderEndpoint")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface OrderEndpoint {

    @WebMethod(operationName = "Order", action = "http://camel.asodc.com/Order")
    @WebResult(name = "resultCode", targetNamespace = "http://camel.asodc.com", partName = "resultCode")
    public java.lang.String order(
        @WebParam(partName = "partName", name = "partName", targetNamespace = "http://camel.asodc.com")
        java.lang.String partName,
        @WebParam(partName = "amount", name = "amount", targetNamespace = "http://camel.asodc.com")
        int amount,
        @WebParam(partName = "customerName", name = "customerName", targetNamespace = "http://camel.asodc.com")
        java.lang.String customerName
    );
}
