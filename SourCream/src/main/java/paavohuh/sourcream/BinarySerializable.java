package paavohuh.sourcream;

/**
 * An interface for types that can be serialized into binary.
 */
public interface BinarySerializable {
    /**
     * Writes a binary representation of this object to the binary serializer.
     * @param binarySerializer The binary serializer to use.
     */
    void serializeTo(BinarySerializer binarySerializer);
}
