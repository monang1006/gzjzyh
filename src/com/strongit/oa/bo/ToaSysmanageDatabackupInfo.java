package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_DATABACKUP_INFO"
 *     
*/
public class ToaSysmanageDatabackupInfo implements Serializable {

    /** identifier field */
    private String backupFileId;

    /** nullable persistent field */
    private Long backupFileSize;

    /** nullable persistent field */
    private String backupFileName;

    /** full constructor */
    public ToaSysmanageDatabackupInfo(String backupFileId, Long backupFileSize, String backupFileName) {
        this.backupFileId = backupFileId;
        this.backupFileSize = backupFileSize;
        this.backupFileName = backupFileName;
    }

    /** default constructor */
    public ToaSysmanageDatabackupInfo() {
    }

    /** minimal constructor */
    public ToaSysmanageDatabackupInfo(String backupFileId) {
        this.backupFileId = backupFileId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="BACKUP_FILE_ID"
     *         
     */
    public String getBackupFileId() {
        return this.backupFileId;
    }

    public void setBackupFileId(String backupFileId) {
        this.backupFileId = backupFileId;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_FILE_SIZE"
     *             length="10"
     *         
     */
    public Long getBackupFileSize() {
        return this.backupFileSize;
    }

    public void setBackupFileSize(Long backupFileSize) {
        this.backupFileSize = backupFileSize;
    }

    /** 
     *            @hibernate.property
     *             column="BACKUP_FILE_NAME"
     *             length="400"
     *         
     */
    public String getBackupFileName() {
        return this.backupFileName;
    }

    public void setBackupFileName(String backupFileName) {
        this.backupFileName = backupFileName;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("backupFileId", getBackupFileId())
            .toString();
    }

}
