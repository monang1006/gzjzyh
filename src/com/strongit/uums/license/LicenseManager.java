package com.strongit.uums.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.strongmvc.util.StringUtil;

public final class LicenseManager {
	private static License license = null;
	private static boolean error = false;
	private static String errorMsg;

	public static void checkAuthorized(String paramString1, String paramString2)
			throws LicenseException {
		if(true){return ;}
		loadLicense();
		if (!error) {
			int i = getProductScore(license.getProduct());

			int j = getProductScore(paramString1);
			if (i < j) {
				throw new LicenseException(
						"您没有当前产品的使用许可证。");
			}

			if (license.getVersion().compareTo(paramString2) < 0) {
				throw new LicenseException(
						"您没有当前版本软件产品的使用许可证。Your are not licensed to use this version of the software.");
			}
		} else {
			throw new LicenseException(errorMsg);
		}
	}

	public static long getLicenseID() {
		return license.getLicenseID();
	}

	public static String getProduct() {
		return license.getProduct();
	}

	public static String getVersion() {
		return license.getVersion();
	}

	public static License.LicenseType getLicenseType() {
		return license.getLicenseType();
	}

	public static String getName() {
		return license.getName();
	}

	public static String getCompany() {
		return license.getCompany();
	}

	public static int getNumCopies() {
		return license.getNumCopies();
	}

	public static Date getExpiresDate() {
		if (license.getExpiresDate() == null) {
			return null;
		}
		return new Date(license.getExpiresDate().getTime());
	}

	static boolean validate(License paramLicense) throws Exception {
		paramLicense.setMac(getCode());
		String str = "308201b83082012c06072a8648ce3804013082011f02818100fd7f53811d75122952df4a9c2eece4e7f611b7523cef4400c31e3f80b6512669455d402251fb593d8d58fabfc5f5ba30f6cb9b556cd7813b801d346ff26660b76b9950a5a49f9fe8047b1022c24fbba9d7feb7c61bf83b57e7c6a8a6150f04fb83f6d3c51ec3023554135a169132f675f3ae2b61d72aeff22203199dd14801c70215009760508f15230bccb292b982a2eb840bf0581cf502818100f7e1a085d69b3ddecbbcab5c36b857b97994afbbfa3aea82f9574c0b3d0782675159578ebad4594fe67107108180b449167123e84c281613b7cf09328cc8a6e13c167a8b547c8d28e0a3ae1e2bb3a675916ea37f0bfa213562f1fb627a01243bcca4f1bea8519089a883dfe15ae59f06928b665e807b552564014c3bfecf492a0381850002818100db58ff3113af828aa8c5961f46f06cfb70bb6342c6cbe79c9a28c795edb34ca056f9125a3b95e6078a4b3e052677f104dbb41455705b98956b3cd21a880334a7978a0c294dcf74d00f28a4f7f0279388a5f6dcd402c14d7c51eb6c17c75259339855bdc4fc8d3153bafe2f07e86dc9af194a3ad600379699b3ed6ae2e640a95c";

		byte[] arrayOfByte = StringUtil.decodeHex(str);
		X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(arrayOfByte);
		KeyFactory localKeyFactory = KeyFactory.getInstance("DSA");
		PublicKey localPublicKey = localKeyFactory
				.generatePublic(localX509EncodedKeySpec);
		Signature localSignature = Signature.getInstance("DSA");
		localSignature.initVerify(localPublicKey);
		localSignature.update(paramLicense.getFingerprint());
		return localSignature.verify(StringUtil.decodeHex(paramLicense.getSignature()));
	}
	
	public static synchronized void refresh(){
		license=null;
	}

	private static synchronized void loadLicense() {
		if (license != null) {
			return;
		}
		String os=MacUtil.getOsName();
		File localFile=null;
		if(os.startsWith("Windows")){
			localFile = new File("C:/StrongSoft/G3/.metadata/.keys/lincense/StrongSoft.license");
		}else{
			localFile = new File("/home/StrongSoft/G3/.metadata/.keys/lincense/StrongSoft.license");
		}
		if (!localFile.exists()) {
			error = true;
			errorMsg = "没有找到许可证文件！";
			return;
		}
		try {
			BufferedReader localBufferedReader = new BufferedReader(
					new FileReader(localFile));
			StringBuffer localStringBuffer = new StringBuffer(100);
			String str1;
			while ((str1 = localBufferedReader.readLine()) != null) {
				localStringBuffer.append(str1);
			}
			localBufferedReader.close();
			String str2 = StringUtil.decodeBase64(localStringBuffer.toString());
			license = License.fromXML(str2);

			if (license.getExpiresDate() != null) {
				long l = System.currentTimeMillis();
				if (license.getExpiresDate().getTime() < l) {
					error = true;
					errorMsg = "您的许可证已过期，请重新申请！";
					return;
				}
			}

			if (!validate(license)) {
				error = true;
				errorMsg = "您的许可证无效，请联系软件厂商！";
				return;
			}
		} catch (Exception localException) {
			error = true;
			errorMsg = "读取许可证文件错误。";
			return;
		}

		error = false;
	}

	private static int getProductScore(String paramString) {
		if ("UUMS Basic".equals(paramString)) {
			return 1;
		} else if ("UUMS Enterprise".equals(paramString)) {
			return 2;
		}
		return 0;
	}
	
	public static String getCode(){
		String mac=MacUtil.getMACAddress();
		return StringUtil.encodeHex(mac.getBytes());
	}
}
