//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.27 at 09:45:12 AM CEST 
//


package info.bitcrate.rebatch.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PartitionPlan complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="PartitionPlan">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="properties" type="{http://xmlns.jcp.org/xml/ns/javaee}Properties" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="partitions" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="threads" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PartitionPlan", propOrder = {
    "properties"
})
public class PartitionPlan {

    protected List<JSLProperties> properties;
    @XmlAttribute(name = "partitions")
    protected String partitions;
    @XmlAttribute(name = "threads")
    protected String threads;

    /**
     * Gets the value of the properties property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the properties property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperties().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link JSLProperties }
     */
    public List<JSLProperties> getProperties() {
        if (properties == null) {
            properties = new ArrayList<JSLProperties>();
        }
        return this.properties;
    }

    /**
     * Gets the value of the partitions property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPartitions() {
        return partitions;
    }

    /**
     * Sets the value of the partitions property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPartitions(String value) {
        this.partitions = value;
    }

    /**
     * Gets the value of the threads property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getThreads() {
        return threads;
    }

    /**
     * Sets the value of the threads property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setThreads(String value) {
        this.threads = value;
    }

}
