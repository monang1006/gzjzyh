package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_DATABACKUP"
 *     
*/
public class ToaSysmanageDatabackup implements Serializable {

    /** identifier field */
    private String backupId;

    /** nullable persistent field */
    private String backupTime;

    /** nullable persistent field */
    private String backupFile;

    /** nullable persistent field */
    private String backupMethod;

    /** nullable persistent field */
    private String backupOpenstate;

    /** full constructor */
    public ToaSysmanageDatabackup(String backupId, String backupTime, String backupFile, String backupMethod, String backupOpenstate) {
        this.backupId = backupId;
        this.backupTime = backupTime;
        this.backupFile = backupFile;
        this.backupMethod = backupMethod;
        this.backupOpenstate = backupOpenstate;
    }

    /** default constructor */
    public ToaSysmanageDatabackup() {
    }

    /** minimal constructor */
    public ToaSysmanageDatabackup(String backupId) {
        this.backupId = backupId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="BACKUP_ID"
     *         
     */
    public String getBackupId() {
        return this.backupId;
    }

    public void setBackupId(String backupId) {
        this.backupId = backupId;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_TIME"
     *             length="30"
     *         
     */
    public String getBackupTime() {
        return this.backupTime;
    }

    public void setBackupTime(String backupTime) {
        this.backupTime = backupTime;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_FILE"
     *             length="400"
     *         
     */
    public String getBackupFile() {
        return this.backupFile;
    }

    public void setBackupFile(String backupFile) {
        this.backupFile = backupFile;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_METHOD"
     *             length="1"
     *         
     */
    public String getBackupMethod() {
        return this.backupMethod;
    }

    public void setBackupMethod(String backupMethod) {
        this.backupMethod = backupMethod;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_OPENSTATE"
     *             length="1"
     *         
     */
    public String getBackupOpenstate() {
        return this.backupOpenstate;
    }

    public void setBackupOpenstate(String backupOpenstate) {
        this.backupOpenstate = backupOpenstate;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("backupId", getBackupId())
            .toString();
    }

}
