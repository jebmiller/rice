/*
 * Copyright 2006 The Kuali Foundation.
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
package org.kuali.core.rule.event;

import org.kuali.core.document.Document;
import org.kuali.core.rule.BusinessRule;
import org.kuali.core.rule.SaveDocumentRule;

/**
 * This class represents the save event that is part of an eDoc in Kuali. This could be triggered when a user presses the save
 * button for a given document or it could happen when another piece of code calls the save method in the document service. This
 * event does not trigger sub-events for validation.
 * 
 * 
 */
public final class SaveOnlyDocumentEvent extends KualiDocumentEventBase {
    /**
     * Constructs a SaveOnlyDocumentEvent with the specified errorPathPrefix and document
     * 
     * @param document
     * @param errorPathPrefix
     */
    public SaveOnlyDocumentEvent(String errorPathPrefix, Document document) {
        super("creating unvalidated save event for document " + getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a UnvalidatedSaveDocumentEvent with the given document
     * 
     * @param document
     */
    public SaveOnlyDocumentEvent(Document document) {
        this("", document);
    }

    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return SaveDocumentRule.class;
    }

    /**
     * @see org.kuali.core.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.core.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((SaveDocumentRule) rule).processSaveDocument(document);
    }
}