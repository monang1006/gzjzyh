package com.strongit.uums.license;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public final class License {
	private long licenseID;
	private String mac;
	private String product;
	private String version;
	private LicenseType licenseType;
	private String name;
	private String company;
	private int numCopies;
	private Date expiresDate;
	private String signature;
	private static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy/MM/dd");

	License(long paramLong, String paramString1, String paramString2,
			LicenseType paramLicenseType) {
		this.licenseID = paramLong;
		this.product = paramString1;
		this.version = paramString2;
		this.licenseType = paramLicenseType;
		this.name = null;
		this.company = null;
		paramString1 = null;
		this.numCopies = 1;
		this.expiresDate = null;
		this.signature = null;
	}

	public long getLicenseID() {
		return this.licenseID;
	}

	public void setLicenseID(long paramLong) {
		this.licenseID = paramLong;
	}

	public String getProduct() {
		return this.product;
	}

	public void setProduct(String paramString) {
		this.product = paramString;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String paramString) {
		this.version = paramString;
	}

	public LicenseType getLicenseType() {
		return this.licenseType;
	}

	public void setLicenseType(LicenseType paramLicenseType) {
		this.licenseType = paramLicenseType;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String paramString) {
		this.company = paramString;
	}

	public int getNumCopies() {
		return this.numCopies;
	}

	public void setNumCopies(int paramInt) {
		this.numCopies = paramInt;
	}

	public Date getExpiresDate() {
		if (this.expiresDate == null) {
			return null;
		}
		return new Date(this.expiresDate.getTime());
	}

	public void setExpiresDate(Date paramDate) {
		this.expiresDate = paramDate;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String paramString) {
		this.signature = paramString;
	}

	public byte[] getFingerprint() {
		StringBuffer localStringBuffer = new StringBuffer(100);
		localStringBuffer.append(this.licenseID);
		if (this.mac != null && this.mac.trim().length() > 0) {
			localStringBuffer.append(this.mac);
		}
		localStringBuffer.append(this.product);
		localStringBuffer.append(this.version);
		localStringBuffer.append(this.licenseType);
		localStringBuffer.append(this.numCopies);
		if (this.expiresDate != null) {
			localStringBuffer.append(formatter.format(this.expiresDate));
		}
		if (this.name != null) {
			localStringBuffer.append(this.name);
		}
		if (this.company != null) {
			localStringBuffer.append(this.company);
		}
		return localStringBuffer.toString().getBytes();
	}

	public boolean equals(Object paramObject) {
		if (!(paramObject instanceof License)) {
			return false;
		}
		return (this == paramObject)
				|| (this.licenseID == ((License) paramObject).getLicenseID());
	}

	public static String toXML(License paramLicense) throws Exception {
		Element localElement = new Element("license");
		localElement.addContent(new Element("licenseID").addContent(""
				+ paramLicense.getLicenseID()));
		localElement.addContent(new Element("product").addContent(paramLicense
				.getProduct()));
		localElement.addContent(new Element("licenseType")
				.addContent(paramLicense.getLicenseType().toString()));

		localElement.addContent(new Element("name").addContent((paramLicense
				.getName() == null) ? "" : paramLicense.getName()));

		localElement.addContent(new Element("company").addContent((paramLicense
				.getCompany() == null) ? "" : paramLicense.getCompany()));

		localElement.addContent(new Element("version").addContent(paramLicense
				.getVersion()));
		localElement.addContent(new Element("numCopies").addContent(""
				+ paramLicense.getNumCopies()));
		localElement.addContent(new Element("expiresDate")
				.addContent((paramLicense.getExpiresDate() == null) ? ""
						: formatter.format(paramLicense.getExpiresDate())));

		localElement.addContent(new Element("signature")
				.addContent((paramLicense.getSignature() == null) ? ""
						: paramLicense.getSignature()));

		Document localDocument = new Document(localElement);

		XMLOutputter localXMLOutputter = new XMLOutputter();
		StringWriter localStringWriter = new StringWriter(500);
		localXMLOutputter.output(localDocument, localStringWriter);
		return localStringWriter.toString();
	}

	public static License fromXML(String paramString) throws Exception {
		SAXBuilder localSAXBuilder = new SAXBuilder();
		StringReader localStringReader = new StringReader(paramString);
		Document localDocument = localSAXBuilder.build(localStringReader);
		localStringReader.close();
		Element localElement = localDocument.getRootElement();
		int i = Integer.parseInt(localElement.getChild("licenseID").getText());
		String str1 = localElement.getChild("product").getText();
		String str2 = localElement.getChild("version").getText();
		LicenseType localLicenseType = LicenseType.fromString(localElement
				.getChild("licenseType").getText());

		License localLicense = new License(i, str1, str2, localLicenseType);
		String str3 = localElement.getChild("name").getText();
		if ((str3 != null) && (!str3.equals(""))) {
			localLicense.setName(str3);
		}
		String str4 = localElement.getChild("company").getText();
		if ((str4 != null) && (!str4.equals(""))) {
			localLicense.setCompany(str4);
		}
		localLicense.setNumCopies(Integer.parseInt(localElement.getChild(
				"numCopies").getText()));
		String str5 = localElement.getChild("expiresDate").getText();
		if ((str5 != null) && (!str5.equals(""))) {
			Date localDate = formatter.parse(str5);
			localLicense.setExpiresDate(localDate);
		}
		localLicense.setSignature(localElement.getChild("signature").getText());

		return localLicense;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public static final class LicenseType {
		private final String name;
		public static final LicenseType NON_COMMERCIAL = new LicenseType(
				"Non-Commercial");

		public static final LicenseType COMMERCIAL = new LicenseType(
				"Commercial");

		private LicenseType(String paramString) {
			this.name = paramString;
		}

		public String toString() {
			return this.name;
		}

		public static LicenseType fromString(String paramString) {
			if (NON_COMMERCIAL.toString().equals(paramString)) {
				return NON_COMMERCIAL;
			}
			if (COMMERCIAL.toString().equals(paramString)) {
				return COMMERCIAL;
			}
			return NON_COMMERCIAL;
		}
	}
}
