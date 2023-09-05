package searchengine.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import searchengine.model.Page;
import searchengine.model.Website;
import searchengine.repositories.PageRepository;
import searchengine.repositories.SiteRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.RecursiveAction;

public class LinksFinder extends RecursiveAction{

    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private String currentLink;
    private String rootLink;

    public LinksFinder(SiteRepository siteRepository, PageRepository pageRepository, String currentLink, String rootLink) {
        this.siteRepository = siteRepository;
        this.pageRepository = pageRepository;
        this.currentLink = currentLink;
        this.rootLink = rootLink;
    }

    @Override
    protected void compute() {
        System.out.println("Parent link: " + currentLink);
        if (!pageRepository.existsByPath(currentLink)) {
            Page page = createPage(currentLink);
            pageRepository.save(page);
            ArrayList<String> subLinks = findNestedLinks(currentLink);
            List<LinksFinder> taskList = new ArrayList<>();
            for (String subLink : subLinks) {
                LinksFinder task = new LinksFinder(siteRepository, pageRepository, subLink, rootLink);
                task.fork();
                taskList.add(task);
            }
            System.out.println();
            for (LinksFinder task : taskList) {
                task.join();
            }
        }
    }


    public ArrayList<String> findNestedLinks(String parentLink) {
        Document htmlDoc;
        ArrayList<String> linksFromPage = new ArrayList<>();
        try {
            Thread.sleep(2500);
            htmlDoc = Jsoup.connect(parentLink)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();


            String childLink = null;
            Elements elements = htmlDoc.select("a[href]");
            for (Element element : elements) {
                childLink = element.attr("href");

                if (LinkFilter.shouldBeFilteredOut(childLink)) {
                    continue;
                }
                if (childLink.charAt(0) == '/') {
                    childLink = rootLink + childLink;
                }

                System.out.println(childLink);
                linksFromPage.add(childLink);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return linksFromPage;
    }

    public Page createPage(String link) {
        Page page = new Page();
        page.setPath(link);

        page.setCode(getPageResponseCode(link));

        page.setContent("CONTENT");
        Website website = siteRepository.findByUrl(rootLink);
        page.setWebsite(website);

        return page;
    }

    public int getPageResponseCode(String link) {
        URL url = null;
        int code = 0;
        try {
            url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            System.out.println("Request URL ... " + url);
            code = con.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    public String getPageContent(String link) {

        return "9";
    }
}

//TODO: заменить String link на Page page;

