package generated.api;

import generated.model.Foo;
import generated.support.Generated;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Post(uri = "/endpoint", consumes = {"application/json"})
    void postEndpoint(@Body @Valid @NotNull Foo body);

}
