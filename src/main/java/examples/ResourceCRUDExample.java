package examples;

import com.vmware.ops.api.client.Client;
import com.vmware.ops.api.client.controllers.ResourcesClient;
import com.vmware.ops.api.model.property.PropertyContent;
import com.vmware.ops.api.model.property.PropertyContents;
import com.vmware.ops.api.model.resource.ResourceDto;
import com.vmware.ops.api.model.resource.ResourceIdentifier;
import com.vmware.ops.api.model.resource.ResourceKey;
import com.vmware.ops.api.model.stat.StatContent;
import com.vmware.ops.api.model.stat.StatContents;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.*;


/**
 * Example that illustrates the use of API client bindings for resource
 * CRUD operations
 */
public class ResourceCRUDExample extends ExampleBase {
    private ResourcesClient resourcesClient = null;


    public void run(String str){
        Client client = getClient();
        this.resourcesClient = client.resourcesClient();
        System.out.println("Creating a dummy VM Resource...");
        UUID vCenterGUID = UUID.randomUUID();
        //vCenterGUID = UUID.fromString("f27dce6c-2867-4b1d-a1dd-a8c9cbf5066b");
        //resourcesClient.deleteResource(vCenterGUID);
        //ResourceDto dto = resourcesClient.get(vCenterGUID);


        ResourceDto resource = createResource("RCM-COMPLIANCE-TAKE10", "Monitoring RCM Compliance", vCenterGUID, "vm-999");
        //ResourceDto resource = createResource("RCM-Info", "Monitoring RCM Compliance", vCenterGUID, "vm-999");
        //ResourceDto resource = createResource("RCM-COMPLIANCE", "Monitoring RCM Compliance", vCenterGUID, "vm-999");
        double[] data = new double[1];
        long[] timestamps = new long[1];
        String[] values = new String[1];

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long currentTime = cal.getTimeInMillis();

        timestamps[0] = currentTime - ((0+1)*300000);

        UUID uuid = resource.getIdentifier();

        JSONObject jsonObject = getJsonObject(str);

        Map firmware630Map = (Map)jsonObject.get("VXMA-DellBIOSforDellR6301");
        String rcm630Version = (String)firmware630Map.get("RCM-Version");
        String discovered630Version = (String)firmware630Map.get("Discovered-Version");
        String ip630 = (String)((Map)firmware630Map.get("Component-Info")).get("IP");
        String type630 = (String)((Map)firmware630Map.get("Component-Info")).get("type");

        Map firmware730Map = (Map)jsonObject.get("VXMADellPERCH730Firmware2");
        String rcm730Version = (String)firmware730Map.get("RCM-Version");
        String discovered730Version = (String)firmware730Map.get("Discovered-Version");
        String ip730 = (String)((Map)firmware730Map.get("Component-Info")).get("IP");
        String type730 = (String)((Map)firmware730Map.get("Component-Info")).get("type");



        System.out.println("UUID to be added= " + uuid.toString() );
        String[] discovered630Versions = new String[1];
        discovered630Versions[0] = discovered630Version;//"2.2.13";
        String[] discovered730Versions = new String[1];
        discovered730Versions[0] = discovered730Version;//"25.4.0.0020";
        String[] rcm630Versions = new String[1];
        rcm630Versions[0] = rcm630Version;//"2.2.14";
        String[] rcm730Versions = new String[1];
        rcm730Versions[0] = rcm730Version;//"25.4.0.0021";



        String[] ips630 = new String[1];
        ips630[0] = ip630;
        String [] ips730 = new String[1];
        ips730[0] = ip730;

        String[] types630 = new String[1];
        types630[0] = type630;
        String [] types730 = new String[1];
        types730[0] = type730;


        //System.out.println("Pushing Numeric Stats data...");
        //addStats(null, uuid, "test|test-123|bar1", timestamps, data, null, false);
        System.out.println("Pushing String Stats data...");
        //timestamps[0] = currentTime - ((0+1)*300000);
        addStats(null, uuid, "VXMADellPERCH730Firmware2|RCM-Version", timestamps, null, rcm730Versions, false);
        addStats(null, uuid, "VXMADellPERCH730Firmware2|Discovered-Version", timestamps, null, discovered730Versions, false);
        addStats(null, uuid, "VXMA-DellBIOSforDellR6301|Discovered-Version", timestamps, null, discovered630Versions, false);
        addStats(null, uuid, "VXMA-DellBIOSforDellR6301|RCM-Version", timestamps, null, rcm630Versions, false);

        addStats(null, uuid, "VXMA-DellBIOSforDellR6301|Component-Info|IP", timestamps, null, ips630, false);
        addStats(null, uuid, "VXMA-DellBIOSforDellR6301|Component-Info|type", timestamps, null, types630, false);
        addStats(null, uuid, "VXMADellPERCH730Firmware2|Component-Info|IP", timestamps, null, ips730, false);
        addStats(null, uuid, "VXMADellPERCH730Firmware2|Component-Info|type", timestamps, null, types730, false);

        //addStats(null, uuid, "PowerEdgeServer_Disc_Version", timestamps, powerEdgeVersions, null, false);




        System.out.println("Pushing Numeric Properties data...");
        //addProperties(null, uuid, "property|foo1|bar1", timestamps, data, null);
        System.out.println("Pushing String Properties data...");
        //addProperties(null, uuid, "property|foo2|bar2", timestamps, null, values);

        /*discovered[0] = "25.5.0.0018";
        rcm[0] = "25.5.0.0019";
        addStats(null, uuid, "VXMA - Dell PERC H730 Firmware|Discovered-Version", timestamps, null, discovered, false);
        addStats(null, uuid, "VXMA - Dell PERC H730 Firmware|RCM-Version", timestamps, null,rcm , false);*/

        System.out.println("Sleep for 20 seconds for resource to get created...zzzz...");
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ResourceDto resourceDto= getResourcesWithUUID(resource.getIdentifier());
        if(resourceDto.getResourceKey().getName().equals("Foo")) {
            System.out.println("Resource Foo is indeed created.");
        }
        else {
            System.out.println("Resource is successfully created.");
        }

        /*System.out.println("Updating a dummy VM Resource...");
        ResourceKey resourceKey = resource.getResourceKey();
        resourceKey.setName("Bar");
        resource.setResourceKey(resourceKey);
        resourceDto = updateResource(resource);
        System.out.println("Sleep for 20 seconds for resource to get updated...zzzz...");
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resourceDto= getResourcesWithUUID(resource.getIdentifier());
        if(resourceDto.getResourceKey().getName().equals("Bar")) {
            System.out.println("Resource Foo is indeed updated to Bar.");
        }
        else {
            System.out.println("Resource Foo is not updated to Bar.");
        }*/

        //System.out.println("Deleting previously created dummy VM resource...");
        //deleteResource(resource.getIdentifier());
        //System.out.println("Sleep for 20 seconds for resource to get deleted...zzzz...");
        try {
            Thread.sleep(20*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        resourceDto= getResourcesWithUUID(resource.getIdentifier());
        if(resourceDto!=null) {
          //  System.out.println("Resource is not deleted successfully.");
        }
        else {
            System.out.println("Resource is deleted successfully.");
        }

    }

    @Override
    public void run(){

    }


    private JSONObject getJsonObject(String fileName){
        JSONObject jsonObject = null;
       try {
           // read the json file

           FileReader reader = new FileReader(fileName);
           JSONParser jsonParser = new JSONParser();
           jsonObject = (JSONObject) jsonParser.parse(reader);
       }
       catch(Exception ex){
           ex.printStackTrace();
       }
       return jsonObject;
    }

    /**
     * Creates a Dummy vSphere Virtual Machine corresponding Resource in vRealize Operations Manager.<br/>
     * Assumes that the vSphere solution pack has already been installed.
     *
     * @param name Name of the Resource
     * @param description Description for the Resource
     * @param vCenterGUID UUID of the vCenter where this VM is present
     * @param moid Mo-id of the VM
     */
    public ResourceDto createResource(String name, String description,
                                      UUID vCenterGUID, String moid) {
        ResourceDto resource = new ResourceDto();
        ResourceKey resourceKey = new ResourceKey();

        resource.setDescription(description);
        resourceKey.setName(name);
        resourceKey.setAdapterKindKey("vCenter Adapter");
        //resourceKey.setResourceKindKey("RCM Compliance");
        resourceKey.setResourceKindKey("Custom Datacenter");
        List<ResourceIdentifier> resourceIdentifiers = new ArrayList<ResourceIdentifier>();
        /*ResourceIdentifier kv = new ResourceIdentifier("VMEntityVCID", vCenterGUID.toString());
        resourceIdentifiers.add(kv);
        kv = new ResourceIdentifier("VMEntityObjectID", moid);
        resourceIdentifiers.add(kv);
        kv = new ResourceIdentifier("VMEntityName", name);
        resourceIdentifiers.add(kv);*/
        resourceKey.setResourceIdentifiers(resourceIdentifiers);
        resource.setResourceKey(resourceKey);
        //resource.setIdentifier(UUID.fromString("f27dce6c-2867-4b1d-a1dd-a8c9cbf5066b"));
        //StatContents stats = new StatContents();
        //resourcesClient.findOrCreateResourceAndPushData(resourceKey,"",);
        return resourcesClient.findOrCreateResourceUsingResourceKey(resourceKey, "ENG-POC-KEY-TAKE-1");
        //return resourcesClient.findOrCreateResourceUsingResourceKey(resourceKey, "vRealizeOpsMgrAPI");
        //return resourcesClient.createResource(resource, "ENG-POC-KEY-TAKE-1");
    }



    /**
     * Delete a resource
     */
    public void deleteResource(UUID resourceId) {
        resourcesClient.deleteResource(resourceId);
    }

    public ResourceDto updateResource(ResourceDto resource) {
        return resourcesClient.updateResource(resource);
    }

    public ResourceDto getResourcesWithUUID(UUID uuid) {
        try {
            ResourceDto resourceDto = resourcesClient.get(uuid);
            return resourceDto;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Push stat data for the specified Stat Keys
     *
     * @param adapterSourceId the ID of the adapter kind that will push the stats,
     *          may be null which defaults to SuiteAPI adapter
     * @param resourceUUID vRealize Operations Manager UUID of the Resource
     * @param statKey Name of the Stat Key
     * @param timestamps Array of long values as timestamps
     * @param data Array of double values as data
     * @param storeOnly true if we want to store the data only, false if we want
     *            analytics processing to be performed
     */
    public void addStats(String adapterSourceId, UUID resourceUUID,
                         String statKey, long[] timestamps,
                         double[] data, String[] values, boolean storeOnly) {
        System.out.println("Pushing some data for stat key: '" + statKey + "' ...");
        StatContents contents = new StatContents();
        StatContent content = new StatContent();
        content.setStatKey(statKey);
        content.setData(data);
        content.setValues(values);
        content.setTimestamps(timestamps);
        contents.getStatContents().add(content);
        if (adapterSourceId == null) {
            resourcesClient.addStats(resourceUUID, contents, true);
        } else {
            resourcesClient.addStats(adapterSourceId, resourceUUID, contents, storeOnly);
        }
    }

    /**
     * Push stat data for the specified Stat Keys
     *
     * @param adapterSourceId the ID of the adapter kind that will push the stats,
     *          may be null which defaults to SuiteAPI adapter
     * @param resourceUUID vRealize Operations Manager UUID of the Resource
     * @param statKey Name of the Stat Key
     * @param timestamps Array of long values as timestamps
     * @param data Array of double values as data
     */
    public void addProperties(String adapterSourceId, UUID resourceUUID,
                              String statKey, long[] timestamps,
                              double[] data, String[] values) {
        System.out.println("Pushing some data for property: '" + statKey + "' ...");
        PropertyContents contents = new PropertyContents();
        PropertyContent content = new PropertyContent();
        content.setStatKey(statKey);
        content.setData(data);
        content.setValues(values);
        content.setTimestamps(timestamps);
        contents.getPropertyContents().add(content);
        if (adapterSourceId == null) {
            resourcesClient.addProperties(resourceUUID, contents);
        } else {
            resourcesClient.addProperties(adapterSourceId, resourceUUID, contents);
        }
    }

    public static void main(String[] args) {
        //"/Users/nemadn/VCE/vRealize/javavropspush/poccc/DiscoveryRCM.json"
        new ResourceCRUDExample().run(args[0]);
    }
}
