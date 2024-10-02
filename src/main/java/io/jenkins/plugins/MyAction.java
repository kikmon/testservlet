package io.jenkins.plugins;

import hudson.model.Action;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.GET;
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
            rsp.sendError(404, "Build not started yet");
        }
    }

    private boolean buildStarted(String queueId) {
        // Implement logic to check if the build has started
        return false;
    }

    private String buildUrl(String queueId) {
        // Implement logic to get the build URL
        return "";
    }
}
