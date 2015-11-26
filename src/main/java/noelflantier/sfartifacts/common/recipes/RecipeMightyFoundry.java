package noelflantier.sfartifacts.common.recipes;

public class RecipeMightyFoundry extends RecipeBase{

	private int tickPerItem = 0;
	public RecipeMightyFoundry(String uid) {
		super(uid);
	}

    public int getTickPerItem() {
		return tickPerItem;
	}
	public void setTickPerItem(int tickPerItem) {
		this.tickPerItem = tickPerItem;
	}
}
