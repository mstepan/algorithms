package com.max.algs;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public final class AlgorithmsMain {

    private static final Pattern DOT_REGEXP = Pattern.compile("[.]");

    private static final Comparator<String> IP_STR_CMP = new Comparator<String>() {
        @Override
        public int compare(String str1, String str2) {

            // TODO: think about better comparison technique, with less space overhead

            String[] first = DOT_REGEXP.split(str1);
            String[] second = DOT_REGEXP.split(str2);

            for (int i = 0; i < first.length; ++i) {

                int partCmpRes = Integer.compare(Integer.valueOf(first[i]), Integer.valueOf(second[i]));

                if (partCmpRes != 0) {
                    return partCmpRes;
                }
            }

            return 0;
        }
    };

    /**
     * `Indexed sequential access` file for IP addresses.
     */
    private static final class IpsIndex implements Externalizable {

        private static final long serialVersionUID = 4290176883693140980L;

        private static final long SINGLE_IP_RECORD_SIZE = 16; // in bytes
        private static final long PAGE_SIZE = 4 * 1024; // 4 KB = 4 * 1024 bytes
        private static final int IPS_PER_PAGE = (int) (PAGE_SIZE / SINGLE_IP_RECORD_SIZE);

        private List<IpPage> pages;
        private Path dataFilePath;

        public IpsIndex() {
            dataFilePath = null;
        }

        public IpsIndex(Path dataFilePath, List<IpPage> pages) {
            this.dataFilePath = dataFilePath;
            this.pages = pages;
        }

        public static IpsIndex createFromDataFile(Path path, int linesToRead, boolean debugMode) {
            try {
                List<IpPage> pages = new ArrayList<>();

                try (BufferedReader reader = Files.newBufferedReader(path)) {

                    IpPage page = null;

                    String line;
                    long offset = 0L;
                    int ipsLeftOnPage = IPS_PER_PAGE;

                    while ((line = reader.readLine()) != null && linesToRead != 0) {

                        --linesToRead;

                        if (page == null) {
                            page = new IpPage();
                            page.startIp = line;
                            page.startOffset = offset;
                        }

                        offset += line.length() + 1;

                        --ipsLeftOnPage;

                        if (ipsLeftOnPage == 0) {
                            pages.add(page);

                            if (debugMode) {
                                System.out.printf("Page built: [%s, %s] %n", page.startIp, "***");
                            }

                            ipsLeftOnPage = 255;
                            page = null;
                        }
                    }

                    if (page != null) {
                        if (debugMode) {
                            System.out.printf("Page built: [%s, %s] %n", page.startIp, "***");
                        }
                        pages.add(page);
                    }
                }

                return new IpsIndex(path, pages);
            }
            catch (IOException ioEx) {
                throw new IllegalStateException("Can't create index for file '" + path.toString() + "'",
                        ioEx);
            }
        }

        public static IpsIndex readFromFile(Path path) {
            try (InputStream in = Files.newInputStream(path);
                 ObjectInputStream objIn = new ObjectInputStream(in)) {
                return (IpsIndex) objIn.readObject();
            }
            catch (IOException ioEx) {
                throw new IllegalStateException("Can't read index from file '" + path.toString() + "'",
                        ioEx);
            }
            catch (ClassNotFoundException classNotFoundEx) {
                throw new IllegalStateException(classNotFoundEx);
            }
        }

        public boolean contains(String searchIp) {

            System.out.printf("pages ips: [%s; %s] %n", pages.get(0).startIp, pages.get(pages.size() - 1).startIp);

            IpPage lastPage = null;

            for (IpPage curPage : pages) {

                int cmpRes = IP_STR_CMP.compare(curPage.startIp, searchIp);

                if (cmpRes == 0) {
                    return true;
                }

                if (cmpRes > 0) {
                    break;
                }

                lastPage = curPage;
            }

            return lastPage.hasIp(dataFilePath, searchIp);
        }

        public void writeToFile(Path indexPath) {
            try {
                Files.deleteIfExists(indexPath);
                Files.createFile(indexPath);

                try (OutputStream out = Files.newOutputStream(indexPath);
                     ObjectOutputStream objOut = new ObjectOutputStream(out)) {
                    objOut.writeObject(this);
                }
            }
            catch (IOException ioEx) {
                throw new IllegalStateException("Can't serilize index to file '" + indexPath.toString() + "'",
                        ioEx);
            }
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

            out.writeUTF(dataFilePath.toString());
            out.writeInt(pages.size());

            for (IpPage page : pages) {
                out.writeObject(page);
            }
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

            dataFilePath = Paths.get(in.readUTF());

            int pagesSize = in.readInt();
            pages = new ArrayList<>(pagesSize);

            for (int i = 0; i < pagesSize; ++i) {
                pages.add((IpPage) in.readObject());
            }
        }

        private static final class IpPage implements Externalizable {

            private static final long serialVersionUID = 2825972565923015695L;

            long startOffset;
            String startIp;

            public IpPage() {
            }

            public boolean hasIp(Path path, String ipToFind) {

                try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
                    file.seek(startOffset);

                    for (int i = 0; i < IPS_PER_PAGE; ++i) {
                        String ip = file.readLine();

                        if (ip == null) {
                            break;
                        }

                        if (ip.equals(ipToFind)) {
                            return true;
                        }
                    }
                }
                catch (IOException ioEx) {
                    throw new IllegalStateException(ioEx);
                }

                return false;
            }

            @Override
            public void writeExternal(ObjectOutput out) throws IOException {
                out.writeLong(startOffset);
                out.writeUTF(startIp);
            }

            @Override
            public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
                startOffset = in.readLong();
                startIp = in.readUTF();
            }
        }
    }

    private AlgorithmsMain() throws Exception {

//        IpsIndex index = IpsIndex.createFromDataFile(Paths.get("/Users/mstepan/Desktop/ips.txt"), Integer.MAX_VALUE,
//                false);
//        index.writeToFile(Paths.get("/Users/mstepan/Desktop/ips-index.data"));
//        index = null;

        IpsIndex indexFromFile = IpsIndex.readFromFile(Paths.get("/Users/mstepan/Desktop/ips-index.data"));

        String[] arr = {
//                "10.11.12.13", "133.211.0.0", "192.168.56.78",
//                "10.25.67.89",
                "250.133.100.50",
//                "57.89.54.67"
        };

        for (String ipToCheck : arr) {
            System.out.printf("contains('%s') = %b %n", ipToCheck, indexFromFile.contains(ipToCheck));
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

