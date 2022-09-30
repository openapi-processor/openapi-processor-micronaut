package generated.api;

import generated.support.Generated;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Get(uri = "/endpoint/{foo}")
    void getEndpointFoo(@PathVariable(value = "foo") String foo);

}
