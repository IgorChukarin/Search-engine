package searchengine.task;

import java.util.concurrent.ForkJoinPool;

public class LinksFinderLauncher implements Runnable{

    private LinksFinder linksFinder;

    public LinksFinderLauncher(LinksFinder linksFinder) {
        this.linksFinder = linksFinder;
    }

    @Override
    public void run() {
        new ForkJoinPool().invoke(linksFinder);
    }
}
