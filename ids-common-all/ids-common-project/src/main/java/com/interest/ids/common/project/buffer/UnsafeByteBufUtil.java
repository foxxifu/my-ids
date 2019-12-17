package com.interest.ids.common.project.buffer;

import io.netty.util.internal.PlatformDependent;

import java.nio.ByteOrder;

/**
 * 
 * @author lhq
 *
 */
class UnsafeByteBufUtil {

    static final boolean BIG_ENDIAN_NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
    private static final boolean UNALIGNED = PlatformDependent.isUnaligned();

    static byte getByte(long address) {
        return PlatformDependent.getByte(address);
    }

    static short getShort(long address) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(address);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short) (PlatformDependent.getByte(address) << 8 | PlatformDependent.getByte(address + 1) & 0xff);
    }

    static int getUnsignedMedium(long address) {
        if (UNALIGNED) {
            if (BIG_ENDIAN_NATIVE_ORDER) {
                return (PlatformDependent.getByte(address) & 0xff) |
                        (PlatformDependent.getShort(address + 1) & 0xffff) << 8;
            }
            return (Short.reverseBytes(PlatformDependent.getShort(address)) & 0xffff) << 8 |
                    PlatformDependent.getByte(address + 2) & 0xff;
        }
        return (PlatformDependent.getByte(address) & 0xff) << 16 |
               (PlatformDependent.getByte(address + 1) & 0xff) << 8 |
               PlatformDependent.getByte(address + 2) & 0xff;
    }

    static int getInt(long address) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(address);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return PlatformDependent.getByte(address) << 24 |
               (PlatformDependent.getByte(address + 1) & 0xff) << 16 |
               (PlatformDependent.getByte(address + 2) & 0xff) <<  8 |
               PlatformDependent.getByte(address + 3) & 0xff;
    }

    static long getLong(long address) {
        if (UNALIGNED) {
            long v = PlatformDependent.getLong(address);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (long) PlatformDependent.getByte(address) << 56 |
               ((long) PlatformDependent.getByte(address + 1) & 0xff) << 48 |
               ((long) PlatformDependent.getByte(address + 2) & 0xff) << 40 |
               ((long) PlatformDependent.getByte(address + 3) & 0xff) << 32 |
               ((long) PlatformDependent.getByte(address + 4) & 0xff) << 24 |
               ((long) PlatformDependent.getByte(address + 5) & 0xff) << 16 |
               ((long) PlatformDependent.getByte(address + 6) & 0xff) <<  8 |
               (long) PlatformDependent.getByte(address + 7) & 0xff;
    }

    static void setByte(long address, int value) {
        PlatformDependent.putByte(address, (byte) value);
    }

    static void setShort(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(
                    address, BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(address, (byte) (value >>> 8));
            PlatformDependent.putByte(address + 1, (byte) value);
        }
    }

    static void setMedium(long address, int value) {
        if (UNALIGNED) {
            if (BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putByte(address, (byte) value);
                PlatformDependent.putShort(address + 1, (short) (value >>> 8));
            } else {
                PlatformDependent.putShort(address, Short.reverseBytes((short) (value >>> 8)));
                PlatformDependent.putByte(address + 2, (byte) value);
            }
        } else {
            PlatformDependent.putByte(address, (byte) (value >>> 16));
            PlatformDependent.putByte(address + 1, (byte) (value >>> 8));
            PlatformDependent.putByte(address + 2, (byte) value);
        }
    }

    static void setInt(long address, int value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(address, BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
        } else {
            PlatformDependent.putByte(address, (byte) (value >>> 24));
            PlatformDependent.putByte(address + 1, (byte) (value >>> 16));
            PlatformDependent.putByte(address + 2, (byte) (value >>> 8));
            PlatformDependent.putByte(address + 3, (byte) value);
        }
    }

    static void setLong(long address, long value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(address, BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
        } else {
            PlatformDependent.putByte(address, (byte) (value >>> 56));
            PlatformDependent.putByte(address + 1, (byte) (value >>> 48));
            PlatformDependent.putByte(address + 2, (byte) (value >>> 40));
            PlatformDependent.putByte(address + 3, (byte) (value >>> 32));
            PlatformDependent.putByte(address + 4, (byte) (value >>> 24));
            PlatformDependent.putByte(address + 5, (byte) (value >>> 16));
            PlatformDependent.putByte(address + 6, (byte) (value >>> 8));
            PlatformDependent.putByte(address + 7, (byte) value);
        }
    }

    static byte getByte(byte[] array, int index) {
        return PlatformDependent.getByte(array, index);
    }

    static short getShort(byte[] array, int index) {
        if (UNALIGNED) {
            short v = PlatformDependent.getShort(array, index);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Short.reverseBytes(v);
        }
        return (short) (PlatformDependent.getByte(index) << 8 | PlatformDependent.getByte(index + 1) & 0xff);
    }

    static int getUnsignedMedium(byte[] array, int index) {
        if (UNALIGNED) {
            if (BIG_ENDIAN_NATIVE_ORDER) {
                return (PlatformDependent.getByte(array, index) & 0xff) |
                        (PlatformDependent.getShort(array, index + 1) & 0xffff) << 8;
            }
            return (Short.reverseBytes(PlatformDependent.getShort(array, index)) & 0xffff) << 8 |
                    PlatformDependent.getByte(array, index + 2) & 0xff;
        }
        return  (PlatformDependent.getByte(array, index) & 0xff) << 16 |
                (PlatformDependent.getByte(array, index + 1) & 0xff) <<  8 |
                PlatformDependent.getByte(array, index + 2) & 0xff;
    }

    static int getInt(byte[] array, int index) {
        if (UNALIGNED) {
            int v = PlatformDependent.getInt(array, index);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Integer.reverseBytes(v);
        }
        return PlatformDependent.getByte(array, index) << 24 |
                (PlatformDependent.getByte(array, index + 1) & 0xff) << 16 |
                (PlatformDependent.getByte(array, index + 2) & 0xff) <<  8 |
                PlatformDependent.getByte(array, index + 3) & 0xff;
    }

    static long getLong(byte[] array, int index) {
        if (UNALIGNED) {
            long v =  PlatformDependent.getLong(array, index);
            return BIG_ENDIAN_NATIVE_ORDER ? v : Long.reverseBytes(v);
        }
        return (long) PlatformDependent.getByte(array, index) << 56 |
                ((long) PlatformDependent.getByte(array, index + 1) & 0xff) << 48 |
                ((long) PlatformDependent.getByte(array, index + 2) & 0xff) << 40 |
                ((long) PlatformDependent.getByte(array, index + 3) & 0xff) << 32 |
                ((long) PlatformDependent.getByte(array, index + 4) & 0xff) << 24 |
                ((long) PlatformDependent.getByte(array, index + 5) & 0xff) << 16 |
                ((long) PlatformDependent.getByte(array, index + 6) & 0xff) <<  8 |
                (long) PlatformDependent.getByte(array, index + 7) & 0xff;
    }

    static void setByte(byte[] array, int index, int value) {
        PlatformDependent.putByte(array, index, (byte) value);
    }

    static void setShort(byte[] array, int index, int value) {
        if (UNALIGNED) {
            PlatformDependent.putShort(
                    array, index, BIG_ENDIAN_NATIVE_ORDER ? (short) value : Short.reverseBytes((short) value));
        } else {
            PlatformDependent.putByte(array, index, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 1, (byte) value);
        }
    }

    static void setMedium(byte[] array, int index, int   value) {
        if (UNALIGNED) {
            if (BIG_ENDIAN_NATIVE_ORDER) {
                PlatformDependent.putByte(array, index, (byte) value);
                PlatformDependent.putShort(array, index + 1, (short) (value >>> 8));
            } else {
                PlatformDependent.putShort(array, index, Short.reverseBytes((short) (value >>> 8)));
                PlatformDependent.putByte(array, index + 2, (byte) value);
            }
        } else {
            PlatformDependent.putByte(array, index, (byte) (value >>> 16));
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 2, (byte) value);
        }
    }

    static void setInt(byte[] array, int index, int   value) {
        if (UNALIGNED) {
            PlatformDependent.putInt(array, index, BIG_ENDIAN_NATIVE_ORDER ? value : Integer.reverseBytes(value));
        } else {
            PlatformDependent.putByte(array, index, (byte) (value >>> 24));
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 16));
            PlatformDependent.putByte(array, index + 2, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 3, (byte) value);
        }
    }

    static void setLong(byte[] array, int index, long  value) {
        if (UNALIGNED) {
            PlatformDependent.putLong(array, index, BIG_ENDIAN_NATIVE_ORDER ? value : Long.reverseBytes(value));
        } else {
            PlatformDependent.putByte(array, index, (byte) (value >>> 56));
            PlatformDependent.putByte(array, index + 1, (byte) (value >>> 48));
            PlatformDependent.putByte(array, index + 2, (byte) (value >>> 40));
            PlatformDependent.putByte(array, index + 3, (byte) (value >>> 32));
            PlatformDependent.putByte(array, index + 4, (byte) (value >>> 24));
            PlatformDependent.putByte(array, index + 5, (byte) (value >>> 16));
            PlatformDependent.putByte(array, index + 6, (byte) (value >>> 8));
            PlatformDependent.putByte(array, index + 7, (byte) value);
        }
    }

    
   

   

   

    private UnsafeByteBufUtil() { }
}
