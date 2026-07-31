package org.example.controller;

import org.example.entity.IpoSubscription;
import org.example.repository.IpoSubscriptionRepository;
import org.example.service.IpoDataService;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ipos")
public class IpoController {

    private final IpoSubscriptionRepository repository;
    private final IpoDataService ipoDataService;

    public IpoController(IpoSubscriptionRepository repository, IpoDataService ipoDataService) {
        this.repository = repository;
        this.ipoDataService = ipoDataService;
    }

    @GetMapping("/live")
    public List<IpoSubscription> getLatestSnapshots() {
        List<IpoSubscription> all = repository.findTop50ByOrderByFetchedAtDesc();
        Map<String, IpoSubscription> latestBySlug = all.stream()
                .collect(Collectors.toMap(
                        IpoSubscription::getSlug,
                        s -> s,
                        (existing, replacement) ->
                                existing.getFetchedAt().isAfter(replacement.getFetchedAt()) ? existing : replacement
                ));
        return latestBySlug.values().stream()
                .sorted(Comparator.comparing(IpoSubscription::getClosingDate))
                .collect(Collectors.toList());
    }

    @GetMapping("/{slug}/history")
    public List<IpoSubscription> getHistory(@PathVariable String slug) {
        return repository.findBySlugOrderByFetchedAtDesc(slug);
    }

    @PostMapping("/refresh")
    public String triggerRefresh() {
        ipoDataService.fetchAndSaveOpenIpos();
        return "Refresh triggered";
    }
}