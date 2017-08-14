/**
 * IPPService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.strongit.oa.webservice.client.ipp;

public interface IPPService_PortType extends java.rmi.Remote {
    public java.lang.String selectData(java.lang.String condiction) throws java.rmi.RemoteException;
    public java.lang.String adds(java.lang.String xmlStr) throws java.rmi.RemoteException;
    public java.lang.String addsArticle(java.lang.Object[] objs) throws java.rmi.RemoteException;
    public java.lang.String getColumnTree() throws java.rmi.RemoteException, DocumentException;
}
