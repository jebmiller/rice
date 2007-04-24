/*
 * Copyright 2005-2007 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.core.inquiry;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.kuali.Constants;
import org.kuali.core.bo.BusinessObject;
import org.kuali.core.bo.user.UniversalUser;
import org.kuali.core.datadictionary.InquirySectionDefinition;
import org.kuali.core.lookup.CollectionIncomplete;
import org.kuali.core.lookup.LookupUtils;
import org.kuali.core.service.BusinessObjectDictionaryService;
import org.kuali.core.service.DataDictionaryService;
import org.kuali.core.service.EncryptionService;
import org.kuali.core.service.LookupService;
import org.kuali.core.service.PersistenceStructureService;
import org.kuali.core.service.UniversalUserService;
import org.kuali.core.util.ObjectUtils;
import org.kuali.core.util.UrlFactory;
import org.kuali.core.web.ui.Section;
import org.kuali.core.web.ui.SectionBridge;
import org.kuali.rice.KNSServiceLocator;

/**
 * Kuali inquirable implementation. Implements methods necessary to retrieve the business object and render the ui.
 */
public class KualiInquirableImpl implements Inquirable {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(KualiInquirableImpl.class);

    private BusinessObjectDictionaryService dataDictionary;
    private LookupService lookupService;
    private Class businessObjectClass;

    
    public static List<Class> HACK_LIST = new ArrayList<Class>();

    /**
     * Default constructor, initializes services from spring
     */
    public KualiInquirableImpl() {
        lookupService = KNSServiceLocator.getLookupService();
        dataDictionary = KNSServiceLocator.getBusinessObjectDictionaryService();
    }

    /**
     * Return a business object by searching with map, the map keys should be a property name of the business object, with the map
     * value as the value to search for.
     */
    public BusinessObject getBusinessObject(Map fieldValues) {
        if (getBusinessObjectClass() == null) {
            LOG.error("Business object class not set in inquirable.");
            throw new RuntimeException("Business object class not set in inquirable.");
        }

        CollectionIncomplete searchResults = null;
        // user inquiries need to go through user service
        if (UniversalUser.class.equals(getBusinessObjectClass())) {
            searchResults = (CollectionIncomplete) getUniversalUserService().findUniversalUsers(fieldValues);
        }
        else {
            searchResults = (CollectionIncomplete) lookupService.findCollectionBySearch(getBusinessObjectClass(), fieldValues);
        }

        BusinessObject foundObject = null;
        if (searchResults != null && searchResults.size() > 0) {
            foundObject = (BusinessObject) searchResults.get(0);
        }
        return foundObject;
    }


    /**
     * Objects extending KualiInquirableBase must specify the Section objects used to display the inquiry result.
     */
    public List<Section> getSections(BusinessObject bo) {
        
        List<Section> sections = new ArrayList<Section>();
        if (getBusinessObjectClass() == null) {
            LOG.error("Business object class not set in inquirable.");
            throw new RuntimeException("Business object class not set in inquirable.");
        }

        Collection inquirySections = dataDictionary.getInquirySections(getBusinessObjectClass());
        for (Iterator iter = inquirySections.iterator(); iter.hasNext();) {
            
            InquirySectionDefinition inquirySection = (InquirySectionDefinition) iter.next();
            Section section = SectionBridge.toSection(inquirySection, bo);
            sections.add(section);
            
        }

        return sections;
    }

    /**
     * Helper method to build an inquiry url for a result field.
     * 
     * @param bo the business object instance to build the urls for
     * @param propertyName the property which links to an inquirable
     * @return String url to inquiry
     */
    public static String getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
        Properties parameters = new Properties();
        parameters.put(Constants.DISPATCH_REQUEST_PARAMETER, "start");

        // If the field is subAccountNumber, financialSubObjectCode or projectCode and the value is dashes, don't give a url
        if ("subAccountNumber".equals(attributeName) || "financialSubObjectCode".equals(attributeName) || "projectCode".equals(attributeName)) {
            Object objFieldValue = ObjectUtils.getPropertyValue(businessObject, attributeName);
            String fieldValue = objFieldValue == null ? "" : objFieldValue.toString();

            if ("subAccountNumber".equals(attributeName) && fieldValue.equals(Constants.DASHES_SUB_ACCOUNT_NUMBER)) {
                return "";
            }
            if ("financialSubObjectCode".equals(attributeName) && fieldValue.equals(Constants.DASHES_SUB_OBJECT_CODE)) {
                return "";
            }
            if ("projectCode".equals(attributeName) && fieldValue.equals(Constants.DASHES_PROJECT_CODE)) {
                return "";
            }
        }

        BusinessObjectDictionaryService businessDictionary = KNSServiceLocator.getBusinessObjectDictionaryService();
        PersistenceStructureService persistenceStructureService = KNSServiceLocator.getPersistenceStructureService();
        DataDictionaryService dataDictionaryService = KNSServiceLocator.getDataDictionaryService();
        EncryptionService encryptionService = KNSServiceLocator.getEncryptionService();

