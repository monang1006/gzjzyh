
package com.strongit.oa.sms.gsm3040;

import com.strongit.oa.sms.gsm3040.ie.*;
import com.strongit.oa.sms.wappush.WapSiPdu;

//PduUtils Library - A Java library for generating GSM 3040 Protocol Data Units (PDUs)
//
//Copyright (C) 2008, Ateneo Java Wireless Competency Center/Blueblade Technologies, Philippines.
//PduUtils is distributed under the terms of the Apache License version 2.0
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
public class PduFactory
{
	public static SmsSubmitPdu newSmsSubmitPdu()
	{
		// apply defaults
		int additionalFields = PduUtils.TP_RD_ACCEPT_DUPLICATES | PduUtils.TP_VPF_INTEGER;
		return newSmsSubmitPdu(additionalFields);
	}

	public static SmsSubmitPdu newSmsSubmitPdu(int additionalFields)
	{
		// remove any TP_MTI values
		int firstOctet = PduUtils.TP_MTI_SMS_SUBMIT | additionalFields;
		return (SmsSubmitPdu) createPdu(firstOctet);
	}

	public static WapSiPdu newWapSiPdu()
	{
		// apply defaults
		int additionalFields = PduUtils.TP_RD_ACCEPT_DUPLICATES | PduUtils.TP_VPF_INTEGER;
		return newWapSiPdu(additionalFields);
	}

	public static WapSiPdu newWapSiPdu(int additionalFields)
	{
		// remove any TP_MTI values
		WapSiPdu pdu;
		int firstOctet = PduUtils.TP_MTI_SMS_SUBMIT | additionalFields;
		int messageType = getFirstOctetField(firstOctet, PduUtils.TP_MTI_MASK);
		switch (messageType)
		{
			case PduUtils.TP_MTI_SMS_SUBMIT:
				pdu = new WapSiPdu();
				break;
			default:
				throw new RuntimeException("Invalid TP-MTI value: " + messageType);
		}
		pdu.setFirstOctet(firstOctet);
		return pdu;
	}

	// NOTE: this is only for testing since Incoming Message Pdus
	//       are created directly using createPdu()
	public static SmsSubmitPdu newSmsDeliveryPdu()
	{
		int firstOctet = PduUtils.TP_MTI_SMS_DELIVER | PduUtils.TP_MMS_MORE_MESSAGES;
		return (SmsSubmitPdu) createPdu(firstOctet);
	}

	public static SmsDeliveryPdu newSmsDeliveryPdu(int additionalFields)
	{
		// remove any TP_MTI values
		int firstOctet = PduUtils.TP_MTI_SMS_DELIVER | additionalFields;
		return (SmsDeliveryPdu) createPdu(firstOctet);
	}

	private static int getFirstOctetField(int firstOctet, int fieldName)
	{
		return firstOctet & ~fieldName;
	}

	// used to determine what Pdu to use based on the first octet
	// this is the only way to instantiate a Pdu object
	public static Pdu createPdu(int firstOctet)
	{
		Pdu pdu = null;
		int messageType = getFirstOctetField(firstOctet, PduUtils.TP_MTI_MASK);
		switch (messageType)
		{
			case PduUtils.TP_MTI_SMS_DELIVER:
				pdu = new SmsDeliveryPdu();
				break;
			case PduUtils.TP_MTI_SMS_STATUS_REPORT:
				pdu = new SmsStatusReportPdu();
				break;
			case PduUtils.TP_MTI_SMS_SUBMIT:
				pdu = new SmsSubmitPdu();
				break;
			default:
				throw new RuntimeException("Invalid TP-MTI value: " + messageType);
		}
		// once set, you can't change it
		// this method is only available in this package
		pdu.setFirstOctet(firstOctet);
		return pdu;
	}
}
