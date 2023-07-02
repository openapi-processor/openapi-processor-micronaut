package generated.api;

import generated.support.Generated;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Post(uri = "/multipart/single-file", consumes = {"multipart/form-data"})
    void postMultipartSingleFile(
            @Part(value = "file") CompletedFileUpload file,
            @Part(value = "other") String other);

    @Post(uri = "/multipart/multiple-files", consumes = {"multipart/form-data"})
    void postMultipartMultipleFiles(
            @Part(value = "files") CompletedFileUpload[] files,
            @Part(value = "other") String other);

}
