package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;

public final class WebCrawlerRecursiveAction extends RecursiveAction {
    private final Clock clock;
    private final PageParserFactory parserFactory;
    private final int maxDepth;
    private final List<Pattern> ignoredUrls;
    private final Instant deadline;
    private final String url;
    private final ConcurrentSkipListSet<String> visitedUrls;
    private final ConcurrentMap<String, Integer> counts;
    @Inject
    WebCrawlerRecursiveAction(
            Clock clock,
            PageParserFactory parserFactory,
            int maxDepth,
            List<Pattern> ignoredUrls,
            Instant deadline,
            String url,
            ConcurrentSkipListSet<String> visitedUrls,
            ConcurrentMap<String, Integer> counts) {
        this.clock = clock;
        this.parserFactory = parserFactory;
        this.maxDepth = maxDepth;
        this.ignoredUrls = ignoredUrls;
        this.deadline = deadline;
        this.url = url;
        this.visitedUrls = visitedUrls;
        this.counts = counts;
    }
    @Override
    protected void compute() {
        if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
            return;
        }
        for (Pattern pattern : ignoredUrls) {
            if (pattern.matcher(url).matches()) {
                return;
            }
        }
        if (visitedUrls.contains(url)) {
            return;
        }
        visitedUrls.add(url);
        PageParser.Result result = parserFactory.get(url).parse();
        for (ConcurrentMap.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
            if (counts.containsKey(e.getKey())) {
                counts.put(e.getKey(), e.getValue() + counts.get(e.getKey()));
            } else {
                counts.put(e.getKey(), e.getValue());
            }
        }
        List<WebCrawlerRecursiveAction> webCrawlerRecursiveActions = result.getLinks().stream()
                .map(s -> new WebCrawlerRecursiveAction(clock, parserFactory, maxDepth - 1, ignoredUrls, deadline, s, visitedUrls, counts)).toList();
        invokeAll(webCrawlerRecursiveActions);
    }
}
