package uk.co.datadisk.photoapp.api.users.data;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.co.datadisk.photoapp.api.users.model.AlbumResponseModel;

import java.util.List;

@FeignClient(name="albums-ws")
public interface AlbumsServiceClient {

  @GetMapping("/users/{id}/albums")
  public List<AlbumResponseModel> getAlbums(@PathVariable String id);
}
