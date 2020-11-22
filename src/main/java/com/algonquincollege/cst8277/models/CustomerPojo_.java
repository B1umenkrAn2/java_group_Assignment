package com.algonquincollege.cst8277.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-11-16T15:29:10.778-0500")
@StaticMetamodel(CustomerPojo.class)
public class CustomerPojo_ extends PojoBase_ {
	public static volatile SingularAttribute<CustomerPojo, String> firstName;
	public static volatile SingularAttribute<CustomerPojo, String> email;
	public static volatile SingularAttribute<CustomerPojo, String> phoneNumber;
	public static volatile SingularAttribute<CustomerPojo, BillingAddressPojo> billing_addr;
	public static volatile SingularAttribute<CustomerPojo, ShippingAddressPojo> shipping_addr;
	public static volatile ListAttribute<CustomerPojo, OrderPojo> orders;
	public static volatile SingularAttribute<CustomerPojo, String> lastName;
}
