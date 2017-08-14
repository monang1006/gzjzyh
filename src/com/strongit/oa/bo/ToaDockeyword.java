package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_DOCKEYWORDS"
 *     
*/
public class ToaDockeyword implements Serializable {

    /** identifier field */
    private String dockeywordsId;

    /** nullable persistent field */
    private String dockeywords;

    /** full constructor */
    public ToaDockeyword(String dockeywordsId, String dockeywords) {
        this.dockeywordsId = dockeywordsId;
        this.dockeywords = dockeywords;
    }

    /** default constructor */
    public ToaDockeyword() {
    }

    /** minimal constructor */
    public ToaDockeyword(String dockeywordsId) {
        this.dockeywordsId = dockeywordsId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="DOCKEYWORDS_ID"
     *         
     */
    public String getDockeywordsId() {
        return this.dockeywordsId;
    }

    public void setDockeywordsId(String dockeywordsId) {
        this.dockeywordsId = dockeywordsId;
    }

    /** 
     *            @hibernate.property
     *             column="DOCKEYWORDS"
     *             length="150"
     *         
     */
    public String getDockeywords() {
        return this.dockeywords;
    }

    public void setDockeywords(String dockeywords) {
        this.dockeywords = dockeywords;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("dockeywordsId", getDockeywordsId())
            .toString();
    }

}
