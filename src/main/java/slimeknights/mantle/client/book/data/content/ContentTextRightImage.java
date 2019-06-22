package slimeknights.mantle.client.book.data.content;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;

import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.element.ImageData;
import slimeknights.mantle.client.book.data.element.TextData;
import slimeknights.mantle.client.gui.book.GuiBook;
import slimeknights.mantle.client.gui.book.element.BookElement;
import slimeknights.mantle.client.gui.book.element.ElementImage;
import slimeknights.mantle.client.gui.book.element.ElementText;

@OnlyIn(Dist.CLIENT)
public class ContentTextRightImage extends PageContent {

  public String title;
  public TextData[] text1;
  public TextData[] text2;
  public ImageData image;

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
    int y = TITLE_HEIGHT;

    if(this.title == null || this.title.isEmpty()) {
      y = 0;
    } else {
      this.addTitle(list, this.title);
    }

    if(this.text1 != null && this.text1.length > 0) {
      list.add(new ElementText(0, y, GuiBook.PAGE_WIDTH - 55, 50, this.text1));
    }

    if(this.image != null && this.image.location != null) {
      list.add(new ElementImage(GuiBook.PAGE_WIDTH - 50, y, 50, 50, this.image));
    } else {
      list.add(new ElementImage(GuiBook.PAGE_WIDTH - 50, y, 50, 50, ImageData.MISSING));
    }

    if(this.text2 != null && this.text2.length > 0) {
      list.add(new ElementText(0, y + 55, GuiBook.PAGE_WIDTH, GuiBook.PAGE_HEIGHT - 55 - y, this.text2));
    }
  }
}
