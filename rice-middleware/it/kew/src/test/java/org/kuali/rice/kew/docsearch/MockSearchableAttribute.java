/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.kew.docsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.kuali.rice.core.api.data.DataType;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.document.DocumentWithContent;
import org.kuali.rice.kew.api.document.attribute.DocumentAttribute;
import org.kuali.rice.kew.api.document.attribute.DocumentAttributeFactory;
import org.kuali.rice.kew.api.document.attribute.WorkflowAttributeDefinition;
import org.kuali.rice.kew.api.document.search.DocumentSearchCriteria;
import org.kuali.rice.kew.api.extension.ExtensionDefinition;
import org.kuali.rice.kew.framework.document.attribute.SearchableAttribute;

public class MockSearchableAttribute implements SearchableAttribute {

    public static final String SEARCH_CONTENT = "<mockContent>MockSearchableAttribute Search Content</mockContent>";

    @Override
    public String generateSearchContent(ExtensionDefinition extensionDefinition,
            String documentTypeName,
            WorkflowAttributeDefinition attributeDefinition) {
        return SEARCH_CONTENT;
    }

    @Override
    public List<DocumentAttribute> extractDocumentAttributes(ExtensionDefinition extensionDefinition,
            DocumentWithContent documentWithContent) {
        List<DocumentAttribute> savs = new ArrayList<DocumentAttribute>();
        savs.add(DocumentAttributeFactory.createStringAttribute("MockSearchableAttributeKey", "MockSearchableAttributeValue"));
        return savs;
    }

    @Override
    public List<RemotableAttributeField> getSearchFields(ExtensionDefinition extensionDefinition,
            String documentTypeName) {
        List<RemotableAttributeField> fields = new ArrayList<RemotableAttributeField>();
        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create("MockSearchableAttributeKey");
        builder.setLongLabel("title");
        builder.setDataType(DataType.STRING);
		fields.add(builder.build());
        return fields;
    }

    @Override
    public List<RemotableAttributeError> validateDocumentAttributeCriteria(ExtensionDefinition extensionDefinition,
            DocumentSearchCriteria documentSearchCriteria) {
        return Collections.emptyList();
    }

}
