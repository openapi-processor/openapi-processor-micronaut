/*
 * This class is auto generated by https://github.com/hauner/openapi-generatr-spring.
 * DO NOT EDIT.
 */

package generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;

public class Obj1 {

    @JsonProperty("prop1")
    @NotNull
    private String prop1;

    @JsonProperty("prop2")
    @DecimalMax(value = "3")
    private Integer prop2;

    @JsonProperty("prop3")
    @Valid
    private Obj2 prop3;

    public String getProp1() {
        return prop1;
    }

    public void setProp1(String prop1) {
        this.prop1 = prop1;
    }

    public Integer getProp2() {
        return prop2;
    }

    public void setProp2(Integer prop2) {
        this.prop2 = prop2;
    }

    public Obj2 getProp3() {
        return prop3;
    }

    public void setProp3(Obj2 prop3) {
        this.prop3 = prop3;
    }

}