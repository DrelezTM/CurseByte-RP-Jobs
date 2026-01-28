package org.cursebyte.module.jobs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JobsService {
    private static final Map<UUID, Long> leaveCooldown = new HashMap<>();

    public static final String UNEMPLOYMENT = "UNEMPLOYMENT";

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
        return UNEMPLOYMENT.equals(getJob(uuid));
    }

    public static boolean canLeaveJob(UUID uuid, long cooldownSeconds) {
        if (!leaveCooldown.containsKey(uuid)) return true;

        long lastLeave = leaveCooldown.get(uuid);
        long now = System.currentTimeMillis();

        return (now - lastLeave) >= cooldownSeconds * 1000;
    }

    public static long getLeaveCooldownLeft(UUID uuid, long cooldownSeconds) {
        long lastLeave = leaveCooldown.getOrDefault(uuid, 0L);
        long now = System.currentTimeMillis();

        long remaining =
                (cooldownSeconds * 1000) - (now - lastLeave);

        return Math.max(0, remaining / 1000);
    }

    public static void leaveJob(UUID uuid) {
        changeJob(uuid, UNEMPLOYMENT);
        leaveCooldown.put(uuid, System.currentTimeMillis());
    }
}
