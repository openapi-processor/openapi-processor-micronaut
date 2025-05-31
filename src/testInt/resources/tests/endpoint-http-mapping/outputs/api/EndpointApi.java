package generated.api;

import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Head;
import io.micronaut.http.annotation.Options;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.annotation.Trace;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface EndpointApi {

    @Status(HttpStatus.NO_CONTENT)
    @Delete(uri = "/endpoint")
    void deleteEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/endpoint")
    void getEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Head(uri = "/endpoint")
    void headEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Options(uri = "/endpoint")
    void optionsEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Patch(uri = "/endpoint")
    void patchEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/endpoint")
    void postEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Put(uri = "/endpoint")
    void putEndpoint();

    @Status(HttpStatus.NO_CONTENT)
    @Trace(uri = "/endpoint")
    void traceEndpoint();

}
