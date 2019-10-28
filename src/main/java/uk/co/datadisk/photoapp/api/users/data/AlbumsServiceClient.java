package uk.co.datadisk.photoapp.api.users.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.datadisk.photoapp.api.users.model.AlbumResponseModel;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name="albums-ws", fallback = AlbumsFallback.class)
public interface AlbumsServiceClient {

  @GetMapping("/users/{id}/albums")
  public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}

// If the album microservice is down then this fallback class will be
// called and an empty array list is returned
@Component
class AlbumsFallback implements AlbumsServiceClient {

  @Override
  public List<AlbumResponseModel> getAlbums(@PathVariable String id) {
    return new ArrayList<>();
  }
}