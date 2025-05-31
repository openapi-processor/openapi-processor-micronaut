package generated.api;

import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint")
    void getEndpoint(@QueryValue(value = "foo") String foo);

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint-optional")
    void getEndpointOptional(@QueryValue(value = "foo", defaultValue = "bar") String foo);

}
