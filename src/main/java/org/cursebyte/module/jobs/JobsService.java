package org.cursebyte.module.jobs;

import java.util.UUID;

public class JobsService {
    public static void initPlayer(UUID uuid) {
        JobsRepository.createPlayer(uuid);
    }

    public static String getJob(UUID uuid) {
        return JobsRepository.getJobs(uuid);
    }

    public static void changeJob(UUID uuid, String job) {
        JobsRepository.changeJob(uuid, job);
    }

    public static boolean isUnemployed(UUID uuid) {
        return getJob(uuid) == "UNEMPLOYMENT";
    }
}
