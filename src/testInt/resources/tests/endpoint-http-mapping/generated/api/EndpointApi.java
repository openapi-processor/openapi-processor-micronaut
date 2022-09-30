package generated.api;

import generated.support.Generated;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Get(uri = "/endpoint")
    void getEndpoint();

    @Put(uri = "/endpoint")
    void putEndpoint();

    @Post(uri = "/endpoint")
    void postEndpoint();

    @Patch(uri = "/endpoint")
    void patchEndpoint();

}
