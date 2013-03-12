package rictas.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class GuiButtonSideControl extends GuiButton {

	private int texPosX;
	private int texPosY;
	private int buttonIndex = 0;
	private boolean isSelected = false;
	private String texture;
	
	/**
	 * @param pID
	 * @param xPos = ist linke xPosition auf dem Bildschirm
	 * @param yPos = ist obere yPosition auf dem Bildschirm
	 * @param pWidth = Breite des Button
	 * @param pHeight = Höhe des Button
	 * @param par6Str = Text auf dem Button
	 * @param texPosX = ist linke xPosition in der Texturdatei
	 * @param TexPosY = ist obere yPosition in der Texturdatei
	 */
    public GuiButtonSideControl(int pID, int xPos, int yPos, int pWidth, int pHeight, int texPosX, int texPosY, String texture)
    {
    	super(pID, xPos, yPos,  pWidth, pHeight, "");
    	this.texPosX = texPosX;
    	this.texPosY = texPosY;
    	this.texture = texture;
    }
    
    public void setButtonIndex(int index) {
    	buttonIndex = index;
    }
    
    public void setSelected(boolean selected) {
    	isSelected = selected;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft par1Minecraft, int par2, int par3)
    {
        if (this.drawButton)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture(texture));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var5 = par2 >= this.xPosition && par3 >= this.yPosition && par2 < this.xPosition + this.width && par3 < this.yPosition + this.height;
            //int var6 = this.getHoverState(var5);
            //int buttonIndex = 0;//tile.getSideType(side).ordinal();
            if(var5 || isSelected) {
            	//Hovered
            	this.drawTexturedModalRect(this.xPosition, this.yPosition, texPosX+buttonIndex*this.width, texPosY+this.height, width, height);
            } else {
            	//normal
            	this.drawTexturedModalRect(this.xPosition, this.yPosition, texPosX+buttonIndex*this.width, texPosY, width, height);
            }
            this.mouseDragged(par1Minecraft, par2, par3);
//            int var7 = 4210752;
//            if (!this.enabled)
//            {
//                var7 = 4210752;
//            }
//            else if (var5)
//            {
//                var7 = 4210752;
//            }
//            this.drawCenteredString(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var7);
        }
    }
    
//    public void drawCenteredString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5)
//    {
//        par1FontRenderer.drawString(par2Str, par3 - par1FontRenderer.getStringWidth(par2Str) / 2+1, par4+1, par5);
//    }
}

