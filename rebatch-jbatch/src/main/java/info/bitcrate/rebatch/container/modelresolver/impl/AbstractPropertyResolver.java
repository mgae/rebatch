/*
 * Copyright 2012 International Business Machines Corp.
 * 
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership. Licensed under the Apache License, 
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package info.bitcrate.rebatch.container.modelresolver.impl;

import info.bitcrate.rebatch.container.exception.IllegalBatchPropertyException;
import info.bitcrate.rebatch.container.modelresolver.PropertyResolver;
import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public abstract class AbstractPropertyResolver<B> implements PropertyResolver<B> {

	static final int PROPERTY_SYSTEM = 0;
	static final int PROPERTY_JOB_PARAMETER = 1;
	static final int PROPERTY_JOB = 2;
	static final int PROPERTY_STEP = 3;
	static final int PROPERTY_PARTITION = 4;
	
	private static final String EXPR_BEGIN = "#{";
	private static final String EXPR_END = "}";

	private static final String PROP_NAME_BEGIN = "['";
	private static final String PROP_NAME_END = "']";
	
	private static final String DEFAULT_BEGIN = "?:";
	private static final String DEFAULT_END = ";";
	
	private enum ExpressionType {
		SYSTEM_PROPERTIES("systemProperties", PROPERTY_SYSTEM),
		JOB_PARAMETERS("jobParameters", PROPERTY_JOB_PARAMETER),
		JOB_PROPERTIES("jobProperties", PROPERTY_JOB),
		/* Note: This is a non-standard property reference scope */
		STEP_PROPERTIES("stepProperties", PROPERTY_STEP),
		PARTITION_PLAN("partitionPlan", PROPERTY_PARTITION);
		
		final int index;
		final String referenceName;
		final int baseLength;
		
		ExpressionType(String referenceName, int index) {
			this.index = index;
			this.referenceName = referenceName;
			this.baseLength = 
					EXPR_BEGIN.length() + 
					referenceName.length() +
					PROP_NAME_BEGIN.length() +
					PROP_NAME_END.length() +
					EXPR_END.length();
		}
		
		static ExpressionType forReferenceName(String referenceName) {
			for (ExpressionType referenceType : values()) {
				if (referenceType.referenceName.equals(referenceName)) {
					return referenceType;
				}
			}
			
			return null;
		}
	}
	
    protected boolean isPartitionedStep = false;

    //Substitute empty String for unresolvable props
    public static final String UNRESOLVED_PROP_VALUE = "";
    
    public AbstractPropertyResolver(boolean isPartitionStep) {
        this.isPartitionedStep = isPartitionStep;
    }

    @Override
    public B resolve(final B element, Properties jobParameters) {
    	List<Properties> properties = new ArrayList<Properties>(5);
    	
    	if (jobParameters == null) {
    		jobParameters = new Properties();
    	}
    	
    	properties.add(PROPERTY_SYSTEM, System.getProperties());
    	properties.add(PROPERTY_JOB_PARAMETER , jobParameters);
    	properties.add(PROPERTY_JOB, null);
    	properties.add(PROPERTY_STEP, null);
    	properties.add(PROPERTY_PARTITION, null);
    	
    	return resolve(element, properties);
    }
    
    @Override
    public final B resolvePartition(B element, Properties partitionPlan) {
    	List<Properties> properties = new ArrayList<Properties>(5);

    	if (partitionPlan == null) {
    		partitionPlan = new Properties();
    	}
    	
    	properties.add(PROPERTY_SYSTEM, System.getProperties());
    	properties.add(PROPERTY_JOB_PARAMETER , null);
    	properties.add(PROPERTY_JOB, null);
    	properties.add(PROPERTY_STEP, null);
    	properties.add(PROPERTY_PARTITION, partitionPlan);
    	
    	return resolve(element, properties);
	}

    protected void resolveJSLProperties(
    		final JSLProperties jslProperties,
    		final List<Properties> properties,
    		final Properties addedScope) {

    	for (final Property jslProperty : jslProperties.getPropertyList()) {
    		String name = resolveReferences(jslProperty.getName(), properties);
    		String value = resolveReferences(jslProperty.getValue(), properties);
    		
            // Update JAXB model
    		jslProperty.setName(name);
    		jslProperty.setValue(value);
    		
    		if (addedScope != null) {
    			/*-
    			 *  If a new scope is in the process of coming into being, add
    			 *  the properties as we go so that they can be referenced 
    			 *  by subsequent properties of this same JSL properties 
    			 *  element.
    			 */
    			addedScope.put(name, value);
    		}
    	}
    }
    
    /**
     * @param jslProperties 
     * 			JSL properties that are direct children of the current element
     * @param properties 
     * 			Resolved properties from all scopes available to the current
     * 			element
     */
    protected void resolveJSLProperties(
    		final JSLProperties jslProperties,
    		final List<Properties> properties) {
    	
    	resolveJSLProperties(jslProperties, properties, null);
    }
    
    protected static Properties toProperties(
    		final JSLProperties jslProperties) {
    	
    	Properties properties = new Properties();
    	
    	for (final Property jslProperty : jslProperties.getPropertyList()) {
    		String name = jslProperty.getName();
    		String value = jslProperty.getValue();
    		
    		properties.put(name, value);
    	}
    	
    	return properties;
    }
    
    class PropertyReference {
    	final String name;
    	final ExpressionType type;
    	final String defaultValueExpression;

    	public PropertyReference(
    			String name, 
    			ExpressionType type, 
    			String defaultValueExpression) {
    		this.name = name;
    		this.type = type;
    		this.defaultValueExpression = defaultValueExpression;
		}
    	
    	boolean isActive() {
    		return type != ExpressionType.PARTITION_PLAN || isPartitionedStep;
    	}
    	
    	int getLength() {
    		return name.length() + 
    				type.baseLength + 
    				((defaultValueExpression != null) ? 
    						defaultValueExpression.length() + 
    						DEFAULT_BEGIN.length() + 
    						DEFAULT_END.length() : 0);
    	}
    	
    	int getLengthPlusDefaultValExpr() {
    		return getLength() + getDefaultValExprWithDelimiters().length();
    	}
    	
        String getValExpr() {
        	return EXPR_BEGIN + 
        			type.referenceName + 
        			PROP_NAME_BEGIN + 
        			name + 
        			PROP_NAME_END + 
        			EXPR_END;
        }
    	
        String getDefaultValExprWithDelimiters() {
            if (defaultValueExpression != null) {
                return DEFAULT_BEGIN + defaultValueExpression + DEFAULT_END;
            }

            return "";
        }
    }
    
    protected String resolveReferences(
    		String jslValue, 
    		final List<Properties> properties) {
    	
    	int index = 0;
    	PropertyReference reference = null;
    	int count = 0;
    	
    	while ((reference = nextReference(jslValue, index)) != null) {
    		if (!reference.isActive()) {
    			/*-
    			 * References to partition plan properties are not active 
    			 * during the first attempt to resolve values. Partition
    			 * plans are resolved when the sub-jobs for each partition is
    			 * started by the runtime.
    			 */
    			index += reference.getLengthPlusDefaultValExpr();
    			if (++count > 100) {
    				throw new IllegalStateException("infinite loop");
    			}
    			continue;
    		}
    		
    		String value = resolveReference(reference, properties);
    	
            //if the property didn't resolve use the default value if it exists
            if (value.equals(UNRESOLVED_PROP_VALUE)) {
                if (reference.defaultValueExpression != null) {
                    value = resolveReferences(
                    		reference.defaultValueExpression, properties);
                }
            }
            
            jslValue = jslValue.replace(
            		reference.getValExpr() + 
            		reference.getDefaultValExprWithDelimiters(), value);

            index += (value.length() - reference.getLength());
    	}
    	
    	return jslValue;
    }
    
    private PropertyReference nextReference(
    		final String jslValue, 
    		final int startIndex) {
    	
        if (jslValue == null) {
            return null;
        }

        final int exprStart = jslValue.indexOf(EXPR_BEGIN, startIndex);
        
        if (exprStart == -1) {
            return null;
        }
        
        final int exprNameStart = exprStart + EXPR_BEGIN.length();
        final int exprNameEnd = jslValue.indexOf(PROP_NAME_BEGIN, exprNameStart);
        
        if (exprNameEnd == -1) {
    		throw new IllegalBatchPropertyException(
    				"Incorrectly formatted property reference, "
    				+ "missing map access \"['\": " 
					+ jslValue.substring(startIndex));
        }
        
        final int propNameStart = exprNameEnd + PROP_NAME_BEGIN.length();
        final int propNameEnd = jslValue.indexOf(PROP_NAME_END, propNameStart);

        if (propNameEnd == -1) {
    		throw new IllegalBatchPropertyException(
    				"Incorrectly formatted property reference, "
    				+ "missing property delimiter \"']\": " 
					+ jslValue.substring(startIndex));
        }
        
        final int exprEnd = propNameEnd + PROP_NAME_END.length();
        
        if (!jslValue.startsWith(EXPR_END, exprEnd)) {
    		throw new IllegalBatchPropertyException(
    				"Property expression missing '}' delimiter: " 
					+ jslValue.substring(startIndex));
        }

        final String refName = jslValue.substring(exprNameStart, exprNameEnd);
        final ExpressionType type = ExpressionType.forReferenceName(refName);
        
        if (type == null) {
    		throw new IllegalBatchPropertyException(
    				"Unknown property reference name: " + refName);
        }
        
        final String propName = jslValue.substring(propNameStart, propNameEnd);
        String defaultValueExpression = null;

        /*-
         * Look for the ?:<default-value-expression>; syntax after the 
         * property to see if it has a default value
         */
        final int defaultExprFlag = exprEnd + EXPR_END.length();

        if (jslValue.startsWith(DEFAULT_BEGIN, defaultExprFlag)) {
            int defaultExprStart = defaultExprFlag + DEFAULT_BEGIN.length();
        	int defaultExprEnd = jslValue.indexOf(DEFAULT_END, defaultExprStart);
        	
            if (defaultExprEnd == -1) {
                throw new IllegalArgumentException(
                		"The default property expression " 
        				+ jslValue.substring(defaultExprStart)
						+ " is not properly terminated with ';'");
            }
            
            /*-
             * This string does not include the '?:' and ';' 
             * It only contains the content in between.
             */
            defaultValueExpression = 
            		jslValue.substring(defaultExprStart, defaultExprEnd);
        }
        
        return new PropertyReference(propName, type, defaultValueExpression);
	}
    
    private String resolveReference(
    		final PropertyReference reference,
    		final List<Properties> properties) {
    	
    	Properties referencedProperties = properties.get(reference.type.index);
    	
    	if (referencedProperties == null) {
    		throw new IllegalBatchPropertyException(
    				"Referenced " + reference.type.referenceName + "property '" + reference.name + 
    				"' is not in scope at point of reference");
    	}
    	
    	String value = 
    			referencedProperties.getProperty(
    					reference.name, 
    					UNRESOLVED_PROP_VALUE);
    	
    	return value;
    }
}
