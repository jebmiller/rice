/*
 * Copyright 2007-2009 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kns.lookup.keyvalues;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kuali.rice.core.util.KeyLabelPair;
import org.kuali.rice.kim.bo.types.dto.KimTypeInfo;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kim.service.support.KimTypeService;
import org.kuali.rice.kim.util.KimCommonUtils;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KimAttributeValuesFinder extends KeyValuesBase {

	private static final Logger LOG = Logger.getLogger( KimAttributeValuesFinder.class );
	
	protected String kimTypeId;
	protected String kimAttributeName; 
	
	/**
	 * @see org.kuali.rice.kns.lookup.keyvalues.KeyValuesFinder#getKeyValues()
	 */
	public List<KeyLabelPair> getKeyValues() {
        KimTypeInfo kimType = KIMServiceLocator.getTypeInfoService().getKimType(kimTypeId);
        if ( kimType != null ) {
	        KimTypeService service = KimCommonUtils.getKimTypeService(kimType);
	        if ( service != null ) {
				return service.getAttributeValidValues(kimTypeId,kimAttributeName);
	        } else {
	        	LOG.error( "Unable to get type service " + kimType.getKimTypeServiceName() );
	        }
        } else {
        	LOG.error( "Unable to obtain KIM type for kimTypeId=" + kimTypeId );
        }
        return new ArrayList<KeyLabelPair>(0);
	}

	/**
	 * @return the kimAttributeName
	 */
	public String getKimAttributeName() {
		return this.kimAttributeName;
	}

	/**
	 * @param kimAttributeName the kimAttributeName to set
	 */
	public void setKimAttributeName(String kimAttributeName) {
		this.kimAttributeName = kimAttributeName;
	}

	/**
	 * @return the kimTypeId
	 */
	public String getKimTypeId() {
		return this.kimTypeId;
	}

	/**
	 * @param kimTypeId the kimTypeId to set
	 */
	public void setKimTypeId(String kimTypeId) {
		this.kimTypeId = kimTypeId;
	}

}