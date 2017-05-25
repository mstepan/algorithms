package com.max.algs.isa;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * `Indexed sequential access` file for IP addresses.
 */
final class IpsMasterIndex implements Externalizable {

    private static final long serialVersionUID = 4290176883693140980L;

    private static final long SINGLE_IP_RECORD_SIZE = 16; // in bytes
    private static final long PAGE_SIZE = 4 * 1024; // 4 KB = 4 * 1024 bytes
    static final int IPS_PER_PAGE = (int) (PAGE_SIZE / SINGLE_IP_RECORD_SIZE);

    private List<IpSinglePage> pages;
    private Path dataFilePath;

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

    public IpsMasterIndex() {
        dataFilePath = null;
    }

    public IpsMasterIndex(Path dataFilePath, List<IpSinglePage> pages) {
        this.dataFilePath = dataFilePath;
        this.pages = pages;
    }

    public static IpsMasterIndex createFromDataFile(Path path, boolean debugMode) {
        try {
            List<IpSinglePage> pages = new ArrayList<>();

            try (BufferedReader reader = Files.newBufferedReader(path)) {

                IpSinglePage page = null;

                String line;
                long offset = 0L;
                int ipsLeftOnPage = IPS_PER_PAGE;

                while ((line = reader.readLine()) != null) {

                    if (page == null) {
                        page = new IpSinglePage();
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

            System.out.printf("Index created: [%s; %s] %n",
                    pages.get(0).startIp, pages.get(pages.size() - 1).startIp);

            return new IpsMasterIndex(path, pages);
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't create index for file '" + path.toString() + "'",
                    ioEx);
        }
    }

    public static IpsMasterIndex readFromFile(Path path) {
        try (InputStream in = Files.newInputStream(path);
             ObjectInputStream objIn = new ObjectInputStream(in)) {
            return (IpsMasterIndex) objIn.readObject();
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

        System.out.printf("'contains' called for index with range: [%s; %s] %n",
                pages.get(0).startIp, pages.get(pages.size() - 1).startIp);

        IpSinglePage lastPage = null;

        for (IpSinglePage curPage : pages) {

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

        for (IpSinglePage page : pages) {
            out.writeObject(page);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        dataFilePath = Paths.get(in.readUTF());

        int pagesSize = in.readInt();
        pages = new ArrayList<>(pagesSize);

        for (int i = 0; i < pagesSize; ++i) {
            pages.add((IpSinglePage) in.readObject());
        }
    }

}
