package gemini.webcrawler;

import java.util.*;

public class WebCrawler {

    private final int crawlDepth;
    private final Queue<CrawlTask> queue;
    private final HtmlDownloader htmlDownloader;

    public WebCrawler(List<String> seedURLs, int crawlDepth, HtmlDownloader htmlDownloader) {
        if (crawlDepth <= 0) {
            throw new IllegalArgumentException("Crawl depth should be greater than 0");
        }
        if (seedURLs == null || seedURLs.isEmpty()) {
            throw new IllegalArgumentException("seedURLs null or empty.");
        }
        if (htmlDownloader == null) {
            throw new IllegalArgumentException("seedURLs cannot be null.");
        }
        this.crawlDepth = crawlDepth;
        this.htmlDownloader = htmlDownloader;
        this.queue = new LinkedList<>();

        seedURLs.forEach( url -> queue.add(new CrawlTask(url, 1)));
    }

    public List<String> crawl() {
        Set<String> processedURLs = new HashSet<>();

        while (!queue.isEmpty()) {
            CrawlTask nextTask = queue.poll();
            if (nextTask.depth <= this.crawlDepth && !processedURLs.contains(nextTask.url)) {
                String downloadHtml = this.htmlDownloader.downloadHtml(nextTask.url);
                List<String> urls = this.htmlDownloader.extractUrls(downloadHtml);
                urls.forEach(url -> {
                    if (!processedURLs.contains(url)) {
                        queue.add(new CrawlTask(url, nextTask.depth + 1));
                    }
                });
                processedURLs.add(nextTask.url());
            }
        }
        return processedURLs.stream().toList();
    }

    private record CrawlTask(String url, int depth) {}

    public static class HtmlDownloader {
        public String downloadHtml(String url) {
            return "";
        }

        public List<String> extractUrls(String html) {
            return List.of("");
        }
    }
}
