package examples;

import com.vmware.ops.api.client.Client;
import com.vmware.ops.api.client.Client.ClientConfig;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * A base example that allows for code reuse amongst all the Example classes.
 */
public abstract class ExampleBase {
    private final Client client;

    public ExampleBase() {
        try {
            /*Properties props = new Properties();
            props.load(getClass().getResourceAsStream(
                    "C:\\workspace\\poccc\\src\\com\\vmware\\ops\\api\\client\\examples\\client-config.properties"));
            for (Object property : props.keySet()) {
                String key = (String) property;
                props.setProperty(key,
                        System.getProperty(key, props.getProperty(key)));
            }*/

            String username = "admin";
            String password = "V1rtu@1c3!";
            String serverUrl = "https://nemadn-38.mpe.lab.vce.com/suite-api";
            String verifyCertificate = "false";
            boolean ignoreHostName = true;

            this.client = ClientConfig.builder()
                    .basicAuth(username, password)
                    .useJson()
                    .locale("en-us")
                    .timezone("PST")
                    .serverUrl(serverUrl)
                    .verify(verifyCertificate)
                    .ignoreHostName(ignoreHostName)
                    .useInternalApis(true)
                    .build()
                    .newClient();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Properties
    }

    protected String multilineString(Object obj) {
        return ReflectionToStringBuilder.reflectionToString(obj,
                ToStringStyle.MULTI_LINE_STYLE);
    }

    protected Client getClient() {
        return client;
    }

    public abstract void run();

}
