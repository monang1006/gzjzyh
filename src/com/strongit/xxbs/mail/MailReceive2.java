package com.strongit.xxbs.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class MailReceive2
{
	private MimeMessage msg;
	private StringBuffer bodytext = new StringBuffer();
	private String dateformate = "yyyy-MM-dd HH:mm";

	public MailReceive2(MimeMessage msg)
	{
		this.msg = msg;
	}
	
    public void setMsg(MimeMessage msg)
    {  
        this.msg = msg;  
    }
    
	public String getFrom() throws MessagingException
	{
		InternetAddress[] address = (InternetAddress[]) msg.getFrom();
		String from = address[0].getAddress();
		if (from == null)
		{
			from = "";
		}
		String personal = address[0].getPersonal();
		if (personal == null)
		{
			personal = "";
		}
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	public String getMailAddress(String type) throws MessagingException,
			UnsupportedEncodingException
	{
		String mailaddr = "";
		String addrType = type.toUpperCase();
		InternetAddress[] address = null;

		if (addrType.equals("TO") || addrType.equals("CC")
				|| addrType.equals("BCC"))
		{
			if (addrType.equals("TO"))
			{
				address = (InternetAddress[]) msg
						.getRecipients(Message.RecipientType.TO);
			}
			if (addrType.equals("CC"))
			{
				address = (InternetAddress[]) msg
						.getRecipients(Message.RecipientType.CC);
			}
			if (addrType.equals("BCC"))
			{
				address = (InternetAddress[]) msg
						.getRecipients(Message.RecipientType.BCC);
			}

			if (address != null)
			{
				for (int i = 0; i < address.length; i++)
				{
					String mail = address[i].getAddress();
					if (mail == null)
					{
						mail = "";
					}
					else
					{
						mail = MimeUtility.decodeText(mail);
					}
					String personal = address[i].getPersonal();
					if (personal == null)
					{
						personal = "";
					}
					else
					{
						personal = MimeUtility.decodeText(personal);
					}
					String compositeto = personal + "<" + mail + ">";
					mailaddr += "," + compositeto;
				}
				mailaddr = mailaddr.substring(1);
			}
		}
		else
		{
			throw new RuntimeException("Error email Type!");
		}
		return mailaddr;
	}

	public String getSubject() throws UnsupportedEncodingException,
			MessagingException
	{
		String subject = "";
		subject = MimeUtility.decodeText(msg.getSubject());
		if (subject == null)
		{
			subject = "";
		}
		return subject;
	}

	public String getSendDate() throws MessagingException
	{
		Date sendDate = msg.getSentDate();
		SimpleDateFormat smd = new SimpleDateFormat(dateformate);
		return smd.format(sendDate);
	}

	public String getBodyText()
	{

		return bodytext.toString();
	}

	public void getMailContent(Part part) throws MessagingException,
			IOException
	{

		String contentType = part.getContentType();
		int nameindex = contentType.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
		{
			conname = true;
		}
		System.out.println("CONTENTTYPE:" + contentType);
		if (part.isMimeType("text/plain") && !conname)
		{
			bodytext.append((String) part.getContent());
		}
		else if (part.isMimeType("text/html") && !conname)
		{
			bodytext.append((String) part.getContent());
		}
		else if (part.isMimeType("multipart/*"))
		{
			Multipart multipart = (Multipart) part.getContent();
			int count = multipart.getCount();
			for (int i = 0; i < count; i++)
			{
				getMailContent(multipart.getBodyPart(i));
			}
		}
		else if (part.isMimeType("message/rfc822"))
		{
			getMailContent((Part) part.getContent());
		}

	}

	public boolean getReplySign() throws MessagingException
	{
		boolean replySign = false;
		String needreply[] = msg.getHeader("Disposition-Notification-TO");
		if (needreply != null)
		{
			replySign = true;
		}
		return replySign;
	}

	public String getMessageId() throws MessagingException
	{
		return msg.getMessageID();
	}

	public boolean isNew() throws MessagingException
	{
		boolean isnew = false;
		Flags flags = ((Message) msg).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		System.out.println("flags's length:" + flag.length);
		for (int i = 0; i < flag.length; i++)
		{
			if (flag[i] == Flags.Flag.SEEN)
			{
				isnew = true;
				System.out.println("seen message .......");
				break;
			}
		}
		return isnew;
	}

	public void setDateformate(String dateformate)
	{
		this.dateformate = dateformate;
	}

	public void receive(Part part, int i) throws MessagingException,
			IOException
	{
		System.out.println("------------------START-----------------------");
		System.out.println("Message" + i + " subject:" + getSubject());
		System.out.println("Message" + i + " from:" + getFrom());
		getMailContent(part);
		System.out.println("Message" + i + " content:" + getBodyText());
		System.out.println("Message" + i + " isNew:" + isNew());
		System.out.println("Message" + i + " replySign:" + getReplySign());
		System.out.println("------------------END-----------------------");
	}

	public static void main(String[] args) throws MessagingException,
			IOException
	{
		Properties props = new Properties();
		props.setProperty("mail.smtp.host", "smtp.163.com");
		props.setProperty("mail.smtp.auth", "true");
		Session session = Session.getDefaultInstance(props, null);
		URLName urlname = new URLName("pop3", "pop.163.com", 110, null,
				"strongtest123", "strongtest789");

		Store store = session.getStore(urlname);
		store.connect();
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);
		Message msgs[] = folder.getMessages();
		int count = msgs.length;
		System.out.println("Message Count:" + count);
		MailReceive2 rm = null;
		for (int i = 0; i < count; i++)
		{
			rm = new MailReceive2((MimeMessage) msgs[i]);
			rm.receive(msgs[i], i);
		}
	}
}
