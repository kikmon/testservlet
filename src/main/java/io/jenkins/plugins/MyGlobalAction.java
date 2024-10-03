package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import java.io.IOException;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

@Extension
public class MyGlobalAction implements Action {

    private static final Logger LOGGER = Logger.getLogger(MyGlobalAction.class.getName());

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "My Global Action";
    }

    @Override
    public String getUrlName() {
        return "myglobalaction";
    }

    public void doDynamic(StaplerRequest req, StaplerResponse rsp) throws IOException {

        LOGGER.info("doDynamic called");
    }
}
