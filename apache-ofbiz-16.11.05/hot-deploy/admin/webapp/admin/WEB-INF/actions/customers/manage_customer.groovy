import org.apache.ofbiz.base.util.UtilMisc
import org.apache.ofbiz.party.party.PartyHelper

String orgPartyId = parameters.orgPartyId

context.orgPartyId = orgPartyId;
String organizationName = PartyHelper.getPartyName(delegator, orgPartyId, false);

context.organizationName = organizationName

//TODO: Fetch more details (Status, etc)
