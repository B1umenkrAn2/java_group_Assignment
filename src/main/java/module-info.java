open module com.algonquincollege.cst8277.rest.orderSystem {
	requires org.slf4j;
    // Jakarta EE 8
    requires java.annotation;
    requires java.persistence;
    requires java.transaction;
    requires java.ws.rs;
    requires jakarta.servlet.api;
    requires jakarta.ejb.api;
    requires jakarta.inject.api;
    requires jakarta.enterprise.cdi.api;
    requires jakarta.security.enterprise;
    requires jakarta.security.enterprise.api;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.annotation;
}