/*
 * DO NOT MODIFY - this class was auto generated by openapi-processor-micronaut
 *
 * test
 * time
 * https://docs.openapiprocessor.io/micronaut
 */

package generated.api;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

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