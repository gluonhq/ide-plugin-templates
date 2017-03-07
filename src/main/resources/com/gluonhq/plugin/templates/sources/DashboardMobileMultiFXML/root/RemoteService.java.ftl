package ${packageName};

import com.gluonhq.cloudlink.client.data.DataClient;
import com.gluonhq.cloudlink.client.data.DataClientBuilder;
import com.gluonhq.cloudlink.client.data.OperationMode;
import com.gluonhq.connect.GluonObservableObject;
import com.gluonhq.connect.provider.DataProvider;

public class RemoteService {

    private final DataClient cloudDataClient;

    public RemoteService() {
        cloudDataClient = DataClientBuilder.create()
            .operationMode(OperationMode.CLOUD_FIRST)
            .build();
    }
<#list functions as function>

    public <T> GluonObservableObject<T> ${function.name}(Class<T> clazz <#if function.hasParameters> <#list function.parameters as param> , String ${param} </#list> </#if>) {
        GluonObservableObject<T> answer = DataProvider.retrieveObject(
            cloudDataClient.createFunctionObjectDataReader("${function.name}", clazz <#if function.hasParameters> <#list function.parameters as param> , ${param} </#list> </#if> ));
        return answer;
    }
</#list>
}

