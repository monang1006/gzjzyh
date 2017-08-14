package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCSUGGESTION"
 *     
*/
public class ToaDocsuggestion implements Serializable {

    /** identifier field */
    private String docsuggestionId;

    /** nullable persistent field */
    private String docsuggestionContent;

    /** nullable persistent field */
    private String userId;

    /** full constructor */
    public ToaDocsuggestion(String docsuggestionId, String docsuggestionContent, String userId) {
        this.docsuggestionId = docsuggestionId;
        this.docsuggestionContent = docsuggestionContent;
        this.userId = userId;
    }

    /** default constructor */
    public ToaDocsuggestion() {
    }

    /** minimal constructor */
    public ToaDocsuggestion(String docsuggestionId) {
        this.docsuggestionId = docsuggestionId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCSUGGESTION_ID"
     *         
     */
    public String getDocsuggestionId() {
        return this.docsuggestionId;
    }

    public void setDocsuggestionId(String docsuggestionId) {
        this.docsuggestionId = docsuggestionId;
    }

    /** 
     *            @hibernate.property
     *             column="DOCSUGGESTION_CONTENT"
     *             length="2000"
     *         
     */
    public String getDocsuggestionContent() {
        return this.docsuggestionContent;
    }

    public void setDocsuggestionContent(String docsuggestionContent) {
        this.docsuggestionContent = docsuggestionContent;
    }

    /** 
     *            @hibernate.property
     *             column="USER_ID"
     *             length="32"
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
            .append("docsuggestionId", getDocsuggestionId())
            .toString();
    }

}
