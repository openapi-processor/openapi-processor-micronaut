package generated.api;

import generated.model.Props;
import generated.support.Generated;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import java.util.Map;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Get(uri = "/endpoint-object")
    void getEndpointObject(Props props);

    @Get(uri = "/endpoint-map")
    void getEndpointMap(@QueryValue(value = "props") Map<String, String> props);

}
