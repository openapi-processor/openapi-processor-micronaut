package generated.api;

import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Status;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint/{foo}")
    void getEndpointFoo(@PathVariable(value = "foo") String foo);

}
