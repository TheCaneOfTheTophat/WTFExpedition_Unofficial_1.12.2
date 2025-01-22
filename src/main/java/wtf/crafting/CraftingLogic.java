package wtf.crafting;

public class CraftingLogic {
//
//
//
//	public static ArrayList<RecipeWrapper> getCraftableStack(EntityPlayer player) {
//
//		HashMap<Integer, Integer> inventoryHashMap = new HashMap<Integer, Integer>();
//
//		for (ItemStack stack : player.inventory.mainInventory){
//			if (stack != null){
//				inventoryHashMap.put(itemNum(stack), stack.stackSize);
//			}
//		}
//
//		ArrayList<RecipeWrapper> craftableRecipes = new ArrayList<RecipeWrapper>();
//
//		for (RecipeWrapper recipe : RecipeParser.parsedRecipes) {
//
//			if (canCraft(recipe, inventoryHashMap)) {
//				craftableRecipes.add(recipe);
//			}
//		}
//		return craftableRecipes;
//	}
//
//	public static boolean canCraft(RecipeWrapper recipe, HashMap<Integer, Integer> inventoryHashMap){
//
//		for (ArrayList<ItemStack> subList : recipe.getIngrediants()){
//			if (subList.size() > 0 && !hasIngrediant(inventoryHashMap, subList)){
//					return false;
//			}
//		}
//		return true;
//
//	}
//
//	public static boolean hasIngrediant(HashMap<Integer, Integer> inventoryHashMap, ArrayList<ItemStack> subList){
//
//		for (ItemStack ingrediant : subList){
//			if(ingrediant == null){
//				return true;
//			}
//			if (inventoryHashMap.containsKey(itemNum(ingrediant))){// && inventoryHashMap.get(ingrediant) >= ingrediant.stackSize){
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private static int itemNum(ItemStack stack){
//		return Item.getIdFromItem(stack.getItem())*16+stack.getItemDamage();
//	}
//
//
//
//

}
