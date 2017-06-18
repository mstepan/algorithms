package merger;

import org.apache.log4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;


public final class MergerMain {

    private static final Logger LOG = Logger.getLogger(MergerMain.class);

    private MergerMain() throws Exception {

        Path[] paths = new Path[]{
                Paths.get("/home/max/repo/out/1.txt"),
                Paths.get("/home/max/repo/out/2.txt"),
                Paths.get("/home/max/repo/out/3.txt"),
                Paths.get("/home/max/repo/out/4.txt"),
                Paths.get("/home/max/repo/out/5.txt"),
        };

        Path destPath = Paths.get("/home/max/repo/out/res.txt");

        SortedFilesMerger.merge(paths, destPath);


        LOG.info("MergerMain done");
    }


    public static void main(String[] args) {
        try {
            new MergerMain();
        }
        catch (final Exception ex) {
            LOG.error(ex);
        }

    }

}
