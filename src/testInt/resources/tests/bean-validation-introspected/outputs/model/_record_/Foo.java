package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.support.Generated;
import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotNull;

@Introspected
@Generated(value = "openapi-processor-micronaut", version = "test")
public record Foo(
    @NotNull
    @JsonProperty("flag")
    Boolean flag
) {}
