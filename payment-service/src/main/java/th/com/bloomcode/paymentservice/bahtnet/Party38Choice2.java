//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.05.16 at 11:34:25 PM ICT 
//


package th.com.bloomcode.paymentservice.bahtnet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Party38Choice__2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Party38Choice__2"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="OrgId" type="{urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08}OrganisationIdentification29__1"/&gt;
 *           &lt;element name="PrvtId" type="{urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08}PersonIdentification13__2"/&gt;
 *         &lt;/choice&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Party38Choice__2", propOrder = {
    "orgId",
    "prvtId"
})
public class Party38Choice2 {

    @XmlElement(name = "OrgId")
    protected OrganisationIdentification291 orgId;
    @XmlElement(name = "PrvtId")
    protected PersonIdentification132 prvtId;

    /**
     * Gets the value of the orgId property.
     * 
     * @return
     *     possible object is
     *     {@link OrganisationIdentification291 }
     *     
     */
    public OrganisationIdentification291 getOrgId() {
        return orgId;
    }

    /**
     * Sets the value of the orgId property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrganisationIdentification291 }
     *     
     */
    public void setOrgId(OrganisationIdentification291 value) {
        this.orgId = value;
    }

    /**
     * Gets the value of the prvtId property.
     * 
     * @return
     *     possible object is
     *     {@link PersonIdentification132 }
     *     
     */
    public PersonIdentification132 getPrvtId() {
        return prvtId;
    }

    /**
     * Sets the value of the prvtId property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonIdentification132 }
     *     
     */
    public void setPrvtId(PersonIdentification132 value) {
        this.prvtId = value;
    }

}