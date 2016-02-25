
package paavohuh.sourcream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.joou.UByte;
import org.joou.UShort;

/**
 * Used for converting BinarySerializable objects into byte arrays.
 */
public class BinarySerializer {
    private final ByteArrayOutputStream stream;
    private final Writer writer;
    
    /**
     * Creates a new binary serializer.
     */
    public BinarySerializer() {
        stream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(stream);
    }
    
    /**
     * Writes an unsigned byte to the serializer.
     * The byte is expanded into 4 bytes.
     * @param ubyte The byte
     */
    public void write(UByte ubyte) {
        try {
            writer.write(ubyte.intValue());
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Writes an unsigned short to the serializer.
     * The short is expanded into 4 bytes.
     * @param ushort 
     */
    public void write(UShort ushort) {
        try {
            writer.write(ushort.intValue());
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Writes a byte array to the serializer.
     * @param bytes The byte array.
     */
    public void write(byte[] bytes) {
        try {
            stream.write(bytes);
        } catch (IOException ex) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Writes a serializable object to the serializer.
     * @param child  The object.
     */
    public void write(BinarySerializable child) {
        child.serializeTo(this);
    }
}
