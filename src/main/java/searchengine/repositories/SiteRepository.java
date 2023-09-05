package searchengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Website;

@Repository
public interface SiteRepository extends CrudRepository<Website, Integer> {
    public Website findByName(String name);
    public Website findByUrl(String url);
}
