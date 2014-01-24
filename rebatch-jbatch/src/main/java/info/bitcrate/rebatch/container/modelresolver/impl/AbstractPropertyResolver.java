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

import info.bitcrate.rebatch.container.exception.BatchContainerRuntimeException;
import info.bitcrate.rebatch.container.exception.IllegalBatchPropertyException;
import info.bitcrate.rebatch.container.exception.JobSpecificationException;
import info.bitcrate.rebatch.container.modelresolver.ContextResolver;
import info.bitcrate.rebatch.container.modelresolver.PropertyResolver;
import info.bitcrate.rebatch.container.modelresolver.ResolverFactory;
import info.bitcrate.rebatch.jaxb.JSLProperties;
import info.bitcrate.rebatch.jaxb.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;

public abstract class AbstractPropertyResolver<B> 
	implements Cloneable, ContextResolver<B>, PropertyResolver<B> {

	static final int PROPERTY_SYSTEM = 0;
	static final int PROPERTY_JOB_PARAMETER = 1;
	static final int PROPERTY_PARTITION = 2;
	static final int PROPERTY_JOB = 3;
	
    //Substitute empty String for unresolvable props
    private static final String UNRESOLVED_PROP_VALUE = "";

    private static final String EXPR_BEGIN = "#{";
	private static final String EXPR_END = "}";

	private static final String PROP_NAME_BEGIN = "['";
	private static final String PROP_NAME_END = "']";
	
	private static final String DEFAULT_BEGIN = "?:";
	private static final String DEFAULT_END = ";";
	
	private enum ExpressionType {
		SYSTEM_PROPERTIES("systemProperties", PROPERTY_SYSTEM),
		JOB_PARAMETERS("jobParameters", PROPERTY_JOB_PARAMETER),
		PARTITION_PLAN("partitionPlan", PROPERTY_PARTITION),
		JOB_PROPERTIES("jobProperties", PROPERTY_JOB);
		
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
    
    public AbstractPropertyResolver() {
    }
    
	@Override
    @SuppressWarnings("unchecked")
    public AbstractPropertyResolver<B> clone() {
    	try {
    		return (AbstractPropertyResolver<B>) super.clone();
    	} catch (CloneNotSupportedException e) {
    		throw new BatchContainerRuntimeException(e);
    	}
    }
    
    public void setPartitionedStep(boolean isPartitionedStep) {
		this.isPartitionedStep = isPartitionedStep;
	}

    @Override
    public B resolve(final B element, Properties jobParameters) {
    	List<Properties> properties = new ArrayList<Properties>(6);
    	
    	if (jobParameters == null) {
    		jobParameters = new Properties();
    	}
    	
    	properties.add(PROPERTY_SYSTEM, System.getProperties());
    	properties.add(PROPERTY_JOB_PARAMETER , jobParameters);
    	properties.add(PROPERTY_PARTITION, null);
    	properties.add(PROPERTY_JOB, null);
    	
    	return resolve(element, properties);
    }
    
    @Override
    public final B resolvePartition(B element, Properties partitionPlan) {
    	List<Properties> properties = new ArrayList<Properties>(6);

    	if (partitionPlan == null) {
    		partitionPlan = new Properties();
    	}
    	
    	properties.add(PROPERTY_SYSTEM, System.getProperties());
    	properties.add(PROPERTY_JOB_PARAMETER , null);
    	properties.add(PROPERTY_PARTITION, partitionPlan);
    	properties.add(PROPERTY_JOB, null);
    	
    	return resolve(element, properties);
	}
    
    protected <T> void _resolve(List<T> elements, List<Properties> properties) {
    	for (T element : elements) {
    		_resolve(element, properties);
    	}
    }
    
	@SuppressWarnings("unchecked")
	protected <T> void _resolve(T element, List<Properties> properties) {
		
		if (element == null) {
    		/*-
    		 * Nothing to resolve
    		 */
			return;
		}
		
		Class<T> klazz = (Class<T>) element.getClass();
		
		PropertyResolver<T> resolver = 
				ResolverFactory.newInstance(klazz, isPartitionedStep); 
		
		resolver.resolve(element, properties);
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
    			 *  If a new scope is in the process of coming into view, add
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
    	
    	if (jslProperties == null) {
    		/*-
    		 * Nothing to resolve
    		 */
    		return;
    	}
    	
    	Properties localProperties = new Properties();
    	properties.add(localProperties);
    	
    	resolveJSLProperties(jslProperties, properties, localProperties);
    	
    	properties.remove(properties.size() - 1);
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
    	
    	while ((reference = nextReference(jslValue, index)) != null) {
			/*-
			 * References to partition plan properties are not active 
			 * during the first attempt to resolve values. Partition
			 * plans are resolved when the sub-jobs for each partition is
			 * started by the runtime.
			 */
    		if (!reference.isActive()) {
    			/*-
    			 *  Try to resolve the reference to ensure #{partitionPlan} is
    			 *  not being used out of proper (step) scope. 
    			 */
    			resolveReference(reference, properties);
    			index += reference.getLengthPlusDefaultValExpr();
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
    	
    	Properties referencedProperties = null;
    	
    	if (reference.type.index != PROPERTY_JOB) {
    		/*-
    		 * May be null such as when partition properties are referenced
    		 * outside of a step.
    		 */
    		referencedProperties = properties.get(reference.type.index);
    	} else {
    		/*-
    		 * Iterate from the deepest scope up to and including the job
    		 * properties scope. Take the first property map which contains
    		 * the referenced property name or end up with the job properties.
    		 */
    		for (int i = properties.size(); --i >= PROPERTY_JOB;) {
    			referencedProperties = properties.get(i);
    			
    			if (referencedProperties.containsKey(reference.name)) {
    				break;
    			}
    		}
    	}
    	
    	if (referencedProperties == null) {
    		throw new IllegalBatchPropertyException(
    				"Referenced " + reference.type.referenceName 
    						+ " property '" + reference.name 
    						+ "' is not in scope at point of reference");
    	}
    	
    	String value = 
    			referencedProperties.getProperty(
    					reference.name, 
    					UNRESOLVED_PROP_VALUE);
    	
    	return value;
    }
	/* ********************************************************************** */

    protected <T> void _resolve(List<T> elements, JobContext jobContext) {
    	for (T element : elements) {
    		_resolve(element, jobContext);
    	}
    }
    
	@SuppressWarnings("unchecked")
	protected <T> void _resolve(T element, JobContext jobContext) {
		
		if (element == null) {
    		/*-
    		 * Nothing to resolve
    		 */
			return;
		}
		
		Class<T> klazz = (Class<T>) element.getClass();
		
		ContextResolver<T> resolver = 
				ResolverFactory.newInstance(klazz, isPartitionedStep); 
		
		resolver.resolve(element, jobContext);
	}

    protected void resolveJSLProperties(
    		final JSLProperties jslProperties,
    		final JobContext jobContext) {

    	if (jslProperties == null) {
    		/*-
    		 * Nothing to resolve
    		 */
    		return;
    	}
    	
    	for (final Property jslProperty : jslProperties.getPropertyList()) {
    		String name = resolveReferences(jslProperty.getName(), jobContext);
    		String value = resolveReferences(jslProperty.getValue(), jobContext);
    		
            // Update JAXB model
    		jslProperty.setName(name);
    		jslProperty.setValue(value);
    	}
    }
	
    protected String resolveReferences(String jslValue, JobContext jobContext) {
        if (jslValue == null) {
            return null;
        }
    	
    	Pattern jobContextSearch = Pattern.compile("\\$\\{job([a-zA-Z]+)\\}");
    	Matcher matcher = jobContextSearch.matcher(jslValue);
    	
    	while (matcher.find()) {
    		String reference = matcher.group();
    		String property = matcher.group(1);
    		String replacement = null;

    		if ("Name".equals(property)) {
    			replacement = jobContext.getJobName();
    		} else if ("InstanceId".equals(property)) {
    			replacement = String.valueOf(jobContext.getInstanceId());
    		} else if ("ExecutionId".equals(property)) {
    			replacement = String.valueOf(jobContext.getExecutionId());
    		} else {
        		throw new JobSpecificationException(
        				"Unknown job context attribute " + property
        						+ " can not be resolved");
    		}

    		jslValue = jslValue.replace(reference, replacement);
    	}
    	
    	return jslValue;
    }
	
	/* ********************************************************************** */

    protected <T> void _resolve(List<T> elements, StepContext stepContext) {
    	for (T element : elements) {
    		_resolve(element, stepContext);
    	}
    }
    
	@SuppressWarnings("unchecked")
	protected <T> void _resolve(T element, StepContext stepContext) {
		
		if (element == null) {
    		/*-
    		 * Nothing to resolve
    		 */
			return;
		}
		
		Class<T> klazz = (Class<T>) element.getClass();
		
		ContextResolver<T> resolver = 
				ResolverFactory.newInstance(klazz, isPartitionedStep); 
		
		resolver.resolve(element, stepContext);
	}

    protected void resolveJSLProperties(
    		final JSLProperties jslProperties,
    		final StepContext stepContext) {

    	if (jslProperties == null) {
    		/*-
    		 * Nothing to resolve
    		 */
    		return;
    	}
    	
    	for (final Property jslProperty : jslProperties.getPropertyList()) {
    		String name = resolveReferences(jslProperty.getName(), stepContext);
    		String value = resolveReferences(jslProperty.getValue(), stepContext);
    		
            // Update JAXB model
    		jslProperty.setName(name);
    		jslProperty.setValue(value);
    	}
    }
	
    protected String resolveReferences(String jslValue, StepContext stepContext) {
        if (jslValue == null) {
            return null;
        }
    	
    	Pattern jobContextSearch = Pattern.compile("\\$\\{step([a-zA-Z]+)\\}");
    	Matcher matcher = jobContextSearch.matcher(jslValue);
    	
    	while (matcher.find()) {
    		if (stepContext == null) {
        		throw new JobSpecificationException(
        				"Step context attributes are not in "
        				+ "scope for reference " + jslValue);
    		}
    		
    		String reference = matcher.group();
    		String property = matcher.group(1);
    		String replacement = null;

    		if ("Name".equals(property)) {
    			replacement = stepContext.getStepName();
    		} else if ("ExecutionId".equals(property)) {
    			replacement = String.valueOf(stepContext.getStepExecutionId());
    		} else {
        		throw new JobSpecificationException(
        				"Unknown step context attribute " + property
        						+ " can not be resolved");
    		}
    		
    		jslValue = jslValue.replace(reference, replacement);
    	}
    	
    	return jslValue;
    }
	
	/* ********************************************************************** */
}
