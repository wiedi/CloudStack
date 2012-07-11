// Copyright 2012 Citrix Systems, Inc. Licensed under the
package com.cloud.api.commands;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cloud.api.ApiConstants;
import com.cloud.api.BaseAsyncCmd;
import com.cloud.api.BaseCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.ServerApiException;
import com.cloud.api.response.SuccessResponse;
import com.cloud.event.EventTypes;
import com.cloud.server.ResourceTag.TaggedResourceType;

/**
 * @author Alena Prokharchyk
 */

@Implementation(description = "Deleting resource tag(s)", responseObject = SuccessResponse.class, since = "Burbank")
public class DeleteTagsCmd extends BaseAsyncCmd{
    public static final Logger s_logger = Logger.getLogger(DeleteTagsCmd.class.getName());

    private static final String s_name = "deletetagsresponse";

    // ///////////////////////////////////////////////////
    // ////////////// API parameters /////////////////////
    // ///////////////////////////////////////////////////
    
    @Parameter(name = ApiConstants.TAGS, type = CommandType.MAP, description = "Delete tags matching key/value pairs")
    private Map tag;
    
    @Parameter(name=ApiConstants.RESOURCE_TYPE, type=CommandType.STRING, required=true, description="Delete tag by resource type")
    private String resourceType;
    
    @Parameter(name=ApiConstants.RESOURCE_IDS, type=CommandType.LIST, required=true, 
            collectionType=CommandType.STRING, description="Delete tags for resource id(s)")
    private List<String> resourceIds;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    
    public TaggedResourceType getResourceType(){
        return _taggedResourceService.getResourceType(resourceType);
    } 
    
    public Map<String, String> getTags() {
        Map<String, String> tagsMap = null;
        if (tag != null && !tag.isEmpty()) {
            tagsMap = new HashMap<String, String>();
            Collection<?> servicesCollection = tag.values();
            Iterator<?> iter = servicesCollection.iterator();
            while (iter.hasNext()) {
                HashMap<String, String> services = (HashMap<String, String>) iter.next();
                String key = services.get("key");
                String value = services.get("value");
                tagsMap.put(key, value);
            }
        }
        return tagsMap;
    }
    
    public List<String> getResourceIds() {
        return resourceIds;
    }

    // ///////////////////////////////////////////////////
    // ///////////// API Implementation///////////////////
    // ///////////////////////////////////////////////////

    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        //FIXME - validate the owner here
       return 1;
    }

    @Override
    public void execute() {
        boolean success = _taggedResourceService.deleteTags(getResourceIds(), getResourceType(), getTags());
        
        if (success) {
                SuccessResponse response = new SuccessResponse(getCommandName());
                this.setResponseObject(response);
        } else {
            throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to delete tags");
        }
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_TAGS_DELETE;
    }

    @Override
    public String getEventDescription() {
        return "Deleting tags";
    }
}
