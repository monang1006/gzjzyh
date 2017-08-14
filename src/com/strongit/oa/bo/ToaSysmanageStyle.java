package com.strongit.oa.bo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="T_OA_SYSMANAGE_STYLE"
 *     
*/
public class ToaSysmanageStyle implements Serializable {

    /** identifier field */
    private String styleId;

    /** nullable persistent field */
    private String styleName;

    /** nullable persistent field */
    private String stylePath;

    /** nullable persistent field */
    private String styleDesc;

    /** nullable persistent field */
    private String styleState;

    /** full constructor */
    public ToaSysmanageStyle(String styleId, String styleName, String stylePath, String styleDesc, String styleState) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.stylePath = stylePath;
        this.styleDesc = styleDesc;
        this.styleState = styleState;
    }

    /** default constructor */
    public ToaSysmanageStyle() {
    }

    /** minimal constructor */
    public ToaSysmanageStyle(String styleId) {
        this.styleId = styleId;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *             type="java.lang.String"
     *             column="STYLE_ID"
     *         
     */
    public String getStyleId() {
        return this.styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    /** 
     *            @hibernate.property
     *             column="STYLE_NAME"
     *             length="40"
     *         
     */
    public String getStyleName() {
        return this.styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    /** 
     *            @hibernate.property
     *             column="STYLE_PATH"
     *             length="400"
     *         
     */
    public String getStylePath() {
        return this.stylePath;
    }

    public void setStylePath(String stylePath) {
        this.stylePath = stylePath;
    }

    /** 
     *            @hibernate.property
     *             column="STYLE_DESC"
     *             length="4000"
     *         
     */
    public String getStyleDesc() {
        return this.styleDesc;
    }

    public void setStyleDesc(String styleDesc) {
        this.styleDesc = styleDesc;
    }

    /** 
     *            @hibernate.property
     *             column="STYLE_STATE"
     *             length="1"
     *         
     */
    public String getStyleState() {
        return this.styleState;
    }

    public void setStyleState(String styleState) {
        this.styleState = styleState;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("styleId", getStyleId())
            .toString();
    }

}
