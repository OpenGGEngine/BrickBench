package com.opengg.loader.editor.hook;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;


public class DolphinAPI {
    private static final int MEM_MAPPED = 0x40000;
    public long m_emuRAMAddressStart = 0;
    public long m_MEM2AddressStart = -1;
    long m_emuARAMAdressStart = -1;
    boolean m_ARAMAccessible = false;
    long MEM1_SIZE = 0x1800000;
long MEM1_START = 0x80000000;
long MEM1_END = 0x81800000;

long ARAM_SIZE = 0x1000000;
// Dolphin maps 32 mb for the fakeVMem which is what ends up being the speedhack, but in reality
// the ARAM is actually 16 mb. We need the fake size to do process address calculation
long ARAM_FAKESIZE = 0x2000000;
long ARAM_START = 0x7E000000;
long ARAM_END = 0x7F000000;

long MEM2_SIZE = 0x4000000;
long MEM2_START = 0x90000000;
long MEM2_END = 0x94000000;
public static DolphinAPI INSTANCE;
    public boolean init(WinNT.HANDLE process) {
        com.sun.jna.platform.win32.WinNT.MEMORY_BASIC_INFORMATION info = new  com.sun.jna.platform.win32.WinNT.MEMORY_BASIC_INFORMATION();
        long pointer = 0;
        boolean MEM1Found = false;
        boolean MEM2Found = false;
        boolean m_MEM2Present = false;
        for(Pointer p = new Pointer(pointer); Kernel32.INSTANCE.VirtualQueryEx(process,p,info, new BaseTSD.SIZE_T(info.size())).longValue() == info.size() ; pointer+=info.regionSize.longValue(),p = new Pointer(pointer)){
            System.out.println(p.toString() + "---------");
            System.out.println(info.regionSize.longValue());
            System.out.println(info.size());
            if(info.regionSize.longValue() == 0x4000000){
                long regionBaseAddress = Pointer.nativeValue(info.baseAddress);
                if (MEM1Found && regionBaseAddress > m_emuRAMAddressStart + 0x10000000) {
                    break;
                }
                Psapi.PSAPI_WORKING_SET_EX_INFORMATION wsInfo = new Psapi.PSAPI_WORKING_SET_EX_INFORMATION();
                wsInfo.VirtualAddress = info.baseAddress;
                wsInfo.autoWrite();
                if (Psapi.INSTANCE.QueryWorkingSetEx(process, wsInfo.getPointer(), wsInfo.size())){
                    wsInfo.autoRead();
                    if (wsInfo.isValid()) {
                        m_MEM2AddressStart = regionBaseAddress;
                        m_MEM2Present = true;
                    }
                }
            }else if(info.regionSize.longValue() == 0x2000000 && info.type.equals(new WinDef.DWORD(MEM_MAPPED))){
                Psapi.PSAPI_WORKING_SET_EX_INFORMATION wsInfo = new Psapi.PSAPI_WORKING_SET_EX_INFORMATION();
                wsInfo.VirtualAddress = info.baseAddress;
                var infoPTR = wsInfo.getPointer();
                wsInfo.autoWrite();
                if (Psapi.INSTANCE.QueryWorkingSetEx(process,infoPTR, wsInfo.size())){
                    wsInfo.autoRead();
                    if (wsInfo.isValid()) {
                        if (!MEM1Found)
                        {
                            m_emuRAMAddressStart = Pointer.nativeValue(info.baseAddress);
                            MEM1Found = true;
                        }
                        else {
                            long aramCandidate = 0;
                            aramCandidate = Pointer.nativeValue(info.baseAddress);
                            if (aramCandidate == m_emuRAMAddressStart + 0x2000000)
                            {
                                m_emuARAMAdressStart = aramCandidate;
                                m_ARAMAccessible = true;
                            }
                        }
                    }
                }
            }

        }
        if (m_MEM2Present)
        {
            m_emuARAMAdressStart = 0;
            m_ARAMAccessible = false;
        }
        System.out.println("Found: " + Long.toHexString(m_emuRAMAddressStart) + "," + Long.toHexString(m_MEM2AddressStart));
        if (m_emuRAMAddressStart == 0)
        {
            // Here, Dolphin is running, but the emulation hasn't started
            return false;
        }
        INSTANCE = this;
        return true;
    }
    public long translateAddress(long offset) {
        offset-=MEM1_START;
        long RAMAddress = 0;
        if (m_ARAMAccessible) {
            if (offset >= ARAM_FAKESIZE)
                RAMAddress = m_emuRAMAddressStart + offset - ARAM_FAKESIZE;
            else
                RAMAddress = m_emuARAMAdressStart + offset;
        } else if (offset >= (MEM2_START - MEM1_START)) {
            RAMAddress = m_MEM2AddressStart + offset - (MEM2_START - MEM1_START);
        } else {
            RAMAddress = m_emuRAMAddressStart + offset;
        }

        return RAMAddress;
    }
}
