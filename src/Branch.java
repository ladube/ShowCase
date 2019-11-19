import java.util.ArrayList;
public class Branch <String> {
	//Properties
	ArrayList<String> tokenSequence;
	ArrayList<Branch> children = new ArrayList<Branch>();
	int num = 0;
	
	public Branch(ArrayList<String> token) { //Node Constructor
		this.tokenSequence = token;
	}
	public Branch() { //Null Constructor
		tokenSequence = new ArrayList<String>(); 
	}
	//Methods
	public boolean addNode(Branch<String> node) {
		boolean found = false;
		int i = 0;
		if(tokenSequence.equals(node.tokenSequence)) {
			found = true;
		}else if(amIPrefix(node)||(tokenSequence.size()==0)) {
				while(!found && i < children.size()) {
					found = children.get(i).addNode(node);
					i++;
				}
			if(!found) {
				System.out.println("Not found and added: " + node.tokenSequence);
				children.add(node);
				found = true;
			}
		}
		return found;
	}
	public boolean amIPrefix(Branch<String> node) {	
		//tokenSequence = [What is ] Size 2 : [0 1] but [What is your] Size 3 : [ 0 1 2] works
		if(tokenSequence.size()==0)
			return false;
		int fromIndex = 0;
		int toIndex = node.tokenSequence.size()-2; //[What is your name] Size 4 [0 1 2 3] 
		if(toIndex < 0)
			return false;
		System.out.println("tokenSeq: " + tokenSequence + " // nodeSeq: " + node.tokenSequence.subList(fromIndex, toIndex));
		if(tokenSequence.equals(node.tokenSequence.subList(fromIndex, toIndex))) {
		    return true;
		}else
				return false;
	}
	public int responseNum() { // if added, that node's number corresponding to response increases
		num++; //in this case, they will all be 1 :C
		return num;
	}
//	public boolean pMinElim(int total, double p) {
//		boolean shouldRemove = false;
//		float num = (count/(float)(total-(tokenSequence.size()-1)));
//		
//		if(num <= p && tokenSequence.size() != 0) {
//			shouldRemove = true;
//		}else {
//				for(int i = children.size()-1; i >= 0; i--) {
//					if(children.get(i).pMinElim(total, p)) {
//						children.remove(children.get(i));
//					}
//				}
//			shouldRemove = false;
//		}
//		return shouldRemove; 
//	}
	public void print() {
		System.out.println(tokenSequence); 
		for(int i = 0; i < children.size(); i++) 
			children.get(i).print(1);
	}
	public void print(int spaces) {
		for(int i = 1; i <= spaces; i++) {
			System.out.print(" ");
		}
		System.out.print("-->");
		System.out.println(tokenSequence);
		for(int j = 0; j < children.size(); j++) 
			children.get(j).print(spaces + 1);
	}
//	public void clear() {
//		tokenSequence.clear();
//		for(int i = 0; i < children.size(); i++)
//			children.get(i).clear();
//	}
}
