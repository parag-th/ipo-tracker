package org.example.scheduler;

import org.example.service.IpoDataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IpoPollingScheduler {

    private final IpoDataService ipoDataService;

    public IpoPollingScheduler(IpoDataService ipoDataService) {
        this.ipoDataService = ipoDataService;
    }

////    @Scheduled(initialDelay = 0, fixedRate = 1_800_000) // runs immediately, then every 30 min
//    public void pollIpoSubscriptionData() {
//        ipoDataService.fetchAndSaveOpenIpos();
//    }
}