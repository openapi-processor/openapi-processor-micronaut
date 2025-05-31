package generated.api;

import generated.model.Book;
import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.CREATED)
    @Post(uri = "/book", consumes = {"application/json"}, produces = {"application/json"})
    Book postBook(@Body Book body);

}
