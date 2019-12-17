package com.interest.ids.dev.network.mqtt;

public enum ReadCode {
    dispersion((byte)0x02),cion((byte)0x01),inputReg((byte)0x04),keepReg((byte)0x03);
    private final byte readCode;

    private ReadCode(byte readCode)
    {
        this.readCode = readCode;
    }

    public byte getReadCode() {
        return readCode;
    }
}
