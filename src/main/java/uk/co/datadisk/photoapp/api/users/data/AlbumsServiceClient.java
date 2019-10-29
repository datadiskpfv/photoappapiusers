package uk.co.datadisk.photoapp.api.users.data;

import feign.FeignException;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.datadisk.photoapp.api.users.model.AlbumResponseModel;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name="albums-ws", fallbackFactory = AlbumsFallbackFactory.class)
public interface AlbumsServiceClient {

  @GetMapping("/users/{id}/albums")
  public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

// If the album microservice is down then this fallback class will be
// called and an empty array list is returned
@Component
class AlbumsFallbackFactory implements FallbackFactory<AlbumsServiceClient> {

  @Override
  public AlbumsServiceClient create(Throwable cause) {
    return new AlbumsServiceClientFallback(cause);
  }
}

class AlbumsServiceClientFallback implements AlbumsServiceClient {

  private final Throwable cause;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  AlbumsServiceClientFallback(Throwable cause) {
    this.cause = cause;
  }


  @Override
  public List<AlbumResponseModel> getAlbums(String id) {

    if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
      logger.error("404 error took place when getAlbums was called with userId: " + id + ". Error message: "
          + cause.getLocalizedMessage());
    } else {
      logger.error("Other error took place: " + cause.getLocalizedMessage());
    }

    return new ArrayList<>();
  }
}