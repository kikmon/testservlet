package io.jenkins.plugins;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class MyAction implements Action {
    private static final Logger LOGGER = Logger.getLogger(MyAction.class.getName());

    private final Job<?, ?> job;

    public MyAction(Job<?, ?> job) {
        this.job = job;
    }

    @Override
    public String getIconFileName() {
        return null; // or provide an icon file name
    }

    @Override
    public String getDisplayName() {
        return "My Action";
    }

    @Override
    public String getUrlName() {
        return "from-queue";
    }

    @GET
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        LOGGER.info("doIndex called");
        String queueId = req.getParameter("queueid");
        long id = 0;
        try {
            id = Long.parseLong(queueId);
        } catch (NumberFormatException e) {
            LOGGER.warning("queue ID is not an integer: " + queueId);
            rsp.sendError(404, "Invalid QueueID " + queueId);
            return;
        }

        // Logic to check if the build has started
        if (buildStarted(id)) {
            String newUrl = buildUrl(id);
            if (newUrl == null) {
                rsp.sendError(404, "This queued job couldn't be found " + id);
                return;
            }
            rsp.sendRedirect2(newUrl);
        } else {
            rsp.sendError(404, "Build not started yet " + queueId);
        }
    }

    private boolean buildStarted(Long queueId) {
        // Get the queue item
        Queue.Item item = Jenkins.get().getQueue().getItem(queueId);
        if (item == null) {
            // If the item is not in the queue, it might have started
            LOGGER.info("Found queueId in normal Queue " + queueId);
            return true;
        }

        // Maybe the job was just started and is now in the LeftItem queue
        Collection<Queue.LeftItem> leftItems = Jenkins.get().getQueue().getLeftItems();
        for (Queue.LeftItem leftitem : leftItems) {
            if (leftitem.getId() == queueId) {
                LOGGER.info("Found queueId in Left Queue " + queueId);
                return true;
            }
        }

        LOGGER.info("Found queueId in normal Queue " + queueId);
        return false;
    }

    private String buildUrl(Long queueId) {
        LOGGER.info("Looking for Job that had the queue " + queueId);
        for (Run<?, ?> run : job.getBuilds()) {
            if (run.getQueueId() == queueId) {
                // Construct the URL for the build
                return Jenkins.get().getRootUrl() + run.getUrl();
            }
        }

        // If no matching build is found, return null or an appropriate message
        return null;
    }
}
