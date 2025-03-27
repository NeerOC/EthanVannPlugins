package com.example.InteractionApi;

import com.example.PacketUtils.WidgetInfoExtended;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;

import java.util.List;

public class InteractionHelper {
    static private final int quickPrayerWidgetID = WidgetInfoExtended.MINIMAP_QUICK_PRAYER_ORB.getPackedId();
    static private final int specialWidgetID = WidgetInfoExtended.MINIMAP_SPEC_ORB.getPackedId();

    public static void toggleNormalPrayer(int packedWidgID) {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, packedWidgID, -1, -1);
    }


    public static void toggleNormalPrayers(List<Integer> packedWidgIDs) {
        for (Integer packedWidgID : packedWidgIDs) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, packedWidgID, -1, -1);
        }
    }

    public static void togglePrayer() {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, quickPrayerWidgetID, -1, -1);
    }

    public static void toggleSpecial() {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, specialWidgetID, -1, -1);
    }
}
