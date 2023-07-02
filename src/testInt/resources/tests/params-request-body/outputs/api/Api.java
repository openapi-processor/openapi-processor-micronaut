package generated.api;

import generated.model.Book;
import generated.support.Generated;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Post(uri = "/book", consumes = {"application/json"}, produces = {"application/json"})
    Book postBook(@Body Book body);

}
