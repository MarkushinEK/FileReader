package worktofiles;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class FileReader {
    private long pos = 0;
    private long lastLimit = 0;
    private File path;

    public FileReader(String path) {
        this.path = new File(path);
    }

    public String readText(boolean flag) throws IOException {
        int mapsize = 512 * 1024;
        byte[] text;
        try (FileChannel channel = FileChannel.open(path.toPath(), StandardOpenOption.READ)) {
            final long length = channel.size();
            if (flag) pos -= lastLimit;
            if (flag && pos != 0) pos -= mapsize;
            long remaining = length - pos;
            long limit = Math.min(remaining, mapsize);
            if (limit != 0) lastLimit = limit;
            text = new byte[(int)limit];
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, pos, remaining);
            pos += limit;
            for (int i = 0; i < limit; i++) {
                final byte b = buffer.get(i);
                text[i] = b;
            }
            return new String(text, "cp1251");
        }
    }

}
