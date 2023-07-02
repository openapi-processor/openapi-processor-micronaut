package generated.api;

import generated.support.Generated;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Get(uri = "/endpoint")
    void getEndpoint(@QueryValue(value = "foo") String foo);

    @Get(uri = "/endpoint-optional")
    void getEndpointOptional(@QueryValue(value = "foo", defaultValue = "bar") String foo);

}
