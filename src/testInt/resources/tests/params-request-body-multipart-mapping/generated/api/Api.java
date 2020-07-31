/*
 * DO NOT MODIFY - this class was auto generated by openapi-processor-micronaut
 *
 * test
 * time
 * https://docs.openapiprocessor.io/micronaut
 */

package generated.api;

import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.multipart.CompletedFileUpload;

public interface Api {

    @Post(uri = "/multipart/single-file")
    void postMultipartSingleFile(
            @Part(value = "file") CompletedFileUpload file, @Part(value = "other") String other);

    @Post(uri = "/multipart/multiple-files")
    void postMultipartMultipleFiles(
            @Part(value = "files") CompletedFileUpload[] files,
            @Part(value = "other") String other);

}