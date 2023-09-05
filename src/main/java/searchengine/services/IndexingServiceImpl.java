package searchengine.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.model.Page;
import searchengine.model.Status;
import searchengine.model.Website;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;
import searchengine.task.LinksFinder;
import searchengine.task.LinksFinderLauncher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class IndexingServiceImpl implements IndexingService{

    private final SitesList sites;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;

    @Autowired
    public IndexingServiceImpl(SitesList sites, SiteRepository siteRepository, PageRepository pageRepository) {
        this.sites = sites;
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
    }

    @Override
    public void indexSites() {
        for (Site site: sites.getSites()) {
            removeRelatedRecords(site);
            Website website = createSite(site.getUrl(), site.getName());
            siteRepository.save(website);
            LinksFinder linksFinder = new LinksFinder(siteRepository, pageRepository, site.getUrl(), site.getUrl());
            LinksFinderLauncher launcher = new LinksFinderLauncher(linksFinder);
            Thread thread = new Thread(launcher);
            thread.start();
        }
    }

    public Website createSite(String url, String name) {
        Website site = new Website();
        site.setStatus(Status.INDEXING);
        site.setStatusTime(LocalDateTime.now());
        site.setUrl(url);
        site.setName(name);
        return site;
    }

    public void removeRelatedRecords(Site site) {
        Website oldSiteRecord = siteRepository.findByName(site.getName());
        if (oldSiteRecord != null) {
            List<Page> oldPageRecords = pageRepository.findByWebsite(oldSiteRecord);
            pageRepository.deleteAll(oldPageRecords);
            siteRepository.delete(oldSiteRecord);
        }
    }
}
