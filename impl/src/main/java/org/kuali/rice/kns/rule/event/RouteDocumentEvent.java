/*
 * Copyright 2005-2008 The Kuali Foundation
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
package org.kuali.rice.kns.rule.event;

import java.util.ArrayList;
import java.util.List;

import org.kuali.rice.kns.document.Document;
import org.kuali.rice.kns.rule.BusinessRule;
import org.kuali.rice.kns.rule.RouteDocumentRule;

/**
 * This class represents the route event that is part of an eDoc in Kuali. This could be triggered when a user presses the route
 * button for a given document or it could happen when another piece of code calls the route method in the document service.
 * 
 * 
 */
public final class RouteDocumentEvent extends KualiDocumentEventBase {
    /**
     * Constructs a RouteDocumentEvent with the specified errorPathPrefix and document
     * 
     * @param errorPathPrefix
     * @param document
     */
    public RouteDocumentEvent(String errorPathPrefix, Document document) {
        super("creating route event for document " + getDocumentId(document), errorPathPrefix, document);
    }

    /**
     * Constructs a RouteDocumentEvent with the given document
     * 
     * @param document
     */
    public RouteDocumentEvent(Document document) {
        this("", document);
    }

    /**
     * @see org.kuali.rice.kns.rule.event.KualiDocumentEvent#getRuleInterfaceClass()
     */
    public Class getRuleInterfaceClass() {
        return RouteDocumentRule.class;
    }

    /**
     * @see org.kuali.rice.kns.rule.event.KualiDocumentEvent#invokeRuleMethod(org.kuali.rice.kns.rule.BusinessRule)
     */
    public boolean invokeRuleMethod(BusinessRule rule) {
        return ((RouteDocumentRule) rule).processRouteDocument(document);
    }

    /**
     * @see org.kuali.rice.kns.rule.event.KualiDocumentEvent#generateEvents()
     */
    public List generateEvents() {
        List events = new ArrayList();
        events.add(new SaveDocumentEvent(getDocument()));
        return events;
    }
}