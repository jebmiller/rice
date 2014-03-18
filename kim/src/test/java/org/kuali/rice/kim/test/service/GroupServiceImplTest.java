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
package org.kuali.rice.kim.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.kuali.rice.core.resourceloader.GlobalResourceLoader;
import org.kuali.rice.kim.bo.group.dto.GroupInfo;
import org.kuali.rice.kim.bo.group.dto.GroupMembershipInfo;
import org.kuali.rice.kim.service.impl.GroupServiceImpl;
import org.kuali.rice.kim.service.impl.GroupUpdateServiceImpl;
import org.kuali.rice.kim.test.KIMTestCase;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupServiceImplTest extends KIMTestCase {

	private GroupServiceImpl groupService;
	private GroupUpdateServiceImpl groupUpdateService;

	public void setUp() throws Exception {
		super.setUp();
		groupService = (GroupServiceImpl)GlobalResourceLoader.getService(new QName("KIM", "kimGroupService"));
		groupUpdateService = (GroupUpdateServiceImpl)GlobalResourceLoader.getService(new QName("KIM", "kimGroupUpdateService"));
	}

	@Test
	public void testGetDirectMemberGroupIds() {
		List<String> groupIds = groupService.getDirectMemberGroupIds("g1");

		assertTrue( "g1 must contain group g2", groupIds.contains( "g2" ) );
		assertFalse( "g1 must not contain group g3", groupIds.contains( "g3" ) );

		groupIds = groupService.getDirectMemberGroupIds("g2");
		
		assertTrue( "g2 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g2 must not contain group g4 (inactive)", groupIds.contains( "g4" ) );
		
	}
	
	@Test
	public void testGetMemberGroupIds() {
		List<String> groupIds = groupService.getMemberGroupIds("g1");

		assertTrue( "g1 must contain group g2", groupIds.contains( "g2" ) );
		assertTrue( "g1 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g1 must not contain group g4 (inactive)", groupIds.contains( "g4" ) );

		groupIds = groupService.getMemberGroupIds("g2");

		assertTrue( "g2 must contain group g3", groupIds.contains( "g3" ) );
		assertFalse( "g2 must not contain group g1", groupIds.contains( "g1" ) );
	}
	
	// test principal membership
	@Test
	public void testPrincipalMembership() {
		assertTrue( "p1 must be in g2", groupService.isMemberOfGroup("p1", "g2") );
		assertTrue( "p1 must be direct member of g2", groupService.isDirectMemberOfGroup("p1", "g2") );
		assertTrue( "p3 must be in g2", groupService.isMemberOfGroup("p3", "g2") );
		assertFalse( "p3 should not be a direct member of g2", groupService.isDirectMemberOfGroup("p3", "g2") );
		assertFalse( "p4 should not be reported as a member of g2 (g4 is inactive)", groupService.isMemberOfGroup("p4", "g2") );
		
		// re-activate group 4
		GroupInfo g4Info = groupService.getGroupInfo("g4");
		g4Info.setActive(true);
		groupUpdateService.updateGroup("g4", g4Info);

		assertTrue( "p4 should be reported as a member of g2 (now that g4 is active)", groupService.isMemberOfGroup("p4", "g2") );
		
	}

	// test the various get methods, to verify that they work correctly against
	// circular group memberships.
	@Test
	public void testCircularGetMembers() {
		// get all principals from a circular group reference
		List<String> pIds = groupService.getMemberPrincipalIds("g101");
		assertTrue( "group A should have 3 members", pIds.size() == 3 );		
		assertTrue( "group A should have member p1", pIds.contains( "p1" ) );
		assertTrue( "group A should have member p3", pIds.contains( "p3" ) );
		assertTrue( "group A should have member p5", pIds.contains( "p5" ) );

		// traverse completely through a circular group reference looking
		// for a principal that is not a member of the group.
		boolean isIt = groupService.isMemberOfGroup("p2", "g101");
		assertFalse( "p2 should not be a member of Group A", isIt );
		
		List<String> gIds = groupService.getGroupIdsForPrincipal("p1");
		assertTrue( "p1 should be a member of Group A", gIds.contains("g101"));
		assertTrue( "p1 should be a member of Group B", gIds.contains("g102"));
		assertTrue( "p1 should be a member of Group C", gIds.contains("g103"));
		
		gIds = groupService.getGroupIdsForPrincipalByNamespace("p1", "ADDL_GROUPS_TESTS");
		assertTrue( "p1 should be a member of Group A", gIds.contains("g101"));
		assertTrue( "p1 should be a member of Group B", gIds.contains("g102"));
		assertTrue( "p1 should be a member of Group C", gIds.contains("g103"));
		
		List<String> inList = new ArrayList<String>();
		inList.add("g101");
		inList.add("g102");
		Collection<GroupMembershipInfo> gMembership = groupService.getGroupMembers(inList);
		assertTrue( "Should return 4 members total.", gMembership.size() == 4);
		
		gMembership = groupService.getGroupMembersOfGroup("g102");
		assertTrue( "Group B should have 2 members.", gMembership.size() == 2);
		
		List<GroupInfo> gInfo = groupService.getGroupsForPrincipal("p1");
		assertTrue( "p1 should be a member of at least 3 groups.", gInfo.size() >= 3);
		
		gInfo = groupService.getGroupsForPrincipalByNamespace("p1", "ADDL_GROUPS_TESTS");
		assertTrue( "p1 should be a member of exactly 3 groups with namespace = ADDL_GROUPS_TESTS.", gInfo.size() == 3);
		
		gIds = groupService.getMemberGroupIds("g101");
		assertTrue( "Group A should have 3 member groups", gIds.size() == 3);
		assertTrue( "Group B should be a member Group of Group A", gIds.contains("g102"));
		assertTrue( "Group C should be a member Group of Group A", gIds.contains("g103"));
		assertTrue( "Since these groups have a circular membership, Group A should have itself as a group member", gIds.contains("g101"));
		
		gIds = groupService.getParentGroupIds("g101");
		assertTrue( "Group A should have 3 parent groups", gIds.size() == 3);
		assertTrue( "Group B should be a parent of Group A", gIds.contains("g102"));
		assertTrue( "Group C should be a parent of Group A", gIds.contains("g103"));
		assertTrue( "Since these groups have a circular membership, Group A should be a parent of itself", gIds.contains("g101"));
	}

	
}