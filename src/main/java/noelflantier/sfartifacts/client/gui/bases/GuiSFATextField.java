package noelflantier.sfartifacts.client.gui.bases;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiSFATextField extends GuiTextField{

	public boolean isOnlyNumeric = false;
	public boolean isOnlyLetter = false;
	
	public GuiSFATextField(FontRenderer p_i1032_1_, int p_i1032_2_, int p_i1032_3_, int p_i1032_4_, int p_i1032_5_) {
		super(p_i1032_1_, p_i1032_2_, p_i1032_3_, p_i1032_4_, p_i1032_5_);
	}

}
