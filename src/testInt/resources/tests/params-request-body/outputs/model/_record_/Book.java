package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.support.Generated;

@Generated(value = "openapi-processor-micronaut", version = "test")
public record Book(
    @JsonProperty("isbn")
    String isbn,

    @JsonProperty("title")
    String title
) {}
