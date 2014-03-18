/*
 * Copyright 2005-2007 The Kuali Foundation
 *
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
package org.kuali.rice.kew.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.kuali.rice.core.jaxb.AttributeSetAdapter;
import org.kuali.rice.core.jaxb.SqlTimestampAdapter;
import org.kuali.rice.kew.dto.ActionItemDTO;
import org.kuali.rice.kew.dto.ActionRequestDTO;
import org.kuali.rice.kew.dto.ActionTakenDTO;
import org.kuali.rice.kew.dto.DocumentContentDTO;
import org.kuali.rice.kew.dto.DocumentDetailDTO;
import org.kuali.rice.kew.dto.DocumentLinkDTO;
import org.kuali.rice.kew.dto.DocumentSearchCriteriaDTO;
import org.kuali.rice.kew.dto.DocumentSearchResultDTO;
import org.kuali.rice.kew.dto.DocumentStatusTransitionDTO;
import org.kuali.rice.kew.dto.DocumentTypeDTO;
import org.kuali.rice.kew.dto.ReportCriteriaDTO;
import org.kuali.rice.kew.dto.RouteHeaderDTO;
import org.kuali.rice.kew.dto.RouteNodeInstanceDTO;
import org.kuali.rice.kew.dto.RuleDTO;
import org.kuali.rice.kew.dto.RuleReportCriteriaDTO;
import org.kuali.rice.kew.dto.WorkflowAttributeDefinitionDTO;
import org.kuali.rice.kew.dto.WorkflowAttributeValidationErrorDTO;
import org.kuali.rice.kew.exception.WorkflowException;
import org.kuali.rice.kew.util.KEWWebServiceConstants;
import org.kuali.rice.kim.bo.types.dto.AttributeSet;

/**
 * A remotable service which provides an API for performing various queries and
 * other utilities on KEW.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@WebService(name = KEWWebServiceConstants.WorkflowUtility.WEB_SERVICE_NAME, targetNamespace = KEWWebServiceConstants.MODULE_TARGET_NAMESPACE)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface WorkflowUtility {

    public DocumentDetailDTO getDocumentDetailFromAppId(String documentTypeName, String appId) throws WorkflowException;

	public RouteHeaderDTO getRouteHeaderWithPrincipal(
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public RouteHeaderDTO getRouteHeader(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public DocumentDetailDTO getDocumentDetail(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public RouteNodeInstanceDTO getNodeInstance(
			@WebParam(name = "nodeInstanceId") Long nodeInstanceId)
			throws WorkflowException;

	public DocumentTypeDTO getDocumentType(
			@WebParam(name = "documentTypeId") Long documentTypeId)
			throws WorkflowException;

	public DocumentTypeDTO getDocumentTypeByName(
			@WebParam(name = "documentTypeName") String documentTypeName)
			throws WorkflowException;

	public Long getNewResponsibilityId() throws WorkflowException;

	public Integer getUserActionItemCount(
			@WebParam(name = "principalId") String principalId)
			throws WorkflowException;

	public ActionItemDTO[] getAllActionItems(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public ActionItemDTO[] getActionItems(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "actionRequestedCodes") String[] actionRequestedCodes)
			throws WorkflowException;

	public ActionRequestDTO[] getAllActionRequests(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public ActionRequestDTO[] getActionRequests(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "nodeName") String nodeName,
			@WebParam(name = "principalId") String principalId)
			throws WorkflowException;

	public ActionTakenDTO[] getActionsTaken(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public WorkflowAttributeValidationErrorDTO[] validateWorkflowAttributeDefinitionVO(
			@WebParam(name = "definition") WorkflowAttributeDefinitionDTO definition)
			throws WorkflowException;

	public boolean isUserInRouteLog(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "lookFuture") boolean lookFuture)
			throws WorkflowException;

	public boolean isUserInRouteLogWithOptionalFlattening(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "lookFuture") boolean lookFuture,
			@WebParam(name = "flattenNodes") boolean flattenNodes)
			throws WorkflowException;

	public void reResolveRole(
			@WebParam(name = "documentTypeName") String documentTypeName,
			@WebParam(name = "roleName") String roleName,
			@WebParam(name = "qualifiedRoleNameLabel") String qualifiedRoleNameLabel)
			throws WorkflowException;

	public void reResolveRoleByDocumentId(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "roleName") String roleName,
			@WebParam(name = "qualifiedRoleNameLabel") String qualifiedRoleNameLabel)
			throws WorkflowException;

	public DocumentDetailDTO routingReport(
			@WebParam(name = "reportCriteria") ReportCriteriaDTO reportCriteria)
			throws WorkflowException;

	public boolean isFinalApprover(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "principalId") String principalId)
			throws WorkflowException;

	public boolean isSuperUserForDocumentType(
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "documentTypeId") Long documentTypeId)
			throws WorkflowException;

	public String getAppDocId(
			@WebParam(name = "documentId") Long documentId);
	
	public DocumentSearchResultDTO performDocumentSearch(
			@WebParam(name = "criteriaVO") DocumentSearchCriteriaDTO criteriaVO)
			throws WorkflowException;

	public DocumentSearchResultDTO performDocumentSearchWithPrincipal(
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "criteriaVO") DocumentSearchCriteriaDTO criteriaVO)
			throws WorkflowException;

	// new in 2.3

	public RuleDTO[] ruleReport(
			@WebParam(name = "ruleReportCriteria") RuleReportCriteriaDTO ruleReportCriteria)
			throws WorkflowException;

	// deprecated as of 2.1

	/**
	 * @deprecated use isLastApproverAtNode instead
	 */
	public boolean isLastApproverInRouteLevel(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "routeLevel") Integer routeLevel)
			throws WorkflowException;

	/**
	 * @deprecated use routeNodeHasApproverActionRequest instead
	 */
	public boolean routeLevelHasApproverActionRequest(
			@WebParam(name = "docType") String docType,
			@WebParam(name = "docContent") String docContent,
			@WebParam(name = "routeLevel") Integer routeLevel)
			throws WorkflowException;

	// new in 2.1

	public boolean isLastApproverAtNode(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "nodeName") String nodeName)
			throws WorkflowException;

	public boolean routeNodeHasApproverActionRequest(
			@WebParam(name = "docType") String docType,
			@WebParam(name = "docContent") String docContent,
			@WebParam(name = "nodeName") String nodeName)
			throws WorkflowException;

	public RouteNodeInstanceDTO[] getDocumentRouteNodeInstances(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public RouteNodeInstanceDTO[] getActiveNodeInstances(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public RouteNodeInstanceDTO[] getTerminalNodeInstances(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public DocumentContentDTO getDocumentContent(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	// 2.2
	public String[] getPreviousRouteNodeNames(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	// 2.4
	public boolean documentWillHaveAtLeastOneActionRequest(
			@WebParam(name = "reportCriteriaDTO") ReportCriteriaDTO reportCriteriaDTO,
			@WebParam(name = "actionRequestedCodes") String[] actionRequestedCodes,
			@WebParam(name = "ignoreCurrentActionRequests") boolean ignoreCurrentActionRequests);

	/**
	 * @since 0.9.1
	 */
	public String getDocumentStatus(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	public RouteNodeInstanceDTO[] getCurrentNodeInstances(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	// added for KS per Scott
	ActionItemDTO[] getActionItemsForPrincipal(
			@WebParam(name = "principalId") String principalId)
			throws WorkflowException;

	/**
	 * 
	 * This method gets a list of ids of all principals who have a pending
	 * action request for a document.
	 * 
	 * @param actionRequestedCd
	 * @param documentId
	 * @return
	 * @throws WorkflowException
	 */
	public String[] getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(
			@WebParam(name = "actionRequestedCd") String actionRequestedCd,
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	/**
	 * This method gets a list of ids of all principals in the route log - -
	 * initiators, - people who have taken action, - people with a pending
	 * action request, - people who will receive an action request for the
	 * document in question
	 * 
	 * @param documentId
	 * @param lookFuture
	 * @return
	 * @throws WorkflowException
	 */
	public String[] getPrincipalIdsInRouteLog(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "lookFuture") boolean lookFuture)
			throws WorkflowException;

	/**
	 * Returns the principal ID of the initiator of the given document.
	 * <b>null</b> if the document can not be found.
	 * 
	 * @throws WorkflowException
	 */
	public String getDocumentInitiatorPrincipalId(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	/**
	 * Returns the principal ID of the user who routed the given document.
	 * <b>null</b> if the document can not be found.
	 * 
	 * @throws WorkflowException
	 */
	public String getDocumentRoutedByPrincipalId(
			@WebParam(name = "documentId") Long documentId)
			throws WorkflowException;

	@XmlJavaTypeAdapter(value = AttributeSetAdapter.class)
	public AttributeSet getActionsRequested(
			@WebParam(name = "principalId") String principalId,
			@WebParam(name = "documentId") Long documentId);

	/**
	 * 
	 * This method does a direct search for the searchableAttribute without
	 * going through the doc search.
	 * 
	 * @param documentId
	 * @param key
	 * @return
	 */
	public String[] getSearchableAttributeStringValuesByKey(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "key") String key);

	/**
	 * 
	 * This method does a direct search for the searchableAttribute without
	 * going through the doc search.
	 * 
	 * @param documentId
	 * @param key
	 * @return
	 */
	@XmlJavaTypeAdapter(value = SqlTimestampAdapter.class)
	public Timestamp[] getSearchableAttributeDateTimeValuesByKey(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "key") String key);

	/**
	 * 
	 * This method does a direct search for the searchableAttribute without
	 * going through the doc search.
	 * 
	 * @param documentId
	 * @param key
	 * @return
	 */
	public BigDecimal[] getSearchableAttributeFloatValuesByKey(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "key") String key);

	/**
	 * 
	 * This method does a direct search for the searchableAttribute without
	 * going through the doc search.
	 * 
	 * @param documentId
	 * @param key
	 * @return
	 */
	public Long[] getSearchableAttributeLongValuesByKey(
			@WebParam(name = "documentId") Long documentId,
			@WebParam(name = "key") String key);

	public String getFutureRequestsKey(
			@WebParam(name = "principalId") String principalId);

	public String getReceiveFutureRequestsValue();

	public String getDoNotReceiveFutureRequestsValue();

	public String getClearFutureRequestsValue();

	public boolean hasRouteNode(
			@WebParam(name = "documentTypeName") String documentTypeName,
			@WebParam(name = "routeNodeName") String routeNodeName)
			throws WorkflowException;

	public boolean isCurrentActiveDocumentType(
			@WebParam(name = "documentTypeName") String documentTypeName)
			throws WorkflowException;
			
    // new for 1.0.kc
    public DocumentStatusTransitionDTO[] getDocumentStatusTransitionHistory(
    		@WebParam(name = "routeHeaderId") Long routeHeaderId)
    		throws WorkflowException;
    
    //for docmentlink
    public void addDocumentLink(DocumentLinkDTO docLinkVO) throws WorkflowException;

    public void deleteDocumentLink(DocumentLinkDTO docLinkVO) throws WorkflowException;
    
    public void deleteDocumentLinksByDocId(Long docId) throws WorkflowException;
    
    public List<DocumentLinkDTO> getLinkedDocumentsByDocId(Long id) throws WorkflowException;
    
    public DocumentLinkDTO getLinkedDocument(DocumentLinkDTO docLinkVO) throws WorkflowException;
    
    public List<String> getActiveRouteNodeNames(Long documentId);
    
    public List<String> getTerminalRouteNodeNames(Long documentId);

    public List<String> getCurrentRouteNodeNames(Long documentId);
}