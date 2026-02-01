package generated.api;

import generated.model.Foo;
import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/foo/params")
    void postFooParams(
            @QueryValue(value = "foo") String foo,
            @QueryValue(value = "bar") String bar);

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/foo/object")
    void postFooObject(Foo body);

}
