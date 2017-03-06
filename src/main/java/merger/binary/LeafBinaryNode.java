package merger.binary;

import merger.iterator.FileIterator;


public class LeafBinaryNode implements IBinaryNode {
	
	private final FileIterator it;
	private String value;

	public LeafBinaryNode( FileIterator it ) {
		super();		
		this.it = it;
		if( it.hasNext() ){
			value = it.next();
		}
	}
	
	public FileIterator getIt() {
		return it;
	}

	@Override	
	public String getValue() {
		return value;
	}
	
	@Override
	public String next(){		
		value = it.hasNext() ? it.next() : null;		
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}

}
