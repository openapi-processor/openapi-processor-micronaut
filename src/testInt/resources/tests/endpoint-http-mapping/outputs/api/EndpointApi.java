package generated.api;

import generated.support.Generated;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Head;
import io.micronaut.http.annotation.Options;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Trace;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Delete(uri = "/endpoint")
    void deleteEndpoint();

    @Get(uri = "/endpoint")
    void getEndpoint();

    @Head(uri = "/endpoint")
    void headEndpoint();

    @Options(uri = "/endpoint")
    void optionsEndpoint();

    @Patch(uri = "/endpoint")
    void patchEndpoint();

    @Post(uri = "/endpoint")
    void postEndpoint();

    @Put(uri = "/endpoint")
    void putEndpoint();

    @Trace(uri = "/endpoint")
    void traceEndpoint();

}
