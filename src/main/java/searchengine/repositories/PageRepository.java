package searchengine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Website;

import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    boolean existsByPath(String path);
    List<Page> findByWebsite(Website website);
}
