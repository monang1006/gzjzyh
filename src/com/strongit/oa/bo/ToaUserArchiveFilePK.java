package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ToaUserArchiveFilePK implements Serializable {

    /** identifier field */
    private String fileId;

    /** identifier field */
    private String userId;

    /** full constructor */
    public ToaUserArchiveFilePK(String fileId, String userId) {
        this.fileId = fileId;
        this.userId = userId;
    }

    /** default constructor */
    public ToaUserArchiveFilePK() {
    }

    /** 
     *                @hibernate.property
     *                 column="FILE_ID"
     *                 length="32"
     *             
     */
    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /** 
     *                @hibernate.property
     *                 column="USER_ID"
     *                 length="32"
     *             
     */
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("fileId", getFileId())
            .append("userId", getUserId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToaUserArchiveFilePK) ) return false;
        ToaUserArchiveFilePK castOther = (ToaUserArchiveFilePK) other;
        return new EqualsBuilder()
            .append(this.getFileId(), castOther.getFileId())
            .append(this.getUserId(), castOther.getUserId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getFileId())
            .append(getUserId())
            .toHashCode();
    }

}
