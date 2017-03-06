package pe;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public final class PEReader {
	
	private static final Logger LOG = Logger.getLogger(PEReader.class);
	
	private static final short PE_MAGIC = 0x4D5A; 
	
	private PEReader() throws IOException {
		

		try( DataInputStream inStream = new DataInputStream( PEReader.class.getResourceAsStream("/pe/console1.exe") ) ){
						
			int magic = inStream.readShort();
			
			if( magic != PE_MAGIC ){
				throw new IllegalArgumentException("PE magic number is incorrect '" + magic + "'");
			}
		}
		
		LOG.info("PE reader finished");
	}
	
	public static void main(String[] args) {
		try {
			new PEReader();
		}
		catch( Exception ex ){
			LOG.error(ex);
		}
	}

}
