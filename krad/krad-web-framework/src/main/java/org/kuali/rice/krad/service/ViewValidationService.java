/**
 * Copyright 2005-2016 The Kuali Foundation
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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.datadictionary.validation.result.DictionaryValidationResult;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.view.ViewModel;


/**
 * Validation service for KRAD views
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ViewValidationService {

    /**
     * This is the main validation method that should be used when validating Views
     * Validates the view based on the model passed in, this will correctly use previousView by default
     * as it automatically contains the generated data the validation requires.
     * @param model
     * @return DictionaryValidationResult that contains any errors/messages if any, messages will have already
     * been added to the MessageMap
     */
    public DictionaryValidationResult validateView(ViewModel model);

    /**
     * Additional validation method when you want to explicitly define the View being validated.  Note
     * that the view must have the correct binding information on its InputFields already generated by
     * its lifecycle for this method to be used correctly.
     * @param view
     * @param model
     * @return DictionaryValidationResult that contains any errors/messages if any,, messages will have already
     * been added to the MessageMap
     */
    public DictionaryValidationResult validateView(View view, ViewModel model);

}