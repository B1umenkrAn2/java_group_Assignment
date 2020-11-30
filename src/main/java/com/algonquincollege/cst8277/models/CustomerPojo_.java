package com.algonquincollege.cst8277.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-11-26T20:27:49.092-0500")
@StaticMetamodel(CustomerPojo.class)
public class CustomerPojo_ extends PojoBase_ {
	public static volatile SingularAttribute<CustomerPojo, String> firstName;
	public static volatile SingularAttribute<CustomerPojo, String> lastName;
	public static volatile SingularAttribute<CustomerPojo, String> email;
	public static volatile SingularAttribute<CustomerPojo, String> phoneNumber;
	public static volatile SingularAttribute<CustomerPojo, AddressPojo> shippingAddress;
	public static volatile SingularAttribute<CustomerPojo, AddressPojo> billingAddress;
	public static volatile ListAttribute<CustomerPojo, OrderPojo> orders;
}
