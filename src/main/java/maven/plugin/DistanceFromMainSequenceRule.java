package maven.plugin;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
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


public final class DistanceFromMainSequenceRule implements EnforcerRule {

    private double distanceThreshold;

    @Override
    public void execute(@Nonnull EnforcerRuleHelper helper) throws EnforcerRuleException {

        Log log = helper.getLog();

        try {
            final MavenProject project = (MavenProject) helper.evaluate("${project}");
            final File targetDir = new File((String) helper.evaluate("${project.build.directory}"));
            final String groupId = (String) helper.evaluate("${project.groupId}");
            final File classesDir = new File(targetDir, "classes");

            if ("jar".equalsIgnoreCase(project.getPackaging()) && classesDir.exists()) {
                JDepend jdepend = new JDepend();
                jdepend.addDirectory(classesDir.getAbsolutePath());
                jdepend.analyze();

                for (Object pckg : jdepend.getPackages()) {
                    JavaPackage singleJavaPackage = (JavaPackage) pckg;

                    if (singleJavaPackage.getName().startsWith(groupId)) {
                        double distanceFromMain = singleJavaPackage.distance();

                        if (Double.compare(distanceFromMain, distanceThreshold) >= 0) {
                            log.info(describePackageProblem(singleJavaPackage));
                            // throw new EnforcerRuleException("DistanceFromMainSequenceRule violated");
                        }
                    }
                }
            }
            else {
                log.warn("Skipping jdepend analysis as a '" + classesDir + "' does not exist.");
            }
        }
        catch (ExpressionEvaluationException expEx) {
            throw new EnforcerRuleException("Unable to lookup an expression " + expEx.getLocalizedMessage(), expEx);
        }
        catch (IOException ioEx) {
            throw new EnforcerRuleException("Unable to access target directory " + ioEx.getLocalizedMessage(), ioEx);
        }
    }

    private static String describePackageProblem(JavaPackage singleJavaPackage) {
        if (Float.compare(singleJavaPackage.abstractness(), 0.5F) >= 0) {
            return "'" + singleJavaPackage.getName() +
                    "', is too abstract and is not used a lot [just remove]" +
                    "[D = " + singleJavaPackage.distance() +
                    ", A = " + singleJavaPackage.abstractness() +
                    ", I = " + singleJavaPackage.instability() + "].";
        }

        return "'" + singleJavaPackage.getName() +
                "', is very concrete and lot's of packages depends on it [make more abstract or reduce dependants]" +
                "[D = " + singleJavaPackage.distance() +
                ", A = " + singleJavaPackage.abstractness() +
                ", I = " + singleJavaPackage.instability() + "].";
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
