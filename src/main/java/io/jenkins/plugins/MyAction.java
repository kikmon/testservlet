package io.jenkins.plugins;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class MyAction implements Action {
    private static final Logger LOGGER = Logger.getLogger(MyAction.class.getName());

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
        return "my-action";
    }

    @GET
    public void doIndex(StaplerRequest req, StaplerResponse rsp) throws IOException {
        LOGGER.info("doIndex called");
        String queueId = req.getParameter("queueid");

        // Logic to check if the build has started
        if (buildStarted(queueId)) {
            rsp.sendRedirect2(buildUrl(queueId));
        } else {
            rsp.sendError(404, "Build not started yet " + queueId);
        }
    }

    private boolean buildStarted(String queueId) {
        try {
            // Parse the queue ID
            long id = Long.parseLong(queueId);

            // Get the queue item
            Queue.Item item = Jenkins.get().getQueue().getItem(id);
            if (item == null) {
                // If the item is not in the queue, it might have started
                return true;
            }

            // If the item is still in the queue, the build has not started
            return false;
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid queue ID: " + queueId);
            return false;
        }
    }

    private String buildUrl(String queueId) {
        try {
            // Parse the queue ID
            long id = Long.parseLong(queueId);

            // Iterate through all jobs and their builds to find the matching queue ID
            for (Job<?, ?> job : Jenkins.get().getAllItems(Job.class)) {
                for (Run<?, ?> run : job.getBuilds()) {
                    if (run.getQueueId() == id) {
                        // Construct the URL for the build
                        return Jenkins.get().getRootUrl() + run.getUrl();
                    }
                }
            }

            // If no matching build is found, return null or an appropriate message
            return "Build Not Found";
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid queue ID: " + queueId);
            return "Invalid queue ID";
        }
    }
}
