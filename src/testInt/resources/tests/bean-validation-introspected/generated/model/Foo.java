package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import generated.support.Generated;
import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotNull;

@Introspected
@Generated(value = "openapi-processor-micronaut", version = "test")
public class Foo {

    @NotNull
    @JsonProperty("flag")
    private Boolean flag;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

}