        Class inquiryBusinessObjectClass = null;
        String attributeRefName = "";
        boolean isPkReference = false;
        if (attributeName.equals(businessDictionary.getTitleAttribute(businessObject.getClass()))) {
            inquiryBusinessObjectClass = businessObject.getClass();
            isPkReference = true;
        }
        else {
            if (ObjectUtils.isNestedAttribute(attributeName)) {
                inquiryBusinessObjectClass = LookupUtils.getNestedReferenceClass(businessObject, attributeName);
            }
            else {
                Map primitiveReference = LookupUtils.getPrimitiveReference(businessObject, attributeName);
                if (primitiveReference != null && !primitiveReference.isEmpty()) {
                    attributeRefName = (String) primitiveReference.keySet().iterator().next();
                    inquiryBusinessObjectClass = (Class) primitiveReference.get(attributeRefName);
                }
            }
        }

        if (inquiryBusinessObjectClass == null || businessDictionary.isInquirable(inquiryBusinessObjectClass) == null || !businessDictionary.isInquirable(inquiryBusinessObjectClass).booleanValue()) {
            return Constants.EMPTY_STRING;
        }

        synchronized (HACK_LIST) {
            for (Class clazz : HACK_LIST) {
                if (clazz.isAssignableFrom(inquiryBusinessObjectClass)) {
                    inquiryBusinessObjectClass = clazz;
                    break;
                }
            }    
        }
        
        parameters.put(Constants.BUSINESS_OBJECT_CLASS_ATTRIBUTE, inquiryBusinessObjectClass.getName());

        List keys = new ArrayList();
        if (persistenceStructureService.isPersistable(inquiryBusinessObjectClass)) {
            keys = persistenceStructureService.listPrimaryKeyFieldNames(inquiryBusinessObjectClass);
        }

        // build key value url parameters used to retrieve the business object
        String keyName = null;
        String keyConversion = null;
        String encryptedList = "";
        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            keyName = (String) iter.next();
            keyConversion = keyName;
            if (ObjectUtils.isNestedAttribute(attributeName)) {
                keyConversion = ObjectUtils.getNestedAttributePrefix(attributeName) + "." + keyName;
            }
            else {
                if (isPkReference) {
                    keyConversion = keyName;
                }
                else {
                    keyConversion = persistenceStructureService.getForeignKeyFieldName(businessObject.getClass(), attributeRefName, keyName);
                }
            }
            Object keyValue = null;
            if (keyConversion != null) {
                keyValue = ObjectUtils.getPropertyValue(businessObject, keyConversion);
            }

            if (keyValue == null) {
                keyValue = "";
            }
            else {
                keyValue = keyValue.toString();
            }

            // Encrypt value if it is a secure field
            String displayWorkgroup = dataDictionaryService.getAttributeDisplayWorkgroup(businessObject.getClass(), keyName);
            if (StringUtils.isNotBlank(displayWorkgroup)) {
                try {
                    keyValue = encryptionService.encrypt(keyValue);
                }
                catch (GeneralSecurityException e) {
                    LOG.error("Exception while trying to encrypted value for inquiry framework.", e);
                    throw new RuntimeException(e);
                }

                // add to parameter list so that KualiInquiryAction can identify which parameters are encrypted
                if (encryptedList.equals("")) {
                    encryptedList = keyName;
                }
                else {
                    encryptedList = encryptedList + Constants.FIELD_CONVERSIONS_SEPERATOR + keyName;
                }
            }

            parameters.put(keyName, keyValue);
        }

        // if we did encrypt a value (or values), add the list of those that are encrypted to the parameters
        if (!encryptedList.equals("")) {
            parameters.put(Constants.ENCRYPTED_LIST_PREFIX, encryptedList);
        }

        return UrlFactory.parameterizeUrl(Constants.INQUIRY_ACTION, parameters);
    }

    public void addAdditionalSections(List columns, BusinessObject bo) {
    }

    /**
     * @see org.kuali.core.inquiry.Inquirable#getHtmlMenuBar()
     */
    public String getHtmlMenuBar() {
        // TODO: replace with inquiry menu bar
        return dataDictionary.getLookupMenuBar(getBusinessObjectClass());
    }

    /**
     * @see org.kuali.core.inquiry.Inquirable#getTitle()
     */
    public String getTitle() {
        return dataDictionary.getInquiryTitle(getBusinessObjectClass());
    }

    /**
     * @return Returns the businessObjectClass.
     */
    public Class getBusinessObjectClass() {
        return businessObjectClass;
    }

    /**
     * @param businessObjectClass The businessObjectClass to set.
     */
    public void setBusinessObjectClass(Class businessObjectClass) {
        this.businessObjectClass = businessObjectClass;
    }

    /**
     * Gets the kualiUserService attribute.
     * 
     * @return Returns the kualiUserService.
     */
    public UniversalUserService getUniversalUserService() {
        return KNSServiceLocator.getUniversalUserService();
    }

}