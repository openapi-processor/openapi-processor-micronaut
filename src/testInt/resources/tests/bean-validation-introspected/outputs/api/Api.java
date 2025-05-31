package generated.api;

import generated.model.Foo;
import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/endpoint", consumes = {"application/json"})
    void postEndpoint(@Body @Valid @NotNull Foo body);

}
