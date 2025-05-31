package generated.api;

import generated.model.Props;
import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import java.util.Map;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint-object")
    void getEndpointObject(Props props);

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint-map")
    void getEndpointMap(@QueryValue(value = "props") Map<String, String> props);

}
