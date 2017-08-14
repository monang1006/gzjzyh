package com.strongit.oa.bo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_OA_TRAININFO_ATTACH", catalog = "", schema = "")
public class ToaTraininfoAttach  implements Serializable{
	
	    private String attachId;

	    /** nullable persistent field */
	    private String attachName;

	    /** nullable persistent field */
	    private byte[] attachCon;

	    /** persistent field */
	    private ToaTrainInfo traininfo;
	    
	    public ToaTraininfoAttach(){
	    	
	    }
	    @Column(name = "TRAIN_ATTACH_CON")
		@Lob
		public byte[] getAttachCon() {
			return attachCon;
		}

		public void setAttachCon(byte[] attachCon) {
			this.attachCon = attachCon;
		}
		    @Id
			@Column(name = "TRAIN_ATTACH_ID", nullable = false)
			@GeneratedValue(generator = "hibernate-uuid")
			@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
		public String getAttachId() {
			return attachId;
		}

		public void setAttachId(String attachId) {
			this.attachId = attachId;
		}
		@Column(name = "TRAIN_ATTACH_NAME")
		public String getAttachName() {
			return attachName;
		}

		public void setAttachName(String attachName) {
			this.attachName = attachName;
		}
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "TRAIN_ID")
		public ToaTrainInfo getTraininfo() {
			return traininfo;
		}

		public void setTraininfo(ToaTrainInfo traininfo) {
			this.traininfo = traininfo;
		}


}
