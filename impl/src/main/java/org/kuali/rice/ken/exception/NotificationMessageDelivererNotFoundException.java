/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.ken.exception;

/**
 * This class represents a notification message deliverer not found exception - when a deliverer 
 * that is thought to be registered is not found in the registry.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class NotificationMessageDelivererNotFoundException extends Exception {
    /**
     * Constructs a NotificationMessageDeliveryException instance.
     */
    public NotificationMessageDelivererNotFoundException() {
        super();
    }

    /**
     * Constructs a NotificationMessageDelivererNotFoundException instance.
     * @param message
     * @param cause
     */
    public NotificationMessageDelivererNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a NotificationMessageDelivererNotFoundException instance.
     * @param message
     */
    public NotificationMessageDelivererNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a NotificationMessageDelivererNotFoundException instance.
     * @param cause
     */
    public NotificationMessageDelivererNotFoundException(Throwable cause) {
        super(cause);
    }
}