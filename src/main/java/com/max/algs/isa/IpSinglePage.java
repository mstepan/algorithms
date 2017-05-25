package com.max.algs.isa;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by mstepan on 5/23/17.
 */
final class IpSinglePage implements Externalizable {

    private static final long serialVersionUID = 2825972565923015695L;

    long startOffset;
    String startIp;

    public IpSinglePage() {
    }

    public boolean hasIp(Path path, String ipToFind) {

        try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
            file.seek(startOffset);

            for (int i = 0; i < IpsMasterIndex.IPS_PER_PAGE; ++i) {
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
