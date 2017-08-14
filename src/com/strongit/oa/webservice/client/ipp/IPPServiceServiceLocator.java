/**
 * IPPServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.strongit.oa.webservice.client.ipp;

public class IPPServiceServiceLocator extends org.apache.axis.client.Service implements IPPServiceService {

    public IPPServiceServiceLocator() {
    }


    public IPPServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IPPServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IPPService
    private java.lang.String IPPService_address = NotifyWebService.WebServiceAddress;//"http://192.168.2.177:8585/services/IPPService";

    public java.lang.String getIPPServiceAddress() {
        return IPPService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IPPServiceWSDDServiceName = "IPPService";

    public java.lang.String getIPPServiceWSDDServiceName() {
        return IPPServiceWSDDServiceName;
    }

    public void setIPPServiceWSDDServiceName(java.lang.String name) {
        IPPServiceWSDDServiceName = name;
    }

    public IPPService_PortType getIPPService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IPPService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIPPService(endpoint);
    }

    public IPPService_PortType getIPPService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            IPPServiceSoapBindingStub _stub = new IPPServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getIPPServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIPPServiceEndpointAddress(java.lang.String address) {
        IPPService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (IPPService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                IPPServiceSoapBindingStub _stub = new IPPServiceSoapBindingStub(new java.net.URL(IPPService_address), this);
                _stub.setPortName(getIPPServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IPPService".equals(inputPortName)) {
            return getIPPService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName(NotifyWebService.WebServiceAddress, "IPPServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName(NotifyWebService.WebServiceAddress, "IPPService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if (NotifyWebService.WebServiceAddress.equals(portName)) {
            setIPPServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
