package examples;

import com.vmware.ops.api.client.exceptions.ServerException;
import com.vmware.ops.api.model.adapter.AdapterInstanceCreationRequestDto;
import com.vmware.ops.api.model.adapter.AdapterInstanceInfoDto;
import com.vmware.ops.api.model.adapterkind.AdapterKind;
import com.vmware.ops.api.model.common.NameValuePair;
import com.vmware.ops.api.model.credential.CredentialInstance;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Example that illustrates the use of API client side bindings
 * to retrieve all the installed adapters in the system. It then
 * assumes that the vSphere Solution Pack is installed in the system
 * and proceeds further to configure a VC adapter instance.
 */
public class AdaptersExample extends ExampleBase {

    @Override
    public void run() {
        try {
            System.out.println("Listing all the installed Adapters in the system:");

            getClient().metadataClient();

            System.out.println("Adapters##### :" + getClient().metadataClient().listAdapterKinds().getAdapterKinds());
            int i = 0;
            for(AdapterKind adapterType : getClient().metadataClient().listAdapterKinds().getAdapterKinds()){
                System.out.printf("\n[%d] %s ", i++, ReflectionToStringBuilder.toString(adapterType));
            }
        } catch (ServerException e) {
            System.out.println(e.getRawResponseText());
            System.out.println(e.getStatusLine());
            e.printStackTrace();
        }
        // Let's create a VC adapter instance
        AdapterInstanceCreationRequestDto request = new AdapterInstanceCreationRequestDto();
        request.setAdapterKindKey("VMWARE");
        // Collector ID and Collector Group ID are optional.
        // Let the system choose the location for the Adapter Instance
        // Set the monitoring interval to be 3 minutes instead of the default 5 minutes
        request.setMonitoringIntervalMinutes(3);
        // Configure the Credential that will be used by the Adapter Instance
        CredentialInstance credInstance = configureCredentialInstance("VMWARE");
        request.setCredential(credInstance);

        // Resource Identifiers for the Adapter Instance
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair pair = new NameValuePair();
        pair.setName("AUTODISCOVERY");
        pair.setValue("true");
        nameValuePairs.add(pair);
        pair = new NameValuePair();
        pair.setName("PROCESSCHANGEEVENTS");
        pair.setValue("true");
        nameValuePairs.add(pair);
        pair = new NameValuePair();
        pair.setName("VCURL");
        pair.setValue("1.2.3.4");
        nameValuePairs.add(pair);
        request.setIdentifiers(nameValuePairs);

        // Specify a name and some description for the Adapter Instance
        request.setName("Example-VC-Adapter-Instance");
        request.setDescription("Sample adapter instance for illustrative purposes");

        // Create the Adapter Instance and accept the certificates presented by VC
        AdapterInstanceInfoDto ai = getClient().adapterInstancesClient().createAdapterInstanceByAcceptingCertificates(request);

        // Start the Adapter Instance to collect data
        getClient().adapterInstancesClient().startMonitoringResourcesOfAdapterInstance(ai.getId());
    }

    /**
     * Helper method that creates a Credential Instance used by the Adapter Instance
     *
     * @param adapterKindKey Adapter Kind key whose Credential metadata needs to be used
     * @return The Credential Instance
     */
    private CredentialInstance configureCredentialInstance(String adapterKindKey) {
        CredentialInstance cred = new CredentialInstance();
        cred.setAdapterKindKey(adapterKindKey);
        cred.setName("VC-adapter-instance-credential");
        cred.setCredentialKindKey("PRINCIPALCREDENTIAL");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        NameValuePair pair = new NameValuePair();
        pair.setName("USER");
        pair.setValue("admin");
        nameValuePairs.add(pair);
        pair = new NameValuePair();
        pair.setName("PASSWORD");
        pair.setValue("V1rtu@1c3!");
        nameValuePairs.add(pair);
        cred.setFields(nameValuePairs);
        return cred;
    }

    public static void main(String[] args) {
        new AdaptersExample().run();
    }
}
