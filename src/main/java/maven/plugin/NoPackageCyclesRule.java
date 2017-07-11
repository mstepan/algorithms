package maven.plugin;

import jdepend.framework.JDepend;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;


public final class NoPackageCyclesRule implements EnforcerRule {

    @Override
    public void execute(@Nonnull EnforcerRuleHelper helper) throws EnforcerRuleException {

        Log log = helper.getLog();

        try {
            MavenProject project = (MavenProject) helper.evaluate("${project}");
            File targetDir = new File((String) helper.evaluate("${project.build.directory}"));
            File classesDir = new File(targetDir, "classes");

            if ("jar".equalsIgnoreCase(project.getPackaging()) && classesDir.exists()) {
                JDepend jdepend = new JDepend();
                jdepend.addDirectory(classesDir.getAbsolutePath());
                jdepend.analyze();

                if (jdepend.containsCycles()) {
                    throw new EnforcerRuleException("There are package cycles");
                }
            }
            else {
                log.warn("Skipping jdepend analysis as " + classesDir + " does not exist.");
            }
        }
        catch (ExpressionEvaluationException e) {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(), e);
        }
        catch (IOException ioEx) {
            throw new EnforcerRuleException("Unable to access target directory " + ioEx.getLocalizedMessage(), ioEx);
        }
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isResultValid(@Nonnull EnforcerRule enforcerRule) {
        return false;
    }

    @Nullable
    @Override
    public String getCacheId() {
        return null;
    }
}
