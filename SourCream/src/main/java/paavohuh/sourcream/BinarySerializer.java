
package paavohuh.sourcream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.joou.UByte;
import org.joou.UShort;

public class BinarySerializer {
    private final ByteArrayOutputStream stream;
    private final Writer writer;
    
    public BinarySerializer() {
        stream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(stream);
    }
    
    public void write(UByte ubyte) {
        try {
            writer.write(ubyte.intValue());
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    public void write(UShort ushort) {
        try {
            writer.write(ushort.intValue());
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    public void write(byte[] bytes) {
        try {
            stream.write(bytes);
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    public void write(BinarySerializable child) {
        child.serializeTo(this);
    }
}
