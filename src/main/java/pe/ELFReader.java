package pe;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

/**
 * Possible output:
 * 
 * 64 bit object Data encoding: Little-endian (LSB) OS/ABI identiﬁcation: System
 * V ABI Executable file CPU: Advanced Micro Devices x86 – 64. Virtual entry
 * point: 400441
 * 
 */
public final class ELFReader {

	private static final Logger LOG = Logger.getLogger(ELFReader.class);

	private static final int ELF_MAGIC = 0x7F_45_4C_46;

	private static final int EI_CLASS = 4;
	private static final int EI_DATA = 5;
	
	// OS/ABI identiﬁcation
	private static final int EI_OSABI = 7;

	private ELFReader() throws Exception {

		Path filePath = Paths
				.get("/home/max/repo/incubator/algorithms/src/main/java/pe/main");

		if (!Files.exists(filePath)) {
			throw new IllegalArgumentException("Can't find file '" + filePath
					+ "'");
		}

		// 0x7f 0x45 0x4c 0x46
		try (DataInputStream inStream = new DataInputStream(
				Files.newInputStream(filePath))) {

			byte[] arr = new byte[16];
			inStream.read(arr);

			int magicValue = arr[0];
			magicValue <<= 8;
			magicValue |= (arr[1] & 0xFF);
			magicValue <<= 8;
			magicValue |= (arr[2] & 0xFF);
			magicValue <<= 8;
			magicValue |= (arr[3] & 0xFF);

			if (magicValue != ELF_MAGIC) {
				throw new IllegalArgumentException(
						"Not ELF file format, magic value is incorrect '"
								+ magicValue + "'");
			}

			if (arr[EI_CLASS] == 1) {
				LOG.info("32 bit object");
			}
			else
				if (arr[EI_CLASS] == 2) {
					LOG.info("64 bit object");
				}

			boolean bigEndian = true;

			if (arr[EI_DATA] == 1) {
				bigEndian = false;
				LOG.info("Data encoding: Little-endian (LSB)");
			}
			else
				if (arr[EI_DATA] == 2) {
					LOG.info("Data encoding: Big-endian (MSB)");
				}

			switch ((arr[EI_OSABI] & 0xFF)) {
			case 0:
				LOG.info("OS/ABI identiﬁcation: System V ABI");
				break;
			case 1:
				System.out
						.println("OS/ABI identiﬁcation: HP-UX operating system");
				break;
			case 255:
				System.out
						.println("OS/ABI identiﬁcation: Standalone (embedded) application");
				break;
			default:
				throw new IllegalStateException(
						"Can't determine OS/ABI identiﬁcation");
			}

			short etype = readShort(inStream, bigEndian);

			switch (etype) {
			case 0:
				LOG.info("None");
				break;
			case 1:
				LOG.info("Object file");
				break;
			case 2:
				LOG.info("Executable file");
				break;
			case 3:
				LOG.info("Shared library");
				break;
			case 4:
				LOG.info("Memoty dump file");
				break;
			default:
				LOG.info("Unknown");
			}

			short emachine = readShort(inStream, bigEndian); // cpu architecture

			if (emachine == 62) {
				LOG.info("CPU: Advanced Micro Devices x86 – 64.");
			}

			long entryPoint = readLong(inStream, bigEndian);

			LOG.info("Virtual entry point: "
					+ Long.toHexString(entryPoint));

		}
	}

	private long readLong(DataInputStream inStream, boolean bigEndian)
			throws IOException {
		if (bigEndian) {
			return inStream.readLong();
		}

		long int1 = (readInt(inStream, bigEndian) & 0x7F_FF_FF_FF);
		long int2 = (readInt(inStream, bigEndian) & 0x7F_FF_FF_FF);

		return (int2 << 32) | int1;
	}

	private int readInt(DataInputStream inStream, boolean bigEndian)
			throws IOException {
		if (bigEndian) {
			return inStream.readInt();
		}

		short short1 = readShort(inStream, bigEndian);
		short short2 = readShort(inStream, bigEndian);

		return (short2 << 16) | short1;
	}

	short readShort(DataInputStream inStream, boolean bigEndian)
			throws IOException {

		if (bigEndian) {
			return inStream.readShort();
		}

		byte byte1 = inStream.readByte();
		byte byte2 = inStream.readByte();
		return (short) ((byte2 << 8) | (byte1 & 0xFF));

	}

	public static void main(String[] args) {
		try {
			new ELFReader();
		}
		catch (Exception ex) {
			LOG.error(ex);
		}
	}

}
