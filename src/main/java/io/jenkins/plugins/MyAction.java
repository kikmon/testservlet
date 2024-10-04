package io.jenkins.plugins;

import hudson.model.Action;
import hudson.model.Job;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
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
        if (BuildUtils.buildStarted(id)) {
            String newUrl = BuildUtils.buildUrl(job, id);
            if (newUrl == null) {
                rsp.sendError(404, "This queued job couldn't be found " + id);
                return;
            }
            rsp.sendRedirect2(newUrl);
        } else {
            rsp.sendError(404, "Build not started yet " + queueId);
        }
    }
}
