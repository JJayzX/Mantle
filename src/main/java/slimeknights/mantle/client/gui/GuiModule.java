package slimeknights.mantle.client.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

// a sub-gui. Mostly the same as a separate GuiContainer, but doesn't do the calls that affect the game as if this were the only gui
@OnlyIn(Dist.CLIENT)
public abstract class GuiModule extends ContainerScreen
{

  protected final GuiMultiModule parent;

  // left or right of the parent
  protected final boolean right;
  // top or bottom of the parent
  protected final boolean bottom;

  public int yOffset = 0;
  public int xOffset = 0;

  public GuiModule(GuiMultiModule parent, Container container, boolean right, boolean bottom) {
    super(container);

    this.parent = parent;
    this.right = right;
    this.bottom = bottom;
  }

  public int guiRight() {
    return guiLeft + xSize;
  }

  public int guiBottom() {
    return guiTop + ySize;
  }

  public Rectangle2d getArea() {
    return new Rectangle2d(guiLeft, guiTop, xSize, ySize);
  }

  @Override
  public void initGui() {
    this.guiLeft = (this.width - this.xSize) / 2;
    this.guiTop = (this.height - this.ySize) / 2;
  }

  public void updatePosition(int parentX, int parentY, int parentSizeX, int parentSizeY) {
    if(right) {
      this.guiLeft = parentX + parentSizeX;
    }
    else {
      this.guiLeft = parentX - this.xSize;
    }

    if(bottom) {
      this.guiTop = parentY + parentSizeY - this.ySize;
    }
    else {
      this.guiTop = parentY;
    }

    this.guiLeft += xOffset;
    this.guiTop += yOffset;
  }

  public boolean shouldDrawSlot(Slot slot) {
    return true;
  }

  public boolean isMouseInModule(int mouseX, int mouseY) {
    return mouseX >= this.guiLeft && mouseX < this.guiRight() &&
           mouseY >= this.guiTop && mouseY < this.guiBottom();
  }

  public boolean isMouseOverFullSlot(double mouseX, double mouseY) {
    for(Slot slot : inventorySlots.inventorySlots) {
      if(parent.isSlotSelected(slot, mouseX, mouseY) && slot.getHasStack()) {
        return true;
      }
    }
    return false;
  }

  /*
    public void updateDragged(boolean dragSplitting, Set draggedSlots) {
      this.dragSplitting = dragSplitting;
      this.dragSplittingSlots.clear();
      for(Object o : draggedSlots) {
        if(o instanceof SlotWrapper)
          this.dragSplittingSlots.add(((SlotWrapper) o).parent);
        else
          this.dragSplittingSlots.add(o);
      }
    }
  */
  public void handleDrawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
  }

  public void handleDrawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    this.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }


  /**
   * Custom mouse click handling.
   *
   * @return True to prevent the main container handling the mouseclick
   */
  public boolean handleMouseClicked(double mouseX, double mouseY, int mouseButton) {
    return false;
  }

  /**
   * Custom mouse click handling.
   *
   * @return True to prevent the main container handling the mouseclick
   */
  public boolean handleMouseClickMove(double mouseX, double mouseY, int clickedMouseButton, double timeSinceLastClick) {
    return false;
  }

  /**
   * Custom mouse click handling.
   *
   * @return True to prevent the main container handling the mouseclick
   */
  public boolean handleMouseReleased(double mouseX, double mouseY, int state) {
    return false;
  }

}
