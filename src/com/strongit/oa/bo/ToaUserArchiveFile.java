package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_USER_ARCHIVE_FILE"
 *     
*/
public class ToaUserArchiveFile implements Serializable {

    /** identifier field */
    private com.strongit.oa.bo.ToaUserArchiveFilePK comp_id;

    /** nullable persistent field */
    private com.strongit.oa.bo.ToaArchiveFile toaArchiveFile;

    /** nullable persistent field */
    private com.strongit.oa.bo.ToaUser toaUser;

    /** full constructor */
    public ToaUserArchiveFile(com.strongit.oa.bo.ToaUserArchiveFilePK comp_id, com.strongit.oa.bo.ToaArchiveFile toaArchiveFile, com.strongit.oa.bo.ToaUser toaUser) {
        this.comp_id = comp_id;
        this.toaArchiveFile = toaArchiveFile;
        this.toaUser = toaUser;
    }

    /** default constructor */
    public ToaUserArchiveFile() {
    }

    /** minimal constructor */
    public ToaUserArchiveFile(com.strongit.oa.bo.ToaUserArchiveFilePK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public com.strongit.oa.bo.ToaUserArchiveFilePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(com.strongit.oa.bo.ToaUserArchiveFilePK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="FILE_ID"
     *         
     */
    public com.strongit.oa.bo.ToaArchiveFile getToaArchiveFile() {
        return this.toaArchiveFile;
    }

    public void setToaArchiveFile(com.strongit.oa.bo.ToaArchiveFile toaArchiveFile) {
        this.toaArchiveFile = toaArchiveFile;
    }

    /** 
     *            @hibernate.many-to-one
     *             update="false"
     *             insert="false"
     *         
     *            @hibernate.column
     *             name="USER_ID"
     *         
     */
    public com.strongit.oa.bo.ToaUser getToaUser() {
        return this.toaUser;
    }

    public void setToaUser(com.strongit.oa.bo.ToaUser toaUser) {
        this.toaUser = toaUser;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToaUserArchiveFile) ) return false;
        ToaUserArchiveFile castOther = (ToaUserArchiveFile) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
