package io.jenkins.plugins;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.Job;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;
import jenkins.model.TransientActionFactory;

@Extension
public class MyActionFactory extends TransientActionFactory<Job<?, ?>> {
    private static final Logger LOGGER = Logger.getLogger(MyActionFactory.class.getName());

    @Override
    public Class<Job<?, ?>> type() {
        return jobClass();
    }

    @SuppressWarnings("unchecked")
    private static Class<Job<?, ?>> jobClass() {
        return (Class<Job<?, ?>>) (Class<?>) Job.class;
    }

    @Override
    public Collection<? extends Action> createFor(Job<?, ?> target) {
        LOGGER.info("Creating MyAction for job: " + target.getName());
        return Collections.singleton(new MyAction(target));
    }
}
