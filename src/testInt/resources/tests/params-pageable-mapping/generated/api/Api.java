package generated.api;

import generated.support.Generated;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Get(
            uri = "/page",
            produces = {"application/json"})
    Page<String> getPage(@QueryValue(value = "pageable") Pageable pageable);

    @Get(
            uri = "/page-inline",
            produces = {"application/json"})
    Page<String> getPageInline(@QueryValue(value = "pageable") Pageable pageable);

}
