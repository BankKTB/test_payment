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
 * <p>Java class for MarketPractice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MarketPractice"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Regy" type="{urn:iso:std:iso:20022:tech:xsd:head.001.001.02}HVPS__RestrictedFINXMax350Text"/&gt;
 *         &lt;element name="Id" type="{urn:iso:std:iso:20022:tech:xsd:head.001.001.02}HVPS__RestrictedFINXMax2048Text"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MarketPractice", namespace = "urn:iso:std:iso:20022:tech:xsd:head.001.001.02", propOrder = {
    "regy",
    "id"
})
public class MarketPractice {

    @XmlElement(name = "Regy", required = true)
    protected String regy;
    @XmlElement(name = "Id", required = true)
    protected String id;

    /**
     * Gets the value of the regy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegy() {
        return regy;
    }

    /**
     * Sets the value of the regy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegy(String value) {
        this.regy = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}