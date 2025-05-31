package generated.api;

import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Status;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.NO_CONTENT)
    @Get(uri = "/foo")
    void getFoo();

}
