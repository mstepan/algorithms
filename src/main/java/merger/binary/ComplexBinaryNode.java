package merger.binary;

public class ComplexBinaryNode implements IBinaryNode {
	
	private String value;
	private final IBinaryNode left;
	private final IBinaryNode right;
	
	public ComplexBinaryNode(IBinaryNode left, IBinaryNode right) {
		super();
		this.left = left;
		this.right = right;
		
		if( left.getValue().compareTo( right.getValue() ) <= 0 ){
			this.value = left.getValue();
		}
		else {
			this.value = right.getValue();
		}
		
	}
	
	
	@Override
	public String next(){
		
		if( value == null ){
			return null;
		}
		
		if( left.getValue() == null ){
			value = right.next();
		}
		else if( right.getValue() == null ){
			value = left.next();
		}
		else if(left.getValue() == null && right.getValue() == null) {
			value = null;
		}
		else {
			if( value.equals(left.getValue()) ){
				left.next();				
			}
			else {
				right.next();
			}
			
			if(left.getValue() == null ){
				value = right.getValue();
			}
			else if(right.getValue() == null){
				value = left.getValue();
			}
			else {
				value = left.getValue().compareTo(right.getValue()) <= 0 ? left.getValue() : right.getValue();
			}
		}
		
		return value;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	public IBinaryNode getLeft() {
		return left;
	}
	
	public IBinaryNode getRight() {
		return right;
	}
	
	@Override
	public String toString(){
		return value;
	}
	

}
