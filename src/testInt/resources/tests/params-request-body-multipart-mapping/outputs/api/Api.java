package generated.api;

import generated.support.Generated;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.multipart.CompletedFileUpload;

@Generated(value = "openapi-processor-micronaut", version = "test")
public interface Api {

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/multipart/single-file", consumes = {"multipart/form-data"})
    void postMultipartSingleFile(
            @Part(value = "file") CompletedFileUpload file,
            @Part(value = "other") String other);

    @Status(HttpStatus.NO_CONTENT)
    @Post(uri = "/multipart/multiple-files", consumes = {"multipart/form-data"})
    void postMultipartMultipleFiles(
            @Part(value = "files") CompletedFileUpload[] files,
            @Part(value = "other") String other);

}
