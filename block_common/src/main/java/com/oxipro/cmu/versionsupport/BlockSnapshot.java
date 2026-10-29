package com.oxipro.cmu.versionsupport;

public final class BlockSnapshot {

    final int legacyId;
    final byte legacyData;
    final Object modernState;

    BlockSnapshot(int legacyId, byte legacyData, Object modernState) {
        this.legacyId = legacyId;
        this.legacyData = legacyData;
        this.modernState = modernState;
    }
}
