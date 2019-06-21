package slimeknights.mantle.client.gui.book;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static slimeknights.mantle.client.gui.book.Textures.TEX_BOOK;

@OnlyIn(Dist.CLIENT)
public class GuiArrow extends Button
{
  private static final int X = 412;

  public static final int WIDTH = 18;
  public static final int HEIGHT = 10;

  // Appearance
  public ArrowType arrowType;
  public int color;
  public int hoverColor;

  public GuiArrow(int buttonId, int x, int y, ArrowType arrowType, int color, int hoverColor) {
    super(buttonId, x, y, arrowType.w, arrowType.h, "");

    this.arrowType = arrowType;
    this.color = color;
    this.hoverColor = hoverColor;
  }

  @Override
  public void render(int mouseX, int mouseY, float partialTicks) {
    if(this.visible) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.getTextureManager().bindTexture(TEX_BOOK);

      this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

      int color = this.isHovered ? this.hoverColor : this.color;

      float r = ((color >> 16) & 0xff) / 255.F;
      float g = ((color >> 8) & 0xff) / 255.F;
      float b = (color & 0xff) / 255.F;

      GlStateManager.color3f(r, g, b);
      AbstractGui.blit(x, y, arrowType.x, arrowType.y, width, height, width, height, 512, 512);
      this.renderBg(minecraft, mouseX, mouseY);
    }
  }

  public enum ArrowType {
    NEXT(412, 0),
    PREV(412, 10),
    RIGHT(412, 20),
    LEFT(412, 30),
    BACK_UP(412, 40, 18, 18),
    UP(412, 58, 10, 18),
    DOWN(412+10, 58, 10, 18),
    REFRESH(412, 76, 18, 18);

    public final int x, y, w, h;

    ArrowType(int x, int y) {
      this(x, y, WIDTH, HEIGHT);
    }

    ArrowType(int x, int y, int w, int h) {
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
    }
  }
}
