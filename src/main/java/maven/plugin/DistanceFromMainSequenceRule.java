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
import java.util.*;


public final class DistanceFromMainSequenceRule implements EnforcerRule {

    private String skipPackages;

    @Override
    public void execute(@Nonnull EnforcerRuleHelper helper) throws EnforcerRuleException {

        String[] skipPackagesArr = skipPackages.trim().split(",");

        Set<String> skipPackagesSet = new HashSet<>();

        for (String packageToSkip : skipPackagesArr) {
            skipPackagesSet.add(packageToSkip.trim());
        }

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

                List<JavaPackage> projectPackagesOnly = filter(jdepend.getPackages(), groupId, skipPackagesSet);

                final double standardDeviation = distanceStandardDeviation(projectPackagesOnly);
                final double threshold = 2 * standardDeviation;

                log.info("standardDeviation: " + standardDeviation + ", threshold: " + threshold);

                for (JavaPackage singleJavaPackage : projectPackagesOnly) {
                    if (singleJavaPackage.getName().startsWith(groupId)) {
                        double distanceFromMain = singleJavaPackage.distance();

                        // distance is greater than 2 standard deviations
                        if (Double.compare(distanceFromMain, threshold) > 0) {
                            log.warn(describePackageProblem(singleJavaPackage));
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

    private static List<JavaPackage> filter(Collection allPackages, String groupId, Set<String> skipPackagesSet) {
        List<JavaPackage> res = new ArrayList<>();

        for (Object pckg : allPackages) {
            JavaPackage singleJavaPackage = (JavaPackage) pckg;

            // skip not project packages and utility packages
            if (singleJavaPackage.getName().startsWith(groupId) &&
                    !(skipPackagesSet.contains(singleJavaPackage.getName()))) {
                res.add(singleJavaPackage);
            }
        }
        return res;
    }

    private static double distanceStandardDeviation(List<JavaPackage> packagesList) {

        final int n = packagesList.size();

        double sum = 0.0;

        for (JavaPackage singleJavaPackage : packagesList) {
            sum += singleJavaPackage.distance();
        }

        double avg = sum / n;

        double deviation = 0.0;

        for (JavaPackage singleJavaPackage : packagesList) {
            double diff = (singleJavaPackage.distance() - avg);
            deviation += (diff * diff);
        }

        return Math.sqrt(deviation / n);
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
